package oracle.iam.service.captcha.core;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import java.awt.Color;

import oracle.iam.platform.captcha.color.Palette;
import oracle.iam.platform.captcha.color.Space;
import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.platform.captcha.visual.ContextConfig;

import oracle.iam.service.captcha.core.font.RandomFontFactory;

import oracle.iam.service.captcha.core.word.RandomWordFactory;

import oracle.iam.service.captcha.core.filter.library.WobbleImageOp;

import oracle.iam.service.captcha.core.service.CaptchaService;
import oracle.iam.service.captcha.core.service.DefaultCaptchaService;

import oracle.iam.service.captcha.core.canvas.PerlinNoiseCanvasFactory;

import oracle.iam.service.captcha.core.color.ColorFactory;
import oracle.iam.service.captcha.core.color.SingleColorFactory;
import oracle.iam.service.captcha.core.color.GradientColorFactory;

import oracle.iam.service.captcha.core.color.RandomColorFactory;
import oracle.iam.service.captcha.core.filter.FilterFactory;
import oracle.iam.service.captcha.core.filter.DefaultFilterFactory;

import oracle.iam.service.captcha.core.filter.library.RippleImageOp;
import oracle.iam.service.captcha.core.filter.library.MarbleImageOp;
import oracle.iam.service.captcha.core.filter.library.CurvesImageOp;
import oracle.iam.service.captcha.core.filter.library.DiffuseImageOp;
import oracle.iam.service.captcha.core.filter.library.DoubleRippleImageOp;

import oracle.iam.service.captcha.core.renderer.CenterTextRenderer;
import oracle.iam.service.captcha.core.renderer.RandomTextRenderer;

// https://www.programmersought.com/article/61283904536/
public class ValidationCodeAction {

  private static ContextConfig  cfg      = ContextConfig.build();

  private static Palette        canvas   = Palette.monochromatic(Space.rgb(Color.magenta.getRed(), Color.magenta.getGreen(), Color.magenta.getBlue()));

  private static ColorFactory   cf       = SingleColorFactory.build(Color.black);
  // picture filter settings
  private static FilterFactory  filter;

  private static CaptchaService service;
  static {
    cfg.size().height(64.0).width(160.0);
    cfg.renderer().lengthLower(8).lengthUpper(8).fontSizeLower(32).fontSizeUpper(32);

    // generates five kinds of random effect (filter)
    switch (Digester.instance.nextInt(5)) {
      case 2  : filter = DefaultFilterFactory.build(RippleImageOp.build(), MarbleImageOp.build());
                break;
      case 3  : filter = DefaultFilterFactory.build(RippleImageOp.build(), DoubleRippleImageOp.build());
                break;
      case 4  : filter = DefaultFilterFactory.build(RippleImageOp.build(), WobbleImageOp.build().amplitudeLength(6.0).amplitudeHeight(7.0));
                break;
      case 5  : filter = DefaultFilterFactory.build(RippleImageOp.build(), DiffuseImageOp.build());
                break;
      default : filter = DefaultFilterFactory.build(RippleImageOp.build(), CurvesImageOp.build().color(cf));
    }

//    service = DefaultCaptchaService.build(PerlinNoiseCanvasFactory.build(GradientColorFactory.build(new Color(20, 20, 170), new Color(75, 60, 170))))
    service = DefaultCaptchaService.build(PerlinNoiseCanvasFactory.build(GradientColorFactory.build(Space.awt(canvas.color(0)), Space.awt(canvas.color(1)))))
      .size(cfg.size())
      .margin(cfg.margin())
      // color creation factory, using random colors within a certain range
      .color(cf)
      // random font generator
      .font(RandomFontFactory.build().lower(cfg.renderer().fontSize().lower()).upper(cfg.renderer().fontSize().upper()))
      // random character generator, remove letters and numbers that are easily
      // confused, such as o and 0, etc.
      .word(RandomWordFactory.build().length(cfg.renderer().length()))
      // text renderer settings
      .renderer(CenterTextRenderer.build())
      .filter(filter)
    ;
  }

  public static void main(String[] args) {
    OutputStream fos = null;
    try {
      fos = new FileOutputStream(new File("saved.png"));
      final DataOutputStream  out = new DataOutputStream(fos);
      // get the verification code object, with verification code picture and
      // verification code string
      String challenge= Encoder.generate(service, "png", out);
      System.out.println(challenge);
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
