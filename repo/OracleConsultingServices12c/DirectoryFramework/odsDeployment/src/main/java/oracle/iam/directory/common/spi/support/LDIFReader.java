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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   LDIFReader.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    LDIFReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.util.List;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.StringTokenizer;

import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.net.URL;
import java.net.MalformedURLException;

import javax.naming.directory.DirContext;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import javax.naming.ldap.Control;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureConstant;
import oracle.iam.directory.common.FeatureException;
import oracle.iam.directory.common.FeatureResourceBundle;

////////////////////////////////////////////////////////////////////////////////
// class LDIFReader
// ~~~~~ ~~~~~~~~~~
/**
 ** LDAP Data Interchange Format (LDIF) is a file format used to import and
 ** export directory data from an LDAP server and to describe a set of changes
 ** to be applied to data in a directory. This format is described in the
 ** Internet draft
 ** <a href="ftp://ftp.ietf.org/internet-drafts/draft-good-ldap-ldif-00.txt" target="_blank">The LDAP Data Interchange Format (LDIF) - Technical Specification</a>.
 ** <p>
 ** This class implements an LDIF file parser.
 ** <p>
 ** You can construct an object of this class to parse data in LDIF format and
 ** manipulate the data as individual {@link LDAPRecord} objects.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LDIFReader extends LDAPReader {

  /////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private int     version             = 1;
  private boolean done                = false;
  private boolean entryDone           = false;
  private int     currentLine;
  private int     continuationLength;

  private Parser  parser              = null;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Parser
  // ~~~~~ ~~~~~~
  /**
   ** Concatenate continuation lines, if present
   */
  class Parser {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private BufferedReader delegate;
    private String         next     = null;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    //
    Parser(final BufferedReader reader) {
      // ensure inheritance
      super();

      this.delegate = reader;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   readLine
    //
    /**
     ** Reads a non-comment line.
     **
     ** @return                  a string or null.
     */
    String readLine()
      throws FeatureException {

      String line               = null;
      String result             = null;
      int    readCount          = 0;
      int    continuationLength = 0;
      try {
        do {
          // Leftover line from last time?
          if (this.next != null) {
            line = this.next;
            this.next = null;
          }
          else {
            line = this.delegate.readLine();
          }
          if (line != null) {
            readCount++;
            // Empty line means end of record
            if (line.length() < 1) {
              if (result == null)
                result = line;
              else {
                this.next = line;
                break;
              }
            }
            else if (line.charAt(0) == COMMENT) {
              // Ignore comment lines
              continue;
            }
            else if (line.charAt(0) != SystemConstant.BLANK) {
              // Not a continuation line
              if (result == null)
                result = line;
              else {
                this.next = line;
                break;
              }
            }
            else {
              // Continuation line
              if (result == null) {
                LDIFReader.this.currentLine += readCount;
                throwFeatureException(FeatureError.LDIF_LINE_NOWHERE);
              }
              result += line.substring(1);
              continuationLength++;
            }
          }
          else {
            // End of file
            break;
          }
        }
        while (true);
      }
      catch (IOException e) {
        throw new FeatureException(FeatureError.GENERAL, e);
      }

      LDIFReader.this.done = (line == null);

      LDIFReader.this.currentLine += readCount;
      if (this.next != null) {
        // read one line ahead
        LDIFReader.this.currentLine--;
      }
      LDIFReader.this.continuationLength = continuationLength;

      return result;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFReader</code> object to parse the LDAP data read
   ** from stdin.
   **
   ** @throws FeatureException if an I/O error occurs
   */
  public LDIFReader()
    throws FeatureException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.parser = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(System.in), StringUtility.LATIN)));
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFReader</code> object to parse the LDIF data read
   ** from a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public LDIFReader(final File file)
    throws FeatureException {

    // ensure inheritance
    super(file);

    try {
      this.parser  = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StringUtility.LATIN)));
    }
    catch (IOException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIF</code> object to parse the LDIF data read from a
   ** specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse
   ** @param  separator          the character to use between attribute names
   **                            and values; the default is ":"
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public LDIFReader(final File file, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(file, separator);

    try {
      this.parser  = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StringUtility.LATIN)));
    }
    catch (IOException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIF</code> object to parse the LDIF data read from a
   ** specified file.
   **
   ** @param  file               the name of the LDIF file to parse
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public LDIFReader(final String file)
    throws FeatureException {

    // ensure inheritance
    super(file);

    try {
      this.parser  = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StringUtility.LATIN)));
    }
    catch (IOException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIF</code> object to parse the LDIF data read from an
   ** input stream.
   **
   ** @param  stream             input stream providing the LDIF data
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public LDIFReader(final DataInputStream stream)
    throws FeatureException {

    // ensure inheritance
    super(stream.toString());

    this.parser = new Parser(new BufferedReader(new InputStreamReader(stream, StringUtility.LATIN)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFWriter</code> object to output entries to a stream
   ** as LDIF.
   **
   ** @param  stream             input stream providing the LDIF data
   ** @param  separator          String to use between attribute names and
   **                            values; the default is ":"
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public LDIFReader(final DataInputStream stream, final char separator)
    throws FeatureException {

    // ensure inheritance
    super(stream.toString(), separator);

    this.parser = new Parser(new BufferedReader(new InputStreamReader(stream, StringUtility.LATIN)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory Method to create a proper LDIF reader.
   **
   ** @param  file               the abstract path of the LDIF file to parse.
   **
   ** @return                    the {@link LDIFReader} for <code>file</code>.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  public static LDIFReader create (final File file)
    throws FeatureException {

    return new LDIFReader(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (LDAPReader)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further read() invocations will cause an
   ** IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  @Override
  public void close()
    throws FeatureException {

    try {
      this.parser.delegate.close();
    }
    catch (IOException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord (DirectoryFileReader)
  /**
   ** Returns the next record in the LDIF data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** LDIF data.
   **
   ** @return                    the next record as a {@link LDAPRecord} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws FeatureException   if an I/O error occurs
   **
   ** @see    LDAPRecord
   */
  @Override
  public LDAPRecord nextRecord()
    throws FeatureException {

    return (this.done) ? null : parse(this.parser);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwLDAPException
  /**
   ** Throws a {@link FeatureException} file exception including the current
   ** line number.
   **
   ** @param  code               error code of message message
   **
   ** @throws FeatureException   with the configured poperties.
   */
  protected void throwLDAPException(final String code)
    throws FeatureException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), FeatureResourceBundle.string(code)};
    throw new FeatureException(FeatureError.LDIF_LINE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwLDAPException
  /**
   ** Throws a {@link FeatureException} file exception including the current
   ** line number.
   **
   ** @param  code               error code of message message
   ** @param  argument           the substitution for placholder contained in
   **                            the message regarding to <code>code</code>.
   **
   ** @throws FeatureException   with the configured poperties.
   */
  protected void throwLDAPException(final String code, final String argument)
    throws FeatureException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), FeatureResourceBundle.format(code, argument)};
    throw new FeatureException(FeatureError.LDIF_LINE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwFeatureException
  /**
   ** Throws a {@link FeatureException} exception including the current line
   ** number.
   **
   ** @param  code               error code of message message
   **
   ** @throws FeatureException   with the configured poperties.
   */
  protected void throwFeatureException(final String code)
    throws FeatureException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), FeatureResourceBundle.string(code)};
    throw new FeatureException(FeatureError.LDIF_LINE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwFeatureException
  /**
   ** Throws a LDAP file exception including the current line number.
   **
   ** @param  code               error code of message message
   ** @param  argument           the substitution for placholder contained in
   **                            the message regarding to <code>code</code>.
   **
   ** @throws FeatureException   with the configured poperties.
   */
  protected void throwFeatureException(final String code, final String argument)
    throws FeatureException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), FeatureResourceBundle.format(code, argument)};
    throw new FeatureException(FeatureError.LDIF_LINE, parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses ldif content.
   ** <p>
   ** The list of attributes is terminated by \r\n or '-'. This function is also
   ** used to parse the attributes in modifications.
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parse(final Parser reader)
    throws FeatureException {

    String line = reader.readLine();
    // Skip past any blank lines
    while (line != null && line.length() < 1)
      line = reader.readLine();

    if (line == null)
      return null;

    String tag = FeatureConstant.VERSION + this.separator;
    if (line.startsWith(tag)) {
      this.version = Integer.parseInt(line.substring(tag.length()).trim());
      if (this.version != 1)
        throwFeatureException(FeatureError.LDIF_UNEXPECTED, line);

      // do the next record and skip newlines
      line = reader.readLine();
      if ((line != null) && (line.length() == 0))
        line = reader.readLine();

      if (line == null)
        return null;
    }

    tag = FeatureConstant.DN + this.separator;
    if (!line.startsWith(tag))
      throwFeatureException(FeatureError.LDIF_EXPECTING_PREFIX, tag);

    String dn = line.substring(3).trim();
    if (dn.charAt(0) == this.separator && dn.length() > 1) {
      String substr = dn.substring(1).trim();
      // value encoding/decoding is always done using an ISO character set;
      // files are instead of written and read with the ASCII charcater set
      dn = new String(decodeBytes(substr), StringUtility.LATIN);
    }

    return parseContent(reader, dn);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseContent
  /**
   ** Parses ldif content.
   ** <p>
   ** The list of attributes is terminated by \r\n or '-'. This function is also
   ** used to parse the attributes in modifications.
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parseContent(final Parser reader, final String dn)
    throws FeatureException {

    LDAPRecord content = null;
    String line = reader.readLine();
    if ((line == null) || (line.length() < 1) || (line.equals("-"))) {
      // if this is empty line, then we're finished reading all the info for the
      // current entry
      if ((line != null) && (line.length() < 1))
        this.entryDone = true;

      return content;
    }

    // handle changerecord
    String tag = FeatureConstant.CHANGE_TYPE  + this.separator;
    if (line.startsWith(tag)) {
      String changetype = line.substring(11).trim();
      if (changetype.equals(FeatureConstant.CHANGE_TYPE_MODIFY))
        content = parseModification(reader, dn);
      else if (changetype.equals(FeatureConstant.CHANGE_TYPE_ADD))
        content = parseAddition(reader, dn);
      else if (changetype.equals(FeatureConstant.CHANGE_TYPE_DELETE))
        content = parseDeletion(reader, dn);
      else if (changetype.equals(FeatureConstant.CHANGE_TYPE_RENAME_DN) || changetype.equals(FeatureConstant.CHANGE_TYPE_RENAME_RDN))
        content = parseModificationDN(reader);
      else
        throwFeatureException(FeatureError.LDIF_CHANGE_TYPE_NOTSUPPORTED);

      return content;
    }

    // handles 1*(attrval-spec)
    Hashtable<String, Attribute> mapping = new Hashtable<String, Attribute>();
    List<Control>                control = null;
    while (true) {
      tag = FeatureConstant.CONTROL  + this.separator;
      if (line.startsWith(tag)) {
        if (control == null)
          control = new ArrayList<Control>();

        control.add(parseControl(line));
      }
      else {
        // an attribute
        int length = line.length();
        if (length < 1)
          break;

        int index = line.indexOf(this.separator);
        // Must have a colon
        if (index == -1)
          throwFeatureException(FeatureError.LDIF_EXPECTING_SEPARATOR, Character.toString(this.separator));

        // attribute type
        String type  = line.substring(0, index);
        if (!excludes(type)) {
          String value = SystemConstant.EMPTY;
          // Could be :: for binary
          index++;
          if (length > index) {
            try {
              if (line.charAt(index) == this.separator) {
                index++;
                value = new String(decodeBytes(line.substring(index).trim()));
              }
              else if (line.charAt(index) == '<') {
                index++;
                URL url = new URL(line.substring(index).trim());
                // value encoding/decoding is always done using an ISO
                // character set; files are instead of written and read with
                // the ASCII charcater set
                value = new String(fileContent(url.getFile()), StringUtility.LATIN);
              }
              else {
                value = line.substring(index).trim();
              }
            }
            catch (MalformedURLException e) {
              throwFeatureException(FeatureError.LDIF_CONSTRUCT_URL, line.substring(index + 1).trim());
            }
          }
          // Is there a previous value for this attribute?
          Attribute attribute = mapping.get(type);
          if (attribute == null)
            mapping.put(type, new BasicAttribute(type, value));
          else
            attribute.add(value);
        }
      }
      line = reader.readLine();
      if (line == null || (line.length() < 1) || (line.equals("-"))) {
        if ((line != null) && (line.length() < 1)) {
          this.entryDone = true;
        }
        break;
      }
    }

    content = new LDAPRecord();
    // Copy over the attributes to the record
    Enumeration<Attribute> en = mapping.elements();
    while (en.hasMoreElements())
      content.add(en.nextElement());
    mapping.clear();

    if (control != null) {
      content.controls(control.toArray(new Control[0]));
      control.clear();
    }
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseAddition
  /**
   ** Parses add content
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parseAddition(final Parser reader, final String dn)
    throws FeatureException {

    LDAPRecord record = parseContent(reader, dn);
    if (this.entryDone)
      this.entryDone = false;

    return new LDAPAddContent(record.controls(), record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseDeletion
  /**
   ** Parses delete content
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parseDeletion(final Parser reader, final String dn)
    throws FeatureException {

    String      line    = reader.readLine();
    LDAPRecord content = new LDAPDeleteContent(dn);

    List<Control> control = null;
    while (line != null && !line.equals("")) {
      final String tag = FeatureConstant.CONTROL + this.separator;
      if (line.startsWith(tag)) {
        if (content == null)
          control = new ArrayList<Control>();

        control.add(parseControl(line));
      }
      else
        throwFeatureException(FeatureResourceBundle.format(FeatureError.LDIF_EXPECTING_SEPARATOR, this.separator, line));

      line = reader.readLine();
    }

    if (control != null) {
      Control[] controls = control.toArray(new Control[0]);
      content.controls(controls);
      control.clear();
    }
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseModification
  /**
   ** Parses change modification.
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parseModification(final Parser reader, final String dn)
    throws FeatureException {

    String            line    = reader.readLine();
    LDAPModifyContent content = new LDAPModifyContent(dn);
    List<Control>     control = null;
    do {
      int operation = -1;
      int index = line.indexOf(Character.toString(this.separator));
      if (index == -1)
        throwFeatureException(FeatureError.LDIF_EXPECTING_SEPARATOR, line);

      final String prefix = line.substring(0, index);
      if (FeatureConstant.CHANGE_OPERATION_ADD.equalsIgnoreCase(prefix)) {
        operation = DirContext.ADD_ATTRIBUTE;
      }
      else if (FeatureConstant.CHANGE_OPERATION_REMOVE.equalsIgnoreCase(prefix)) {
        operation = DirContext.REMOVE_ATTRIBUTE;
      }
      else if (FeatureConstant.CHANGE_OPERATION_REPLACE.equalsIgnoreCase(prefix)) {
        operation = DirContext.REPLACE_ATTRIBUTE;
      }
      else
        throwFeatureException(FeatureError.LDIF_CHANGE_TYPE_UNKNOW);

      LDAPRecord ac = parseContent(reader, dn);
      if (ac != null) {
        Control[] controls = ac.controls();
        if (controls != null) {
          if (control == null)
            control = new ArrayList<Control>();

          for (int i = 0; i < controls.length; i++)
            control.add(controls[i]);
        }
        // if there is no attrval-spec, go into the else statement
      }
      else {
        index = line.indexOf(Character.toString(this.separator));
        if (index == -1)
          throwLDAPException(FeatureResourceBundle.format(FeatureError.LDIF_EXPECTING_SEPARATOR, this.separator, line));

        String attrName = line.substring(index + 1).trim();

        if (operation == DirContext.ADD_ATTRIBUTE)
          throwLDAPException(FeatureResourceBundle.format(FeatureError.LDIF_EXPECTING_ATTRIBUTE, attrName));

        Attribute attr = new BasicAttribute(attrName);
        content.add(operation, attr);
      }
      if (this.entryDone) {
        this.entryDone = false;
        break;
      }
      line = reader.readLine();
    } while (line != null && !line.equals(SystemConstant.EMPTY));

    if (control != null) {
      content.controls(control.toArray(new Control[0]));
      control.clear();
    }
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseModificationDN
  /**
   ** Parses moddn/modrdn modification.
   **
   ** @param  reader             data input stream
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parseModificationDN(Parser reader)
    throws FeatureException {

    String           line    = reader.readLine();
    LDAPModDNContent content = new LDAPModDNContent();
    List<Control>    control = null;
    do {
      final String newtag     = FeatureConstant.RDNNEW        + this.separator;
      final String oldtag     = FeatureConstant.RDNOLD_DELETE + this.separator;
      final String newparent  = FeatureConstant.PARENTNEW     + this.separator;
      final String newuperior = FeatureConstant.SUPERIORNEW   + this.separator;
      final String ctltag     = FeatureConstant.CONTROL       + this.separator;

      if (line.startsWith(newtag) && (line.length() > (newtag.length() + 1)))
        content.rdn(line.substring(newtag.length()).trim());
      else if (line.startsWith(oldtag) && (line.length() > (oldtag.length() + 1))) {
        String str = line.substring(oldtag.length()).trim();
        if (str.equals("0"))
          content.deleteOld(false);
        else if (str.equals("1"))
          content.deleteOld(true);
        else
          throwLDAPException("Incorrect input for deleteOldRdn ");
      }
      else if (line.startsWith(newuperior) && (line.length() > (newuperior.length() + 1)))
        content.parent(line.substring(newuperior.length()).trim());
      else if (line.startsWith(newparent) && (line.length() > (newparent.length() + 1)))
        content.parent(line.substring(newparent.length()).trim());
      else if (line.startsWith(ctltag)) {
        if (control == null)
          control = new ArrayList<Control>();

        control.add(parseControl(line));
      }
      line = reader.readLine();
    }
    while (line != null && !line.equals(SystemConstant.EMPTY));

    if (control != null) {
      Control[] controls = control.toArray(new Control[0]);
      content.controls(controls);
      control.clear();
    }

    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseControl
  /**
   ** Parses the specification of a control
   ** <p>
   ** A control looks line one of the following:
   ** <br>
   ** control: 1.2.3.4.10.210
   ** <br>
   ** control: 1.2.3.4.10.210 true
   ** <br>
   ** control: 1.2.3.4.10.210 true: someASCIIvalue
   ** <br>
   ** control: 1.2.3.4.10.210: someASCIIvalue
   ** <br>
   ** control: 1.2.3.4.10.210 true:: 44GK44GM44GV44KP44KJ
   ** <br>
   ** control: 1.2.3.4.10.210:: 44GK44GM44GV44KP44KJ
   ** <br>
   ** control: 1.2.3.4.10.210 true:< file:///usr/local/directory/cont.dta
   ** <br>
   ** control: 1.2.3.4.10.210:< file:///usr/local/directory/cont.dta
   **
   ** @param  line               a line containing a control spec
   **
   ** @return                    a parsed control.
   **
   **
   ** @throws FeatureException if the line could not be parsed
   */
  private Control parseControl(String line)
    throws FeatureException {

    byte[] value = null;
    int    index = line.indexOf(this.separator) + 2;
    // OID, must be present
    if (index >= line.length())
      throwLDAPException(FeatureResourceBundle.string(FeatureError.LDIF_EXPECTING_OID));

    boolean criticality = true;
    line  = line.substring(index).trim();
    index = line.indexOf(SystemConstant.BLANK);

    String  oid;
    if (index < 0)
      oid = line;
    else {
      // Optional criticality
      oid   = line.substring(0, index);
      line  = line.substring(index + 1);
      index = line.indexOf(this.separator);
      String criticalVal = (index > 0) ? line.substring(0, index) : line;
      if (criticalVal.compareTo(SystemConstant.TRUE) == 0)
        criticality = Control.CRITICAL;
      else if (criticalVal.compareTo(SystemConstant.FALSE) == 0)
        criticality = Control.NONCRITICAL;
      else
        throwLDAPException(FeatureResourceBundle.format(FeatureError.LDIF_EXPECTING_CRITICALITY, criticalVal));

      // Optional value
      if (index > 0) {
        // Could be :: for binary
        index++;
        if (line.length() > index) {
          if (line.charAt(index) == this.separator) {
            index++;
            line = line.substring(index).trim();
            value = decodeBytes(line);
          }
          else if (line.charAt(index) == '<') {
            String urlString = line.substring(index + 1).trim();
            try {
              final URL url = new URL(urlString);
              String filename = url.getFile();
              value = fileContent(filename);
            }
            catch (MalformedURLException e) {
              throwFeatureException(FeatureError.LDIF_CONSTRUCT_URL, urlString);
            }
          }
          else {
            try {
              // value encoding/decoding is always done using an ISO character
              // set; files are instead of written and read with the ASCII
              // charcater set
              value = line.substring(index).trim().getBytes(StringUtility.LATIN);
            }
            catch (Exception x) {
              // intentionally left blank
              ;
            }
          }
        }
      }
    }
    return new LDAPControl(oid, criticality, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:  fileContent
  /**
   ** Reads the byte saquence from the specified file url..
   **
   ** @param  url                base name of file to read.
   **
   ** @return                    the byte sequence fetched from.
   **
   ** @throws FeatureException if the file couldn't be opened for output
   */
  private byte[] fileContent(final String url)
    throws FeatureException {

    StringTokenizer tokenizer = new StringTokenizer(url, "|");
    String filename = url;
    int num = tokenizer.countTokens();
    if (num == 2) {
      String token = (String)tokenizer.nextElement();
      int index = token.lastIndexOf(SystemConstant.SLASH);
      String drive = token.substring(index + 1);
      token = (String)tokenizer.nextElement();
      token = token.replace('/', '\\');
      filename = drive + ":" + token;
    }

    File file = new File(filename);
    byte[] b = new byte[(int)file.length()];

    FileInputStream fi = null;
    try {
      fi = new FileInputStream(filename);
      fi.read(b);
    }
    catch (IOException e) {
      throw new FeatureException(FeatureError.GENERAL, e);
    }
    return b;
  }
}