package cn.cuddly.pdf.stamp.signature;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * PDF签名盖章工具类
 *
 * @author 单开宇
 * @since 2022-06-27
 */
@SuppressWarnings("unused")
public class PDFStampSignatureUtil {

  public static void sign(InputStream p12Stream, //p12 路径
      char[] password,
      InputStream src,//需要签章的pdf文件路径
      OutputStream dest,// 签完章的pdf文件路径
      String reason,//签名的原因，显示在pdf签名属性中
      String location,//签名的地点，显示在pdf签名属性中
      String chapterPath,//电子签章的图片
      float llx,//左下角x
      float lly,//左下角y
      float urx,//右上角x
      float ury,//右上角y
      int page,//页码
      String signatureField//签名域
  ) throws GeneralSecurityException, IOException, DocumentException {
    //读取keystone，获得私钥和证书链
    KeyStore pkcs12 = KeyStore.getInstance("PKCS12");
    pkcs12.load(p12Stream, password);
    String alias = pkcs12.aliases().nextElement();
    PrivateKey key = (PrivateKey) pkcs12.getKey(alias, password);
    Certificate[] chain = pkcs12.getCertificateChain(alias);

    PdfReader reader = new PdfReader(src);

    //目标文件输出流
    //创建签章工具PdfStamper ，最后一个boolean参数
    //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
    //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
    PdfStamper stamper = PdfStamper.createSignature(reader, dest, '\0', null, false);
    // 获取数字签章属性对象，设定数字签章的属性
    PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
    appearance.setReason(reason);
    appearance.setLocation(location);

    //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名域名称不能一样
    //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
    //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
    appearance.setVisibleSignature(new Rectangle(llx, lly, urx, ury), page, signatureField);

    //读取图章图片，这个image是itext包的image
    Image image = Image.getInstance(chapterPath);
    appearance.setSignatureGraphic(image);
    appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
    //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
    appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

    // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
    // 摘要算法
    BouncyCastleDigest digest = new BouncyCastleDigest();
    // 签名算法
    PrivateKeySignature signature = new PrivateKeySignature(key, DigestAlgorithms.SHA1, null);
    // 调用itext签名方法完成pdf签章CryptoStandard.CMS 签名方式，建议采用这种
    MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0,
        MakeSignature.CryptoStandard.CMS);
  }

  public static boolean verifySignature(InputStream resource) throws Exception {
    BouncyCastleProvider bcp = new BouncyCastleProvider();
    //Security.addProvider(bcp);
    Security.insertProviderAt(bcp, 1);
    Field rsaDataField = PdfPKCS7.class.getDeclaredField("RSAdata");
    rsaDataField.setAccessible(true);

    assert resource != null;
    PdfReader reader = new PdfReader(resource);
    AcroFields acroFields = reader.getAcroFields();

    List<String> names = acroFields.getSignatureNames();
    if (names.isEmpty()) {
      return false;
    }
    for (String name : names) {
      System.out.println("Signature name: " + name);
      System.out.println("Signature covers whole document: " + acroFields.signatureCoversWholeDocument(name));
      PdfPKCS7 pk = acroFields.verifySignature(name);
      X509Certificate certificate = pk.getSigningCertificate();

      System.out.println("Subject: " + CertificateInfo.getSubjectFields(pk.getSigningCertificate()));

      Object rsaDataFieldContent = rsaDataField.get(pk);
      if (rsaDataFieldContent != null && ((byte[]) rsaDataFieldContent).length == 0) {
        System.out.println("Found zero-length encapsulated content: ignoring");
        rsaDataField.set(pk, null);
      }
      if (!pk.verify()) {
        return false;
      }
    }
    return true;
  }

}
