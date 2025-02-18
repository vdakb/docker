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

    Copyright © 2024. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Grafana Connector

    File        :   ServiceFeatureFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@icloud.com

    Purpose     :   This file implements the class
                    ServiceFeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2024-03-22  DSteding    First release version
*/

package oracle.iam.identity.gws.integration.grafana;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.mds.persistence.PDocument;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.connector.integration.FeatureFactory;

////////////////////////////////////////////////////////////////////////////////
// final class ServiceFeatureFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of feature description for
 ** a Service.
 **
 ** @author  dieter.steding@icloud.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
final class ServiceFeatureFactory extends FeatureFactory  {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>ServiceFeatureFactory</code>.
   **
   ** @param  feature            the {@link ServiceFeature} that this
   **                            factory will configure.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private ServiceFeatureFactory(final ServiceFeature feature)
    throws SAXException {

    // ensure inheritance
    super(new ServiceFeatureParser(feature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from a {@link File}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final File file)
    throws TaskException {

    return configure(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from a {@link File}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final File file, final boolean validate)
    throws TaskException {

    try {
      return configure(feature, new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final InputStream stream)
    throws TaskException {

    return configure(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final InputStream stream, final boolean validate)
    throws TaskException {

    configure(feature, new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final InputSource source)
    throws TaskException {

    return configure(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    the {@link ServiceFeature} to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object {@link ServiceFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static ServiceFeature configure(final ServiceFeature feature, final InputSource source, final boolean validate)
    throws TaskException {

    try {
      final ServiceFeatureFactory factory = new ServiceFeatureFactory(feature);
      factory.unmarshal(source, validate);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
    return feature;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link ServiceFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link ServiceFeature} to be configured by
   **                            this {@link FeatureFactory}.
   **                            <br>
   **                            Allowed object is {@link ServiceFeature}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **                            <br>
   **                            Allowed object is {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final ServiceFeature feature, final PDocument document)
    throws TaskException {

    try {
      final ServiceFeatureFactory factory = new ServiceFeatureFactory(feature);
      factory.unmarshal(document);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}