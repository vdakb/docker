package oracle.iam.service.captcha.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import oracle.iam.platform.captcha.color.Palette;
import oracle.iam.platform.captcha.color.Space;

public class PaletteTest {

  public static void main(String[] args) {
    final Color   base = Color.blue;
    final Palette comp = Palette.monochromatic(Space.rgb(base.getRed(), base.getGreen(), base.getBlue()), 5);
    Color c1 = Space.awt(comp.color(0));
    Color c2 = Space.awt(comp.color(1));
    Color c3 = Space.awt(comp.color(2));
    Color c4 = Space.awt(comp.color(3));
    Color c5 = Space.awt(comp.color(4));

    final BufferedImage             image  = new BufferedImage(60, 150, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)image.getGraphics();
    g.setColor(c1);
    g.fillRect(0,   0, 60, 30);
    g.setColor(c2);
    g.fillRect(0,  30, 60, 30);
    g.setColor(c3);
    g.fillRect(0,  60, 60, 30);
    g.setColor(c4);
    g.fillRect(0,  90, 60, 30);
    g.setColor(c5);
    g.fillRect(0, 120, 60, 30);
    g.dispose();           
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