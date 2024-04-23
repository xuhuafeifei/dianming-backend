package com.xhf.model.user.dtos;

import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import lombok.Data;

@Data
public class UserDto extends StudentEntity {
    /**
     *
     */
    private String uname;
    /**
     *
     */
    private String passwd;

    private String roles;

    private Integer modifyType;


    public StudentEntity toStu() {
        StudentEntity stu = new StudentEntity();
        stu.setId(this.getId());
        stu.setSclass(this.getSclass());
        stu.setSno(this.getSno());
        stu.setSname(this.getSname());
        stu.setLabel(this.getLabel());
        stu.setSportrait(this.getSportrait());
        return stu;
    }

    /**
     * 不能写getUser, 否则会被Lombook自动生成getter方法, 导致json反序列化得时候生成user字段
     * @return
     */
    public UserEntity toUser() {
        UserEntity user = new UserEntity();
        user.setId(this.getId());
        user.setUname(this.getUname());
        return user;
    }

    public void convert(UserEntity userEntity, StudentEntity stuEntity) {
        this.setUname(userEntity.getUname());
        this.setPasswd(userEntity.getPasswd());
        this.setRoles(userEntity.getRoles());
        this.setId(stuEntity.getId());
        this.setSclass(stuEntity.getSclass());
        this.setSno(stuEntity.getSno());
        this.setSname(stuEntity.getSname());
        this.setLabel(stuEntity.getLabel());
        this.setAbsent(stuEntity.getAbsent());
        this.setSportrait(stuEntity.getSportrait());
        this.setLockTime(stuEntity.getLockTime());
        this.setAge(stuEntity.getAge());
        this.setSex(stuEntity.getSex());
        this.setPhoneNumber(stuEntity.getPhoneNumber());
        this.setInterest(stuEntity.getInterest());
        this.setRegisterTime(stuEntity.getRegisterTime());
    }
}
