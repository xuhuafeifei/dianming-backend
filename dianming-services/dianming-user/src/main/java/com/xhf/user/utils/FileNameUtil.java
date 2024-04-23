package com.xhf.user.utils;

import com.github.houbb.pinyin.constant.enums.PinyinStyleEnum;
import com.github.houbb.pinyin.util.PinyinHelper;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.model.user.dtos.UserDto;
import com.xhf.model.user.entity.StudentEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileNameUtil {

    /**
     * 文件后缀
     */
    private static final String FILE_SUFFIX = ".png";

    /**
     * 姓名转拼音
     * @param sname
     * @return
     */
    public static String getPinyin(String sname) {
        return PinyinHelper.toPinyin(sname, PinyinStyleEnum.NORMAL).replace(" ", "");
    }

    /**
     * 生成文件名字
     * @param student
     * @return
     */
    public static String getFileName(StudentEntity student) {
        if (StringUtils.isAnyBlank(student.getSname(), student.getSno())) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        return  student.getSno() + "-" + getPinyin(student.getSname()) + FILE_SUFFIX;
    }

    /**
     * 生产文件名字
     * @param sno
     * @param sname
     * @return
     */
    public static String getFileName(String sno, String sname) {
        if (StringUtils.isAnyBlank(sname, sno)) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        return sno + "-" + getPinyin(sname) + FILE_SUFFIX;
    }

    public static String getFileName(MultipartFile e) {
        // 1. 获取原来的图片名称 sno-name
        try {
            String originalFilename = e.getOriginalFilename();
            String[] split = originalFilename.split("-");
            String sno = split[0], sname = split[1].split("\\.")[0];
            return getFileName(sno, sname);
        } catch (Exception ex) {
            ex.printStackTrace();
            return e.getOriginalFilename();
        }
    }

    /**
     * 生成文件名字
     * @param userDto
     * @return
     */
    public static String getFileName(UserDto userDto) {
        StudentEntity student = userDto.toStu();
        if (StringUtils.isAnyBlank(student.getSname(), student.getSno())) {
            throw new RRException(ErrorCode.DATA_ERROR);
        }
        return  student.getSno() + "-" + getPinyin(student.getSname()) + FILE_SUFFIX;
    }

}
