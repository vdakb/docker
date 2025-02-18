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

    File        :   DSMLV1Reader.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLV1Reader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.File;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// class DSMLV1Reader
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Directory Synchronization Markup Language (DSML) is a file format used to
 ** import directory data into an LDAP directory and to describe a set of
 ** changes to be applied to data in the underlying directory. This format is
 ** described in the OASIS draft
 ** <a href="http://www.oasis-open.org/committees/dsml/docs/DSMLv2.doc" target="_blank">TDirectory Services Markup Language v2.0 December 18, 2001</a>.
 ** <p>
 ** Whereas DSML version 1 provides a means for representing directory contents
 ** XML documents, DSML version 2 with bindings such as the SOAP
 ** Request/Response Binding, allows for directories to be manipulated via XML.
 ** DSMLv2 focuses on extending the reach of LDAP directories. Therefore, as in
 ** DSMLv1, the design approach is not to abstract the capabilities of LDAP
 ** directories as they exist today, but instead to faithfully represent LDAP
 ** directories in XML. The difference is that DSMLv1 represented the state of a
 ** directory while DSMLv2 represents the operations that an LDAP directory can
 ** perform and the results of such operations. Therefore the design approach
 ** for DSMLv2 is to express LDAP requests and responses as XML document
 ** fragments.
 ** <p>
 ** This class implements an DSML V1 input operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class DSMLV1Reader extends DSMLReader {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV1Reader</code> object to parse the DSML from a
   ** specified file.
   ** <p>
   ** Essentially it does the following (implementations are free to optimized
   ** but must do equivalent processing):
   ** <ol>
   **   <li>Positions the parser on the first valid element and verify that this
   **       is a  {@link DSMLGrammar#V1#Namespace#ROOT}.
   **   <li>Obtain the namespace declaration if any.
   **   <li>Positions the parser on the secon valid element and verify that this
   **       is a  {@link DSMLGrammar#V1#Entries#ELEMENT}.
   ** </ol>
   ** @param  file               the abstract path of the DSML file to parse.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected DSMLV1Reader(final File file)
    throws FeatureException {

    // parser inheritance
    super(file, DSMLGrammar.V1.Namespace.ROOT);

    assertNextTag();
    assertStartElement(DSMLGrammar.V1.Entries.ELEMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   nextRecord (LDAPReader)
  /**
   ** Returns the next record in the DSML data.
   ** <p>
   ** You can call this method repeatedly to iterate through all records in the
   ** DSMLdata.
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

    return hasNext() ? parse() : null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasNext
  /**
   ** Returns <code>true</code> if there are more parsing entries and
   ** <code>false</code> if there are no more entries. This method will return
   ** <code>false</code> if the current state of the XMLStreamReader is
   ** {@link DSMLInput#END_DOCUMENT}.
   **
   ** @return                    <code>true</code> if there are more entries,
   **                            <code>false</code> otherwise.
   **
   ** @throws FeatureException   if there is a fatal error detecting the next
   **                            state.
   */
  private boolean hasNext()
    throws FeatureException {

    final int    eventType = assertNextTag();
    final String eventName = this.parser.getLocalName();
    if (eventType == DSMLInput.END_ELEMENT && DSMLGrammar.V1.Namespace.ROOT.equals(eventName)) {
      return false;
    }
    else if (eventType == DSMLInput.END_ELEMENT && DSMLGrammar.V1.Entries.ELEMENT.equals(eventName)) {
      return hasNext();
    }
    else if (eventType == DSMLInput.END_ELEMENT && DSMLGrammar.V1.Entries.Elements.ENTRY.equals(eventName)) {
      return hasNext();
    }
    else {
      return true;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses DSML content.
   **
   ** @return                    the next record as a {@link LDAPRecord} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord parse()
    throws FeatureException {

    assertStartElement(DSMLGrammar.V1.Entries.Elements.ENTRY);

    // at this stage we are on an <dsml:entry> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V1.Entries.Elements.ENTRY, DSMLGrammar.V1.Entries.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V1.Entries.Elements.ENTRY, DSMLGrammar.V1.Entries.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    final LDAPAddContent content = new LDAPAddContent(this.parser.getAttributeValue(0));

    // position the parser on the next valid element this should be either
    // <dsml:objectclass> or <dsml:attr>
    assertNextTag();

    // first path try <dsml:objectclass> declared before <dsml:attr> elements
    String type = this.parser.getLocalName();
    if (DSMLGrammar.V1.Entries.Elements.OBJECT_CLASS.equals(type)) {
      content.add(parseOjectClass());
    }
    else if (DSMLGrammar.V1.Entries.Elements.ATTRIBUTE.equals(type)) {
      while (DSMLInput.START_ELEMENT == this.parser.getEventType() && DSMLGrammar.V1.Entries.Elements.ATTRIBUTE.equals(this.parser.getLocalName()))
        content.add(parseAttribute());
    }
    else {
      final String[] parameter = {DSMLGrammar.V1.Entries.Elements.ATTRIBUTE, type};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_OPENING_TAG, exceptionPosition(parameter));
    }

    // second path try <dsml:objectclass> declared after <dsml:attr> elements
    type = this.parser.getLocalName();
    if (DSMLGrammar.V1.Entries.Elements.ATTRIBUTE.equals(type)) {
      while (DSMLInput.START_ELEMENT == this.parser.getEventType() && DSMLGrammar.V1.Entries.Elements.ATTRIBUTE.equals(this.parser.getLocalName()))
        content.add(parseAttribute());
    }
    else if (DSMLGrammar.V1.Entries.Elements.OBJECT_CLASS.equals(type)) {
      content.add(parseOjectClass());
    }
    assertEndElement(DSMLGrammar.V1.Entries.Elements.ENTRY);
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseOjectClass
  /**
   ** Parses DSML content specific to objectClasses.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private Attribute parseOjectClass()
    throws FeatureException {

    assertStartElement(DSMLGrammar.V1.Entries.Elements.OBJECT_CLASS);
    final Attribute objectClass = new BasicAttribute("objectClass");
    // position the parser on the next valid element
    assertNextTag();
    // loop over the object class values until the end tag is reached
    while (DSMLInput.END_ELEMENT != this.parser.getEventType()) {
      objectClass.add(parseValue(DSMLGrammar.V1.Entries.Elements.OBJECT_CLASS_VALUE));
      assertNextTag();
    }
    assertEndElement(DSMLGrammar.V1.Entries.Elements.OBJECT_CLASS);
    assertNextTag();
    return objectClass;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseAttribute
  /**
   ** Parses DSML content specific to attribute.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private Attribute parseAttribute()
    throws FeatureException {

    assertStartElement(DSMLGrammar.V1.Entries.Elements.ATTRIBUTE);

    // at this stage we are on an <dsml:attr> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V1.Entries.Elements.ATTRIBUTE, DSMLGrammar.V1.Entries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    else if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V1.Entries.Elements.ATTRIBUTE, DSMLGrammar.V1.Entries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    final Attribute attribute = new BasicAttribute(this.parser.getAttributeValue(0));
    // position the parser on the next valid element loop over the attribute
    // values if there are any until the end tag is reached
    while (DSMLInput.START_ELEMENT == assertNextTag() && DSMLGrammar.V1.Entries.Elements.VALUE.equals(this.parser.getLocalName())) {
      attribute.add(parseValue(DSMLGrammar.V1.Entries.Elements.VALUE));
    }
    assertEndElement(DSMLGrammar.V1.Entries.Elements.ATTRIBUTE);
    assertNextTag();
    return attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseValue
  /**
   ** Parses DSML content specific to values.
   ** <br>
   ** Pre-Condition: the current event is {@link DSMLInput#START_ELEMENT}.
   ** <br>
   ** Post-Condition: the current event is the corresponding
   ** {@link DSMLInput#END_ELEMENT}.
   **
   ** @param  type               the expected type attribute element either
   **                            <ul>
   **                              <li>DSMLGrammar.Entries.Elements.OBJECT_CLASS_VALUE
   **                              <li>DSMLGrammar.Entries.Elements.VALUE
   **                            </ul>
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private String parseValue(final String type)
    throws FeatureException {

    assertStartElement(type);
    // at this stage we are on an oc-value or value hence we can start the work
    if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {type, DSMLGrammar.V1.Entries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    boolean encoded = false;
    if (this.parser.getAttributeCount() > 0 && DSMLGrammar.V1.Entries.Attributes.Encodings.BASE64.equals(this.parser.getAttributeValue(0))) {
      encoded = true;
    }
    String value = encoded ? new String(Base64Transcoder.decode(elementText())) : elementText();

    assertEndElement(type);
    return value;
  }
}