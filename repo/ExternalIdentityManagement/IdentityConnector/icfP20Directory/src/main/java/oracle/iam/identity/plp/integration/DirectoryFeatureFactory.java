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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryFeatureFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryFeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/
package oracle.iam.identity.plp.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import oracle.iam.identity.connector.integration.FeatureFactory;
import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;

import oracle.mds.persistence.PDocument;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
////////////////////////////////////////////////////////////////////////////////
// final class DirectoryFeatureFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of for a Directory Service.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class DirectoryFeatureFactory extends FeatureFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DirectoryFeatureFactory</code>.
   **
   ** @param  feature            the {@link DirectoryFeature} that this factory
   **                            will configure.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private DirectoryFeatureFactory(final DirectoryFeature feature)
    throws SAXException {

    // ensure inheritance
    super(new DirectoryFeatureParser(feature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final File file)
    throws TaskException {

    configure(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from a
   ** {@link File}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DirectoryFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DirectoryFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature configure(final DirectoryFeature feature, final File file, final boolean validate)
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
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link DirectoryFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DirectoryFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature configure(final DirectoryFeature feature, final InputStream stream)
    throws TaskException {

    return configure(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to create a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DirectoryFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DirectoryFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature configure(final DirectoryFeature feature, final InputStream stream, final boolean validate)
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
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link DirectoryFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DirectoryFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature configure(final DirectoryFeature feature, final InputSource source)
    throws TaskException {

    return configure(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DirectoryFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DirectoryFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DirectoryFeature configure(final DirectoryFeature feature, final InputSource source, final boolean validate)
    throws TaskException {

    try {
      final DirectoryFeatureFactory factory = new DirectoryFeatureFactory(feature);
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
   ** Factory method to configure a {@link DirectoryFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DirectoryFeature} to be configured
   **                            by this {@link FeatureFactory}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DirectoryFeature feature, final PDocument document)
    throws TaskException {

    try {
      final DirectoryFeatureFactory factory = new DirectoryFeatureFactory(feature);
      factory.unmarshal(document);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}