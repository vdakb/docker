package oracle.iam.identity.foundation.jps;

import com.thortech.xl.crypto.tcCryptoUtil;

public class Password {
  public Password() {
    super();
  }
  public static void main(String[] args) {
    try {
      String encrypted = tcCryptoUtil.encrypt("wmkah1mdkh", "DBSecretKey", "UTF-8");
      System.out.println(encrypted);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
 }
}
