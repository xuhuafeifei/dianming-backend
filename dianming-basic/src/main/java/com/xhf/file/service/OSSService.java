package com.xhf.file.service;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.PutObjectRequest;
import com.xhf.common.exception.ErrorCode;
import com.xhf.common.exception.RRException;
import com.xhf.file.config.OSSConfig;
import com.xhf.file.config.OSSConfigProperties;
import com.xhf.file.service.impl.FileService;
import com.xhf.model.user.entity.StudentEntity;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@EnableConfigurationProperties(OSSConfigProperties.class)
@Import(OSSConfig.class)
public class OSSService implements FileService {

    private static final int WEIGHT = 300;
    private static final int HEIGHT = 300;

    @Autowired
    private OSSConfigProperties properties;

    /**
     * 单张图片最大大小,超过压缩
     */
    private static Integer SINGLE_FILE_MAX_SIZE = 204800;

    private OSS buildOSSClient() {
        return new OSSClientBuilder().build(
                properties.getEndpoint(),
                properties.getAccessKeyId(),
                properties.getSecretAccessKey()
        );
    }

    /**
     * 上传文件
     *
     * @param inputStream
     * @return 访问路径
     */
    @Override
    public String uploadFile(InputStream inputStream, String fileName) {
        OSS ossClient = buildOSSClient();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    properties.getBucketName(), fileName, inputStream
            );
            // 创建PutObject请求。
            ossClient.putObject(putObjectRequest);
            return getFileURL(fileName);
        } catch (OSSException oe) {
            oeExceptionHandler(oe);
        } catch (ClientException ce) {
            ceExceptionHandler(ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 下载文件
     *
     * @param fileName 文件名称
     * @return
     */
    @Override
    public byte[] downLoadFile(String fileName) {
        OSS ossClient = buildOSSClient();
        try {
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(
                    properties.getBucketName(), fileName
            );

            InputStream inputStream = ossObject.getObjectContent();
            // 将inputStream转为byte Stream
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buff = new byte[1024];
            int rc = 0;
            while (true) {
                try {
                    if (!((rc = inputStream.read(buff, 0, 1024)) > 0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                byteArrayOutputStream.write(buff, 0, rc);
            }

            // ossObject对象使用完毕后必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            ossObject.close();
            inputStream.close();

            return byteArrayOutputStream.toByteArray();
        } catch (OSSException oe) {
            oeExceptionHandler(oe);
        } catch (ClientException ce) {
            ceExceptionHandler(ce);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 删除文件
     */
    @Override
    public boolean delete(String fileName) {
        OSS ossClient = buildOSSClient();
        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(
                    properties.getBucketName(), fileName
            );
            return true;
        } catch (OSSException oe) {
            oeExceptionHandler(oe);
        } catch (ClientException ce) {
            ceExceptionHandler(ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return false;
    }

    /**
     * 文件上传, 同时为student赋值
     *
     * @param image
     * @param student
     */
    @Override
    public void uploadFile(MultipartFile image, StudentEntity student, String fileName) {
        String fileUrl = uploadFile(image, fileName);
        if (StringUtils.isBlank(fileUrl)) {
            throw new RRException(ErrorCode.FILE_UPLOAD_ERROR);
        }
        student.setSportrait(fileUrl);
    }

    /**
     * 返回当前bucket下所有图片
     *
     * @return
     */
    @Override
    public List<String> listAllFileInBucket() {
        OSS ossClient = buildOSSClient();
        List<String> urlList = new ArrayList<String>();
        try {
            for (OSSObjectSummary objectSummary : ossClient.listObjects(properties.getBucketName()).getObjectSummaries()) {
                // 获取每张图片的名称, 拼接url地址
                urlList.add(getFileURL(objectSummary.getKey()));
            }
            return urlList;
        } catch (OSSException oe) {
            oeExceptionHandler(oe);
        } catch (ClientException ce) {
            ceExceptionHandler(ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }

    /**
     * 上传图片
     *
     * @param image
     * @return
     */
    @Override
    public String uploadFile(MultipartFile image, String fileName) {
        if (image == null) {
            return null;
        }
        try {
            // 如果图片大小小于 200KB，则不进行裁剪，直接上传
            InputStream inputStream = null;
            long size = image.getSize();
            if (size > SINGLE_FILE_MAX_SIZE) {
                inputStream = resizeImage(image, size);
            } else {
                inputStream = image.getInputStream();
            }

            return uploadFile(inputStream, fileName);
        }catch (IOException e) {
            throw new RRException(ErrorCode.FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 图片裁剪
     * @param image
     * @param size
     * @return
     */
    private InputStream resizeImage(MultipartFile image, long size) throws IOException {
        log.info("初始大小:{}", size / 1024);
        // base64转bufferdImage
        BufferedImage src = ImageIO.read(image.getInputStream());
        // 图片大小重塑
        BufferedImage output = Thumbnails.of(src).size(WEIGHT, HEIGHT).asBufferedImage();
        long newSize = estimateImageSize(output);
        log.info("一次裁剪:{}", (newSize * 1.0) / 1024);
        if (newSize > SINGLE_FILE_MAX_SIZE) {
            // 如果图片任然过大,则重新处理
            output = Thumbnails.of(src).scale(SINGLE_FILE_MAX_SIZE / (newSize * 1.0)).asBufferedImage();
        }
        log.info("最终裁剪:{}", (estimateImageSize(output) * 1.0) / 1024);
        // 流数据转化
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(output, "png", outputStream);
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public static long estimateImageSize(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int bitsPerPixel = image.getColorModel().getPixelSize();
        int numChannels = image.getColorModel().getNumColorComponents();

        // 计算图像的字节数
        long imageSizeBytes = (width * height * bitsPerPixel * numChannels) / 8;

        return imageSizeBytes;
    }

    /**
     * 处理ce异常
     * @param ce
     */
    private void ceExceptionHandler(ClientException ce) {
        log.error("Caught an ClientException, which means the client encountered "
            + "a serious internal problem while trying to communicate with OSS, "
            + "such as not being able to access the network.");
        log.error("Error Message:" + ce.getMessage());
        throw new RRException(ErrorCode.FILE_UPLOAD_ERROR);
    }

    /**
     * 处理oe异常
     */
    private void oeExceptionHandler(OSSException os) {
        log.error("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.");
        log.error("Error Message:" + os.getErrorMessage());
        log.error("Error Code:" + os.getErrorCode());
        log.error("Request ID:" + os.getRequestId());
        log.error("Host ID:" + os.getHostId());
        throw new RRException(ErrorCode.FILE_UPLOAD_ERROR);
    }

    /**
     * 返回文件访问url
     * @param fileName 文件名称
     * @return
     */
    private String getFileURL(String fileName) {
        return properties.getBucketEndpoint() + "/" + fileName;
    }
}
