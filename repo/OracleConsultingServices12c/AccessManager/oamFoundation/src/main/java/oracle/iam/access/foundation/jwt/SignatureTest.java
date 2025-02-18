package oracle.iam.access.foundation.jwt;

import java.util.Enumeration;

import java.io.File;
import java.io.FileInputStream;

import java.nio.charset.StandardCharsets;

import java.security.KeyStore;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.cert.Certificate;

import oracle.hst.foundation.utility.Base64Decoder;

public class SignatureTest {
  private static String token1 = "eyJraWQiOiJkZWZhdWx0IiwieDV0IjoiME4zaHdXVXlUZEVkdHpuSHctemY2RzQzX0tVIiwiYWxnIjoiUlMyNTYifQ.eyJleHAiOjE1NDA0MDEwNzQsImp0aSI6Im5sT1RGZDRMbG5ZaDBacnBWMkRIUnciLCJpYXQiOjE1NDAzOTc0NzQsInN1YiI6InNzdHJlY2tlIiwic2Vzc2lvbl9pZCI6IjY4ckFTODhwNm9yMzQzNEFoRlZ0c2c9PX5PS0lCM3d1SmdzQUlrV3lsTVdHMFZnYlQ1UVZWc3J4MDF3NjVIUmVHdmdBQ2wyenptcFBnZFhPTnc2OHhOU3dDdVNVMncyZWw0TWV3VWlvTjNpV1lWZ1EzSG5Rb2dzcHR0Y1hMaFRja21nOWQ0bHloK2ZhZmJlejFlZlM0T2JmbSIsImRvbWFpbiI6ImRlZmF1bHQifQ.m9mb4fcchnVZZn-2SxHLIGwqOl0fUwdJ6BskS5jALPF-cr_GKjo5Rm8jOygcih8-9gljOP5tWgfVCmzEFi7tftouEeWsKOfWB0xLB4ApXytMXVE3dGSFuSV92uLT-QTidojuIwwSJAc_95ZqWdefaZ6SNJy_KR6OAoonLfbjFvHYfa87EqX0OqrBG_ne2dtUAiUd801s3wW5EGFdBKVPQwai_u--4Z51mQL_cJY60qD4ipUIsry9re_YM-xvO4GDW7Ky8bQPitsuShlyfoXkUHxv3tSkrQt19kC2RyyX6DJNV-E9vBaQDADZc4Tc9Uaz9P2q-FvQ4-7xksjpBf97KA";
  private static String token2 = "eyJraWQiOiJkZWZhdWx0IiwieDV0IjoiME4zaHdXVXlUZEVkdHpuSHctemY2RzQzX0tVIiwiYWxnIjoiUlMyNTYifQ.eyJleHAiOjE1NDA0MDEwNzQsImp0aSI6Im5sT1RGZDRMbG5ZaDBacnBWMkRIUnciLCJpYXQiOjE1NDAzOTc0NzQsInN1YiI6InNzdHJlY2tlIiwic2Vzc2lvbl9pZCI6IjY4ckFTODhwNm9yMzQzNEFoRlZ0c2c9PX5PS0lCM3d1SmdzQUlrV3lsTVdHMFZnYlQ1UVZWc3J4MDF3NjVIUmVHdmdBQ2wyenptcFBnZFhPTnc2OHhOU3dDdVNVMncyZWw0TWV3VWlvTjNpV1lWZ1EzSG5Rb2dzcHR0Y1hMaFRja21nOWQ0bHloK2ZhZmJlejFlZlM0T2JmbSIsImRvbWFpbiI6ImRlZmF1bHQifQ.m9mb4fcchnVZZn-2SxHLIGwqOl0fUwdJ6BskS5jALPF-cr_GKjo5Rm8jOygcih8-9gljOP5tWgfVCmzEFi7tftouEeWsKOfWB0xLB4ApXytMXVE3dGSFuSV92uLT-QTidojuIwwSJAc_95ZqWdefaZ6SNJy_KR6OAoonLfbjFvHYfa87EqX0OqrBG_ne2dtUAiUd801s3wW5EGFdBKVPQwai_u--4Z51mQL_cJY60qD4ipUIsry9re_YM-xvO4GDW7Ky8bQPitsuShlyfoXkUHxv3tSkrQt19kC2RyyX6DJNV-E9vBaQDADZc4Tc9Uaz9P2q-FvQ4-7xksjpBf97KA";
  private static String token3 = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsIng1dCI6ImEzck1VZ01Gdjl0UGNsTGE2eUYzekFrZnF1RSIsImtpZCI6ImEzck1VZ01Gdjl0UGNsTGE2eUYzekFrZnF1RSJ9.eyJub25jZSI6IjYzNjA3MDM0OTc3NDIzODg2NS5OMlkxTldKbU1EZ3RZbU13TkMwME9XWTNMVGt5TlRJdE9ERXpOell4Wm1NME0yVmxNV1l5TkdOaFlXTXRaVEpqT1MwME4yRmpMVGd6WmpVdFpXWTVOVEEwWmpFMU1qWTEiLCJpYXQiOjE0NzE0MzgxODIsImF0X2hhc2giOiJLWUJpVkl1Uy1YZERzU3NHcWU5dTJBIiwic3ViIjoiMSIsImFtciI6InBhc3N3b3JkIiwiYXV0aF90aW1lIjoxNDcxNDM4MTgyLCJpZHAiOiJpZHNydiIsImlzcyI6Imh0dHBzOi8vZWx3ZWJhcHBsaWNhdGlvbjEuYXp1cmV3ZWJzaXRlcy5uZXQvaWRlbnRpdHkiLCJhdWQiOiJtdmMiLCJleHAiOjE0NzE0Mzg0ODIsIm5iZiI6MTQ3MTQzODE4Mn0.Ehck2-rA09cJzlfURhDMp-WcXm_t_dl-u0Mli3exdv1HxX8i77x5VfFPM6rP4lcpI3lpN8Yj-FefZYDTUY_UmxCYvXf6ILSrhzEfQVaXSPKX1RUQQIDJGPU6NuFLcR416JpUAkE8joYae3WPj5VsM4yNENGGjUANm4qgj6G_mYy_BiXcSqvRGRYwW5GHDsnnANrIw4oktIYS05yCbjdiNYgQZ043L6Pb2p-5eTPCFqG7WRHp208dhg8D3nhtYEov2Kxod93oKHXSp1zf-Ot0cadk6Ss4fClaTE9S1f29lbwxw7ZxI1L3R4oOL3FZPSSHGp4d3a3AdUKOjKvvTVPv6w";

  public static void main(String[] args) {
    final File     publicKeyFile  = new File("D:\\Project\\OracleConsultingServices12c\\AccessManager\\assertion.cer");
    final File     privateKeyFile = new File("D:\\Project\\OracleConsultingServices12c\\AccessManager\\assertion.key");
    final File     keyStoreFile   = new File("D:\\Project\\OracleConsultingServices12c\\AccessManager\\.oamkeystore");

    PublicKey  publicKey  = null;
    PrivateKey privateKey = null;
    try {
      KeyStore keyStore = KeyStore.getInstance("JCEKS");
      // Provide location of Java Keystore and password for access
      keyStore.load(new FileInputStream(keyStoreFile), "ifar42e5ntueh3f1tirm2q6lhm".toCharArray());
      // iterate over all aliases
      Enumeration<String> es = keyStore.aliases();
      while (es.hasMoreElements()) {
        final String alias = es.nextElement();
        if ("stscertalias_sha256".equals(alias)) {
          // Get certificate of public key
          final Certificate cert = keyStore.getCertificate(alias);
          // Get public key
          publicKey = cert.getPublicKey();
        }
        // if alias refers to the desired private key break at that point
        // as we want to use that certificate
        if ("assertion-key".equals(alias)) {
          final KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, new KeyStore.PasswordProtection("ifar42e5ntueh3f1tirm2q6lhm".toCharArray()));
          privateKey = entry.getPrivateKey();
          // Get certificate of public key
//          final Certificate cert = keyStore.getCertificate(alias);
          // Get public key
//          publicKey = cert.getPublicKey();
          break;
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    final String[] part           = token3.split("\\.");
    // spilt the JWT in chunks
    String         header  = null;
    String         payload = null;
    try {
      // the first chunk is the JWT header.
      // It indicates that it's a JWT request, and indicates the type of hashing algorithm used.
      header = new String(Base64Decoder.decode(part[0].getBytes()), "UTF-8");
      // the second chunk is considerably longer because it contains the payload.
      // It's known as the 'JWT Claims Set'.
      payload = new String(Base64Decoder.decode(part[1].getBytes()), "UTF-8");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    // the last chunk of the request is the encrypted part.
    // You don't need to worry too much about this, but basically it takes all
    // the information above (iat, jti, name, email, etc) along with the shared
    // secret and generates an encrypted string out of all of it. Then, per JWT
    // standards, a chunk of that encrypted string is taken (known as a
    // checksum), and that is the JWS signature.
    final Algorithm  algorithm  = Algorithm.RS256;
    final Signer signer  = new oracle.iam.access.foundation.jwt.rsa.Signer(algorithm, privateKey);
    final String message = String.format("%s.%s", part[0], part[1]);
    byte[] guess  = signer.sign(message);
    final Verifier verifier = new oracle.iam.access.foundation.jwt.rsa.Verifier(PEMUtility.publicKey(privateKeyFile));
    final byte[]     expected  = Decoder.base64Decode(part[2]);
    verifier.verify(algorithm, message.getBytes(StandardCharsets.UTF_8), expected);
  }
}
