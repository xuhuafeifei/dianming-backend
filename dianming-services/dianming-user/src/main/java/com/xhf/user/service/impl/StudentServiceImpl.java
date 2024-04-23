package com.xhf.user.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xhf.common.exception.RRException;
import com.xhf.common.utils.R;
import com.xhf.file.service.OSSService;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.user.dao.StudentDao;
import com.xhf.user.service.StudentService;
import com.xhf.user.service.UserService;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.xhf.user.utils.FileNameUtil.getFileName;

@Slf4j
@Service("studentService")
public class StudentServiceImpl extends ServiceImpl<StudentDao, StudentEntity> implements StudentService {
    @Autowired
    private OSSService ossService;

    @Resource
    private UserService userService;

    @Autowired
    private StudentDao studentDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        // 获取班级
        String sclass = (String) params.get("sclass");
        LambdaQueryWrapper<StudentEntity> query = new LambdaQueryWrapper<>();
        if (!"全部".equals(sclass) && StringUtils.isNotBlank(sclass)) {
            query.like(StudentEntity::getSclass, sclass);
        }
        // 获取id
        String id = (String) params.get("id");
        // 获取学号
        String sno = (String) params.get("sno");
        // 姓名
        String sname = (String) params.get("sname");
        if (id != null) {
            query.eq(StudentEntity::getId, Long.valueOf(id));
        }
        if (sno != null) {
            query.eq(StudentEntity::getSno, sno);
        }
        if (sname != null) {
            query.like(StudentEntity::getSname, sname);
        }
        // 构造查询条件
        IPage<StudentEntity> page = this.page(
                new Query<StudentEntity>().getPage(params),
                query
        );

        return new PageUtils(page);
    }

    @Override
    public void export(HttpServletResponse response) throws IOException {
        LambdaQueryWrapper<StudentEntity> query = new LambdaQueryWrapper<>();
        query.orderByAsc(StudentEntity::getSclass).orderByAsc(StudentEntity::getSno);

        List<StudentEntity> studentEntityList = this.list();
        log.info("studentEntityList: " + studentEntityList);
        try {
            EasyExcel.write(response.getOutputStream(), StudentEntity.class).sheet("学生情况统计").doWrite(studentEntityList);
        } catch (IOException e) {
            throw new RRException("excel导出失败, 请联系管理员");
        }
    }

//    @Override
//    public String savePicture(MultipartFile file) {
//        BASE64Encoder bEncoder=new BASE64Encoder();
//        try {
//            // 转码
//            String base64 = bEncoder.encode(file.getBytes());
//            String upload = fileHandler.upload(base64);
//            log.info("url : " + upload);
//            return upload;
//        } catch (IOException e) {
//            throw new RRException(ErrorCode.PICTURE_CONVERT_FAILED);
//        }
//    }

    @Override
    public void updatePersonal(StudentEntity stu, UserEntity user) {
        if (stu != null) {
            this.updateById(stu);
        }
        if (user != null) {
            userService.updateById(user);
        }
    }

    /**
     * 保存学生图片信息
     *
     * @param multipartFileList
     * @return
     */
    @Override
    public R uploadImages(List<MultipartFile> multipartFileList) {
        List<StudentEntity> studentEntityList = new ArrayList<StudentEntity>();

        multipartFileList.forEach(e -> {
            // 1. 获取原来的图片名称 sno-name
            String originalFilename = e.getOriginalFilename();
            String[] split = originalFilename.split("-");
            String sno = split[0], sname = split[1].split(".")[0];
            // 2. 获取图片新名称
            String fileName = getFileName(sno, sname);
            // 3. 保存图片, 获取url
            String pictUrl = ossService.uploadFile(e, fileName);
            log.info(fileName, pictUrl);
            // 4. 保存到学生信息中
            StudentEntity entity = new StudentEntity();
            entity.setSno(sno);
            entity.setSportrait(pictUrl);
            studentEntityList.add(entity);
        });
        // 按照学号修改
        studentDao.updateImages(studentEntityList);
        return R.ok();
    }
}