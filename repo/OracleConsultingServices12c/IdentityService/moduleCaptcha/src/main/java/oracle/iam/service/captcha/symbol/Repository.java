/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Repository.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Repository.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.symbol;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import oracle.hst.platform.core.logging.AbstractLogger;

import oracle.iam.platform.captcha.core.Digester;

import oracle.iam.service.captcha.resources.SymbolBundle;

////////////////////////////////////////////////////////////////////////////////
// class Repository
// ~~~~~ ~~~~~~~~~~
/**
 ** The persistence layer to kepp resources initiated at application startup
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Repository extends AbstractLogger<Repository> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final  Repository instance           = new Repository();

  public static final  int        OPTION_DEFAULT     = 5;

  private static final String     PATH_IMAGE_DEFAULT = "/WEB-INF/resources/symbol/images/";
  private static final String     PATH_AUDIO_DEFAULT = "/WEB-INF/resources/symbol/audios/";

  private static final String     PATH_IMAGE         = "symbol-image-path";
  private static final String     PATH_AUDIO         = "symbol-audio-path";

  private static final String     CONFIG_IMAGE       = "/WEB-INF/resources/symbol/images.json";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // the base path to the image files
  private String                       image;

  // the base path to the audio files
  private String                       audio;

  // all the image options.
  // These can be easily overwritten or extended using addImageOptions(<Array>),
  // or replaceImageOptions(<Array>)
  // By default, they're populated using the ./images.json file
  private List<Captcha.Image>          images  = new ArrayList<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Repository</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Repository() {
    // ensure inheritance
    super(Repository.class);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Returns the base path to the image resources.
   **
   ** @return                    the base path to the image resources.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String image() {
    return this.image;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   audio
  /**
   ** Returns the base path to the audio resources.
   **
   ** @return                    the base path to the audio resources.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String audio() {
    return this.audio;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize
  /**
   ** Initialize the web application.
   **
   ** @param  context            the {@link ServletContext} with context
   **                            parameter populated.
   **                            <br>
   **                            Allowed object is {@link ServletContext}.
   **
   ** @throws ServletException   in the event the required resources could not
   **                            be loaded.
   */
  public synchronized void initialize(final ServletContext context)
    throws ServletException {

    final String method = "initialize";
    entering(method);
    InputStream stream = null;
    try {
      stream = context.getResourceAsStream(CONFIG_IMAGE);
      if (stream != null) {
        try {
          this.images = Serializer.images(stream);
        }
        catch (IOException e) {
          throw new ServletException(SymbolBundle.string("init.image.failed"), e);
        }
        this.image = context.getInitParameter(PATH_IMAGE);
        if (this.image == null)
          this.image = PATH_IMAGE_DEFAULT;
        if (!this.image.endsWith("/"))
          this.image = this.image + "/";

        this.audio = context.getInitParameter(PATH_AUDIO);
        if (this.audio == null)
          this.audio = PATH_AUDIO_DEFAULT;
        if (!this.image.endsWith("/"))
          this.image = this.image + "/";
      }
      else
        throw new ServletException(SymbolBundle.format("init.image.config", CONFIG_IMAGE));
    }
    finally {
      if (stream != null) {
        try {
          stream.close();
        }
        catch (IOException e) {
          throw new ServletException(SymbolBundle.string("init.image.failed"), e);
        }
      }
      exiting(method);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   randomize
  public final List<Captcha.Tile> randomize(final int count, final String salt) {
    synchronized(instance) {
      final String method = "randomize";
      entering(method);
      // create a copy of the image repository
      final List<Captcha.Image> images = new ArrayList<>(this.images);
      // randomly permute the collection of images using a default source of
      // randomness
      Collections.shuffle(images);
      // transfer the amount of images to the selection buffer
      final List<Captcha.Tile> selection = new ArrayList<>(count);
      for (Captcha.Image cursor : images.subList(0, count)) {
        selection.add(Captcha.tile(cursor.name(), Digester.instance.hash(cursor.name(), salt), cursor.image()));
      }
      // randomly permute the selection of images using a default source of
      // randomness
      Collections.shuffle(selection);
      exiting(method);
      return selection;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stream
  /**
   ** Stream image file given a name in the session captcha images array.
   **
   ** @param  source             the {@link InputStream} to a resource.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the desired image.
   **                            <br>
   **                            Possible object is array of <code>byte</code>s.
   **
   ** @throws IOException        if an input or output error is detected when
   **                            this servlet handles the GET request.
   */
  public byte[] stream(final InputStream source)
    throws IOException {

    int length;
    final ByteArrayOutputStream bos = new ByteArrayOutputStream();
    // create a buffer of 4KB to decrease the iterations
    final byte[] buffer = new byte[4096];
    // read bytes from stream, and store them in buffer
    while ((length = source.read(buffer)) > 0) {
      // Writes bytes from byte array (buffer) into output stream.
      bos.write(buffer, 0, length);
    }
    source.close();
    bos.flush();
    bos.close();
    return bos.toByteArray();
  }
}
