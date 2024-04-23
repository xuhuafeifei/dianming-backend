import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeGenerator {

    public static void main(String[] args) {
        String qrCodeData = "https://book-recommender-fnq9.onrender.com/"; // 要编码的数据
        String filePath = "E:\\点名\\微服务\\dianming\\dianming-test\\Two-dimensional-code\\src\\main\\resources\\image\\qrcode.png"; // 保存的文件路径
        int width = 300; // 图片宽度
        int height = 300; // 图片高度


        try {
            generateQRCode(qrCodeData, filePath, width, height);
            System.out.println("二维码已生成。");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateQRCode(String qrCodeData, String filePath, int width, int height)
            throws WriterException, IOException {
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

        File outputFile = new File(filePath);
        ImageIO.write(image, "png", outputFile);
    }
}
