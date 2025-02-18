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

    File        :   CSVDescriptor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CSVDescriptor.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.Iterator;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.IOException;
import java.io.EOFException;

import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.TransformationIterator;

////////////////////////////////////////////////////////////////////////////////
// final class CSVDescriptor
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** This is the superclass for the meta information about queryable and mutable
 ** attributes in a CSV file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVDescriptor extends FileDescriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the attribute of this descriptor object */
  private List<CSVAttribute>              attributes  = null;

  /** the identifier descriptor */
  private CSVIdentifier<CSVAttribute>     identifiers = null;

  /** the sorting executed before a file has to be read */
  private List<Comparator<CSVComparator>> sortInbound;

  /** the sorting executed after a file has to been written */
  private List<Comparator<CSVComparator>> sortOutbound;

  /** the attribute of this access object */
  private Map<String, CSVAttribute>       mapping     = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>CSVDescriptor</code>.
   */
  public CSVDescriptor() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  attribute          the list with <code>CSVAttribute</code>s to
   **                            populate data from the
   **                            <code>dataProvider</code>.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public CSVDescriptor(final List<CSVAttribute> attribute)
    throws SystemException {

    // ensure inheritance
    this(attribute.toArray(new CSVAttribute[0]));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  attributes         the array with <code>CSVAttribute</code>s to
   **                            populate data from the
   **                            <code>dataProvider</code>.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public CSVDescriptor(final CSVAttribute[] attributes)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    int  length      = (attributes == null) ? 0  : attributes.length;
    this.attributes  = new ArrayList<CSVAttribute>(length);
    this.mapping     = new HashMap<String, CSVAttribute>(length);
    this.identifiers = new CSVIdentifier<CSVAttribute>(0);
    for (int i = 0; i < length; i++) {
      this.attributes.add(attributes[i]);
      this.mapping.put(attributes[i].name(), attributes[i]);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  identifiers        the list with names to identify a record in a CSV file.
   ** @param  attributes         the list with <code>CSVAttribute</code>s to
   **                            populate data from the
   **                            <code>dataProvider</code>.
   ** @param  sortInbound        the {@link List} of @link CSVComparator}s to
   **                            sort the file in case it is fetched from the
   **                            file system.
   ** @param  sortOutbound       the {@link List} of @link CSVComparator}s to
   **                            sort the file in case it is written to the
   **                            file system.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public CSVDescriptor(final List<String> identifiers, final List<CSVAttribute> attributes, final List<Comparator<CSVComparator>> sortInbound, final List<Comparator<CSVComparator>> sortOutbound)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.sortInbound  = sortInbound;
    this.sortOutbound = sortOutbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: Ctor
  /**
   ** Constructor for <code>CSVDescriptor</code>
   **
   ** @param  identifiers        the list with names to identify a record in a CSV file.
   ** @param  attributes         the list with <code>CSVAttribute</code>s to
   **                            populate data from the
   **                            <code>dataProvider</code>.
   **
   ** @throws SystemException    in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public CSVDescriptor(final List<String> identifiers, final List<CSVAttribute> attributes)
    throws SystemException {

    // ensure inheritance
    super();

    // initialize instance attributes
    int length      = (attributes == null) ? 0  : attributes.size();
    this.attributes = new ArrayList<CSVAttribute>(length);
    this.mapping    = new HashMap<String, CSVAttribute>(length);
    for (int i = 0; i < length; i++) {
      final CSVAttribute attribute = attributes.get(i);
      this.attributes.add(attribute);
      this.mapping.put(attribute.name(), attribute);
    }

    length           = (identifiers == null) ? 0 : identifiers.size();
    this.identifiers = new CSVIdentifier<CSVAttribute>(length);
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
   ** Returns the number of <code>CSVAttribute</code>s in this descriptor. If
   ** this descriptor contains more than <code>Integer.MAX_VALUE</code>
   ** <code>CSVAttribute</code>s, returns <code>Integer.MAX_VALUE</code>.
   **
   ** @return           the number of <code>CSVAttribute</code>s in this
   **                   descriptor.
   */
  public int size() {
    return this.attributes != null ? this.attributes.size() : 0;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isAttribute
  /**
   ** Checks if the specified {@link CSVAttribute} is enlisted as an attribute.
   **
   ** @param  attribute          the {@link CSVAttribute} to check.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link CSVAttribute} is enlisted as an
   **                            attribute; otherwise <code>false</code>.
   */
  public boolean isAttribute(final CSVAttribute attribute) {
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
   ** Checks if the specified {@link CSVAttribute} is enlisted as an identifier.
   **
   ** @param  attribute          the {@link CSVAttribute} to check.
   **
   ** @return                    <code>true</code> if the specified
   **                            {@link CSVAttribute} is enlisted as an
   **                            identifier; otherwise <code>false</code>.
   */
  public boolean isIdentifier(final CSVAttribute attribute) {
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
  public final Iterator<CSVAttribute> identifierIterator() {
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
  public final List<CSVAttribute> identifier() {
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
  public final Iterator<CSVAttribute> attributeIterator() {
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
  public final List<CSVAttribute> attribute() {
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
  public final Iterator<CSVAttribute> nameIterator() {
    return new TransformationIterator<CSVAttribute>(this.attributes.iterator()) {
      public String transform(CSVAttribute origin) {
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
  public final CSVAttribute findAttribute(final String name) {
    return (this.mapping == null) ? null :  (CSVAttribute)this.mapping.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInboundSorting
  /**
   ** Sets the {@link List} of {@link Comparator}s to be used for sorting
   ** purposes.
   **
   ** @param  comparator         the class to be used for sorting purposes.
   */
  public final void setInboundSorting(final List<Comparator<CSVComparator>> comparator) {
    if (this.sortInbound == null)
      this.sortInbound = new ArrayList<Comparator<CSVComparator>>(comparator.size());

    this.sortInbound.addAll(comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInboundSorting
  /**
   ** Returns the {@link List} of  <code>Comparator</code>s to be used for
   ** sorting purposes.
   **
   ** @return                    the class to be used for sorting purposes.
   */
  public final List<Comparator<CSVComparator>> getInboundSorting() {
    return this.sortInbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOutboundSorting
  /**
   ** Sets the {@link List} of <code>Comparator</code>s to be used for sorting
   ** purposes.
   **
   ** @param  comparator         the class to be used for sorting purposes.
   */
  public final void setOutboundSorting(final List<Comparator<CSVComparator>> comparator) {
    if (this.sortOutbound == null)
      this.sortOutbound = new ArrayList<Comparator<CSVComparator>>(comparator.size());

    this.sortOutbound.addAll(comparator);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOutboundTransformer
  /**
   ** Returns the {@link List} of <code>Comparator</code>s to be used for
   ** sorting purposes.
   **
   ** @return                    the class to be used for sorting purposes.
   */
  public final List<Comparator<CSVComparator>> getOutboundTransformer() {
    return this.sortOutbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Create a new instance of {@link CSVAttribute} and initialize it with a
   ** name, type, formatExternal and formatInternal.
   ** <p>
   ** The created {@link CSVAttribute} is appended to the end of the list of
   ** attributes.
   ** <p>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the CSV column.
   ** @param  type               the type of the CSV column.
   ** @param  formatExternal     the format of the attribute how it is fetched
   **                            from the dataProvider.
   ** @param  formatInternal     the format of the attribute to that it should
   **                            be converted internaly.
   ** @param  constraint         the name of the field where an attribute may be
   **                            constraint to in dependency.
   **
   ** @return                    the created {@link CSVAttribute}
   */
  public CSVAttribute addAttribute(final String name, final String type, final String formatExternal, final String formatInternal, final String constraint) {
    final CSVAttribute attribute = new CSVAttribute(name, type, formatExternal, formatInternal, constraint);
    addAttribute(attribute);
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addAttribute
  /**
   ** Appends the specified {@link CSVAttribute} to the end of the list of
   ** attributes.
   **
   ** @param  attribute          the {@link CSVAttribute} to be appended to the
   **                            list of attributes.
   */
  public void addAttribute(final CSVAttribute attribute) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<CSVAttribute>();
      this.mapping    = new HashMap<String, CSVAttribute>();
    }

    this.attributes.add(attribute);
    this.mapping.put(attribute.name(), attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addIdentifier
  /**
   ** Appends the specified {@link CSVAttribute} to the end of the list of
   ** identifiers.
   **
   ** @param  attribute          the {@link CSVAttribute} to be appended to the
   **                            list of identifiers.
   **
   ** @throws CSVException       if the name of the attribute is not enlisted in
   **                            the registered attributes.
   */
  public void addIdentifier(final CSVAttribute attribute)
    throws CSVException {

    if (!isAttribute(attribute))
      throw new CSVException(CSVError.NOT_ENLISTED_ATTRIBUTE, attribute.name());

    if (this.identifiers == null)
      this.identifiers = new CSVIdentifier<CSVAttribute>();

    // at first make the attribute mandatory
    attribute.setMandatory(true);
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
      CSVAttribute attribute = this.attributes.get(i);
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
   ** Extracts a record from the specified CSV reader.
   **
   ** @param  reader             the {@link CSVReader} where the record should
   **                            be obtained from.
   **
   ** @throws EOFException       in case the end of the file was reached.
   ** @throws CSVException       some problem reading the file, possibly
   **                            malformed data.
   */
  public void read(final CSVReader reader)
    throws EOFException
    ,      CSVException {

    String[] record = reader.readRecord();
    Iterator<CSVAttribute> i = attributeIterator();
    while (i.hasNext())
      i.next().read(record);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the entire header line to the specified CSV file writer.
   ** <br>
   ** The line is closed by a newline character.
   **
   ** @param  writer             the <code>CSVWriter</code> which recieve this
   **                            line.
   **
   ** @throws IOException        if the {@link CSVWriter} is already closed.
   ** @throws CSVException       if the {@link CSVWriter} is already closed.
   */
  public void write(final CSVWriter writer)
    throws IOException
    ,      CSVException {

    Iterator<CSVAttribute> i = attributeIterator();
    while (i.hasNext())
      i.next().write(writer);

    writer.newLine();
  }
}