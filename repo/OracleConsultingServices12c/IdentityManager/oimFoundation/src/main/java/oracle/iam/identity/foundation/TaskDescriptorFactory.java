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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   TaskDescriptorFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TaskDescriptorFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-03-11  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// abstract class TaskDescriptorFactory
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of for an attribute mapping.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class TaskDescriptorFactory extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace declaration of a mapping descriptor */
  public static final String NAMESPACE           = "http://www.oracle.com/schema/oim/mapping";

  /** the XML element that is the root element of the structure */
  public static final String ELEMENT_DESCRIPTOR  = "descriptor";

  /**
   ** the XML element that identifies a source attribute in Oracle Identity
   ** Manager
   */
  public static final String ELEMENT_PROFILE     = "profile";

  /**
   ** the XML element that identifies a source or target attribute in Oracle
   ** Identity Manager
   */
  public static final String ELEMENT_ATTRIBUTE   = "attribute";

  /**
   ** the XML element that identifies a source or target constant in Oracle
   ** Identity Manager
   */
  public static final String ELEMENT_CONSTANT    = "constant";

  /**
   ** the XML element that identifies a transformation to be applied on an
   ** attribute in Oracle Identity Manager
   */
  public static final String ELEMENT_TRANSFORMER = "transformer";

  /** the XML namespace declaration */
  public static final String ATTRIBUTE_NAMESPACE = "xmlns";

  /** the XML attribute specifies the name of an source or target attribute */
  public static final String ATTRIBUTE_NAME      = "name";

  /**
   ** the XML attribute specifies if target attribute name of a logical profile
   ** definition
   */
  public static final String ATTRIBUTE_ATTRIBUTE = "attribute";

  /** the XML attribute specifies if the class name of a transformer */
  public static final String ATTRIBUTE_CLASS     = "class";

  /** the XML attribute specifies the name of a source provider */
  public static final String ATTRIBUTE_SOURCE    = "source";

  /** the XML attribute specifies the name of a constant provider */
  public static final String ATTRIBUTE_VALUE     = "value";

  /** the XML attribute specifies the type of a reference element */
  public static final String ATTRIBUTE_TYPE      = "type";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processDescriptor
  /**
   ** Initialize the instance attributes of the specified {@link TaskDescriptor}
   ** instance from the provided {@link Attributes}.
   **
   ** @param  descriptor         the {@link TaskDescriptor} the instance
   **                            attributes has to be set for.
   ** @param  attributes         the {@link Attributes} as the source.
   **
   ** @throws SAXException       any SAX exception, possibly wrapping another
   **                            exception.
   */
  protected void processDescriptor(final TaskDescriptor descriptor, final Attributes attributes)
    throws SAXException {

    // check if the correct namespace is defined
    String value = attributes.getValue(ATTRIBUTE_NAMESPACE);
    if (!NAMESPACE.equals(value)) {
      final String[] arguments = { "-1", "-1", ATTRIBUTE_NAMESPACE, value, ELEMENT_DESCRIPTOR };
      throw new SAXException(XMLStreamBundle.format(XMLError.NAMESPACE_INVALID, arguments));
    }

    // get the name of the entity identifier
    descriptor.identifier(attributes.getValue(TaskDescriptor.IDENTIFIER));
    // get the name of the hierarchy identifier
    descriptor.hierarchy(attributes.getValue(TaskDescriptor.HIERARCHY));
    // check if a transformation is requested
    value = attributes.getValue(TaskDescriptor.TRANSFORMATION_ENABLED);
    if (!StringUtility.isEmpty(value))
      descriptor.transformationEnabled(StringUtility.stringToBool(value));
    // check if a special handling of the source or target names is requested
    value = attributes.getValue(TaskDescriptor.TARGETNAME_NATIVELY);
    if (!StringUtility.isEmpty(value))
      descriptor.natively(StringUtility.stringToBool(value));
  }
}