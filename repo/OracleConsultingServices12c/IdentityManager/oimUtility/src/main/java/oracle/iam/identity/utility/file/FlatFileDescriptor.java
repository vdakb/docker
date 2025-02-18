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

    Copyright © 2016. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Flatfile Facilities

    File        :   FlatFileDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;

import java.io.IOException;
import java.io.EOFException;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.TransformationIterator;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.utility.resource.FlatFileBundle;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileDescriptor
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** This is the superclass for the meta information about queryable and mutable
 ** attributes in a text file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileDescriptor extends FileDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute of this descriptor object */
  private List<FlatFileAttribute>               attributes  = null;

  /** the identifier descriptor */
  private FlatFileIdentifier<FlatFileAttribute> identifiers = null;

  /** the sorting executed before a file has to be read */
  private List<Comparator<FlatFileComparator>>  sortInbound;

  /** the sorting executed after a file has to been written */
  private List<Comparator<FlatFileComparator>>  sortOutbound;

  /** the attribute of this access object */
  private Map<String, FlatFileAttribute>        mapping     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FlatFileDescriptor</code>.
   */
  public FlatFileDescriptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  attribute          the list with <code>FlatFileAttribute</code>s
   **                            to populate data from the
   **                            <code>DataProvider</code>.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public FlatFileDescriptor(final List<FlatFileAttribute> attribute)
    throws SystemException {

    // ensure inheritance
    this((FlatFileAttribute[])attribute.toArray());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  attributes         the array with <code>FlatFileAttribute</code>s
   **                            to populate data from the
   **                            <code>DataProvider</code>.
   */
  public FlatFileDescriptor(final FlatFileAttribute[] attributes) {
    // ensure inheritance
    super();

    // initialize instance attributes
    int  length      = (attributes == null) ? 0  : attributes.length;
    this.attributes  = new ArrayList<FlatFileAttribute>(length);
    this.mapping     = new HashMap<String, FlatFileAttribute>(length);
    this.identifiers = new FlatFileIdentifier<FlatFileAttribute>(0);
    for (int i = 0; i < length; i++) {
      this.attributes.add(attributes[i]);
      this.mapping.put(attributes[i].name(), attributes[i]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  identifiers        the list with names to identify a record in a
   **                            flat file.
   ** @param  attributes         the list with <code>FlatFileAttribute</code>s
   **                            to populate data from the
   **                            <code>DataProvider</code>.
   ** @param  sortInbound        the {@link List} of @link FlatFileComparator}s to
   **                            sort the file in case it is fetched from the
   **                            file system.
   ** @param  sortOutbound       the {@link List} of @link FlatFileComparator}s to
   **                            sort the file in case it is written to the
   **                            file system.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public FlatFileDescriptor(final List<String> identifiers, final List<FlatFileAttribute> attributes, final List<Comparator<FlatFileComparator>> sortInbound, final List<Comparator<FlatFileComparator>> sortOutbound)
    throws SystemException {

    this(identifiers, attributes);

    this.sortInbound  = sortInbound;
    this.sortOutbound = sortOutbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>FlatFileDescriptor</code>
   **
   ** @param  identifiers        the list with names to identify a record in a
   **                            flat file.
   ** @param  attributes         the list with <code>FlatFileAttribute</code>s
   **                            to populate data from the
   **                            <code>DataProvider</code>.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public FlatFileDescriptor(final List<String> identifiers, final List<FlatFileAttribute> attributes)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    int length      = (attributes == null) ? 0  : attributes.size();
    this.attributes = new ArrayList<FlatFileAttribute>(length);
    this.mapping    = new HashMap<String, FlatFileAttribute>(length);
    for (int i = 0; i < length; i++) {
      final FlatFileAttribute attribute = attributes.get(i);
      this.attributes.add(attribute);
      this.mapping.put(attribute.name(), attribute);
    }

    length           = (identifiers == null) ? 0 : identifiers.size();
    this.identifiers = new FlatFileIdentifier<FlatFileAttribute>(length);
    if (length > 0) {
      for (int i = 0; i < identifiers.size(); i++) {
        // mark the attribute as an identifier
        this.identifiers.add(findAttribute(identifiers.get(i)));
        // and remove it from the list of non-identifier attributes
      }
    }
  }


  //////////////////////////////////////////////////////////////////////////////
  // Accessors and Mutators
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   size
  /**
   ** Returns the number of <code>FlatFileAttribute</code>s in this descriptor.
   ** If this descriptor contains more than <code>Integer.MAX_VALUE</code>
   ** <code>FlatFileAttribute</code>s, returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return           the number of <code>FlatFileAttribute</code>s in this
   **                   descriptor.
   */
  public int size() {
    return this.attributes != null ? this.attributes.size() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAttribute
  /**
   ** Checks if the specified {@link FlatFileAttribute} is enlisted as an
   ** attribute.
   **
   ** @param  attribute          the {@link FlatFileAttribute} to check.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link FlatFileAttribute} is enlisted as an
   **                            attribute; otherwise <code>false</code>.
   */
  public boolean isAttribute(final FlatFileAttribute attribute) {
    return this.isAttribute(attribute.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAttribute
  /**
   ** Checks if the specified name is enlisted as an attribute.
   **
   ** @param  name               the name to check.
   **
   ** @return                    <code>true</code> if the specified name
   **                            is enlisted as an attribute; otherwise
   **                            <code>false</code>.
   */
  public boolean isAttribute(final String name) {
    return (this.mapping == null) ? false :  this.mapping.keySet().contains(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isIdentifier
  /**
   ** Checks if the specified {@link FlatFileAttribute} is enlisted as an
   ** identifier.
   **
   ** @param  attribute          the {@link FlatFileAttribute} to check.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link FlatFileAttribute} is enlisted as an
   **                            identifier; otherwise <code>false</code>.
   */
  public boolean isIdentifier(final FlatFileAttribute attribute) {
    return this.isIdentifier(attribute.name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isIdentifier
  /**
   ** Checks if the specified name is enlisted as an identifier.
   **
   ** @param  name               the name to check.
   **
   ** @return                    <code>true</code> if the specified name
   **                            is enlisted as an identifier; otherwise
   **                            <code>false</code>.
   */
  public boolean isIdentifier(final String name) {
    return (this.identifiers == null) ? false :  this.identifiers.contains(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifierIterator
  /**
   ** Returns the names of all identifying attributes as an {@link Iterator}.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all names of all
   **                            identifying attributes.
   */
  public final Iterator<FlatFileAttribute> identifierIterator() {
    return identifier().iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the names of all identifying attributes as a {@link List}.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link List} over all names of all
   **                            identifying attributes.
   */
  public final List<FlatFileAttribute> identifier() {
    return CollectionUtility.list(this.identifiers.iterator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributeIterator
  /**
   ** Returns an {@link Iterator} over all atrributes.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all attributes.
   */
  public final Iterator<FlatFileAttribute> attributeIterator() {
    return attribute().iterator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attribute
  /**
   ** Returns a {@link List} of all atrributes.
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    a {@link List} of all atrributes.
   */
  public final List<FlatFileAttribute> attribute() {
    return CollectionUtility.list(this.attributes.iterator());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nameIterator
  /**
   ** Returns the names of all attributes as an {@link Iterator}
   ** <br>
   ** This is returns a copy of the origin to avoid concurrency conflicts.
   **
   ** @return                    an {@link Iterator} over all names of
   **                            attributes.
   */
  public final Iterator<FlatFileAttribute> nameIterator() {
    return new TransformationIterator<FlatFileAttribute>(this.attributes.iterator()) {
      public String transform(FlatFileAttribute origin) {
        return origin.name();
      }
    };
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   findAttribute
  /**
   ** Returns the attribute for passed name of this descriptor.
   **
   ** @param  name               the name of the attribute to lookup.
   **
   ** @return                    the attribute for <code>name</code> of this
   **                            descriptor.
   */
  public final FlatFileAttribute findAttribute(final String name) {
    return (this.mapping == null) ? null :  (FlatFileAttribute)this.mapping.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundSorting
  /**
   ** Sets the {@link List} of {@link Comparator}s to be used for sorting
   ** purposes.
   **
   ** @param  comparator         the class to be used for sorting purposes.
   */
  public final void inboundSorting(final List<Comparator<FlatFileComparator>> comparator) {
    if (this.sortInbound == null)
      this.sortInbound = new ArrayList<Comparator<FlatFileComparator>>(comparator.size());

    this.sortInbound.addAll(comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   inboundSorting
  /**
   ** Returns the {@link List} of  <code>Comparator</code>s to be used for
   ** sorting purposes.
   **
   ** @return                    the class to be used for sorting purposes.
   */
  public final List<Comparator<FlatFileComparator>> inboundSorting() {
    return this.sortInbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundSorting
  /**
   ** Sets the {@link List} of <code>Comparator</code>s to be used for sorting
   ** purposes.
   **
   ** @param  comparator         the class to be used for sorting purposes.
   */
  public final void outboundSorting(final List<Comparator<FlatFileComparator>> comparator) {
    if (this.sortOutbound == null)
      this.sortOutbound = new ArrayList<Comparator<FlatFileComparator>>(comparator.size());

    this.sortOutbound.addAll(comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   outboundTransformer
  /**
   ** Returns the {@link List} of <code>Comparator</code>s to be used for
   ** sorting purposes.
   **
   ** @return                    the class to be used for sorting purposes.
   */
  public final List<Comparator<FlatFileComparator>> outboundTransformer() {
    return this.sortOutbound;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Create a new instance of {@link FlatFileAttribute} and initialize it with
   ** a name, type, formatExternal and formatInternal.
   ** <p>
   ** The created {@link FlatFileAttribute} is appended to the end of the list
   ** of attributes.
   ** <p>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the FlatFile column.
   ** @param  type               the type of the FlatFile column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   **
   ** @return                    the created {@link FlatFileAttribute}
   */
  public FlatFileAttribute addAttribute(final String name, final String type, final int start, final int length) {
    final FlatFileAttribute attribute = new FlatFileAttribute(name, type, start, length);
    addAttribute(attribute);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Create a new instance of {@link FlatFileAttribute} and initialize it with
   ** a name, type, formatExternal and formatInternal.
   ** <p>
   ** The created {@link FlatFileAttribute} is appended to the end of the list
   ** of attributes.
   ** <p>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the FlatFile column.
   ** @param  type               the type of the FlatFile column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   ** @param  formatExternal     the format of the attribute how it is fetched
   **                            from the dataProvider.
   ** @param  formatInternal     the format of the attribute to that it should
   **                            be converted internaly.
   **
   ** @return                    the created {@link FlatFileAttribute}
   */
  public FlatFileAttribute addAttribute(final String name, final String type, final int start, final int length, final String formatExternal, final String formatInternal) {
    final FlatFileAttribute attribute = new FlatFileAttribute(name, type, start, length, formatExternal, formatInternal);
    addAttribute(attribute);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Appends the specified {@link FlatFileAttribute} to the end of the list of
   ** attributes.
   **
   ** @param  attribute          the {@link FlatFileAttribute} to be appended to
   **                            the list of attributes.
   */
  public void addAttribute(final FlatFileAttribute attribute) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<FlatFileAttribute>();
      this.mapping    = new HashMap<String, FlatFileAttribute>();
    }

    this.attributes.add(attribute);
    this.mapping.put(attribute.name(), attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIdentifier
  /**
   ** Appends the specified {@link FlatFileAttribute} to the end of the list of
   ** identifiers.
   **
   ** @param  attribute          the {@link FlatFileAttribute} to be appended to
   **                            the list of identifiers.
   */
  public void addIdentifier(final FlatFileAttribute attribute) {

    if (!isAttribute(attribute))
      throw new RuntimeException(FlatFileBundle.format(FlatFileError.NOT_ENLISTED_ATTRIBUTE, attribute.name()));

    if (this.identifiers == null)
      this.identifiers = new FlatFileIdentifier<FlatFileAttribute>();

    // at first make the attribute mandatory
    attribute.mandatory(true);
    // than add the attribute to the identifiers
    this.identifiers.add(attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   identifier
  /**
   ** Returns the array with the values of the identifier form the specified
   ** entity map.
   **
   ** @param  entity             the tagged value pairs where the values of the
   **                            identifier should be extracted from.
   **
   ** @return                    the extracted identifier values of the
   **                            specified tagged value pairs.
   */
  public String[] identifier(final Map<String, Object> entity) {
    String[] key = new String[this.identifiers.size()];
    for (int i = 0; i < this.identifiers.size(); i++) {
      final String tmp = (String)entity.get(this.identifiers.get(i).name());
      key[i] = StringUtility.isEmpty(tmp) ? SystemConstant.EMPTY : tmp;
    }
    return key;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   payload
  public Object[] payload(final Map<String, Object> entity) {
    Object[] payload = new Object[this.attributes.size() - this.identifiers.size()];
    int j = 0;
    for (int i = 0; i < this.attributes.size(); i++) {
      FlatFileAttribute attribute = this.attributes.get(i);
      if (!this.identifiers.contains(attribute)) {
        final Object tmp = entity.get(this.attributes.get(i).name());
        payload[j++] = (tmp == null) ? SystemConstant.EMPTY : tmp;
      }
    }
    return payload;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   attributes
  /**
   ** Returns the array with all values of the specified entity map.
   **
   ** @param  entity             the tagged value pairs where the values of the
   **                            identifier should be extracted from.
   **
   ** @return                    the extracted identifier values of the
   **                            specified tagged value pairs.
   */
  public String[] attributes(final Map<String, String> entity) {
    String[] attribute = new String[this.attributes.size()];
    for (int i = 0; i < this.attributes.size(); i++)
      attribute[i] = entity.get(this.attributes.get(i).name());

    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read
  /**
   ** Extracts a record from the specified FlatFile reader.
   **
   ** @param  reader             the {@link FlatFileReader} where the record
   **                            should be obtained from.
   **
   ** @throws EOFException       in case the end of the file was reached.
   ** @throws TaskException      some problem reading the file, possibly
   **                            malformed data.
   */
  public void read(final FlatFileReader reader)
    throws EOFException
    ,      TaskException {

    String record = reader.readRecord();
    Iterator<FlatFileAttribute> i = attributeIterator();
    while (i.hasNext())
      i.next().read(record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the entire header line to the specified FlatFile file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  writer             the <code>FlatFileWriter</code> which recieve
   **                            this line.
   **
   ** @throws IOException        if the {@link FlatFileWriter} is already
   **                            closed.
   ** @throws TaskException      if the {@link FlatFileWriter} is already
   **                            closed.
   */
  public void write(final FlatFileWriter writer)
    throws IOException
    ,      TaskException {

    Iterator<FlatFileAttribute> i = attributeIterator();
    while (i.hasNext())
      i.next().write(writer);

    writer.newLine();
  }
}