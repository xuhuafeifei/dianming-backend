package com.xhf.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.google.gson.Gson;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.common.redis.CacheService;
import com.xhf.common.redis.utils.RedisConstant;
import com.xhf.common.utils.R;
import com.xhf.model.user.dtos.UserDto;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.model.user.vo.LoginVo;
import com.xhf.user.dao.StudentDao;
import com.xhf.user.dao.UserDao;
import com.xhf.user.service.UserService;
import com.xhf.utils.common.JWTUtils;
import com.xhf.utils.common.PageUtils;
import com.xhf.utils.common.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Resource
    private StudentDao studentDao;

    @Resource
    private CacheService cacheService;

    @Autowired
    private Gson gson;

    public static String salt = "ipbd@zhengwen";

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<UserEntity> page = this.page(
                new Query<UserEntity>().getPage(params),
                new QueryWrapper<UserEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public LoginVo login(String username, String password) {
        // 获取对象
        UserEntity entity = this.getOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUname, username));
        if (entity == null) {
            throw new RRException(ErrorCode.USER_NOT_FOUND);
        }
        if (!entity.getPasswd().equals(password)) {
            throw new RRException(ErrorCode.PASSWORD_NOT_EQUAL);
        }
        LoginVo loginVo = new LoginVo();
        List<String> roles = new ArrayList<>();
        roles.add(entity.getRoles());
        loginVo.setRoles(roles);
        loginVo.setUsername(username);
        loginVo.setAccessToken(JWTUtils.sign(entity.getId()));
        loginVo.setRefreshToken(JWTUtils.sign(entity.getId()));
        loginVo.setId(entity.getId());
        // 存储redis
        cacheService.setEx(RedisConstant.USER + entity.getId(), gson.toJson(loginVo), 2, TimeUnit.DAYS);
        return loginVo;
    }

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public R getInfo(Integer userId) throws JsonProcessingException {
        // 获取用户实体类
        UserEntity userEntity = this.getById(userId);
        // 获取学生实体类
        StudentEntity stuEntity = studentDao.selectById(userId);
        UserDto userDto = new UserDto();
        userDto.convert(userEntity, stuEntity);
        userDto.setRegisterTime(null);

        String json = objectMapper.writeValueAsString(userDto);

        System.out.println(json);
        return R.ok().put("data", userDto);
    }

    /**
     * 间stu对象注册成user
     * @return
     */
    private UserEntity handleStudentToUser(StudentEntity student) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(student.getId());
        // 获取姓名的拼音
        userEntity.setUname(PinyinHelper.toPinyin(student.getSname(), PinyinStyleEnum.NORMAL));
        // 设置密码
        userEntity.setPasswd(DigestUtils.md5DigestAsHex((salt + "@" + student.getSno()).getBytes()));
        // 设置角色
        userEntity.setRoles("user");
        return userEntity;
    }

    /**
     * 批量处理stu对象
     * @param studentList
     */
    private List<UserEntity> handleStudentToUserList(List<StudentEntity> studentList) {
        List<UserEntity> userList = new ArrayList<UserEntity>();
        for (StudentEntity student : studentList) {
            userList.add(handleStudentToUser(student));
        }
        return userList;
    }

    @Override
    public void saveByStu(StudentEntity student) {
        UserEntity userEntity = handleStudentToUser(student);
        // 注册用户
        this.save(userEntity);
    }

    @Override
    public void saveByStu(List<StudentEntity> studentList) {
        List<UserEntity> userEntities = studentList.stream().map(this::handleStudentToUser).collect(Collectors.toList());
        this.saveBatch(userEntities);
    }

    @Override
    public void saveByStuList(List<StudentEntity> studentEntities) {
        List<UserEntity> userEntities = handleStudentToUserList(studentEntities);
        System.out.println(userEntities);
        this.saveBatch(userEntities);
    }

    @Override
    public boolean update(String id, String usedPassword, String newPassword, String secNewPassword) {
        UserEntity user = this.getById(Long.parseLong(id));
        // 二次密码检验
        if (!newPassword.equals(secNewPassword)) {
            throw new RRException(ErrorCode.PASSWORD_NOT_EQUAL);
        }
        // 密码判断
        if (!usedPassword.equals(user.getPasswd())) {
            throw new RRException(ErrorCode.PASSWORD_ERROR);
        }
        // 重新修改密码
        user.setPasswd(newPassword);
        return this.updateById(user);
    }

}