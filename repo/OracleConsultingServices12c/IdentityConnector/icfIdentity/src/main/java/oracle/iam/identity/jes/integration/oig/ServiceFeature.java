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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Identity Governance Connector

    File        :   ServiceFeature.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ServiceFeature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-21  DSteding    First release version
*/

package oracle.iam.identity.jes.integration.oig;

import java.util.Map;

import java.io.File;
import java.io.InputStream;

import org.xml.sax.InputSource;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.TargetFeature;

////////////////////////////////////////////////////////////////////////////////
// class ServiceFeature
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The <code>ServiceFeature</code> provides the base feature description of an
 ** Identity Governance Service Provider.
 ** <br>
 ** Implementation of OIG may vary in locations of certain informations and
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class ServiceFeature extends TargetFeature {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ServiceFeature</code> which is associated with
   ** the specified {@link Loggable} for logging purpose.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>ServiceFeature</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   */
  private ServiceFeature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   registry  (TargetFeature)
  /**
   ** Returns the mapping of attribute names defined on this
   ** <code>Metadata Descriptor</code> and the parameter names expected by
   ** connector bundle configuration.
   **
   ** @return                    the mapping of attribute names defined
   **                            on this <code>Metadata Descriptor</code> and 
   **                            the parameter names expected by connector
   **                            bundle configuration.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link String} as the value.
   */
  @Override
  public final Map<String, String> registry() {
    return Service.FEATURE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>Metadata Descriptor</code> which
   ** is associated with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to be populated manually
   ** and does not belongs to an Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            <code>Metadata Descriptor</code> configuration
   **                            wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   **
   ** @return                    an instance of <code>ServiceFeature</code> with
   **                            the value provided.
   **                            <br>
   **                            Possible object is <code>ServiceFeature</code>.
   */
  public static ServiceFeature build(final Loggable loggable) {
    return new ServiceFeature(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link File}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            this <code>Metadata Descriptor</code> wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    an instance of <code>ServiceFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is <code>ServiceFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature build(final Loggable loggable, final File file)
    throws TaskException {

    final ServiceFeature feature = new ServiceFeature(loggable);
    ServiceFeatureFactory.configure(feature, file);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputStream}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            this <code>Metadata Descriptor</code> wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    an instance of <code>ServiceFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is <code>ServiceFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature build(final Loggable loggable, final InputStream stream)
    throws TaskException {

    final ServiceFeature feature = new ServiceFeature(loggable);
    ServiceFeatureFactory.configure(feature, stream);
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to configure <code>Metadata Descriptor</code> from a
   ** {@link InputSource}.
   **
   ** @param  loggable           the {@link Loggable} that instantiate this
   **                            this <code>Metadata Descriptor</code> wrapper.
   **                            <br>
   **                            Allowed object is {@link Loggable}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    an instance of <code>ServiceFeature</code>
   **                            with the value provided.
   **                            <br>
   **                            Possible object is <code>ServiceFeature</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature build(final Loggable loggable, final InputSource source)
    throws TaskException {

    final ServiceFeature feature = new ServiceFeature(loggable);
    ServiceFeatureFactory.configure(feature, source);
    return feature;
  }
}