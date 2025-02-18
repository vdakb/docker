package oracle.iam.service.captcha.core;

import java.awt.image.BufferedImage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import oracle.iam.service.captcha.core.canvas.CanvasFactory;
import oracle.iam.service.captcha.core.canvas.SquigglesCanvasFactory;

public class SquigglesTest {

  public static void main(String[] args) {
    final BufferedImage image  = new BufferedImage(160, 70, BufferedImage.TYPE_INT_ARGB);
    final CanvasFactory canvas = SquigglesCanvasFactory.build();
    canvas.render(image);
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
