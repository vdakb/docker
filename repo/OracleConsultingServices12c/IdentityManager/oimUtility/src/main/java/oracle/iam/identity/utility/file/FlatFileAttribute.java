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

    File        :   FlatFileAttribute.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    FlatFileAttribute.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.2.0      2016-10-15  DSteding    First release version
*/

package oracle.iam.identity.utility.file;

import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;

import java.text.ParseException;

import java.util.Iterator;

import oracle.hst.foundation.utility.Transformer;
import oracle.hst.foundation.utility.DateUtility;
import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskException;

////////////////////////////////////////////////////////////////////////////////
// final class FlatFileAttribute
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** An <code>FlatFileAttribute</code> defines a descriptor for a single column
 ** of a record in a flat file.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.2.0
 ** @since   1.0.2.0
 */
public final class FlatFileAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // constants used to initialize the mandatory attribute
  public static final boolean      MANDATORY      = true;
  public static final boolean      OPTIONAL       = false;

  // constant definition for updatable field flag
  public static final boolean      READONLY       = true;
  public static final boolean      UPDATABLE      = false;

  public static final String       BOOLEAN         = "Boolean";
  public static final String       INTEGER         = "Integer";
  public static final String       LONG            = "Long";
  public static final String       FLOAT           = "Float";
  public static final String       DOUBLE          = "Double";
  public static final String       DATE            = "Date";
  public static final String       STRING          = "String";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the start position of the field in a text line */
  private int                      start;
  private int                      length;
	private boolean                  trim            = true;

  /** the name of the header field */
  protected final String            name;

  /** the type of the field */
  protected final String            type;

  /** the format of the attribute how it is fetched from the dataProvider. */
  protected final String            formatExternal;

  /** the format of the attribute to that it should be converted internaly. */
  protected final String            formatInternal;

  /**
   ** defines whether the attribute allows nulls
   ** <br>
   ** true=mandatory; false=optional
   */
  private boolean                   mandatory       = OPTIONAL;

  /**
   ** Flag indicates whether the attribute is updatable per default by the client.
   */
  private boolean                   readonly        = UPDATABLE;

  /** the transformer executed after a field is fetched from a flat file line */
  private List<Transformer<String>> inbound;

  /** the transformer executed before a field is written to a flat file line */
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
   ** @param  name               the mapping name of the column.
   ** @param  type               the type of the column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   */
  public FlatFileAttribute(final String name, final String type, final int start, final int length) {
    this(name, type, start, length, UPDATABLE, OPTIONAL);
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
   ** @param  name               the mapping name of the TXT column.
   ** @param  type               the type of the TXT column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   ** @param  formatExternal     the format of the attribute how it is fetched
   **                            from the dataProvider.
   ** @param  formatInternal     the format of the attribute to that it should
   **                            be converted internaly.
   */
  public FlatFileAttribute(final String name, final String type, final int start, final int length, final String formatExternal, final String formatInternal) {
    this(name, type, start, length, formatExternal, formatInternal, UPDATABLE, OPTIONAL);
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
   ** @param  name               the mapping name of the TXT column.
   ** @param  type               the type of the TXT column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>FlatFileAttribute</code> is mandatory,
   **                            otherwise false.
   */
  public FlatFileAttribute(final String name, final String type, final int start, final int length, final boolean mandatory) {
    this(name, type, start, length, UPDATABLE, mandatory);
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
   ** @param  name               the mapping name of the TXT column.
   ** @param  type               the type of the TXT column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   ** @param  readonly           <code>true</code> if this
   **                            <code>FlatFileAttribute</code> is non-updatable,
   **                            otherwise false.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>FlatFileAttribute</code> is mandatory,
   **                            otherwise false.
   */
  public FlatFileAttribute(final String name, final String type, final int start, final int length, final boolean readonly, final boolean mandatory) {
    this(name, type, start, length, null, null, readonly, mandatory);
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
   ** @param  name               the mapping name of the TXT column.
   ** @param  type               the type of the TXT column.
   ** @param  start              the start position of the column.
   ** @param  length             the character length of the column.
   ** @param  formatExternal     the format of the attribute how it is fetched
   **                            from the dataProvider.
   ** @param  formatInternal     the format of the attribute to that it should
   **                            be converted internaly.
   ** @param  readonly           <code>true</code> if this
   **                            <code>FlatFileAttribute</code> is non-updatable,
   **                            otherwise false.
   ** @param  mandatory          <code>true</code> if this
   **                            <code>FlatFileAttribute</code> is mandatory,
   **                            otherwise false.
   */
  public FlatFileAttribute(final String name, final String type, final int start, final int length, final String formatExternal, final String formatInternal, final boolean readonly, final boolean mandatory) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name           = name;
    this.type           = type;
    this.start          = start;
    this.length         = length;
    this.formatExternal = formatExternal;
    this.formatInternal = formatInternal;
    this.readonly       = readonly;
    this.mandatory      = mandatory;
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
  // Method:   start
  /**
   ** Returns the start position to be used for fetching the value from a record
   ** of a text file.
   ** <br>
   ** It is recommended that as the preceeding step {@link FlatFileDescriptor}
   ** was able to create the start position for this attribute.
   **
   ** @return                    the start position to be used for fetching the
   **                            value from a record of a text file.
   */
  public final int start() {
    return this.start;
  }

  //////////////////////////////////////////////////////////////////////////////
  /**
   ** Returns the length to be used for fetching the value from a record of a
   ** text file.
   ** <br>
   ** It is recommended that as the preceeding step {@link FlatFileDescriptor}
   ** was able to create the length for this attribute.
   **
   ** @return                    the length to be used for fetching the value
   **                            from a record of a text file.
   */
  public final int length() {
    return this.length;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mandatory
  /**
   ** Sets whether the attribute allows nulls.
   **
   ** @param   mandatory         <code>true</code> if this attribute should
   **                            allow nulls; otherwise false.
   */
  public final void mandatory(final boolean mandatory) {
    this.mandatory = mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mandatory
  /**
   ** Returns whether the attribute do allows nulls.
   **
   ** @return                    <code>true</code> if this attribute do allow
   **                            nulls; otherwise false.
   */
  public final boolean mandatory() {
    return this.mandatory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Sets whether the attribute is updateable by user.
   **
   ** @param   readonly          <code>true</code> if this attribute should
   **                            allow nulls; otherwise false.
   */
  public final void readonly(final boolean readonly) {
    this.readonly = readonly;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   readonly
  /**
   ** Returns whether the attribute is updateable by user.
   **
   ** @return                    <code>true</code> if this attribute is
   **                            updatable; otherwise false.
   */
  public final boolean readonly() {
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
   ** Sets the {@link Transformer}s to be used for transformation purposes.
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
   ** Returns the {@link Transformer}s to be used for transformation purposes.
   **
   ** @return                    the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   */
  public final List<Transformer<String>> getInboundTransformer() {
    return this.inbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setOutboundTransformer
  /**
   ** Sets the {@link Transformer}s to be used for transformation purposes.
   **
   ** @param  transformer        the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
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
   ** Returns the {@link Transformer}s to be used for transformation purposes.
   **
   ** @return                    the {link List} of {@link Transformer}s to be
   **                            used for transformation purposes.
   */
  public final List<Transformer<String>> getOutboundTransformer() {
    return this.outbound;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   read (TXTSerializable)
  /**
   ** Reads a part from the specified text file line.
   **
   ** @param  line               a record of a text file.
   **
   ** @return                    the raw data obtained from the passed string.
   */
  public String read(final String line) {
    final String raw = line.substring(this.start, this.start + this.length);
    return StringUtility.isEmpty(raw, false) ? null : (this.trim ? raw.trim() : raw);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write (TXTSerializable)
  /**
   ** Write the entire line to the specified TXT file writer
   **
   ** @param  writer             the {@link FlatFileWriter} which recieve this
   **                            line.
   **
   ** @throws TaskException      if the {@link FlatFileWriter} is already
   **                            closed.
   */
  public void write(final FlatFileWriter writer)
    throws TaskException {

    writer.put(this.name);
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
  // Method:   convertInternal
  /**
   ** Returns the specified <code>origin</code> as an appropriate external type.
   **
   ** @param  origin             the <code>String</code> to convert.
   **
   ** @return                    the conversion of the specified
   **                            <code>origin</code>.
   **
   ** @throws FlatFileException  if the <code>origin</code> cannot be converted
   **                            form <code>formatExternal</code> to
   **                            <code>formatInternal</code>.
   */
  protected String convertInternal(final String origin)
    throws FlatFileException {

    if (STRING.equalsIgnoreCase(this.type)) {
      return origin;
    }
    else if (BOOLEAN.equalsIgnoreCase(this.type)) {
      return Boolean.valueOf(origin).toString();
    }
    else if (INTEGER.equalsIgnoreCase(this.type)) {
      return Integer.valueOf(origin).toString();
    }
    else if (LONG.equalsIgnoreCase(this.type)) {
      return Long.valueOf(origin).toString();
    }
    else if (FLOAT.equalsIgnoreCase(this.type)) {
      return Float.valueOf(origin).toString();
    }
    else if (DOUBLE.equalsIgnoreCase(this.type)) {
      return Double.valueOf(origin).toString();
    }
    else if (DATE.equalsIgnoreCase(this.type)) {
      return convertDate(origin, this.formatExternal, this.formatInternal);
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
   ** @throws FlatFileException  if the <code>origin</code> cannot be converted
   **                            form <code>formatInternal</code> to
   **                            <code>formatExternal</code>.
   */
  protected String convertExternal(final String origin)
    throws FlatFileException {

    // if we don't know a value don't try to convert
    if (StringUtility.isEmpty(origin)) {
      return origin;
    }
    // if we have the same type there is nothing to convert
    else if (STRING.equalsIgnoreCase(type)) {
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
   ** @throws FlatFileException  if the <code>date</code> cannot be converted
   **                            form <code>sourceFormat</code> to
   **                            <code>targetFormat</code>.
   */
  private String convertDate(final String date, final String sourceFormat, final String targetFormat)
    throws FlatFileException {

    try {
      Calendar value = DateUtility.parseDate(date, sourceFormat);
      return DateUtility.formatDate(value, targetFormat);
    }
    catch (ParseException e) {
      throw new FlatFileException(e);
    }
  }
}