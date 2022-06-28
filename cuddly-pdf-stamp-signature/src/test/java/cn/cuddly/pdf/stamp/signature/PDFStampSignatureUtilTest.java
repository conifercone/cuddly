package cn.cuddly.pdf.stamp.signature;

import com.itextpdf.text.DocumentException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * PDF签名盖章工具类单元测试
 *
 * @author 单开宇
 * @since 2022-06-28
 */
@SuppressWarnings("all")
class PDFStampSignatureUtilTest {

  @Test
  void sign() throws IOException, GeneralSecurityException, DocumentException {
    String KEYSTORE = "E:\\iotest\\itextpdf\\test2.p12";
    char[] PASSWORD = "123456".toCharArray();//keystory密码
    String SRC = "E:\\iotest\\itextpdf\\javase.pdf";//原始pdf
    String DEST = "E:\\iotest\\itextpdf\\signjavase.pdf";//签名完成的pdf
    String chapterPath = "E:\\iotest\\itextpdf\\stamp.png";//签章图片
    String reason = "数据不可更改";
    String location = "beijing";
    PDFStampSignatureUtil.sign(new FileInputStream(KEYSTORE), PASSWORD, new FileInputStream(SRC),
        new FileOutputStream(DEST), reason, location, chapterPath, 0, 800, 100, 700, 1, "sig1");
  }

  @Test
  void verifySignature() throws Exception {
    String SRC = "E:\\iotest\\itextpdf\\javase.pdf";//原始pdf
    Assertions.assertTrue(PDFStampSignatureUtil.verifySignature(new FileInputStream(SRC)));
  }
}
