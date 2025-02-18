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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Service Extension
    Subsystem   :   Java Server Faces Feature

    File        :   Path.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Path.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-07-10  DSteding    First release version
*/

package oracle.hst.platform.jsf.view;

import java.util.Map;
import java.util.HashMap;

import java.io.InputStream;
import java.io.IOException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import javax.json.JsonReader;

import oracle.hst.platform.core.utility.FileSystem;

////////////////////////////////////////////////////////////////////////////////
// class Path
// ~~~~~ ~~~~
/**
 ** Java Server Faces (JSF) is a very useful server-side rendering framework to
 ** create HTML UI in Java projects. It?s part of the Java EE specifications.
 ** One of the drawbacks of Java Server Faces is the generated URLs for our
 ** pages, they follow as pattern the structure of the folders in the project.
 ** <p>
 ** From a UIX perspective, having URL's like
 ** <code>/pages/user/user-list.jsf</code> looks ugly, it's better to have an
 ** URL like <code>/users</code>, <code>/user/new</code>. Also, from a security
 ** perspective, we are exposing our folder's structure to other people, and
 ** they can use that information to find a vulnerability.
 ** <p>
 ** That?s why there are some third party libraries for JSF like pretty-faces,
 ** that allows to use friendly and nice URL's in our JSF application. However,
 ** they usually offer a bunch of functionalities (patterns, mapping path
 ** params, and more) that are not necesary in some of the applications.
 ** Therefore, we have created a very simple rewrite URL for JSF and avoid
 ** overload the application with things that not needed.
 ** <br>
 ** There is only one existing instance of the class in a JVM; it is implemented
 ** as singleton.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class Path {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String       ROUTE    = "/META-INF/routing.json";

  /** the singleton instance */
  private static Path               instance = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Map<String, String> page;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Path</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   ** <br>
   ** Access modifier private prevents other classes using "new Path()"
   **
   ** @throws IOException        if there's a problem reading the stream.
   */
  private Path()
    throws IOException {

    // ensure inheritance
    super();
    
    // initialize instance attributes
    this.page = new HashMap<String, String>();
    final InputStream stream = FileSystem.inputStream(ROUTE, (Class)null);
    if (stream != null) {
      final JsonReader reader  = Json.createReader(stream);
      final JsonObject route   = reader.readObject();
      final JsonArray  mapping = route.getJsonArray("map");
      for (int i = 0; i < mapping.size(); i++) {
        final JsonObject entry = mapping.getJsonObject(i);
        if (entry.getString("id") != null) {
          this.page.put(entry.getString("pattern"), entry.getString("view"));
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   page
  /**
   ** Returns the registered pages of the <code>Router</code>.
   **
   ** @return                    the registered pages of the
   **                            <code>RewriteRouter</code>.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link String} for the value.
   */
  public Map<String, String> page() {
    return this.page;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods gouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   instance
  /**
   ** Returns the sole instance of this of the factory.
   **
   ** @return                    the sole instance of this of the factory.
   **                            <br>
   **                            Possible object is <code>Path</code>.
   */
  public static Path instance(){
    synchronized (Path.class) {
      if (instance == null) {
        try {
          instance = new Path();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
      return instance;
    }
  }
}