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

    File        :   DSMLReader.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DSMLReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.support;

import java.io.File;

import javax.xml.stream.XMLStreamException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureException;

////////////////////////////////////////////////////////////////////////////////
// abstract class DSMLReader
// ~~~~~~~~ ~~~~~~ ~~~~~~~~~~
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
public abstract class DSMLReader extends LDAPReader {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace components if declared  */
  protected String          prefix;
  protected String          namespaceURI;

  /** the parser used to walk through the document */
  protected final DSMLInput parser;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>DSMLReader</code> object to parse the DSML from a
   ** specified file.
   ** <p>
   ** Essentially it does the following (implementations are free to optimized
   ** but must do equivalent processing):
   ** <ol>
   **   <li>Positions the parser on the first valid element and verify that this
   **       is a {@link DSMLGrammar.V1.Namespace#ROOT}.
   **   <li>Obtain the namespace declaration if any.
   **   <li>Positions the parser on the secon valid element and verify that this
   **       is a {@link DSMLGrammar.V1.Entries#ELEMENT}.
   ** </ol>
   **
   ** @param  file               the abstract path of the DSML file to parse.
   ** @param  root               the name of the XML element expected at start.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  protected DSMLReader(final File file, final String root)
    throws FeatureException {

    // parser inheritance
    super(file, NULL);

    // initialize instance
    try {
      this.parser = new DSMLInput(null, this.stream);
    }
    catch (XMLStreamException e) {
      throw new FeatureException(FeatureError.UNHANDLED, e);
    }
    // position the parser on the first valid element which is the start of the
    assertNextTag();
    // verify that the file has the correct root entry
    assertStartElement(root);
    // optain the namespace declaration if any
    this.prefix       = this.parser.getPrefix();
    this.namespaceURI = this.parser.getNamespaceURI(this.prefix);
    if (!StringUtility.isEmpty(this.prefix) && StringUtility.isEmpty(this.namespaceURI))
      throw new FeatureException(FeatureError.DSML_EXPECTIING_NAMESPACE, exceptionPosition(root));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Factory Method to create a proper DSML reader belonging to the provided
   ** version.
   **
   ** @param  file               the abstract path of the DSML file to parse.
   ** @param  version            the DSML version the parser has to support.
   **
   ** @return                    a proper DSML reader belonging to the provided
   **                            version.
   **
   ** @throws FeatureException   if the specified file could not be found.
   */
  public static DSMLReader create (final File file, final int version)
    throws FeatureException {

    switch (version) {
      case 1  : return new DSMLV1Reader(file);
      case 2  : return new DSMLV2Reader(file);
      default : return null;// TODO: throw a FeatureException
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertStartElement
  /**
   ** Test if the current event is of {@link DSMLInput#START_ELEMENT} and if the
   ** namespace and name match the current namespace and name of the givent
   ** event. If the namespaceURI is <code>null</code> it is not checked for
   ** equality, if the localName is <code>null</code> it is not checked for
   ** equality.
   **
   ** @param  eventName          the localName of the event, may be
   **                            <code>null</code>
   **
   ** @throws FeatureException  if the required values are not matched.
   */
  protected void assertStartElement(final String eventName)
    throws FeatureException {

    try {
      this.parser.require(DSMLInput.START_ELEMENT, this.namespaceURI, eventName);
    }
    catch (XMLStreamException e) {
      final String[] parameter = {eventName, this.parser.getLocalName()};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_OPENING_TAG, exceptionPosition(parameter));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertEndElement
  /**
   ** Test if the current event is of {@link DSMLInput#END_ELEMENT} and if the
   ** namespace and name match the current namespace and name of the givent
   ** event. If the namespaceURI is <code>null</code> it is not checked for
   ** equality, if the localName is <code>null</code> it is not checked for
   ** equality.
   **
   ** @param  eventName          the localName of the event, may be
   **                            <code>null</code>
   **
   ** @throws FeatureException  if the required values are not matched.
   */
  protected void assertEndElement(final String eventName)
    throws FeatureException {

    try {
      this.parser.require(DSMLInput.END_ELEMENT, this.namespaceURI, eventName);
    }
    catch (XMLStreamException e) {
      final String[] parameter = {eventName, this.parser.getLocalName()};
      throw new FeatureException(FeatureError.DSML_EXPECTIING_CLOSING_TAG, exceptionPosition(parameter));
    }
  }

 //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNext
  /**
   ** Returns the next parsing event.
   ** <p>
   ** <b>NOTE:</b>
   ** <br>
   ** Empty elements (such as &lt;tag/&gt;) will be reported with two separate
   ** events: {@link DSMLInput#START_ELEMENT}, {@link DSMLInput#END_ELEMENT} -
   ** This preserves parsing equivalency of empty element to
   ** &lt;tag&gt;&lt;/tag&gt;.
   ** <p>
   ** This method will throw an IllegalStateException if it is called after
   ** hasNext() returns <code>false</code>.
   **
   ** @return                    the integer code corresponding to the current
   **                            parse event.
   **
   ** @throws FeatureException   if there is an error processing the underlying
   **                            DSML source.
   */
  protected int assertNext()
    throws FeatureException {

    try {
      // position the parser on the next valid element
      return this.parser.next();
    }
    catch (XMLStreamException e) {
      throw new FeatureException(FeatureError.DSML_UNEXPECTED_EVENT, exceptionPosition(DSMLInput.eventTypeString(this.parser.getEventType())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   assertNextTag
  /**
   ** Skips any white space (isWhiteSpace() returns <code>true</code>),
   ** {@link DSMLInput#COMMENT}, or {@link DSMLInput#PROCESSING_INSTRUCTION},
   ** until a {@link DSMLInput#START_ELEMENT} or {@link DSMLInput#END_ELEMENT}
   ** is reached.
   ** <p>
   ** If other than white space characters, {@link DSMLInput#COMMENT},
   ** {@link DSMLInput#PROCESSING_INSTRUCTION}, {@link DSMLInput#START_ELEMENT},
   ** {@link DSMLInput#END_ELEMENT} are encountered, an exception is thrown.
   ** This method should be used when processing element-only content seperated
   ** by white space.
   ** <table summary="">
   ** <tr>
   **   <td><b>Pre-Condition</b>:</td>
   **   <td>none</td>
   ** </tr>
   ** <tr>
   **   <td><b>Post-Condition</b>:</td>
   **   <td>
   **     the current event is {@link DSMLInput#START_ELEMENT} or
   **     {@link DSMLInput#END_ELEMENT} and cursor may have moved over any
   **     whitespace event.
   **   </td>
   ** </tr>
   ** </table>
   **
   ** @return                    the event type of the element read (
   **                            {@link DSMLInput#START_ELEMENT} or
   **                            {@link DSMLInput#END_ELEMENT})
   **
   ** @throws FeatureException   if the current event is not white space,
   **                            {@link DSMLInput#PROCESSING_INSTRUCTION},
   **                            {@link DSMLInput#START_ELEMENT} or
   **                            {@link DSMLInput#END_ELEMENT}.
   */
  protected int assertNextTag()
    throws FeatureException {

    try {
      // position the parser on the next valid element
      return this.parser.nextTag();
    }
    catch (XMLStreamException e) {
      throw new FeatureException(FeatureError.DSML_UNEXPECTED_EVENT, exceptionPosition(DSMLInput.eventTypeString(this.parser.getEventType())));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elementText
  /**
   ** Reads the content of a text-only element, an exception is thrown if this
   ** is not a text-only element. Regardless of value of
   ** javax.xml.stream.isCoalescing this method always returns coalesced
   ** content.
   ** <table summary="">
   ** <tr>
   **   <td><b>Pre-Condition</b>:</td>
   **   <td>the current event is {@link DSMLInput#START_ELEMENT}.</td>
   ** </tr>
   ** <tr>
   **   <td><b>Post-Condition</b>:</td>
   **   <td>
   **     the current event is the corresponding {@link DSMLInput#END_ELEMENT}.
   **   </td>
   ** </tr>
   ** </table>
   **
   ** @return                    the content parsed from the text-only element.
   **
   ** @throws FeatureException   if the current event is not a
   **                            {@link DSMLInput#START_ELEMENT} or if a non
   **                            text element is encountered.
   */
  protected String elementText()
    throws FeatureException {

    final StringBuilder content = new StringBuilder();
    int eventType = assertNext();
    while(eventType != DSMLInput.END_ELEMENT ) {
      if (eventType == DSMLInput.CHARACTERS || eventType == DSMLInput.CDATA || eventType == DSMLInput.SPACE || eventType == DSMLInput.ENTITY_REFERENCE) {
        content.append(this.parser.getText());
      }
      else if (eventType == DSMLInput.PROCESSING_INSTRUCTION || eventType == DSMLInput.COMMENT) {
        // skipping
      }
      else if (eventType == DSMLInput.END_DOCUMENT) {
        throw new FeatureException(FeatureError.DSML_ROOT_CLOSING_OUTSIDE,  exceptionPosition(this.parser.getLocalName()));
      }
      else if (eventType == DSMLInput.START_ELEMENT) {
        throw new FeatureException(FeatureError.DSML_TAG_OPENING_NOT_RECOGNIZED,  exceptionPosition(this.parser.getLocalName()));
      }
      else {
        throw new FeatureException(FeatureError.DSML_UNEXPECTED_EVENT, this.parser.getLocalName());
      }
      eventType = assertNext();
    }
    return content.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exceptionPosition
  /**
   ** Returns the location (line number and colum) where the current event
   ** stays extended by the given string value.
   **
   ** @param  argument           the substitution for placholder contained in
   **                            the message regarding to <code>code</code>.
   **
   ** @return                    the location (line number and colum) where the
   **                            current event stays.
   */
  protected String[] exceptionPosition(final String argument) {
    final int[]    position  = this.parser.position();
    final String[] parameter = {String.valueOf(position[0]), String.valueOf(position[1]), argument};
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exceptionPosition
  /**
   ** Returns a DSML feature exception including the current line number.
   **
   ** @param  argument           the substitution for placholder contained in
   **                            the message regarding to <code>code</code>.
   **
   ** @return                    DSML feature exception including the current
   **                            line number.
   */
  protected String[] exceptionPosition(final String[] argument) {
    final int[]    position  = this.parser.position();
    final String[] parameter = new String[argument.length + 2];
    parameter[0] = String.valueOf(position[0]);
    parameter[1] = String.valueOf(position[1]);
    System.arraycopy(argument, 0, parameter, 2, argument.length);
    return parameter;
  }
}
