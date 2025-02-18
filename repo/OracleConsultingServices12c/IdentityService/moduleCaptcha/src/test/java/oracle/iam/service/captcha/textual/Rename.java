package oracle.iam.service.captcha.textual;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

import oracle.iam.service.captcha.textual.config.Czech;
import oracle.iam.service.captcha.textual.config.Greek;
import oracle.iam.service.captcha.textual.config.Dutch;
import oracle.iam.service.captcha.textual.config.Danish;
import oracle.iam.service.captcha.textual.config.Finish;
import oracle.iam.service.captcha.textual.config.French;
import oracle.iam.service.captcha.textual.config.Irish;
import oracle.iam.service.captcha.textual.config.Polish;
import oracle.iam.service.captcha.textual.config.German;
import oracle.iam.service.captcha.textual.config.Slovak;
import oracle.iam.service.captcha.textual.config.Italian;
import oracle.iam.service.captcha.textual.config.English;
import oracle.iam.service.captcha.textual.config.Latvian;
import oracle.iam.service.captcha.textual.config.Maltese;
import oracle.iam.service.captcha.textual.config.Spanish;
import oracle.iam.service.captcha.textual.config.Swedish;
import oracle.iam.service.captcha.textual.config.Estonian;
import oracle.iam.service.captcha.textual.config.Alphabet;
import oracle.iam.service.captcha.textual.config.Croatian;
import oracle.iam.service.captcha.textual.config.Romanian;
import oracle.iam.service.captcha.textual.config.Bulgarian;
import oracle.iam.service.captcha.textual.config.Hungarian;
import oracle.iam.service.captcha.textual.config.Portuguese;
import oracle.iam.service.captcha.textual.config.Lithuanian;
import oracle.iam.service.captcha.textual.config.Slovenian;

public class Rename {

  //////////////////////////////////////////////////////////////////////////////
  // static final  attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final File         path         = new File("./src/test/static/WEB-INF/resources/textual/audio");

  public static void main(String[] args) {
    rename(Bulgarian.build(path));
    rename(Croatian.build(path));
    rename(Czech.build(path));
    rename(Danish.build(path));
    rename(Dutch.build(path));
    rename(English.build(path));
    rename(Estonian.build(path));
    rename(Finish.build(path));
    rename(French.build(path));
    rename(German.build(path));
    rename(Greek.build(path));
    rename(Hungarian.build(path));
    rename(Irish.build(path));
    rename(Italian.build(path));
    rename(Latvian.build(path));
    rename(Lithuanian.build(path));
    rename(Maltese.build(path));
    rename(Polish.build(path));
    rename(Portuguese.build(path));
    rename(Romanian.build(path));
    rename(Slovak.build(path));
    rename(Slovenian.build(path));
    rename(Spanish.build(path));
    rename(Swedish.build(path));
 }

  private static void rename(final Alphabet alphabet) {
    final File root = new File(path, alphabet.language());
    for (Alphabet.Entry cursor : alphabet.digit()) {
      final File audio = new File(root, String.format("%1c.wav", cursor.character));
      final File dest  = new File(root, cursor.audio);
      if (audio.exists()) {
        audio.renameTo(dest);
      }
      else {
        if (!dest.exists())
          throw new RuntimeException("Missconfigured digit " + cursor.audio + " language " + alphabet.language());
      }
    }

    for (Alphabet.Entry cursor : alphabet.letter()) {
      final File audio = new File(root, String.format("%1c.wav", cursor.character));
      final File dest  = new File(root, cursor.audio);
      if (audio.exists()) {
        audio.renameTo(dest);
      }
      else {
        if (!dest.exists())
          throw new RuntimeException("Missconfigured letter " + cursor.audio + " language " + alphabet.language());
      }
    }
  }
}
