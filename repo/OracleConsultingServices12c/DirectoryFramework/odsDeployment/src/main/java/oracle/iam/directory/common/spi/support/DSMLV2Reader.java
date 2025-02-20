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

    File        :   DSMLV2Reader.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLV2Reader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.File;

import javax.naming.directory.Attribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.BasicAttribute;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.Base64Transcoder;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// class DSMLV2Reader
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Directory Synchronization Markup Language (DSML) is a file format used to
 ** import directory data into an LDAP directory and to describe a set of
 ** changes to be applied to data in the underlying directory. This format is
 ** described in the OASIS draft
 ** <a href="http://www.oasis-open.org/committees/dsml/docs/DSMLv2.doc" target="_blank">Directory Services Markup Language v2.0 December 18, 2001</a>.
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
 ** This class implements an DSML V2 input operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class DSMLV2Reader extends DSMLReader {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String        id;
  private final Error         error;
  private final Processing    processing;
  private final ResponseOrder responseOder;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Processing
  // ~~~~ ~~~~~~~~~~
  public enum Processing {
      SEQUENTIAL("sequential")
    , PARALLEL("parallel")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4152844656139920681")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Processing</code>
     **
     ** @param  value            the string value.
     */
    Processing(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper processing instruction from the given
     ** string value.
     ** <p>
     ** This attribute is optional hence if the passed value isn't resolvable
     ** <code>null</code> is returned.
     **
     ** @param  value              the string value the processing instruction
     **                            should be returned for.
     **
     ** @return                    the processing instruction.
     **                            <br>
     **                            This attribute is optional hence if the
     **                            passed value isn't resolvable
     **                            <code>SEQUENTIAL</code> is returned.
     */
    public static Processing from(final String value) {
      for (Processing cursor : Processing.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      return SEQUENTIAL;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum ResponseOrder
  // ~~~~ ~~~~~~~~~~~~~
  public enum ResponseOrder {
      SEQUENTIAL("sequential")
    , UNORDERED("unordered")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:4010974220374923021")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ResponseOrder</code>
     **
     ** @param  value            the string value.
     */
    ResponseOrder(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper response order from the given string
     ** value.
     ** <p>
     ** This attribute is optional hence if the passed value isn't resolvable
     ** <code>null</code> is returned.
     **
     ** @param  value              the string value the response order
     **                            should be returned for.
     **
     ** @return                    the response order.
     **                            <br>
     **                            This attribute is optional hence if the
     **                            passed value isn't resolvable
     **                            <code>SEQUENTIAL</code> is returned.
     */
    public static ResponseOrder from(final String value) {
      for (ResponseOrder cursor : ResponseOrder.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      return SEQUENTIAL;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Error
  // ~~~~ ~~~~~
  public enum Error {
      RESUME("resume")
    , EXIT("exit")
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:6395215398556936641")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Error</code>
     **
     ** @param  value            the string value.
     */
    Error(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper error behavior from the given string
     ** value.
     ** <p>
     ** This attribute is optional hence if the passed value isn't resolvable
     ** <code>null</code> is returned.
     **
     ** @param  value              the string value the response order
     **                            should be returned for.
     **
     ** @return                    the error behavior.
     **                            <br>
     **                            This attribute is optional hence if the
     **                            passed value isn't resolvable
     **                            <code>EXIT</code> is returned.
     */
    public static Error from(final String value) {
      for (Error cursor : Error.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      return EXIT;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum RequestType
  // ~~~~ ~~~~~~~~~~~
  public enum RequestType {
      ADD(DSMLGrammar.V2.AddRequest.ELEMENT)
    , MODIFY(DSMLGrammar.V2.ModifyRequest.ELEMENT)
    , DELETE(DSMLGrammar.V2.DeleteRequest.ELEMENT)
    , RENAME(DSMLGrammar.V2.RenameRequest.ELEMENT)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:3971782291962120387")
    static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>RequestType</code>
     **
     ** @param  value            the string value.
     */
    RequestType(final String value) {
      this.value   = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper request type from the given string
     ** value.
     **
     ** @param  value              the string value the request type should be
     **                            returned for.
     **
     ** @return                    the request type.
     */
    public static RequestType from(final String value) {
      for (RequestType cursor : RequestType.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLV2Reader</code> object to parse the DSML from a
   ** specified file.
   ** <p>
   ** Essentially it does the following (implementations are free to optimized
   ** but must do equivalent processing):
   ** <ol>
   **   <li>Positions the parser on the first valid element and verify that this
   **       is a  {@link DSMLGrammar#V2#Namespace#ROOT}.
   **   <li>Obtain the namespace declaration if any.
   **   <li>Positions the parser on the secon valid element and verify that this
   **       is a  {@link DSMLGrammar#V2#Entries#ELEMENT}.
   ** </ol>
   ** @param  file               the abstract path of the DSML file to parse.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected DSMLV2Reader(final File file)
    throws FeatureException {

    // parser inheritance
    super(file, DSMLGrammar.V2.Request.ROOT);

    // if the document specifies only the namesspace without a prefix mapping
    // we can not query the parser with the namespace context for an attribute
    // value hence
    if (StringUtility.isEmpty(this.prefix)) {
      // obtain the attribute values which controls the request without
      // namespace
      this.id           = this.parser.getAttributeValue(null, DSMLGrammar.V2.Request.Attributes.ID);
      this.error        = Error.from(this.parser.getAttributeValue(null, DSMLGrammar.V2.Request.Attributes.ERROR));
      this.processing   = Processing.from(this.parser.getAttributeValue(null, DSMLGrammar.V2.Request.Attributes.PROCESSING));
      this.responseOder = ResponseOrder.from(this.parser.getAttributeValue(null, DSMLGrammar.V2.Request.Attributes.ORDER));
    }
    else {
      // obtain the attribute values which controls the request by namespace
      this.id           = this.parser.getAttributeValue(this.namespaceURI, DSMLGrammar.V2.Request.Attributes.ID);
      this.error        = Error.from(this.parser.getAttributeValue(this.namespaceURI, DSMLGrammar.V2.Request.Attributes.ERROR));
      this.processing   = Processing.from(this.parser.getAttributeValue(this.namespaceURI, DSMLGrammar.V2.Request.Attributes.PROCESSING));
      this.responseOder = ResponseOrder.from(this.parser.getAttributeValue(this.namespaceURI, DSMLGrammar.V2.Request.Attributes.ORDER));
    }
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
   ** DSML document.
   ** <p>
   ** <table summary="">
   ** <tr>
   **   <td><b>Pre-Condition</b>:</td>
   **   <td>
   **     the current event is {@link DSMLInput#START_ELEMENT} and the event
   **     name is either
   **     <ul>
   **       <li>DSMLGrammar.V2.AddRequest.ELEMENT
   **       <li>DSMLGrammar.V2.DeleteRequest.ELEMENT
   **       <li>DSMLGrammar.V2.ModifyRequest.ELEMENT
   **       <li>DSMLGrammar.V2.ModDNRequest.ELEMENT
   **     </ul>
   **   .</td>
   ** </tr>
   ** <tr>
   **   <td><b>Post-Condition</b>:</td>
   **   <td>
   **     the current event is {@link DSMLInput#END_ELEMENT} and the event
   **     name is either
   **     <ul>
   **       <li>DSMLGrammar.V2.AddRequest.ELEMENT
   **       <li>DSMLGrammar.V2.DeleteRequest.ELEMENT
   **       <li>DSMLGrammar.V2.ModifyRequest.ELEMENT
   **       <li>DSMLGrammar.V2.ModDNRequest.ELEMENT
   **     </ul>
   **   </td>
   ** </tr>
   ** </table>
   **
   ** @return                    the next record as a {@link LDAPRecord} object
   **                            or <code>null</code> if there are no more
   **                            records.
   **
   ** @throws FeatureException   if an I/O error occurs
   **
   ** @see    LDAPReader
   */
  @Override
  public LDAPRecord nextRecord()
    throws FeatureException {

    if (!hasNext())
      return null;

    return parse(RequestType.from(this.parser.getLocalName()));
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
    if (eventType == DSMLInput.END_ELEMENT && DSMLGrammar.V2.Request.ROOT.equals(eventName)) {
      return false;
    }
    // at here we have to have one of the requests which we will now convert
    // this will throw an IllegalArgumentException if the name of the event
    // isn't a request type
    if ((eventType == DSMLInput.END_ELEMENT) && (RequestType.ADD.value.equals(eventName) || RequestType.MODIFY.value.equals(eventName) || RequestType.DELETE.value.equals(eventName) || RequestType.RENAME.value.equals(eventName))) {
      return hasNext();
    }
    else if (eventType == DSMLInput.START_ELEMENT  && (RequestType.ADD.value.equals(eventName) || RequestType.MODIFY.value.equals(eventName) || RequestType.DELETE.value.equals(eventName) || RequestType.RENAME.value.equals(eventName))) {
      return true;
    }
    else {
      throw new FeatureException(FeatureError.DSML_UNEXPECTED_EVENT, exceptionPosition(eventName));
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
  private LDAPRecord parse(final RequestType type)
    throws FeatureException {

    switch (type) {
      case ADD    : return addRequest();
      case MODIFY : return modRequest();
      case DELETE : return delRequest();
      case RENAME : return renRequest();
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   additionRequest
  /**
   ** Parses DSML content specific to attribute.
   **
   ** @return                    the next record as a {@link LDAPAddContent}
   **                            object.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPAddContent addRequest()
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:addRequest>
    assertStartElement(DSMLGrammar.V2.AddRequest.ELEMENT);

    // at this stage we are on an <dsml:entry> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.AddRequest.Attributes.NAME, DSMLGrammar.V2.AddRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V2.AddRequest.Attributes.NAME, DSMLGrammar.V2.AddRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    final LDAPAddContent content = new LDAPAddContent(this.parser.getAttributeValue(0));
    // position the parser on the next valid element which should be a
    // <dsml:attr>
    assertNextTag();
    while (DSMLInput.START_ELEMENT == this.parser.getEventType() && DSMLGrammar.V2.RequestEntries.Elements.ATTRIBUTE.equals(this.parser.getLocalName())) {
      parseAddition(content);
    }
    // assert that the expected end element is at the current position which
    // should be  </dsml:addRequest>
    assertEndElement(DSMLGrammar.V2.AddRequest.ELEMENT);
    return content;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   modRequest
  /**
   ** Parses DSML content specific to attribute modifications.
   **
   ** @return                    the next record as a {@link LDAPModifyContent}
   **                            object.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPRecord modRequest()
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:modifyRequest>
    assertStartElement(DSMLGrammar.V2.ModifyRequest.ELEMENT);

    // at this stage we are on an <dsml:entry> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Attributes.NAME, DSMLGrammar.V2.ModifyRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Attributes.NAME, DSMLGrammar.V2.ModifyRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    final LDAPModifyContent content = new LDAPModifyContent(this.parser.getAttributeValue(0));
    // position the parser on the next valid element which should be a
    // <dsml:modification>
    assertNextTag();
    while (DSMLInput.START_ELEMENT == this.parser.getEventType() && DSMLGrammar.V2.ModifyRequest.Elements.ATTRIBUTE.equals(this.parser.getLocalName())) {
      parseModification(content);
    }
    // assert that the expected end element is at the current position which
    // should be  </dsml:modifyRequest>
    assertEndElement(DSMLGrammar.V2.ModifyRequest.ELEMENT);
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   renRequest
  /**
   ** Parses DSML content specific to rename entries.
   **
   ** @return                    the next record as a {@link LDAPModDNContent}
   **                            object.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPModDNContent renRequest()
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:modDNRequest>
    assertStartElement(DSMLGrammar.V2.RenameRequest.ELEMENT);

    // at this stage we are on an <dsml:entry> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.AddRequest.Attributes.NAME, DSMLGrammar.V2.AddRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    if (this.parser.getAttributeCount() > 4) {
      final String[] parameter = {DSMLGrammar.V2.AddRequest.Attributes.NAME, DSMLGrammar.V2.AddRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }

    final LDAPModDNContent content = new LDAPModDNContent(
      this.parser.getAttributeValue(0)
    , this.parser.getAttributeValue(null, DSMLGrammar.V2.RenameRequest.Attributes.RDN)
    , this.parser.getAttributeValue(null, DSMLGrammar.V2.RenameRequest.Attributes.SUPERIOR)
    , Boolean.getBoolean(this.parser.getAttributeValue(null, DSMLGrammar.V2.RenameRequest.Attributes.DELETE_OLD))
    );
    // position the parser on the next valid element which should be a
    // </dsml:modDNRequest>
    assertNext();
    // assert that the expected end element is at the current position which
    // should be  </dsml:modDNRequest>
    assertEndElement(DSMLGrammar.V2.RenameRequest.ELEMENT);
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delRequest
  /**
   ** Parses DSML content specific to delete entries.
   **
   ** @return                    the next record as a {@link LDAPDeleteContent}
   **                            object.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private LDAPDeleteContent delRequest()
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:delRequest>
    assertStartElement(DSMLGrammar.V2.DeleteRequest.ELEMENT);

    // at this stage we are on an <dsml:entry> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Attributes.NAME, DSMLGrammar.V2.ModifyRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Attributes.NAME, DSMLGrammar.V2.ModifyRequest.Attributes.DN};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }

    final LDAPDeleteContent content = new LDAPDeleteContent(this.parser.getAttributeValue(0));

    // position the parser on the next valid element which should be a
    // </dsml:delRequest>
    assertNext();
    // assert that the expected end element is at the current position which
    // should be  </dsml:delRequest>
    assertEndElement(DSMLGrammar.V2.DeleteRequest.ELEMENT);
    return content;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseAddition
  /**
   ** Parses DSML content specific to attribute addition.
   **
   ** @param  content            the container receiving the changes to be
   **                            applied at the server side.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private void parseAddition(final LDAPRecord content)
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:attr>
    assertStartElement(DSMLGrammar.V2.RequestEntries.Elements.ATTRIBUTE);

    // at this stage we are on an <dsml:attr> hence we can start the work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.RequestEntries.Elements.ATTRIBUTE, DSMLGrammar.V2.RequestEntries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    else if (this.parser.getAttributeCount() > 1) {
      final String[] parameter = {DSMLGrammar.V2.RequestEntries.Elements.ATTRIBUTE, DSMLGrammar.V2.RequestEntries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }

    final Attribute attribute = new BasicAttribute(this.parser.getAttributeValue(0));
    // position the parser on the next valid element loop over the attribute
    // values if there are any until the end tag is reached
    while (DSMLInput.START_ELEMENT == assertNextTag() && DSMLGrammar.V2.RequestEntries.Elements.VALUE.equals(this.parser.getLocalName())) {
      attribute.add(parseValue(DSMLGrammar.V2.RequestEntries.Elements.VALUE));
    }
    content.add(attribute);
    // assert that the expected end element is at the current position which
    // should be a </dsml:attr>
    assertEndElement(DSMLGrammar.V2.RequestEntries.Elements.ATTRIBUTE);
    // position the parser on the next valid element
    assertNextTag();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parseModification
  /**
   ** Parses DSML content specific to attribute modification.
   **
   ** @param  content            the container reciving the changes to be
   **                            applied at the server side.
   **
   ** @throws FeatureException   if an I/O error occurs
   */
  private void parseModification(final LDAPModifyContent content)
    throws FeatureException {

    // assert that the expected start element is at the current position which
    // should be a <dsml:modification>
    assertStartElement(DSMLGrammar.V2.ModifyRequest.Elements.ATTRIBUTE);

    // at this stage we are on an <dsml:modification> hence we can start the
    // work
    if (this.parser.getAttributeCount() == 0) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.ELEMENT, DSMLGrammar.V2.ModifyRequest.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }
    else if (this.parser.getAttributeCount() > 2) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Elements.ATTRIBUTE, DSMLGrammar.V2.ModifyRequest.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }

    final String operation = this.parser.getAttributeValue(null, DSMLGrammar.V2.ModifyRequest.Attributes.OPERATION);
    if (StringUtility.isEmpty(operation)) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.ELEMENT, DSMLGrammar.V2.ModifyRequest.Attributes.OPERATION};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }

    final String name = this.parser.getAttributeValue(null, DSMLGrammar.V2.ModifyRequest.Attributes.NAME);
    if (StringUtility.isEmpty(name)) {
      final String[] parameter = {DSMLGrammar.V2.ModifyRequest.Elements.ATTRIBUTE, DSMLGrammar.V2.ModifyRequest.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE, exceptionPosition(parameter));
    }

    final Attribute attribute = new BasicAttribute(name);
    // position the parser on the next valid element loop over the attribute
    // values if there are any until the end tag is reached
    while (DSMLInput.START_ELEMENT == assertNextTag() && DSMLGrammar.V2.RequestEntries.Elements.VALUE.equals(this.parser.getLocalName())) {
      attribute.add(parseValue(DSMLGrammar.V2.RequestEntries.Elements.VALUE));
    }
    if ("replace".equals(operation))
      content.add(DirContext.REPLACE_ATTRIBUTE, attribute);
    else if ("remove".equals(operation))
      content.add(DirContext.REMOVE_ATTRIBUTE, attribute);
    else
      content.add(attribute);

    // assert that the expected end element is at the current position which
    // should be a </dsml:modification>
    assertEndElement(DSMLGrammar.V2.ModifyRequest.Elements.ATTRIBUTE);
    // position the parser on the next valid element
    assertNextTag();
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
      final String[] parameter = {type, DSMLGrammar.V2.RequestEntries.Attributes.NAME};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_ATTRIBUTE_ONCE, exceptionPosition(parameter));
    }
    boolean encoded = false;
    if (this.parser.getAttributeCount() > 0 && DSMLGrammar.V2.RequestEntries.Attributes.Encodings.BASE64.equals(this.parser.getAttributeValue(0))) {
      encoded = true;
    }
    String value = encoded ? new String(Base64Transcoder.decode(elementText())) : elementText();

    assertEndElement(type);
    return value;
  }
}