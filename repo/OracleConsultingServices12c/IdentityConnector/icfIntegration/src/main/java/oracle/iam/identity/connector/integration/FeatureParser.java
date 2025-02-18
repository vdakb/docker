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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Connector Bundle Integration

    File        :   FeatureParser.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureParser.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.util.Set;
import java.util.HashSet;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import oracle.hst.foundation.xml.XMLError;
import oracle.hst.foundation.xml.SAXInput;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.resource.XMLStreamBundle;

import oracle.iam.identity.foundation.TaskDescriptor;

////////////////////////////////////////////////////////////////////////////////
// abstract class FeatureParser
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This is the class to unmarshals meta information of feature description for
 ** a <code>IT Resource</code> feature instance.
 ** <p>
 ** Handles parsing of a XML file which defines the mapping descriptor.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class FeatureParser extends SAXInput {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the namespace declaration of a feature descriptor */
  protected static final String    NAMESPACE           = "http://www.oracle.com/schema/iam/endpoint";

  /** the XML element that is the root element of the structure */
  protected static final String    ELEMENT_FEATURE     = "feature";

  /** the XML namespace declaration */
  protected static final String    ATTRIBUTE_NAMESPACE = "xmlns";

  private static final Set<String> exclude = new HashSet<String>();

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final Set<String>     registry;
  protected final TargetFeature   feature;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    exclude.add(TargetFeature.BUNDLE_NAME);
    exclude.add(TargetFeature.BUNDLE_ENTRY);
    exclude.add(TargetFeature.BUNDLE_VERSION);

    exclude.add(TargetFeature.POOL_MIN_IDLE);
    exclude.add(TargetFeature.POOL_MIN_WAIT);
    exclude.add(TargetFeature.POOL_MAX_SIZE);
    exclude.add(TargetFeature.POOL_MAX_WAIT);
    exclude.add(TargetFeature.POOL_MAX_IDLE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  ////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructor for <code>FeatureParser</code>.
   **
   ** @param  feature            the {@link TaskDescriptor} to be configured by
   **                            this {@link SAXInput}.
   ** @param  registry           the {@link Set} of attribute names to be
   **                            obtained from the feature element by this
   **                            {@link SAXInput}.
   **
   ** @throws SAXException       in case {@link SAXInput} is not able to create
   **                            an appropriate parser.
   */
  protected FeatureParser(final TargetFeature feature, final Set<String> registry)
    throws SAXException {

    // ensure inheritance
    super();

    // initialize instance attributes
    this.feature  = feature;
    this.registry = CollectionUtility.set(registry);
    // remove all attributes from the given attribute collection that are
    // managed explicitly to avoid that they are populated as normal attributes
    this.registry.removeAll(exclude);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processFeature
  /**
   ** Factory method to fetch the <code>Feature</code> attributes from a
   ** XML stream.
   **
   ** @param  attributes         the {@link Attributes} providing access to
   **                            the connector bundle configuration.
   **
   ** @throws SAXException       if the required properties name has no value.
   */
  protected void processFeature(final Attributes attributes)
    throws SAXException {

    this.feature.token = TargetFeature.token(
      attributes.getValue(TargetFeature.BUNDLE_NAME)
    , attributes.getValue(TargetFeature.BUNDLE_ENTRY)
    , attributes.getValue(TargetFeature.BUNDLE_VERSION)
    );
    this.feature.pool.put(TargetFeature.POOL_MIN_IDLE, integerValue(attributes.getValue(TargetFeature.POOL_MIN_IDLE), TargetFeature.POOL_MIN_IDLE_DEFAULT));
    this.feature.pool.put(TargetFeature.POOL_MIN_WAIT, longValue(attributes.getValue(TargetFeature.POOL_MIN_WAIT), TargetFeature.POOL_MIN_WAIT_DEFAULT));
    this.feature.pool.put(TargetFeature.POOL_MAX_SIZE, integerValue(attributes.getValue(TargetFeature.POOL_MAX_SIZE), TargetFeature.POOL_MAX_SIZE_DEFAULT));
    this.feature.pool.put(TargetFeature.POOL_MAX_WAIT, longValue(attributes.getValue(TargetFeature.POOL_MAX_WAIT), TargetFeature.POOL_MAX_WAIT_DEFAULT));
    this.feature.pool.put(TargetFeature.POOL_MAX_IDLE, integerValue(attributes.getValue(TargetFeature.POOL_MAX_IDLE), TargetFeature.POOL_MAX_IDLE_DEFAULT));
    
    if (!CollectionUtility.empty(this.registry)) {
      for (int i = 0; i < attributes.getLength(); i++) {
        // obtain the local name of the attribute from the underlying XML stream
        final String external = attributes.getLocalName(i);
        // an empty local name occurs only if we have a namespace declaration in
        // a namespace aware parser
        if (StringUtility.isEmpty(external))
          continue;

        // verify the declared namespace
        if (ATTRIBUTE_NAMESPACE.equals(external)) {
          final String value = attributes.getValue(i);
          if (!NAMESPACE.equals(value)) {
            final int[]    position  = position();
            final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), external, value, ELEMENT_FEATURE };
            throw new SAXException(XMLStreamBundle.format(XMLError.NAMESPACE_INVALID, arguments));
          }
          // skip any namespace declaration so far
          continue;
        }

        // validate both collections if the parsed attribute violates the schema
       if (!this.registry.contains(external) && !exclude.contains(external)) {
          final int[]    position  = position();
          final String[] arguments = { String.valueOf(position[0]), String.valueOf(position[1]), external, ELEMENT_FEATURE };
          throw new SAXException(XMLStreamBundle.format(XMLError.INPUT_ATTRIBUTE_UNKNOWN, arguments));
        }
        // put only those properties in the collection that are required
        if (!exclude.contains(external))
          this.feature.put(external, attributes.getValue(i));
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integerValue
  /**
   ** Returns a <code>int</code> string representation from the attribute
   ** mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the <code>int</code> string representation for
   **                            the given key.
   */
  private static String integerValue(final String value, final int defaultValue) {
    return StringUtility.isEmpty(value) ? String.valueOf(defaultValue) : value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   longValue
  /**
   ** Returns a <code>long</code> string representation from the attribute
   ** mapping of this wrapper.
   **
   ** @param  key                the key for the desired attribute string.
   ** @param  defaultValue       the value that should be returned if no value
   **                            was found for the specified key.
   **
   ** @return                    the <code>long</code> string representation for
   **                            the given key.
   */
  private static String longValue(final String value, final long defaultValue) {
    return StringUtility.isEmpty(value) ? String.valueOf(defaultValue) : value;
  }
}