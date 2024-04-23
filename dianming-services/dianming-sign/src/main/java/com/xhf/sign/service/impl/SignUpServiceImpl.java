package com.xhf.sign.service.impl;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.common.redis.CacheService;
import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.common.utils.R;
import com.xhf.file.service.OSSService;
import com.xhf.model.sign.dto.SignUpFormDto;
import com.xhf.model.sign.entity.SignUpEntity;
import com.xhf.model.sign.entity.TermScheduleEntity;
import com.xhf.model.sign.entity.TermSignStartTimeEntity;
import com.xhf.sign.controller.WebSocketServer;
import com.xhf.sign.dao.SignUpDao;
import com.xhf.sign.service.SignUpService;
import com.xhf.sign.service.TermScheduleService;
import com.xhf.sign.service.TermSignStartService;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service("signUpService")
@Slf4j
public class SignUpServiceImpl extends ServiceImpl<SignUpDao, SignUpEntity> implements SignUpService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Value("${image.urlPath}")
    private String urlPath;

    @Autowired
    private CacheService cacheService;

    @Value("${gateWayName}")
    private String gateWayName;

    @Value("${image.width}")
    private int width;

    @Value("${image.height}")
    private int height;

    @Autowired
    private OSSService ossService;

    @Autowired
    private TermScheduleService termScheduleService;

    @Autowired
    private TermSignStartService termSignStartService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SignUpEntity> page = this.page(
                new Query<SignUpEntity>().getPage(params),
                new QueryWrapper<SignUpEntity>()
        );

        return new PageUtils(page);
    }

    public boolean saveOrUpdate(SignUpEntity entity) {
        try {
            if (entity.getSignId() == null) {
                // 保存
                this.save(entity);
            } else {
                this.updateById(entity);
            }
        }catch (Exception e) {
            log.info("", e);
            if (e instanceof DataIntegrityViolationException) {
                throw new RRException(ErrorCode.SIGN_FORIGN_KEY_ERROR);
            }else {
                throw new RRException("服务器异常", 500);
            }
        }
        return true;
    }

    /**
     * 返回二维码
     *
     * @param id    教师id
     * @param semId 课程id
     * @param response
     * @return
     */
    @Override
    public void getImage(Long id, Long semId, HttpServletResponse response) throws NoSuchAlgorithmException {
        // 生成唯一requestId
        String input = id + new Date().toString();
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(input.getBytes());
        BigInteger hashInt = new BigInteger(1, hash);
        String requestId = hashInt.toString(16);
        // 获取网关, path信息, 生成url访问路径
        StringBuilder url = new StringBuilder("http://");
        try {
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            Instance instance = namingService.selectOneHealthyInstance(gateWayName);
            url.append(instance.getIp());
            url.append(":");
            url.append(instance.getPort());
        } catch (NacosException e) {
            throw new RRException(ErrorCode.GET_IMAGE_PATH_FAILED);
        }
        String address = url.toString();
        url.append(urlPath);
        url.append("?requestId=").append(requestId);
        url.append("&semId=").append(semId);
        url.append("&address=").append(address);
        url.append(("&teacherId=")).append(id);
        // debug
        log.info("address = {}, semId = {}, requestId = {}, teacher = {}", address, semId, requestId, id);
        log.info("url = {}", url.toString());
        // 存储requestId, 默认5分钟过期
        cacheService.setEx(RedisConstant.SEMID + semId, requestId, 5, TimeUnit.MINUTES);
        // 编码二维码
        try {
            getQRCode(response, url.toString(), width, height);
            // 处理签到课程信息
            handleTermScheduleInfo(semId);
        } catch (WriterException e) {
            e.printStackTrace();
            throw new RRException(ErrorCode.IMAGE_GENERATE_FAILED);
        }
    }

    /**
     * 处理学期课程签到信息
     * @param semId
     */
    private void handleTermScheduleInfo(Long semId) {
        // 修改学期课程签到信息
        TermScheduleEntity entity = new TermScheduleEntity();
        entity.setSemesterId(semId);
        entity.setSignStartTime(LocalDateTime.now());
        termScheduleService.updateInfo(entity);
        // 签到时间历史记录存储
        TermSignStartTimeEntity historyEntity = new TermSignStartTimeEntity();
        historyEntity.setSemId(semId);
        historyEntity.setStartTime(entity.getSignStartTime());
        termSignStartService.saveInfo(historyEntity);
    }

    /**
     * 获取二维码
     * @param response
     */
    private void getQRCode(HttpServletResponse response, String qrCodeData, int width, int height) throws WriterException {
        // 设置二维码参数
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeData, BarcodeFormat.QR_CODE, width, height);

        // 保存生成的二维码图片
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixelColor = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                image.setRGB(x, y, pixelColor);
            }
        }
        response.setContentType("image/png");

        // 将图片数据写入out中
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);
            out.close();
        }catch (IOException e) {
            throw new RRException(ErrorCode.IMAGE_GENERATE_FAILED);
        }
    }

    /**
     * 获取绫华图片
     *
     * @return
     */
    @Override
    public R getLinghua() {
        List<String> list = ossService.listAllFileInBucket();
        return R.ok().put("data", list);
    }

    /**
     * 接收签到表单, 执行签到逻辑
     *
     * @param dto
     * @return
     */
    @Override
    public R signUp(SignUpFormDto dto, HttpServletResponse response, HttpServletRequest request) throws IOException {
        // 判断requestId
        String requestId = dto.getRequestId();
        if (StringUtils.isBlank(requestId)) {
            throw new RRException(ErrorCode.SIGN_UP_ERROR, "未携带requestId, 请联系管理员");
        }

        // 课程id作为key, value为requestId
        // 判断携带requestId的签到是否已经过时
        String serviceRequestId = cacheService.get(RedisConstant.SEMID + dto.getSemId());
        if (!requestId.equals(serviceRequestId)) {
            throw new RRException(ErrorCode.SIGN_UP_ERROR, "签到时间已过, 无法继续签到");
        }

        // 判断是否携带cookie, 防止代签情况
        // todo: session, cookie 确保同一部手机只能在同一次签到时间内, 访问一次
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(RedisConstant.SIGN)) {
                    String clientRequestId = cookie.getValue();
                    if (clientRequestId.equals(requestId)) {
                        // todo: 禁止访问
                        // response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        // response.getWriter().write("访问被拒绝. 您已经成功签到过, 无法继续签到");
                        throw new RRException(ErrorCode.SIGN_UP_ERROR, "访问拒绝, 您已经签过到了, 无法继续签到");
                    }
                    break;
                }
            }
        }

        SignUpEntity signUpEntity = new SignUpEntity();
        signUpEntity.setStuSno(dto.getStuSno());
        signUpEntity.setSemId(dto.getSemId());
        signUpEntity.setSignTime(LocalDateTime.now());
        // 学生签到
        String stuSno = cacheService.get(RedisConstant.REQUEST_ID + dto.getStuSno() + "_" + requestId);
        // 防止重复签到
        if (!StringUtils.isBlank(stuSno)) {
            throw new RRException(ErrorCode.REPEAT_SIGN_UP, " 学号:" + dto.getStuSno() + "已签过到");
        }
        boolean flag = this.saveOrUpdate(signUpEntity);
        // 签到成功, websocket推送信息
        if (flag) {
            // 存储签到的学生信息, 防止重复签到
            cacheService.setEx(RedisConstant.REQUEST_ID + dto.getStuSno() + "_" + requestId, dto.getStuSno(), 6, TimeUnit.MINUTES);
            // 种植cookie, 防止重复进入签到界面(防止代签)
            Cookie cookie=new Cookie(RedisConstant.SIGN, requestId);
            // 300秒: 5min
            cookie.setMaxAge(300);
            response.addCookie(cookie);

            StringBuilder message = new StringBuilder();
            message.append(dto.getStuName()).append(", ").append(dto.getStuSno());
            message.append(", ").append(LocalDateTime.now().toString());
            try {
                WebSocketServer.sendInfo(message.toString(), dto.getTeacherId());
            } catch (IOException e) {
                throw new RRException("websocket 后端数据传输失败", 500);
            }
        }
        return R.ok();
    }
}