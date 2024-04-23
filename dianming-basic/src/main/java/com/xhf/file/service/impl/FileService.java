package com.xhf.file.service.impl;

import com.xhf.model.user.entity.StudentEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

public interface FileService {
    /**
     * 上传文件
     * @param inputStream
     * @return 访问路径
     */
    String uploadFile(InputStream inputStream, String fileName);

    /**
     * 下载文件
     * @return
     */
    byte[] downLoadFile(String fileName);

    /**
     * 删除文件
     */
    boolean delete(String fileName);

    /**
     * 文件上传, 同时为student赋值
     * @param image
     * @param student
     */
    void uploadFile(MultipartFile image, StudentEntity student, String fileName);

    /**
     * 上传图片
     * @param image
     * @return
     */
    String uploadFile(MultipartFile image, String fileName);

    /**
     * 返回当前bucket下所有图片
     * @return
     */
    List<String> listAllFileInBucket();
}
