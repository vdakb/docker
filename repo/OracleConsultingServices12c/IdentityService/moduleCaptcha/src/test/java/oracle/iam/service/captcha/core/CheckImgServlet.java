package oracle.iam.service.captcha.core;

import java.awt.Color;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;

import oracle.iam.platform.captcha.color.Palette;
import oracle.iam.platform.captcha.color.Space;
import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.service.captcha.core.canvas.GradientCanvasFactory;
import oracle.iam.service.captcha.core.canvas.PerlinNoiseCanvasFactory;
import oracle.iam.service.captcha.core.canvas.SingleColorCanvasFactory;
import oracle.iam.service.captcha.core.word.RandomWordFactory;

import oracle.iam.service.captcha.core.color.ColorFactory;
import oracle.iam.service.captcha.core.color.GradientColorFactory;
import oracle.iam.service.captcha.core.color.SingleColorFactory;

import oracle.iam.service.captcha.core.service.DefaultCaptchaService;

import oracle.iam.service.captcha.core.filter.CurvesRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.DiffuseRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.DoubleRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.MarbleRippleFilterFactory;
import oracle.iam.service.captcha.core.filter.WobbleRippleFilterFactory;

// https://www.programmersought.com/article/22153273441/
public class CheckImgServlet {

  /** String length codes, default 4 */
  private static Integer                    rcl   = 8;
  /** CAPTCHA image width, default 120 */
  private static Integer                    rcw   = 30 * rcl;
  /** CAPTCHA image height, default 50 */
  private static Integer                    rch   = 75;
  /**
   ** style codes
   ** A curved
   ** 2 swing dress
   ** Shake 3
   ** 4 spread
   ** 5 Marble
   ** 6 random (default)
   */
  private static Integer                    rct   = 6;

  private static Palette                    canvas   = Palette.triadic(Space.rgb(Color.lightGray.getRed(), Color.lightGray.getGreen(), Color.lightGray.getBlue()));
  private static Palette                    visual   = Palette.monochromatic(Space.rgb(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue()), 7);

//  private static DefaultCaptchaService      cs    = DefaultCaptchaService.build(SingleColorCanvasFactory.build()).width(rcw).height(rch);
  private static DefaultCaptchaService      cs    = DefaultCaptchaService.build(PerlinNoiseCanvasFactory.build(GradientColorFactory.build(Space.awt(canvas.color(0)), Space.awt(canvas.color(1))))).width(rcw).height(rch);
    private static ColorFactory               cf    = SingleColorFactory.build(new Color(25, 60, 170));
  private static RandomWordFactory          wf    = RandomWordFactory.build().lengthLower(rcl).lengthUpper(rcl);
  private static CurvesRippleFilterFactory  crff  = CurvesRippleFilterFactory.build().color(cs.color());
  private static MarbleRippleFilterFactory  mrff  = MarbleRippleFilterFactory.build();
  private static DoubleRippleFilterFactory  drff  = DoubleRippleFilterFactory.build();
  private static WobbleRippleFilterFactory  wrff  = WobbleRippleFilterFactory.build();
  private static DiffuseRippleFilterFactory dirff = DiffuseRippleFilterFactory.build();

  public static void main(String[] args) {
    cs.word(wf);
//    cs.color(cf);
    //      randomCodeSessionAttributeName=null==this.getInitParameter("randomCodeSessionAttributeName")?"PATCHCA":this.getInitParameter("randomCodeSessionAttributeName");
    int typeCode=rct;
    if (rct == 6) {
      typeCode = Digester.instance.nextInt(5);
    }
    // generates five kinds of random effect (filter)
    switch (typeCode) {
      case 1  : cs.filter(crff);
                  break;
      case 2  : cs.filter(mrff);
                  break;
      case 3  : cs.filter(drff);
                  break;
      case 4  : cs.filter(wrff);
                  break;
      case 5  : cs.filter(dirff);
                  break;
      default : cs.filter(crff);
    }
//  cs.color(SingleColorFactory.build(new Color(125, 60, 170)));
    cs.color(
      new ColorFactory() {
        public Color get(final int index) {
/*          
          int[] c = new int[3];
          int   i = Digester.instance.nextInt(c.length);
          for (int fi = 0; fi < c.length; fi++) {
            if (fi == i) {
              c[fi] = Digester.instance.nextInt(71);
            }
            else {
              c[fi] = Digester.instance.nextInt(visual.color(i));
            }
          }

          return new Color(c[0], c[1], c[2]);
*/
          int   i = Digester.instance.nextInt(7);
          return Space.awt(visual.color(i));
        }
      }
    );
    OutputStream fos = null;
    try {
      fos = new FileOutputStream(new File("saved.png"));
      final DataOutputStream  out = new DataOutputStream(fos);
      String challenge= Encoder.generate(cs, "png", out);
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
