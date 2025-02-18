package oracle.iam.service.captcha.textual;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.fasterxml.jackson.core.JsonFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

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

public class Test {

  //////////////////////////////////////////////////////////////////////////////
  // static final  attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final ObjectMapper objectMapper = initialize();
  private static final File         path         = new File("./src/main/static/WEB-INF/resources/textual");
  public static void main(String[] args) {
    spool(Bulgarian.build(path));
    spool(Croatian.build(path));
    spool(Czech.build(path));
    spool(Danish.build(path));
    spool(Dutch.build(path));
    spool(English.build(path));
    spool(Estonian.build(path));
    spool(Finish.build(path));
    spool(French.build(path));
    spool(German.build(path));
    spool(Greek.build(path));
    spool(Hungarian.build(path));
    spool(Irish.build(path));
    spool(Italian.build(path));
    spool(Latvian.build(path));
    spool(Lithuanian.build(path));
    spool(Maltese.build(path));
    spool(Polish.build(path));
    spool(Portuguese.build(path));
    spool(Romanian.build(path));
    spool(Slovak.build(path));
    spool(Slovenian.build(path));
    spool(Spanish.build(path));
    spool(Swedish.build(path));
  }

  private static void spool(final Alphabet alphabet) {
    PrintWriter stream = null;
    try {
      stream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(alphabet.path(), false), StandardCharsets.UTF_8));
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(stream, alphabet);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    finally {
      if (stream != null) {
        stream.flush();
        stream.close();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Creates a custom Jackson compatible {@link ObjectMapper}.
   ** <br>
   ** Creating new [@link ObjectMapper} instances are expensive so instances
   ** should be shared if possible.
   ** <br>
   ** This can be used to set the factory used to build new instances of the
   ** object mapper used by the SDK.
   **
   ** @return                    an [@link ObjectMapper} with the correct
   **                            options set for serializing and deserializing
   **                            JSON objects.
   */
  private static ObjectMapper initialize() {
    final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    // don't serialize POJO nulls as JSON nulls.
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // only use xsd:dateTime format for dates.
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    // do not care about case when deserializing POJOs.
    mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

    return mapper;
  }
}