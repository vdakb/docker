package oracle.iam.service.captcha.core;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import oracle.iam.platform.captcha.color.Space;

public class SpaceTest {
  public static void main(String[] args) {
    
    int y =  0;
    int h = 30;
    final BufferedImage i = new BufferedImage(60, 650, BufferedImage.TYPE_INT_ARGB);
    final Graphics2D    g = (Graphics2D)i.getGraphics();
    g.setColor(Space.awt(Space.primary()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.secondary()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.success()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.danger()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.info()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.warning()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.ligth()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.dark()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.muted()));
    g.fillRect(0,  y, 60, h);
    y += h;
    y += h;
    g.setColor(Space.awt(Space.antiqueWhite()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.oldLace()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.ivory()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.seaShell()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.ghostWhite()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.snow()));
    g.fillRect(0,  y, 60, h);
    y += h;
    g.setColor(Space.awt(Space.linen()));
    g.fillRect(0,  y, 60, h);
    y += h;
    y += h;

    g.setColor(Space.awt(Space.steelBlue()));
    g.fillRect(0,  y, 60, h);
    g.dispose();           
    OutputStream fos = null;
    try {
      fos = new FileOutputStream(new File("saved.png"));
      final DataOutputStream  out = new DataOutputStream(fos);
      ImageIO.write(i, "png", out);
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
