package oracle.iam.service.captcha.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import oracle.iam.service.captcha.core.filter.MarbleRippleFilterFactory;

public class MarbleTest {

  public static void main(String[] args) {
    final BufferedImage             image  = new BufferedImage(160, 70, BufferedImage.TYPE_INT_ARGB);
    final MarbleRippleFilterFactory ripple = MarbleRippleFilterFactory.build();
    ripple.apply(image);
    OutputStream fos = null;
    try {
      fos = new FileOutputStream(new File("saved.png"));
      final DataOutputStream  out = new DataOutputStream(fos);
      ImageIO.write(image, "png", out);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (fos != null) {
        try {
          fos.flush();
          fos.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}