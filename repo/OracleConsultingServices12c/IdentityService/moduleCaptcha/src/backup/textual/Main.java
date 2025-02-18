package oracle.iam.service.captcha.textual;

import java.io.File;
import java.io.IOException;

import java.awt.Color;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Main {
	/**
	 ** Parameters of Captcha {WIDTH , HEIGHT , EXPIRY TIME }
	 */
	private static final int         WIDTH   = 400;
	private static final int         HEIGHT  = 200;

  public static void main(String[] args) {
		// build the captcha
    final Captcha captcha = Captcha.builder()
      .with(WIDTH + 0, HEIGHT + 0)
   		// generates the captcha text
//      .with(Producer.buildDefault(captchaTextLength, locales))
      // generates the captcha drawing
//      .with(Renderer.buildExtended(FRGR, SANS, MONO))
      .with(Canvas.gradient(Color.decode("0x0046a0"), Color.WHITE))
                                   
//      .with(Canvas.boxes(555, Arrays.asList(Color.PINK, Color.RED, Color.ORANGE, Color.LIGHT_GRAY, Color.MAGENTA, Color.CYAN, Color.WHITE)))
//      .with(Distorting.straightLine(Color.MAGENTA, 7))
      .with(Distorting.shear(Color.MAGENTA))
      .border()
      .mixedCase()
      .build()
    ;
    
    // retrieve image
    final BufferedImage img = captcha.image();
    final File          out = new File("saved.png");
    try {
      ImageIO.write(img, "png", out);
    }
    catch (IOException e) {
      // handle exception
      e.printStackTrace();
    }
  }
}
