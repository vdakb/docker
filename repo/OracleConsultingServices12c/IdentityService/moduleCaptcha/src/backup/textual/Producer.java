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

    Copyright � 2020. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Service Extension
    Subsystem   :   Captcha Service Feature

    File        :   Producer.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Producer.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2020-07-10  DSteding    First release version
*/

package oracle.iam.service.captcha.textual;

import java.util.Set;
import java.util.HashSet;

import oracle.iam.service.captcha.core.Digester;

////////////////////////////////////////////////////////////////////////////////
// interface Producer
// ~~~~~~~~ ~~~~~~~~~
/**
 ** The <code>Producer</code> of randomized character sequences.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Producer {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The default length of the text. */
  public static final int         LENGTH = 8;

  /** The default dictionary for the text. */
  public static final Set<String> ASCII  = letter(
    "0","1","2","3","4","5","6","7","8","9"
  , "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
  , "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
  );

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Default
  // ~~~~~ ~~~~~~~~
  /**
   ** The default implementation of a {@link Producer} to ranomize a sequence of
   ** character sequences.
   */
  static final class Default implements Producer {

    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////

    private final int         length;
    private final Set<String> dictionary;

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Default</code> character sequence randomizer that
     ** will used the specified <code>length</code> and character
     ** <code>sequence</code> to produce a randomized string.
     **
     ** @param  length           the length of the character sequence to
     **                          generate
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  dictionary       the dictionary of character sequences.
     **                          <br>
     **                          Allowed object is {@link Set} where each
     **                          element is of type {@link String}.
     */
    private Default(final int length, final Set<String> dictionary) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.length     = length;
      this.dictionary = dictionary;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: shuffle (Producer)
    /**
     ** Generate a randomized series of characters to be used as the response for
     ** the <code>CAPTCHA</code>.
     **
     ** @return                  the response for the <code>CAPTCHA</code>.
     **                          <br>
     **                          Possible Object is {@link String}.
     */
    @Override
    public final String shuffle() {
      final StringBuilder capture = new StringBuilder();
      for (int i = 0; i < this.length; i++) {
        capture.append(randomized());
      }
      return capture.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: randomized
    /**
     ** Obtains a randomized string from the <code>dictionary</code>.
     **
     ** @return                  a random {@link String} obtained from the
     **                          <code>dictionary</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    protected String randomized() {
      return this.dictionary.stream().skip(Digester.instance.nextInt(this.dictionary.size())).findFirst().orElse(null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildDefault
  /**
   ** Factory method to creates a default text <code>Producer</code>.
   ** <p>
   ** The the length of the character sequence to generate is <code>8</code> and
   ** the dictionary ar the alhpanumeric characters of the ASCII character set.
   **
   ** @return                    a new <code>Captcha</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Captcha.Builder</code>.
   */
  static Producer buildDefault() {
    return buildDefault(LENGTH, ASCII);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildDefault
  /**
   ** Factory method to creates a default text <code>Producer</code>.
   **
   ** @param  length             the length of the character sequence to
   **                            generate
   **                            <br>
   **                            Allowed object is <code>int</code>.
   ** @param  dictionary         the dictionary of character sequences.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}.
   **
   ** @return                    a new <code>Captcha</code> {@link Builder}.
   **                            <br>
   **                            Possible object is
   **                            <code>Captcha.Builder</code>.
   */
  static Producer buildDefault(final int length, final Set<String> dictionary) {
    return new Default(length, dictionary);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   letter
  static Set<String> letter(final String... string) {
    final Set<String> set = new HashSet<String>();
    for (String c : string) {
        set.add(c);
    }
    return set;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   shuffle
  /**
   ** Generate a randomized series of characters to be used as the response for
   ** the <code>CAPTCHA</code>.
   **
   ** @return                    the response for the <code>CAPTCHA</code>.
   **                            <br>
   **                            Possible Object is {@link String}.
   */
   String shuffle();
}