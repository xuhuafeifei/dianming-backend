package com.xhf.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xhf.common.utils.R;
import com.xhf.model.user.entity.StudentEntity;
import com.xhf.model.user.entity.UserEntity;
import com.xhf.utils.common.PageUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author xuhuafei
 * @email 2508020102@gmail.com
 * @date 2023-02-28 17:15:36
 */
public interface StudentService extends IService<StudentEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void export(HttpServletResponse response) throws IOException;

//    String savePicture(MultipartFile file);

    void updatePersonal(StudentEntity stu, UserEntity user);

    /**
     * 保存学生图片信息
     * @param multipartFileList
     * @return
     */
    R uploadImages(List<MultipartFile> multipartFileList);
}

