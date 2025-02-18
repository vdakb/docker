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

    Copyright © 2023. All Rights reserved

    --------------------------------------------------------------------------

    System      :   JDeveloper Identity and Access Extension
    Subsystem   :   Identity and Access Management Facility

    File        :   LDIFReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter Steding

    Purpose     :   This file implements the class
                    LDIFReader.


    Revisions          Date        Editor      Comment
    ------------------+-----------+-----------+-------------------------------
    12.2.1.3.42.60.102 2023-06-20  DSteding    First release version
*/

package oracle.jdeveloper.connection.iam.support;

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

import java.nio.charset.StandardCharsets;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.BasicAttribute;

import javax.naming.ldap.Control;

import oracle.jdeveloper.workspace.iam.utility.StringUtility;

import oracle.jdeveloper.connection.iam.Bundle;

import oracle.jdeveloper.connection.iam.model.DirectorySchema;

import oracle.jdeveloper.connection.iam.service.DirectoryService;
import oracle.jdeveloper.connection.iam.service.DirectoryException;

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
 ** @version 12.2.1.3.42.60.102
 ** @since   12.2.1.3.42.60.102
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
      throws DirectoryException {

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
            else if (line.charAt(0) != StringUtility.BLANK) {
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
                throwDirectoryException(Bundle.LDIF_LINE_NOWHERE);
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
        throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader()
    throws DirectoryException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.parser = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(System.in), StandardCharsets.ISO_8859_1)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>LDIFReader</code> object to parse the LDIF data read
   ** from a specified file.
   **
   ** @param  file               the abstract path of the LDIF file to parse
   **
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader(final File file)
    throws DirectoryException {

    // ensure inheritance
    super(file);

    try {
      this.parser = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StandardCharsets.ISO_8859_1)));
    }
    catch (IOException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader(final File file, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(file, separator);

    try {
      this.parser = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StandardCharsets.ISO_8859_1)));
    }
    catch (IOException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader(final String file)
    throws DirectoryException {

    // ensure inheritance
    super(file);

    try {
      this.parser = new Parser(new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(file)), StandardCharsets.ISO_8859_1)));
    }
    catch (IOException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader(final DataInputStream stream)
    throws DirectoryException {

    // ensure inheritance
    super(stream.toString());

    this.parser = new Parser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.ISO_8859_1)));
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  public LDIFReader(final DataInputStream stream, final char separator)
    throws DirectoryException {

    // ensure inheritance
    super(stream.toString(), separator);

    this.parser = new Parser(new BufferedReader(new InputStreamReader(stream, StandardCharsets.ISO_8859_1)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory Method to create a proper LDIF reader.
   **
   ** @param  file               the abstract path of the LDIF file to parse.
   **
   ** @return                    the {@link LDIFReader} for <code>file</code>.
   **
   ** @throws DirectoryException if an I/O error occurs.
   */
  public static LDIFReader build(final File file)
    throws DirectoryException {

    return new LDIFReader(file);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close (DirectoryReader)
  /**
   ** Closes the embedded stream, flushing it first.
   ** <p>
   ** Once the stream has been closed, further read() invocations will cause an
   ** IOException to be thrown.
   ** <p>
   ** Closing a previously closed stream has no effect.
   **
   ** @throws DirectoryException if an I/O error occurs.
   */
  @Override
  public void close()
    throws DirectoryException {

    try {
      this.parser.delegate.close();
    }
    catch (IOException e) {
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
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
   ** @throws DirectoryException if an I/O error occurs.
   **
   ** @see    LDAPRecord
   */
  @Override
  public LDAPRecord nextRecord()
    throws DirectoryException {

    return (this.done) ? null : parse(this.parser);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwLDAPException
  /**
   ** Throws a {@link DirectoryException} file exception including the current
   ** line number.
   **
   ** @param  code               error code of message message
   **
   ** @throws DirectoryException with the configured poperties.
   */
  protected void throwLDAPException(final int code)
    throws DirectoryException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), Bundle.string(code)};
    throw new DirectoryException(Bundle.format(Bundle.LDIF_LINE, parameter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   throwDirectoryException
  /**
   ** Throws a {@link DirectoryException} file exception including the current
   ** line number.
   **
   ** @param  code               error code of message message
   ** @param  argument           the substitution for placholder contained in
   **                            the message regarding to <code>code</code>.
   **
   ** @throws DirectoryException with the configured poperties.
   */
  protected void throwDirectoryException(final int code, final String... argument)
    throws DirectoryException {

    final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), Bundle.format(code, argument)};
    throw new DirectoryException(Bundle.format(Bundle.LDIF_LINE, parameter));
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  private LDAPRecord parse(final Parser reader)
    throws DirectoryException {

    String line = reader.readLine();
    // Skip past any blank lines
    while (line != null && line.length() < 1)
      line = reader.readLine();

    if (line == null)
      return null;

    String tag = DirectoryService.VERSION + this.separator;
    if (line.startsWith(tag)) {
      this.version = Integer.parseInt(line.substring(tag.length()).trim());
      if (this.version != 1)
        throwDirectoryException(Bundle.LDIF_UNEXPECTED, line);

      // do the next record and skip newlines
      line = reader.readLine();
      if ((line != null) && (line.length() == 0))
        line = reader.readLine();

      if (line == null)
        return null;
    }

    tag = DirectoryService.DN + this.separator;
    if (!line.startsWith(tag))
      throwDirectoryException(Bundle.LDIF_EXPECTING_PREFIX, tag);

    String dn = line.substring(3).trim();
    if (dn.charAt(0) == this.separator && dn.length() > 1) {
      String substr = dn.substring(1).trim();
      // value encoding/decoding is always done using an ISO character set;
      // files are instead of written and read with the ASCII charcater set
      dn = new String(decodeBytes(substr), StandardCharsets.ISO_8859_1);
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  private LDAPRecord parseContent(final Parser reader, final String dn)
    throws DirectoryException {

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
    String tag = DirectoryService.CHANGE_TYPE  + this.separator;
    if (line.startsWith(tag)) {
      String changetype = line.substring(11).trim();
      if (changetype.equals(DirectoryService.CHANGE_TYPE_MODIFY))
        content = parseModification(reader, dn);
      else if (changetype.equals(DirectoryService.CHANGE_TYPE_ADD))
        content = parseAddition(reader, dn);
      else if (changetype.equals(DirectoryService.CHANGE_TYPE_DELETE))
        content = parseDeletion(reader, dn);
      else if (changetype.equals(DirectoryService.CHANGE_TYPE_RENAME_DN) || changetype.equals(DirectoryService.CHANGE_TYPE_RENAME_RDN))
        content = parseModificationDN(reader);
      else
        throwDirectoryException(Bundle.LDIF_CHANGE_TYPE_NOTSUPPORTED);

      return content;
    }

    // handles 1*(attrval-spec)
    Hashtable<String, Attribute> mapping = new Hashtable<String, Attribute>();
    List<Control>                control = null;
    while (true) {
      tag = DirectoryService.CONTROL + this.separator;
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
        // must have a colon
        if (index == -1)
          throwDirectoryException(Bundle.LDIF_EXPECTING_SEPARATOR, Character.toString(this.separator));

        // attribute type
        String type  = line.substring(0, index);
        // skip any attribute that's omitted by declaration or is a well known
        // operational attribute
        if (!DirectorySchema.omit(type)) {
          String value = StringUtility.EMPTY;
          // could be :: for binary
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
                value = new String(fileContent(url.getFile()), StandardCharsets.ISO_8859_1);
              }
              else {
                value = line.substring(index).trim();
              }
            }
            catch (MalformedURLException e) {
              throwDirectoryException(Bundle.LDIF_CONSTRUCT_URL, line.substring(index + 1).trim());
            }
          }
          // is there a previous value for this attribute?
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

    content = new LDAPRecord(dn);
    // copy over the attributes to the record
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
   ** @throws DirectoryException if an I/O error occurs.
   */
  private LDAPRecord parseAddition(final Parser reader, final String dn)
    throws DirectoryException {

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
   ** @throws DirectoryException if an I/O error occurs.
   */
  private LDAPRecord parseDeletion(final Parser reader, final String dn)
    throws DirectoryException {

    String        line    = reader.readLine();
    LDAPRecord    content = new LDAPDeleteContent(dn);
    List<Control> control = null;
    while (line != null && !line.equals("")) {
      final String tag = DirectoryService.CONTROL + this.separator;
      if (line.startsWith(tag)) {
        if (content == null)
          control = new ArrayList<Control>();

        control.add(parseControl(line));
      }
      else {
        final String[] parameter = { String.valueOf(this.currentLine - this.continuationLength), Bundle.format(Bundle.LDIF_EXPECTING_SEPARATOR, this.separator, line)};
        throw new DirectoryException(Bundle.format(Bundle.LDIF_LINE, parameter));
      }

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
   ** @throws DirectoryException if an I/O error occurs
   */
  private LDAPRecord parseModification(final Parser reader, final String dn)
    throws DirectoryException {

    String            line    = reader.readLine();
    LDAPModifyContent content = new LDAPModifyContent(dn);
    List<Control>     control = null;
    do {
      int operation = -1;
      int index = line.indexOf(Character.toString(this.separator));
      if (index == -1)
        throwDirectoryException(Bundle.LDIF_EXPECTING_SEPARATOR, line);

      final String prefix = line.substring(0, index);
      if (DirectoryService.CHANGE_OPERATION_ADD.equalsIgnoreCase(prefix)) {
        operation = DirContext.ADD_ATTRIBUTE;
      }
      else if (DirectoryService.CHANGE_OPERATION_REMOVE.equalsIgnoreCase(prefix)) {
        operation = DirContext.REMOVE_ATTRIBUTE;
      }
      else if (DirectoryService.CHANGE_OPERATION_REPLACE.equalsIgnoreCase(prefix)) {
        operation = DirContext.REPLACE_ATTRIBUTE;
      }
      else
        throwDirectoryException(Bundle.LDIF_CHANGE_TYPE_UNKNOW);

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
          throwDirectoryException(Bundle.LDIF_EXPECTING_SEPARATOR, "this.separator", line);

        String attrName = line.substring(index + 1).trim();

        if (operation == DirContext.ADD_ATTRIBUTE)
          throwDirectoryException(Bundle.LDIF_EXPECTING_ATTRIBUTE, attrName);

        Attribute attr = new BasicAttribute(attrName);
        content.add(operation, attr);
      }
      if (this.entryDone) {
        this.entryDone = false;
        break;
      }
      line = reader.readLine();
    } while (line != null && !line.equals(StringUtility.EMPTY));

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
   ** @throws DirectoryException if an I/O error occurs.
   */
  private LDAPRecord parseModificationDN(Parser reader)
    throws DirectoryException {

    String           line    = reader.readLine();
    LDAPModDNContent content = new LDAPModDNContent();
    List<Control>    control = null;
    do {
      final String newtag     = DirectoryService.RDNNEW        + this.separator;
      final String oldtag     = DirectoryService.RDNOLD_DELETE + this.separator;
      final String newparent  = DirectoryService.PARENTNEW     + this.separator;
      final String newuperior = DirectoryService.SUPERIORNEW   + this.separator;
      final String ctltag     = DirectoryService.CONTROL       + this.separator;

      if (line.startsWith(newtag) && (line.length() > (newtag.length() + 1)))
        content.rdn(line.substring(newtag.length()).trim());
      else if (line.startsWith(oldtag) && (line.length() > (oldtag.length() + 1))) {
        String str = line.substring(oldtag.length()).trim();
        if (str.equals("0"))
          content.deleteOld(false);
        else if (str.equals("1"))
          content.deleteOld(true);
        else
          throwDirectoryException(Bundle.LDIF_DELETE_OLDRDN);
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
    while (line != null && !line.equals(StringUtility.EMPTY));

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
   ** @throws DirectoryException if the line could not be parsed
   */
  private Control parseControl(String line)
    throws DirectoryException {

    byte[] value = null;
    int    index = line.indexOf(this.separator) + 2;
    // OID, must be present
    if (index >= line.length())
      throwDirectoryException(Bundle.LDIF_EXPECTING_OID);

    boolean criticality = true;
    line  = line.substring(index).trim();
    index = line.indexOf(StringUtility.BLANK);

    String  oid;
    if (index < 0)
      oid = line;
    else {
      // Optional criticality
      oid   = line.substring(0, index);
      line  = line.substring(index + 1);
      index = line.indexOf(this.separator);
      String criticalVal = (index > 0) ? line.substring(0, index) : line;
      if (criticalVal.compareTo("true") == 0)
        criticality = Control.CRITICAL;
      else if (criticalVal.compareTo("false") == 0)
        criticality = Control.NONCRITICAL;
      else
        throwDirectoryException(Bundle.LDIF_EXPECTING_CRITICALITY, criticalVal);

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
              throwDirectoryException(Bundle.LDIF_CONSTRUCT_URL, urlString);
            }
          }
          else {
            try {
              // value encoding/decoding is always done using an ISO character
              // set; files are instead of written and read with the ASCII
              // charcater set
              value = line.substring(index).trim().getBytes(StandardCharsets.ISO_8859_1);
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
   ** @throws DirectoryException if the file couldn't be opened for output
   */
  private byte[] fileContent(final String url)
    throws DirectoryException {

    StringTokenizer tokenizer = new StringTokenizer(url, "|");
    String filename = url;
    int num = tokenizer.countTokens();
    if (num == 2) {
      String token = (String)tokenizer.nextElement();
      int index = token.lastIndexOf('/');
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
      throw new DirectoryException(Bundle.format(Bundle.CONTEXT_ERROR_GENERAL, e.getLocalizedMessage()), e);
    }
    return b;
  }
}