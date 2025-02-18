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

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   CSVRecord.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVRecord.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.io.EOFException;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.ClassUtility;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// final class CSVRecord
// ~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Wrappes a record in CSV (Comma Separated Value) file (or in CSV data).
 ** A record can contain a list of attributes which describes an entry.
 ** <p>
 ** You can get an <code>CSVRecord</code> object from CSV data by calling the
 ** <code>next</code> method of the {@link CSVResultSet} object holding the
 ** data.
 ** <br>
 ** The record constains a "identifier" and the payload which is extracted from
 ** the CSV file and passed to this object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVRecord implements CSVSerializable {

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static int            errorState   = 0;
  private static String         errorText    = null;

  private static MessageDigest  digester     = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

   private Map<String, Object>  entity;
   private Key<String>          identifierComposite;
   private Hash<String>         payloadHash;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      digester = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      errorState = -20;
      errorText  = e.getLocalizedMessage();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Key
  // ~~~~~ ~~~
  /**
   ** Wrappes the identifier of a record in CSV (Comma Separated Value) file.
   */
  public class Key<E> implements Comparable<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Evaluates the MD5 hash of the given criteria.
     **
     ** @param  field            the criteria the MD5 hash will be evaluated
     **                          for.
     */
    public Key(final String[] field) {
      final StringBuilder buffer = new StringBuilder();
      // Fill the buffer with provided data to compute a composite key from.
      for (int i = 0; i < field.length; i++)
        if (!StringUtility.isEmpty(field[i]))
          buffer.append(field[i]);

      this.value = buffer.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   compareTo
    /**
     ** Compares this object with the specified object for order.
     ** <br>
     ** Returns a negative integer, zero, or a positive integer as this object
     ** is less than, equal to, or greater than the specified object.
     **
     ** @param  other            the Object to be compared.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as this object is less than, equal to, or
     **                          greater than the specified object.
     */
    @Override
    public int compareTo(final Object other) {
      if (!ClassUtility.comparable(this.getClass(), other.getClass()))
        return -1;

      return this.value.compareTo(((Key)other).value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     */
    @Override
    public int hashCode() {
      return this.value.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether the <code>other</code> object is "equal to" this one.
     ** <br>
     ** The <code>equals</code> method implements an equivalence relation
     ** on non-null object references.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the <code>other</code> argument;
     **                          <code>false</code> otherwise.
     */
    public boolean equals(final Object other) {
      if (!ClassUtility.comparable(this.getClass(), other.getClass()))
        return false;

      @SuppressWarnings("unchecked")
      final Key<String> subject = (Key<String>)other;
      return this.equals(subject);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether the <code>other</code> object is "equal to" this one.
     ** <br>
     ** The <code>equals</code> method implements an equivalence relation
     ** on non-null <code>Key</code> references.
     **
     ** @param  other            the reference <code>Key</code> with which to
     **                          compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the <code>other</code> argument;
     **                          <code>false</code> otherwise.
     */
    public boolean equals(final Key<String> other) {
      return this.value.equals(other.value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   equals
    /**
     ** Indicates whether the <code>other</code> object is "equal to" this one.
     ** <br>
     ** The <code>equals</code> method implements an equivalence relation
     ** on non-null <code>Key</code> references.
     **
     ** @param  other            the reference <code>Key</code> with which to
     **                          compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the <code>other</code> argument;
     **                          <code>false</code> otherwise.
     */
    public boolean equalsIgnoreCase(final Key<String> other) {
      return this.value.equalsIgnoreCase(((Key)other).value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Hash
  // ~~~~~ ~~~~~
  /**
   ** Wrappes the payload of a record in CSV (Comma Separated Value) file.
   ** <br>
   ** "payload" in this context means that the fields that builds the
   ** "identifier" of the record are excluded.
   */
  public class Hash<E> implements Comparable<E> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Evaluates the MD5 hash of the given criteria.
     **
     ** @param  field            the criteria the MD5 hash will be evaluated
     **                          for.
     */
    public Hash(final Object[] field) {
      // Ensure the digest's buffer is empty.
      digester.reset();

      // Fill the digest's buffer with provided data to compute a message digest
      // from.
      for (int i = 0; i < field.length; i++) {
        if (field[i] == null)
          field[i] = SystemConstant.EMPTY;
        final String content = field[i].toString();
        if (!StringUtility.isEmpty(content))
          digester.update(content.getBytes());
      }

      // Generate the digest.
      // This does any necessary padding required by the algorithm.
      this.value = StringUtility.bytesToHex(digester.digest());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   compareTo
    /**
     ** Compares this object with the specified object for order.
     ** <br>
     ** Returns a negative integer, zero, or a positive integer as this object
     ** is less than, equal to, or greater than the specified object.
     **
     ** @param  other            the Object to be compared.
     **
     ** @return                  a negative integer, zero, or a positive integer
     **                          as this object is less than, equal to, or
     **                          greater than the specified object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int compareTo(final Object other) {
      if (!ClassUtility.comparable(this.getClass(), other.getClass()))
        return -1;

      return this.value.compareTo(((Hash)other).value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
    /**
     ** Returns the hash code value for this <code>Hash</code>. To get the hash
     ** code of this <code>Hash</code>.
     ** <p>
     ** This ensures that <code>s1.equals(s2)</code> implies that
     ** <code>s1.hashCode()==s2.hashCode()</code> for any two
     ** <code>Hash</code> <code>s1</code> and <code>s2</code>, as required by
     ** the general contract of <code>Object.hashCode()</code>.
     **
     ** @return                  the hash code value for this
     **                          <code>Hash</code>.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      return this.value.hashCode();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether the <code>other</code> object is "equal to" this one.
     ** <br>
     ** The <code>equals</code> method implements an equivalence relation
     ** on non-null object references.
     **
     ** @param  other            the reference object with which to compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the <code>other</code> argument;
     **                          <code>false</code> otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean equals(final Object other) {
      if (!ClassUtility.comparable(this.getClass(), other.getClass()))
        return false;

      @SuppressWarnings("unchecked")
      final Hash<String> subject = (Hash<String>)other;
      return this.equals(subject);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   equals (overridden)
    /**
     ** Indicates whether the <code>other</code> object is "equal to" this one.
     ** <br>
     ** The <code>equals</code> method implements an equivalence relation
     ** on non-null <code>Hash</code> references.
     **
     ** @param  other            the reference <code>Hash</code> with which to
     **                          compare.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the <code>other</code> argument;
     **                          <code>false</code> otherwise.
     */
    public boolean equals(final Hash<String> other) {
      return this.value.equals(other.value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>CSVRecord</code> from the givent entity.
   **
   ** @param  entity             the {@link Map} containing the data.
   ** @param  identifier         the array of strings specifying the identifier
   **                            part of the data.
   ** @param  payload            the array of strings specifying the payload
   **                            part of the data.
   */
  public CSVRecord(final Map<String, Object> entity, final String[] identifier, final Object[] payload) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.entity              = entity;
    this.identifierComposite = new Key<String>(identifier);
    this.payloadHash         = new Hash<String>(payload);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   get
  /**
   ** Retrieves the entire entity mapping of this <code>CSVRecord</code> object.
   **
   ** @return                    the entire entity mapping.
   **
   */
  public Map<String, Object> get() {
    return this.entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (CSVSerializer)
  /**
   ** Extracts a record from the specified CSV reader.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  column             the array of string with the content.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @return                    a {@link Map} with the attribute names as the
   **                            keys and the values extractd from the file
   **                            asssociated to the appropriate key.
   **
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   */
  public Map<String, Object> read(final CSVProcessor processor, final String[] column, final boolean applyTransformer)
    throws EOFException
    ,      CSVException {

    // if we are here the CSV record was fetched; otherwise the reader has
    // thrown an EOFException and the array is not filled
    read(processor, column, this.entity, applyTransformer);
    return this.entity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write (CSVSerializable)
  /**
   ** Write the entire line to the specified CSV file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  writer             the {@link CSVWriter} that receive to
   **                            output
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the transformation cannot be applied.
   */
  public void write(final CSVProcessor processor, final CSVWriter writer, final boolean applyTransformer)
    throws IOException
    ,      CSVException {

    write(processor, writer, this.entity, applyTransformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Extracts a record from the specified CSV reader.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  column             the array of string with the content.
   ** @param  entity             the {@link Map} that recceive the attribute
   **                            names as the keys and the values extractd from
   **                            the file asssociated to the appropriate key.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws EOFException       at end of file after all the fields have
   **                            been read.
   ** @throws CSVException       if there is no content in the record for the
   **                            specified attribute name.
   */
  public static void read(final CSVProcessor processor, final String[] column, final Map<String, Object> entity, final boolean applyTransformer)
    throws EOFException
    ,      CSVException {

    // if we are here the CSV record was fetched; otherwise the reader has
    // thrown an EOFException and the array is not filled
//    if (entity == null)
//      entity = new HashMap(column.length);
//    else
      entity.clear();

    // check if we have a valid line
    // this check assumes that the returnd array of fields has the same length
    // as the descriptor specifies
    if (column.length == 1)
      // seems to be we have an empty line
      return;

    // check if we have a valid line
    // this check assumes that the returnd array of fields has the same length
    // as the descriptor specifies
    if (column.length == 1)
      // seems to be we have an empty line
      return;

    // iterate over all attributes and fetch the appropriate values
    for (CSVAttribute attribute : processor.descriptor().attribute()) {
      if (applyTransformer) {
        String value = null;
        // at first apply the format conversion, this creates the
        // appropriate object instance
        value = attribute.convertExternal(column[attribute.index()]);
        // the object instance the fetched string was converted is now
        // passed to the transformer
        value = attribute.transformInbound(value);
        entity.put(attribute.name(), value);
      }
      else
        entity.put(attribute.name(), column[attribute.index()]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the entire line to the specified CSV file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  writer             the {@link CSVWriter} that receive to
   **                            output
   ** @param  entity             the {@link Map} containing the attribute names
   **                            as the keys and the values to write.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the transformation cannot be applied.
   */
  public static void write(final CSVProcessor processor, final CSVWriter writer, final Map<String, Object> entity, final boolean applyTransformer)
    throws IOException
    ,      CSVException {

    // create a container that large enough to hold all known attributes
    Map<String, String> line = new HashMap<String, String>(processor.descriptor().size());

    // iterate over all attributes and fetch the appropriate values
    for (CSVAttribute attribute : processor.descriptor().attribute()) {
      String value = (String)entity.get(attribute.name());
      if (applyTransformer) {
        // at first apply the format conversion, this creates the
        // appropriate object instance
        value = attribute.convertExternal(value);
        // the object instance the fetched string was converted is now
        // passed to the transformer
        value = attribute.transformInbound(value);
      }
      line.put(attribute.name(), value);
    }
    writer.putLine(processor, line);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the entire line to the specified CSV file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  processor          the {@link CSVProcessor} to control the
   **                            marshalling.
   ** @param  writer             the {@link CSVWriter} that receive to
   **                            output
   ** @param  entity             the {@link Map} containing the attribute names
   **                            as the keys and the values to write.
   ** @param  applyTransformer   <code>true</code> advice that a transformation
   **                            should take place.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the transformation cannot be applied.
   */
  public static void write(final CSVProcessor processor, final CSVWriter writer, final tcResultSet entity, final boolean applyTransformer)
    throws IOException
    ,      CSVException {

    // create a container that large enough to hold all known attributes
    Map<String, String> line = new HashMap<String, String>(processor.descriptor().size());

    // iterate over all attributes and fetch the appropriate values
    for (CSVAttribute attribute : processor.descriptor().attribute()) {
      String       value     = null;
      try {
        value = entity.getStringValue(attribute.name());
        if (applyTransformer) {
          // at first apply the format conversion, this creates the
          // appropriate object instance
          value = attribute.convertExternal(value);
          // the object instance the fetched string was converted is now
          // passed to the transformer
          value = attribute.transformInbound(value);
        }
      }
      catch (tcColumnNotFoundException e) {
        value = e.getLocalizedMessage();
      }
      catch (tcAPIException e) {
        value = e.getLocalizedMessage();
      }
      line.put(attribute.name(), value);
    }
    writer.putLine(processor, line);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getString
  /**
   ** Retrieves the value of the designated column in this
   ** <code>CSVRecord</code> object as a <code>String</code> in the Java
   ** programming language.
   **
   ** @param  columnName         the CSV name of the column
   **
   ** @return                    the column value.
   */
  public String getString(final String columnName) {
    return (String)this.entity.get(columnName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
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
   **       <code>hashCode</code> method on each of the two objects must produce
   **       the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results.  However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   */
  @Override
  public int hashCode() {
    return this.identifierComposite.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is &quot;equal to&quot; this
   ** <code>CSVRecord</code>.
   ** <br>
   ** This method must obey the general contract of
   ** <code>Object.equals(Object)</code>. Additionally, this method can return
   ** <code>true</code> <i>only</i> if the specified Object is also a
   ** <code>CSVRecord</code> and it imposes the same key values..
   ** <p>
   ** Note: that it is <i>always</i> safe <i>not</i> to override
   ** <code>Object.equals(Object)</code>. However, overriding this method may,
   ** in some cases, improve performance by allowing programs to determine
   ** that two distinct Comparators impose the same order.
   **
   ** @param  other               the reference object with which to compare.
   **
   ** @return                     <code>true</code> only if the specified object
   **                             is also a <code>CSVRecord</code> and it
   **                             imposes the same key values.
   **
   ** @see    java.lang.Object#equals(java.lang.Object)
   ** @see    java.lang.Object#hashCode()
   */
  public boolean equals(final Object other) {
    if (!ClassUtility.comparable(this.getClass(), other.getClass()))
      return false;

    CSVRecord record = (CSVRecord)other;
    return this.identifierComposite.equals(record.identifierComposite);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareKey
  /**
   ** Compares the key  of <code>CSVRecord</code>s lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings of the keys. The character sequence represented by each
   ** <code>String</code> object is compared lexicographically to the
   ** character sequence represented by the argument object. The result is a
   ** negative integer if this <code>CSVRecord</code> object lexicographically
   ** precedes the argument object. The result is a positive integer if this
   ** <code>CSVRecord</code> object lexicographically follows the argument
   ** object. The result is zero if the objects are equal;
   ** <code>compareTo</code> returns <code>0</code> exactly when the
   ** {@link #equals(Object)} method would return <code>true</code>.
   **
   ** @param  other              the <code>CSVRecord</code> to be compared.
   **
   ** @return                    the value <code>0</code> if the key of the
   **                            argument is equal to this
   **                            <code>CSVRecord</code>; a value less than
   **                            <code>0</code> if the key of this
   **                            <code>CSVRecord</code> is lexicographically
   **                            less than the argument; and a value greater
   **                            than <code>0</code> if key of this
   **                            <code>CSVRecord</code> is lexicographically
   **                            greater than the key of the argument.
   */
  public int compareKey(final CSVRecord other) {
    return this.identifierComposite.compareTo(other.identifierComposite);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compareField
  /**
   ** Compares a attribute of <code>CSVRecord</code>s lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings of the attributes. The character sequence represented by each
   ** <code>String</code> object is compared lexicographically to the
   ** character sequence represented by the argument object. The result is a
   ** negative integer if this <code>CSVRecord</code> object lexicographically
   ** precedes the argument object. The result is a positive integer if this
   ** <code>CSVRecord</code> object lexicographically follows the argument
   ** object. The result is zero if the objects are equal;
   ** <code>compareTo</code> returns <code>0</code> exactly when the
   ** {@link #equals(Object)} method would return <code>true</code>.
   **
   ** @param  other              the <code>CSVRecord</code> to be compared.
   ** @param  name               the name of the attribute to be compared.
   **
   ** @return                    the value <code>0</code> if the attribute of
   **                            the argument is equal to this
   **                            <code>CSVRecord</code>; a value less than
   **                            <code>0</code> if the attribute of this
   **                            <code>CSVRecord</code> is lexicographically
   **                            less than the argument; and a value greater
   **                            than <code>0</code> if attribute of this
   **                            <code>CSVRecord</code> is lexicographically
   **                            greater than the key of the argument.
   **
   ** @throws CSVException       if there is no content in the record for the
   **                            specified attribute name.
   */
  public int compareField(final CSVRecord other, final String name)
    throws CSVException {

    if (!this.entity.containsKey(name))
      throw new CSVException(CSVError.CONTENT_NOT_FOUND, name);

    if (!other.entity.containsKey(name))
      throw new CSVException(CSVError.CONTENT_NOT_FOUND, name);

    final String thisField  = (String)this.entity.get(name);
    final String otherField = (String)other.entity.get(name);
    return thisField.compareTo(otherField);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   comparePayload
  /**
   ** Compares the payload of <code>CSVRecord</code>s lexicographically.
   ** <br>
   ** The comparison is based on the Unicode value of each character in the
   ** strings of the keys. The character sequence represented by each
   ** <code>String</code> object is compared lexicographically to the
   ** character sequence represented by the argument object. The result is a
   ** negative integer if this <code>CSVRecord</code> object lexicographically
   ** precedes the argument object. The result is a positive integer if this
   ** <code>CSVRecord</code> object lexicographically follows the argument
   ** object. The result is zero if the objects are equal;
   ** <code>compareTo</code> returns <code>0</code> exactly when the
   ** {@link #equals(Object)} method would return <code>true</code>.
   **
   ** @param  other              the <code>CSVRecord</code> to be compared.
   **
   ** @return                    the value <code>0</code> if the argument is
   **                            equal to this <code>CSVRecord</code>; a value
   **                            less than <code>0</code> if this
   **                            <code>CSVRecord</code> is lexicographically
   **                            less than the argument; and a value greater
   **                            than <code>0</code> if this
   **                            <code>CSVRecord</code> is lexicographically
   **                            greater than the argument.
   */
  public boolean comparePayload(final CSVRecord other) {
    return this.payloadHash.equals(other.payloadHash);
  }
}