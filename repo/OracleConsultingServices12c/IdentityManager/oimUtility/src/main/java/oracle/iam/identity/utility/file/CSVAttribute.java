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

    File        :   CSVAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    CSVAttribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.text.ParseException;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import oracle.hst.foundation.SystemError;

import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// final class CSVAttribute
// ~~~~~ ~~~~~ ~~~~~~~~~~~~
/**
 ** An <code>CSVAttribute</code> defines a descriptor for a single column of a
 ** record in a CSV file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public final class CSVAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // constants used to initialize the mandatory attribute
  public static final boolean MANDATORY      = true;
  public static final boolean OPTIONAL       = false;

  // constant definition for updatable field flag
  public static final boolean READONLY       = true;
  public static final boolean UPDATABLE      = false;

  public static final String BOOLEAN         = "Boolean";
  public static final String INTEGER         = "Integer";
  public static final String LONG            = "Long";
  public static final String FLOAT           = "Float";
  public static final String DOUBLE          = "Double";
  public static final String DATE            = "Date";
  public static final String STRING          = "String";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the index of each field in a CSV line */
  protected int               index          = -1;

  /** the name of the header field */
  protected final String      name;

  /** the type of the header field */
  protected final String      type;

  /** the format of the attribute how it is fetched from the dataProvider. */
  protected final String      formatExternal;

  /** the format of the attribute to that it should be converted internaly. */
  protected final String      formatInternal;

  /** the name of the header field where an attribute is constraint to*/
  protected final String      contstraint;
  /**
   ** defines whether the attribute allows nulls
   ** <br>
   ** true=mandatory; false=optional
   */
  private boolean             mandatory       = OPTIONAL;

  /**
   ** Flag indicates whether the attribute is updatable per default by the client.
   */
  private boolean             readonly        = UPDATABLE;

  /** the transformer executed after a field is fetched from a CSV line */
  private List<Transformer<String>> inbound;

  /** the transformer executed before a field is written to a CSV line */
  private List<Transformer<String>> outbound;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new instance and initialize with a name.
   ** <br>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the CSV column.
   ** @param  type               the type of the CSV column.
   */
  public CSVAttribute(final String name, final String type) {
    this(name, type, UPDATABLE, OPTIONAL, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new instance and initialize with a name.
   ** <br>
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
   */
  public CSVAttribute(final String name, final String type, final String formatExternal, final String formatInternal, final String constraint) {
    this(name, type, formatExternal, formatInternal, UPDATABLE, OPTIONAL, constraint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new instance and initialize with a name.
   ** <br>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the CSV column.
   ** @param  type               the type of the CSV column.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>CSVAttribute</code> is mandatory,
   **                            otherwise false.
   ** @param  constraint         the name of the field where an attribute may be
   **                            constraint to in dependency.
   */
  public CSVAttribute(final String name, final String type, final boolean mandatory, final String constraint) {
    this(name, type, UPDATABLE, mandatory, constraint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new instance and initialize with a name.
   ** <br>
   ** This instance reflects a <code>NATIV</code> attribute.
   ** <br>
   ** The column name is derived from attribute name.
   **
   ** @param  name               the mapping name of the CSV column.
   ** @param  type               the type of the CSV column.
   ** @param  readonly           <code>true</code> if this
   **                            <code>CSVAttribute</code> is non-updatable,
   **                            otherwise false.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>CSVAttribute</code> is mandatory,
   **                            otherwise false.
   ** @param  constraint         the name of the field where an attribute may be
   **                            constraint to in dependency.
   */
  public CSVAttribute(final String name, final String type, final boolean readonly, final boolean mandatory, final String constraint) {
    this(name, type, null, null, readonly, mandatory, constraint);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new instance and initialize with a name.
   ** <br>
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
   ** @param  readonly           <code>true</code> if this
   **                            <code>CSVAttribute</code> is non-updatable,
   **                            otherwise false.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>CSVAttribute</code> is mandatory,
   **                            otherwise false.
   ** @param  constraint         the name of the field where an attribute may be
   **                            constraint to in dependency.
   */
  public CSVAttribute(final String name, final String type, final String formatExternal, final String formatInternal, final boolean readonly, final boolean mandatory, final String constraint) {
    this.name           = name;
    this.type           = type;
    this.formatExternal = formatExternal;
    this.formatInternal = formatInternal;
    this.readonly       = readonly;
    this.mandatory      = mandatory;
    this.contstraint    = constraint;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name
  /**
   ** Returns the name definition to be used for mapping purposes.
   **
   ** @return                    the name of the to be used.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type definition to be used for mapping purposes.
   **
   ** @return                    the type of the attribute to convert to.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   index
  /**
   ** Returns the index to be used for fetching the value from a record of a CSV
   ** file.
   ** <br>
   ** It is recommended that as the preceeding step {@link CSVDescriptor} was
   ** able to create the index mapping for this attribute.
   **
   ** @return                    the index to be used for fetching the value
   **                            from a record of a CSV file.
   */
  public final int index() {
    return this.index;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setMandatory
  /**
   ** Sets whether the attribute allows nulls.
   **
   ** @param   mandatory         <code>true</code> if this attribute should
   **                            allow nulls; otherwise false.
   */
  public final void setMandatory(final boolean mandatory) {
    this.mandatory = mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isMandatory
  /**
   ** Returns whether the attribute do allows nulls.
   **
   ** @return                    <code>true</code> if this attribute do allow
   **                            nulls; otherwise false.
   */
  public final boolean isMandatory() {
    return this.mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReadonly
  /**
   ** Sets whether the attribute is updateable by user.
   **
   ** @param   readonly          <code>true</code> if this attribute should
   **                            allow nulls; otherwise false.
   */
  public final void setReadonly(final boolean readonly) {
    this.readonly = readonly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isReadonly
  /**
   ** Returns whether the attribute is updateable by user.
   **
   ** @return                    <code>true</code> if this attribute is
   **                            updatable; otherwise false.
   */
  public final boolean isReadonly() {
    return this.readonly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTransformer
  /**
   ** Adds the {@link Transformer} to be used for transformation purposes.
   **
   ** @param  sequence           the execution order of the specified
   **                            {@link Transformer} in the sequence of
   **                            transformation
   ** @param  transformer        the {@link Transformer} to be used for
   **                            transformation purposes.
   ** @param  inbound            indicates the execution direction of the
   **                            specified {@link Transformer} in the sequence
   **                            of transformation
   */
  public final void addTransformer(final int sequence, final Transformer<String> transformer, final boolean inbound) {
    if (inbound) {
      addInboundTransformer(sequence, transformer);
    }
    else {
      addOutboundTransformer(sequence, transformer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addTransformer
  /**
   ** Adds the {@link Transformer} to be used for transformation purposes.
   **
   ** @param  transformer        the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   ** @param  inbound            indicates the execution direction of the
   **                            specified {@link Transformer} in the sequence
   **                            of transformation
   */
  public final void addTransformer(final List<Transformer<String>> transformer, final boolean inbound) {
    if (inbound) {
      addInboundTransformer(transformer);
    }
    else {
      addOutboundTransformer(transformer);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInboundTransformer
  /**
   ** Adds the {@link Transformer} to be used for inbound transformation
   ** purposes.
   **
   ** @param  sequence           the execution order of the specified
   **                            {@link Transformer} in the sequence of
   **                            transformation
   ** @param  transformer        the {@link Transformer} to be used for
   **                            transformation purposes.
   */
  public final void addInboundTransformer(final int sequence, final Transformer<String> transformer) {
    if (this.inbound == null)
      this.inbound = new ArrayList<Transformer<String>>();

    if (sequence >= inbound.size())
      inbound.add(transformer);
    else
      inbound.set(sequence, transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInboundTransformer
  /**
   ** Adds the {@link Transformer} to be used for inbound transformation
   ** purposes.
   **
   ** @param  transformer        the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   */
  public final void addInboundTransformer(final List<Transformer<String>> transformer) {
    if (this.inbound == null)
      this.inbound = new ArrayList<Transformer<String>>();

    this.inbound.addAll(transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOutboundTransformer
  /**
   ** Adds the {@link Transformer} to be used for outbound transformation
   ** purposes.
   **
   ** @param  sequence           the execution order of the specified
   **                            {@link Transformer} in the sequence of
   **                            transformation
   ** @param  transformer        the {@link Transformer} to be used for
   **                            transformation purposes.
   */
  public final void addOutboundTransformer(final int sequence, final Transformer<String> transformer) {
    if (this.outbound == null)
      this.outbound = new ArrayList<Transformer<String>>();

    if (sequence >= outbound.size())
      this.outbound.add(transformer);
    else
      this.outbound.set(sequence, transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addOutboundTransformer
  /**
   ** Adds the {@link Transformer} to be used for outbound transformation
   ** purposes.
   **
   ** @param  transformer        the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   */
  public final void addOutboundTransformer(final List<Transformer<String>> transformer) {
    if (this.outbound == null)
      this.outbound = new ArrayList<Transformer<String>>();

    this.outbound.addAll(transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setInboundTransformer
  /**
   ** Sets the {@link Transformer} to be used for transformation purposes.
   **
   ** @param  transformer        the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   */
  public final void setInboundTransformer(final List<Transformer<String>> transformer) {
    if (this.inbound == null)
      this.inbound = new ArrayList<Transformer<String>>(transformer.size());

    this.inbound.clear();
    this.inbound.addAll(transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getInboundTransformer
  /**
   ** Returns the {@link Transformer} to be used for transformation purposes.
   **
   ** @return                    the {@link Transformer} to be used for
   **                            transformation purposes.
   */
  public final List<Transformer<String>> getInboundTransformer() {
    return this.inbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOutboundTransformer
  /**
   ** Sets the {@link Transformer} to be used for transformation purposes.
   **
   ** @param  transformer        the class to be used for transformation
   **                            purposes.
   */
  public final void setOutboundTransformer(final List<Transformer<String>> transformer) {
    if (this.outbound == null)
      this.outbound = new ArrayList<Transformer<String>>(transformer.size());

    this.outbound.clear();
    this.outbound.addAll(transformer);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getOutboundTransformer
  /**
   ** Returns the {@link Transformer} to be used for transformation purposes.
   **
   ** @return                    the {@link Transformer} to be used for
   **                            transformation purposes.
   */
  public final List<Transformer<String>> getOutboundTransformer() {
    return this.outbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (CSVSerializable)
  /**
   ** Reads a part from the specified CSV file line.
   **
   ** @param  column             the columns of a record of an CVS file.
   **
   ** @throws CSVException       if the name of this attribute was not found in
   **                            the specified CSV line.
   */
  public void read(final String[] column)
    throws CSVException {

    // reset the index always to ensure the shared descriptor is valid
    this.index = -1;

    // iterate over the set and try to find the name of the attribute in the
    // set
    for (int i = 0; i < column.length; i++) {
      if (name.equals(column[i])) {
        index = i;
        break;
      }
    }
    // not found
    if (index == -1)
      throw new CSVException(CSVError.HEADER_UNKNOWN, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write (CSVSerializable)
  /**
   ** Write the entire line to the specified CSV file writer
   **
   ** @param  writer             the {@link CSVWriter} which recieve this
   **                            line.
   **
   ** @throws CSVException       if the {@link CSVWriter} is already closed.
   */
  public void write(final CSVWriter writer)
    throws CSVException {

    writer.put(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformInbound (Transformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  origin             the <code>String</code> to transform.
   **
   ** @return                    the transformation of the specified
   **                            <code>origin</code>.
   */
  public String transformInbound(final String origin) {
    // if no transformer is registered for inbound transformation return
    // immeditally
    if (inbound == null || inbound.isEmpty())
      return origin;

    return transform(this.inbound.iterator(), origin);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformOutbound (Transformer)
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  origin             the <code>String</code> to transform.
   **
   ** @return                    the transformation of the specified
   **                            <code>origin</code>.
   */
  public String transformOutbound(final String origin) {
    // if no transformer is regsitered for outbound transformation return
    // immeditally
    if (outbound == null || outbound.isEmpty())
      return origin;

    return transform(this.outbound.iterator(), origin);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertInternal
  /**
   ** Returns the specified <code>origin</code> as an appropriate external type.
   **
   ** @param  origin             the <code>String</code> to convert.
   **
   ** @return                    the conversion of the specified
   **                            <code>origin</code>.
   **
   ** @throws CSVException       if the <code>origin</code> cannot be converted
   **                            form <code>formatExternal</code> to
   **                            <code>formatInternal</code>.
   */
  protected String convertInternal(final String origin)
    throws CSVException {

    if (STRING.equalsIgnoreCase(type)) {
      return origin;
    }
    else if (BOOLEAN.equalsIgnoreCase(type)) {
      return Boolean.valueOf(origin).toString();
    }
    else if (INTEGER.equalsIgnoreCase(type)) {
      return Integer.valueOf(origin).toString();
    }
    else if (LONG.equalsIgnoreCase(type)) {
      return Long.valueOf(origin).toString();
    }
    else if (FLOAT.equalsIgnoreCase(type)) {
      return Float.valueOf(origin).toString();
    }
    else if (DOUBLE.equalsIgnoreCase(type)) {
      return Double.valueOf(origin).toString();
    }
    else if (DATE.equalsIgnoreCase(type)) {
      return convertDate(origin, formatExternal, formatInternal);
    }

    return origin;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertExternal
  /**
   ** Returns the specified <code>origin</code> as an appropriate internal type.
   **
   ** @param  origin             the <code>String</code> to convert.
   **
   ** @return                    the conversion of the specified
   **                            <code>origin</code>.
   **
   ** @throws CSVException       if the <code>origin</code> cannot be converted
   **                            form <code>formatInternal</code> to
   **                            <code>formatExternal</code>.
   */
  protected String convertExternal(final String origin)
    throws CSVException {

    // if we don't know a value don't try to convert
    if (StringUtility.isEmpty(origin)) {
      return origin;
    }
    // if we have the same type there is nothing to convert
    else if (STRING.equalsIgnoreCase(type)) {
      return origin;
    }
    else if (BOOLEAN.equalsIgnoreCase(type)) {
      return Boolean.valueOf(origin).toString();
    }
    else if (INTEGER.equalsIgnoreCase(type)) {
      return Integer.valueOf(origin).toString();
    }
    else if (LONG.equalsIgnoreCase(type)) {
      return Long.valueOf(origin).toString();
    }
    else if (FLOAT.equalsIgnoreCase(type)) {
      return Float.valueOf(origin).toString();
    }
    else if (DOUBLE.equalsIgnoreCase(type)) {
      return Double.valueOf(origin).toString();
    }
    else if (DATE.equalsIgnoreCase(type)) {
      return convertDate(origin, formatExternal, formatInternal);
    }
    else
      return origin.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Returns the specified <code>origin</code> as an appropriate
   ** transformation.
   **
   ** @param  iterator           the <code>Iterator</code> used to perform the
   **                            sequence of transformations.
   ** @param  origin             the <code>String</code> to transform.
   **
   ** @return                    the transformation of the specified
   **                            <code>origin</code>.
   */
  private String transform(final Iterator<Transformer<String>> iterator, final String origin) {
    String transformed = origin;
    while (iterator.hasNext()) {
      Transformer<String> transformer = iterator.next();
      transformed = transformer.transform(transformed);
    }
    return transformed;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   convertDate
  /**
   ** Creates a {@link String} from the passed {@link String} object.
   ** <br>
   ** The outcome is specified by <code>targetFormat</code>.
   **
   ** @param  date               the <code>Date</code> to convert
   ** @param  sourceFormat       the source format of the <code>date</code>.
   ** @param  targetFormat       the target format of the <code>date</code>.
   **
   ** @return                    the {@link String} object for the
   **                            <code>date</code> in the format of
   **                            <code>targetFormat</code>.
   **
   ** @throws CSVException       if the <code>date</code> cannot be converted
   **                            form <code>sourceFormat</code> to
   **                            <code>targetFormat</code>.
   */
  private String convertDate(final String date, final String sourceFormat, final String targetFormat)
    throws CSVException {

    try {
      Calendar value = DateUtility.parseDate(date, sourceFormat);
      return DateUtility.formatDate(value, targetFormat);
    }
    catch (ParseException e) {
      throw new CSVException(SystemError.GENERAL, e);
    }
  }
}