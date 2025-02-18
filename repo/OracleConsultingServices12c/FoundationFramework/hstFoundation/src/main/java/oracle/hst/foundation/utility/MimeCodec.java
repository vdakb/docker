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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared Utility Facility

    File        :   MimeCodec.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    MimeCodec.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.hst.foundation.utility;

import java.util.Map;
import java.util.HashMap;

import java.io.Writer;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.collection.IntergerMap;

////////////////////////////////////////////////////////////////////////////////
// abstract class MimeCodec
// ~~~~~~~~ ~~~~~ ~~~~~~~~~
/**
 ** The MimeCodec class contains a number of methods for decoding input and
 ** encoding output so that it will be safe for a variety of interpreters.
 ** <p>
 ** To prevent double-encoding, callers should make sure by canonicalizing that
 ** input does not already contain encoded characters.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class MimeCodec implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1652681637157613346")
  static final long serialVersionUID = -5668197681257992628L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private transient Entity map = new Lookup();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Binary
  // ~~~~ ~~~~~~
  /**
   ** This enum store the binary encoding algorithm of {@link MimeCodec}.
   */
  private enum Binary {
      base16("{base16}")
    , base64("{base64}")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:290968993091240824")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the name of the tag for this state. */
    final String algorithm;

    /** the name of the tag for this state. */
    final int   length;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Binary</code> with a multiple parent states.
     **
     ** @param  algorithm        the logical name of this binary encoding
     **                          algorithm.
     */
    Binary(final String algorithm) {
      this.algorithm = algorithm;
      this.length    = algorithm.length();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // interface Entity
  // ~~~~~~~~~ ~~~~~~~
  static interface Entity {

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add
    /**
     ** Add an entry to this entity map.
     **
     ** @param  name             the common name of the entity.
     ** @param  value            the pimitive value to associated with the
     **                          specified name.
     */
    void add(final String name, final int value);

    ////////////////////////////////////////////////////////////////////////////
    // Method: name
    /**
     ** Returns the name of the entity identified by the specified value.
     **
     ** @param  value            the value of the entity the name have to be
     **                          returned for.
     **
     ** @return                  the name of the entity associated with the
     **                          specified value.
     */
    String name(final int value);

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the entity identified by the specified name.
     **
     ** @param  name             the name of the entity the value have to be
     **                          returned for.
     **
     ** @return                  the primitive value of the entity associated
     **                          with the specified name.
     */
    int value(final String name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Primitive
  // ~~~~~~ ~~~~~~~~~
  static class Primitive implements Entity {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Map<String, Integer> forward  = new HashMap<String, Integer>();
    private final IntergerMap<String>  backward = new IntergerMap<String>();

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: add (Entity)
    /**
     ** Add an entry to this entity map.
     **
     ** @param  name             the common name of the entity.
     ** @param  value            the pimitive value to associated with the
     **                          specified name.
     */
    @Override
    public void add(String name, int value) {
      this.forward.put(name, new Integer(value));
      this.backward.put(value, name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (Entity)
    /**
     ** Returns the name of the entity identified by the specified value.
     **
     ** @param  value            the value of the entity the name have to be
     **                          returned for.
     **
     ** @return                  the name of the entity associated with the
     **                          specified value.
     */
    @Override
    public String name(final int value) {
      return this.backward.get(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value (Entity)
    /**
     ** Returns the value of the entity identified by the specified name.
     **
     ** @param  name             the name of the entity the value have to be
     **                          returned for.
     **
     ** @return                  the primitive value of the entity associated
     **                          with the specified name.
     */
    @Override
    public int value(final String name) {
      final Integer value = this.forward.get(name);
      return (value == null) ? -1 :value.intValue();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Lookup
  // ~~~~~~ ~~~~~
  static class Lookup extends Primitive {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String[] table;
    private int      SIZE = 256;

    ////////////////////////////////////////////////////////////////////////////
    // Method group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: name (overridden)
    /**
     ** Returns the name of the entity identified by the specified value.
     **
     ** @param  value            the value of the entity the name have to be
     **                          returned for.
     **
     ** @return                  the name of the entity associated with the
     **                          specified value.
     */
    @Override
    public String name(final int value) {
      return value < SIZE ? table()[value] : super.name(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: table
    private String[] table() {
      if (this.table == null) {
        this.table = new String[SIZE];
        for (int i = 0; i < SIZE; i++)
          this.table[i] = super.name(i);
      }
      return this.table;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:  Ctor
  /**
   ** Constructor for the <code>MimeCodec</code> object.
   **
   ** @param  entities           array of entities to initialize the instance.
   */
  protected MimeCodec(final String[][] entities) {
    // ensure inheritance
    super();

    // initialize entities
    addEntities(entities);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityName
  /**
   ** Returns the name of the entity identified by the specified value.
   **
   ** @param  value              the value of the entity the name have to be
   **                            returned for.
   **
   ** @return                    the name of the entity associated with the
   **                            specified value.
   */
  public String entityName(final int value) {
    return this.map.name(value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityValue
  /**
   ** Returns the value of the entity identified by the specified name.
   **
   ** @param  name               the name of the entity the value have to be
   **                            returned for.
   **
   ** @return                    the primitive value of the entity associated
   **                            with the specified name.
   */
  public int entityValue(final String name) {
    return this.map.value(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntities
  /**
   ** Adds entities to this entity.
   **
   ** @param  entities           array of entities to be added.
   */
  public void addEntities(final String[][] entities) {
    for (String[] cursor : entities)
      addEntity(cursor[0], Integer.parseInt(cursor[1]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addEntity
  /**
   ** Add an entity to this entity.
   **
   **
   ** @param  name               the common name of the entity.
   ** @param  value              the pimitive value to associated with the
   **                            specified name.
   */
  public void addEntity(final String name, final int value) {
    this.map.add(name, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   canonicalize
  /**
   ** Canonicalization is simply the operation of reducing a possibly encoded
   ** string down to its simplest form. This is important, because attackers
   ** frequently use encoding to change their input in a way that will bypass
   ** validation filters, but still be interpreted properly by the target of the
   ** attack. Note that data encoded more than once is not something that a
   ** normal user would generate and should be regarded as an attack.
   ** <p>
   ** Everyone <a href="http://cwe.mitre.org/data/definitions/180.html">says</a>
   ** you shouldn't do validation without canonicalizing the data first. This is
   ** easier said than done. The canonicalize method can be used to simplify
   ** just about any input down to its most basic form. Note that canonicalize
   ** doesn't handle Unicode issues, it focuses on higher level encoding and
   ** escaping schemes. In addition to simple decoding, canonicalize also
   ** handles:
   ** <ul>
   **   <li>Perverse but legal variants of escaping schemes
   **   <li>Multiple escaping (%2526 or &amp;#x26;lt;)
   **   <li>Mixed escaping (%26lt;)
   **   <li>Nested escaping (%%316 or &amp;%6ct;)
   **   <li>All combinations of multiple, mixed, and nested encoding/escaping
   **       (%2&amp;#x35;3c or &amp;#x2526&gt;)
   ** </ul>
   **
   ** @param  input              the text to canonicalize.
   **
   ** @return                    a String containing the canonicalized text.
   **
   ** @see <a href="http://www.w3.org/TR/html4/interact/forms.html#h-17.13.4">W3C specifications</a>
   */
  public final String canonicalize(final String input) {
    // prevent bogus input
    if (input == null)
      return input;

    String tmp = input;
    if (tmp.startsWith(Binary.base16.algorithm))
      tmp = new String(Hexadecimal.decode(tmp.substring(Binary.base16.length)));
    else if (input.startsWith(Binary.base64.algorithm))
      tmp = StringUtility.bytesToString(Base64Transcoder.decode(tmp.substring(Binary.base64.length).getBytes()));

    return tmp;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Escapes the characters in provided by the {@link String}
   ** <code>input</code>.
   ** <p>
   ** For example, if you have called addEntity(&quot;foo&quot;, 0xA1),
   ** escape(&quot;\u00A1&quot;) will return &quot;&amp;foo;&quot;
   **
   ** @param  input              the string to escape. Assumed to be a
   **                            <code>non-null</code> value.
   **
   ** @return                    a new escaped {@link String}.
   **
   ** @see    #escape(Writer, String)
   */
  public String escape(final String input) {
    // prevent bogus input
    if (StringUtility.isEmpty(input))
      return SystemConstant.EMPTY;

    final StringWriter writer = ensureStringWriter(input);
    try {
      this.escape(writer, input);
    }
    catch (IOException e) {
      // This should never happen because ALL the StringWriter methods called by
      // #escape(Writer, String) cannot throw IOExceptions.
      e.printStackTrace();
    }
    return StringUtility.isPrintableAscii(writer.toString()) ? writer.toString() : String.format("%s%s", Binary.base64.algorithm, new String(Base64Transcoder.encode(writer.toString().getBytes())));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
	/**
   ** Unescapes the entities in a <code>String</code>.
   **
   ** <p>
   ** For example, if you have called addEntity(&quot;foo&quot;, 0xA1),
   ** unescape(&quot;&amp;foo;&quot;) will return &quot;\u00A1&quot;
   **
   ** @param  input              the string to unescape. Assumed to be a
   **                            <code>non-null</code> value.
   **
   ** @return                    a new unescaped {@link String}.
   */
  public String unescape(final String input) {
    final StringWriter writer = ensureStringWriter(input);
    try {
      this.unescape(writer, canonicalize(input));
    }
    catch (IOException e) {
      // This should never happen because ALL the StringWriter methods called by
      // #escape(Writer, String) cannot throw IOExceptions.
      e.printStackTrace();
    }
    return writer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
	/**
   ** Unescapes the escaped characters in the {@link String} <code>input</code>
   ** passed and writes the result to the {@link Writer} passed.
   **
   ** @param  writer             the {@link Writer} to write the results of the
   **                            escaping to. Assumed to be a
   **                            <code>non-null</code> value.
   ** @param  input              the string to unescape. Assumed to be a
   **                            <code>non-null</code> value.
   **
   ** @throws IOException        when {@link Writer} passed throws the exception
   **                            from calls to the {@link Writer#write(int)}
   **                            methods.
   **
   ** @see    #unescape(String)
   ** @see    Writer
   */
  public void unescape(final Writer writer, final String input)
    throws IOException {

    int escaped = input.indexOf('&');
    if (escaped < 0) {
      writer.write(input);
      return;
    }
    else {
      unescape(writer, input, escaped);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   escape
  /**
   ** Escapes the characters in the {@link String} <code>input</code> passed and
   ** writes the result to the {@link Writer} passed.
   **
   ** @param  writer             the {@link Writer} to write the results of the
   **                            escaping to. Assumed to be a
   **                            <code>non-null</code> value.
   ** @param  input              the string to escape. Assumed to be a
   **                            <code>non-null</code> value.
   **
   ** @throws IOException        when {@link Writer} passed throws the exception
   **                            from calls to the {@link Writer#write(int)}
   **                            methods.
   **
   ** @see    #escape(String)
   ** @see    Writer
   */
  private void escape(final Writer writer, final String input)
    throws IOException {

    int len = input.length();
    for (int i = 0; i < len; i++) {
      final char   c          = input.charAt(i);
      final String entityName = this.entityName(c);
      if (entityName == null) {
        // we neeed to check for printable first to avoid that a space will be
        // escaped
        if (CharacterUtility.isAsciiPrintable(c))
          writer.write(c);
        // we need to check for whitespaces first white spaces are ' ', \n, \r,
        // \t only
        else if (CharacterUtility.isWhitespace(c)) {
          writer.write("&#");
          writer.write(Integer.toString(c, 10));
          writer.write(';');
        }
        // now we are able to detect any non-ascii character
        else if (!CharacterUtility.isAscii(c)) {
          writer.write("&#");
          writer.write(Integer.toString(c, 10));
          writer.write(';');
        }
        else {
          writer.write(c);
        }
      }
      else {
        writer.write('&');
        writer.write(entityName);
        writer.write(';');
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unescape
	/**
   ** Underlying unescape method that allows the optimisation of not starting
   ** from the 0 index again.
   **
   ** @param  writer             the {@link Writer} to write the results of the
   **                            escaping to. Assumed to be a
   **                            <code>non-null</code> value.
   ** @param  input              the string to unescape. Assumed to be a
   **                            <code>non-null</code> value.
   ** @param  escapedAt          the position index of the first ampersand in
   **                            the source String <code>input</code>.
   **
   ** @throws IOException        when {@link Writer} passed throws the exception
   **                            from calls to the {@link Writer#write(int)}
   **                            methods.
   */
  private void unescape(final Writer writer, final String input, final int escapedAt)
    throws IOException {

    writer.write(input, 0, escapedAt);
    int len = input.length();
    for (int i = escapedAt; i < len; i++) {
      char c = input.charAt(i);
      if (c == '&') {
        final int next      = i + 1;
        final int semicolon = input.indexOf(';', next);
        if (semicolon == -1) {
          writer.write(c);
          continue;
        }
        int ampersand = input.indexOf('&', i + 1);
        if (ampersand != -1 && ampersand < semicolon) {
          // then the text looks like &...&...;
          writer.write(c);
          continue;
        }
        final String entityContent = input.substring(next, semicolon);
        final int    entityContentLen = entityContent.length();
        int entityValue      = -1;
        if (entityContentLen > 0) {
          // escaped value content is an integer (decimal or
          if (entityContent.charAt(0) == '#') {
            // hexidecimal)
            if (entityContentLen > 1) {
              char hexPrefix = entityContent.charAt(1);
              try {
                if (hexPrefix == 'x' || hexPrefix == 'X')
                  entityValue = Integer.parseInt(entityContent.substring(2), 16);
                else
                  entityValue = Integer.parseInt(entityContent.substring(1), 10);

                if (entityValue > 0xFFFF)
                  entityValue = -1;
              }
              catch (NumberFormatException e) {
                entityValue = -1;
              }
            }
          }
          // escaped value content is an entity name
          else
            entityValue = entityValue(entityContent);
        }

        if (entityValue == -1) {
          writer.write('&');
          writer.write(entityContent);
          writer.write(';');
        }
        else {
          writer.write(entityValue);
        }
        // move index up to the semi-colon
        i = semicolon;
      }
      else
        writer.write(c);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ensureStringWriter
	/**
   ** Create a {@link StringWriter} and make it 10% larger than the source
   ** String to avoid growing the writer.
   **
   ** @param  source             the source string
   **
   ** @return                    a newly created StringWriter
   */
  private StringWriter ensureStringWriter(final String source) {
    return new StringWriter((int)(source.length() + (source.length() * 0.1)));
  }
}