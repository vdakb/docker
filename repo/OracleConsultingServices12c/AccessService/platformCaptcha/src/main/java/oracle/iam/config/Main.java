package oracle.iam.config;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;

import oracle.iam.config.captcha.Config;
import oracle.iam.config.captcha.ConfigService;

import oracle.iam.config.captcha.type.ColorScheme;

import oracle.iam.platform.core.captcha.Encoder;
import oracle.iam.platform.core.captcha.Digester;
import oracle.iam.platform.core.captcha.Challenge;

import oracle.iam.platform.core.captcha.color.Space;
import oracle.iam.platform.core.captcha.color.Palette;
import oracle.iam.platform.core.captcha.color.SingleColorFactory;
import oracle.iam.platform.core.captcha.color.GradientColorFactory;

import oracle.iam.platform.core.captcha.canvas.CanvasFactory;
import oracle.iam.platform.core.captcha.canvas.PerlinCanvasFactory;
import oracle.iam.platform.core.captcha.canvas.GradientCanvasFactory;
import oracle.iam.platform.core.captcha.canvas.PixelColorCanvasFactory;

import oracle.iam.platform.core.captcha.filter.FilterFactory;

public class Main {

  public Main() {
    super();
  }

  public static void main(String[] args) {
    final Config          context     = ConfigService.config(Main.class.getResourceAsStream("/META-INF/thn/captcha/config.json"));
    final ColorScheme     palette     = context.schema();
    final Space[]         foreground  = palette.foreground();
    final Space[]         background  = palette.background();
    final FilterFactory[] filter      = context.filter().factory();
    for (int i = 0; i < 10; i++) {
      final CanvasFactory[] canvas  = {
        PerlinCanvasFactory.build(GradientColorFactory.build(Palette.complementary(background[Digester.instance.nextInt(background.length)])))
      , GradientCanvasFactory.build(GradientColorFactory.build(Palette.complementary(background[Digester.instance.nextInt(background.length)])))
      , PixelColorCanvasFactory.build(SingleColorFactory.build(Space.awt(background[Digester.instance.nextInt(background.length)])))
      };
      CanvasFactory canvasFactory = canvas[Digester.instance.nextInt(canvas.length)];
      final Challenge challenge = Challenge.simple(
        canvasFactory
      , SingleColorFactory.build(Space.awt(foreground[Digester.instance.nextInt(foreground.length)]))
      , filter[Digester.instance.nextInt(filter.length)]
      )
        .size(context.canvas().size())
        .margin(context.canvas().margin())
      ;

      OutputStream fos = null;
      try {
        fos = new FileOutputStream(new File("saved" + i + ".png"));
        final DataOutputStream  out = new DataOutputStream(fos);
        // get the verification code object, with verification code picture and
        // verification code string
        String text = Encoder.generate(challenge, "png", out);
        System.out.println(text);
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
}
