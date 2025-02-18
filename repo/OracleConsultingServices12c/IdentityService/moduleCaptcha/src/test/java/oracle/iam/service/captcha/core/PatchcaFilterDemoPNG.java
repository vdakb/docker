package oracle.iam.service.captcha.core;

import java.awt.Color;

import java.io.FileOutputStream;

import oracle.iam.service.captcha.core.color.SingleColorFactory;

import oracle.iam.service.captcha.core.canvas.SingleColorCanvasFactory;

import oracle.iam.service.captcha.core.filter.WobbleRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.CurvesRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.DoubleRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.MarbleRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.DiffuseRippleFilterFactory;

import oracle.iam.service.captcha.core.service.DefaultCaptchaService;

public class PatchcaFilterDemoPNG {

  public static void main(String[] args)
    throws Exception {
    for (int counter = 0; counter < 5; counter++) {
      DefaultCaptchaService cs = DefaultCaptchaService.build(SingleColorCanvasFactory.build());
      cs.width(260).height(70).color(SingleColorFactory.build(new Color(25, 60, 170)));
      cs.word(new SimpleWord());
      switch (counter % 5) {
        case 0: cs.filter(CurvesRippleFilterFactory.build().color(cs.color()));
          break;
        case 1: cs.filter(MarbleRippleFilterFactory.build());
          break;
        case 2: cs.filter(DoubleRippleFilterFactory.build());
          break;
        case 3: cs.filter(WobbleRippleFilterFactory.build());
          break;
        case 4: cs.filter(DiffuseRippleFilterFactory.build());
          break;
      }
      FileOutputStream fos = new FileOutputStream("captcha_demo" + counter + ".png");
      Encoder.generate(cs, "png", fos);
      fos.close();
    }
  }
}
