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

    File        :   Captcha.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Captcha.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.symbol;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

////////////////////////////////////////////////////////////////////////////////
// class Captcha
// ~~~~~ ~~~~~~~
/**
 ** The <code>Captcha</code> challenge/response state.
 ** <p>
 ** The implementation of this class is inspired on the work of
 ** https://github.com/bdotzour/visualCaptcha-java
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Captcha {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final int PRIME      = 31;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String           name;
  private String           response;
  private List<Tile>       tile;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Image
  // ~~~~~ ~~~~~
  /**
   ** The <code>Captcha</code> <code>Image</code> send back by a client.
   */
  public static class Image implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-6260661225065302985")
    private static final long serialVersionUID = 2498349561494810331L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    // the name is used to support national languages
    private String            name;
    @JsonProperty
    private String            image;

   ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Image</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    protected Image() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Image</code> with the specified properties.
     **
     ** @param  name             the translatable name of the image.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  image            the name of the image file.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Image(final String name, final String image) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.name  = name;
      this.image = image;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Methode: name
    /**
     ** Returns the name value of the <code>Response</code>.
     **
     ** @return                  the name value of the <code>Response</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methode: image
    /**
     ** Returns the image value of the <code>Response</code> glyph.
     **
     ** @return                  the image value of the <code>Response</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String image() {
      return this.image;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.name != null ? this.name.hashCode() : 0;
      result = PRIME * result + (this.image      != null ? this.image.hashCode()      : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Challenge
  // ~~~~~ ~~~~~~~~~
  /**
   ** The <code>Captcha</code> <code>Challenge</code> delivered back to a
   ** client.
   */
  public static class Challenge implements Serializable {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-5646192698725722838")
    private static final long serialVersionUID = 4095391272763688323L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    private String            name;
    @JsonProperty
    private String            image;
    @JsonProperty
    private List<String>      values;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Challenge</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Challenge() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a captcha opt-in <code>Challenge</code> use as a JavaBean.
     **
     ** @param  name             the name of the <code>Challenge</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  image            the image of the <code>Challenge</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  values           the glpyh values to the <code>Challenge</code>.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     */
    private Challenge(final String name, final String image, final List<String> values) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.name   = name;
      this.image  = image;
      this.values = values;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Methode: name
    /**
     ** Returns the name of the image of the <code>Challenge</code>.
     **
     ** @return                  the name of the image of the
     **                          <code>Challenge</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String name() {
      return this.name;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methode: image
    /**
     ** Returns the image of the <code>Challenge</code>.
     **
     ** @return                  the image of the <code>Challenge</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String image() {
      return this.image;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methode: values
    /**
     ** Returns the values of the <code>Challenge</code>.
     ** <p>
     ** This accessor method returns a reference to the live list, not a
     ** snapshot. Therefore any modification you make to the returned list will
     ** be present inside the object instance.
     **
     ** @return                  the values of the <code>Challenge</code>.
     **                          <br>
     **                          Possible object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public final List<String> values() {
      return this.values;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = this.name != null ? this.name.hashCode() : 0;
      result = PRIME * result + (this.image  != null ? this.image.hashCode()  : 0);
      result = PRIME * result + (this.values != null ? this.values.hashCode() : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Tile
  // ~~~~~ ~~~~
  /**
   ** The <code>Captcha</code> <code>Tile</code> send back by a client.
   */
  public static class Tile extends Image {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-3699718345995121284")
    private static final long serialVersionUID = 564796400527300319L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @JsonProperty
    private String            obfuscated;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Tile</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Tile() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a captcha opt-in <code>Tile</code> use as a JavaBean.
     **
     ** @param  random           the random value of the <code>Response</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  obfuscated       the obfuscated name of the
                                 <code>Response</code>.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  image            the image of the glpyh.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Tile(final String name, final String obfuscated, final String image) {
      // ensure inheritance
      super(name, image);

      // initialize instance attributes
      this.obfuscated = obfuscated;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Methode: obfuscated
    /**
     ** Returns the obfuscated value of the <code>Response</code> glyph.
     **
     ** @return                  the obfuscated value of the
     **                          <code>Response</code>
     **                          glyph.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public final String obfuscated() {
      return this.obfuscated;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode
    /**
     ** Returns a hash code value for the object.
     ** <br>
     ** This method is supported for the benefit of hash tables such as those
     ** provided by {@link java.util.HashMap}.
     ** <p>
     ** The general contract of <code>hashCode</code> is:
     ** <ul>
     **   <li>Whenever it is invoked on the same object more than once during an
     **       execution of a Java application, the <code>hashCode</code> method
     **       must consistently return the same integer, provided no information
     **       used in <code>equals</code> comparisons on the object is modified.
     **       This integer need not remain consistent from one execution of an
     **       application to another execution of the same application.
     **   <li>If two objects are equal according to the
     **       <code>equals(Object)</code> method, then calling the
     **       <code>hashCode</code> method on each of the two objects must
     **       produce the same integer result.
     **   <li>It is <em>not</em> required that if two objects are unequal
     **       according to the {@link java.lang.Object#equals(java.lang.Object)}
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      int result = super.hashCode();
      result = PRIME * result + (this.obfuscated != null ? this.obfuscated.hashCode() : 0);
      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Captcha</code> with the properties specified.
   **
   ** @param  name               the name value of the <code>Response</code>.
   **                            <br>
   **                             Allowed object is {@link String}.
   ** @param  response           the expected response of the
   **                            <code>Captcha</code> challenge.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  tile               the array of {@link Tile}s send back to
   **                            the client.
   **                            <br>
   **                            Allowed object is array of type {@link Tile}.
   */
  public Captcha(final String name, final String response, final Tile... tile) {
    // ensure inheritance
    this(name, response, Arrays.asList(tile));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Captcha</code> with the properties specified.
   **
   ** @param  name               the name value of the <code>Response</code>.
   **                            <br>
   **                             Allowed object is {@link String}.
   ** @param  response           the expected response of the
   **                            <code>Captcha</code> challenge.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  tile               the collection of {@link Tile}s send back to
   **                            the client.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Tile}.
   */
  public Captcha(final String name, final String response, final List<Tile> tile) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name     = name;
    this.response = response;
    this.tile     = new ArrayList<>(tile);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   response
  /**
   ** Returns the expected response of the <code>Captcha</code> challenge.
   **
   ** @return                    the expected response of the
   **                            <code>Captcha</code> challenge.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String response() {
    return this.response;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   tile
  /**
   ** Returns the tiles of the <code>Captcha</code>.
   **
   ** @return                    the choice of the <code>Captcha</code>.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Tile}.
   */
  public final List<Tile> tile() {
    return this.tile;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   image
  /**
   ** Factory method to create a captcha <code>Image</code> populated
   ** with the specified properties.
   **
   ** @param  name               the translatable name of the image.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  image              the name of the image file.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly create <code>Captcha</code>
   **                            <code>Image</code> populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is <code>Response</code>.
   */
  public static Image image(final String name, final String image) {
    return new Image(name, image);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tile
  /**
   ** Factory method to create a captcha opt-in <code>Tile</code> populated
   ** with the specified properties.
   **
   ** @param  random             the random value of the <code>Response</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  obfuscated         the obfuscated name of the
   **                            <code>Response</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  image              the name to the image file.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly create <code>Captcha</code>
   **                            <code>Response</code> populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is <code>Response</code>.
   */
  public static Tile tile(final String random, final String obfuscated, final String image) {
    return new Tile(random, obfuscated, image);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   challenge
  /**
   ** Factory method to create a captcha opt-in <code>Challenge</code> use as a
   ** JavaBean.
   **
   ** @param  name               the name of the <code>Challenge</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  image              the image of the <code>Challenge</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  values             the glpyh values to the <code>Challenge</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   **
   ** @return                    the newly create <code>Captcha</code>
   **                            <code>Challenge</code> populated with the
   **                            specified properties.
   **                            <br>
   **                            Possible object is <code>Challenge</code>.
   */
  public static Challenge challenge(final String name, final String image, final List<String> values) {
    return new Challenge(name, image, values);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    int result = this.response != null ? this.response.hashCode() : 0;
    result = PRIME * result + (this.name != null ? this.name.hashCode() : 0);
    result = PRIME * result + (this.tile != null ? this.tile.hashCode()   : 0);
    return result;
  }
}