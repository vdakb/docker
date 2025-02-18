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
    Subsystem   :   Openfire Database Connector

    File        :   DatabaseFeatureFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DatabaseFeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-10-06  DSteding    First release version
*/

package oracle.iam.identity.dbs.integration.openfire;

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
// final class DatabaseFeatureFactory
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of feature description for
 ** a Database.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
final class DatabaseFeatureFactory extends FeatureFactory  {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>DatabaseFeatureFactory</code>.
   **
   ** @param  feature            the {@link DatabaseFeature} that this
   **                            factory will configure.
   **
   ** @throws SAXException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private DatabaseFeatureFactory(final DatabaseFeature feature)
    throws SAXException {

    // ensure inheritance
    super(new DatabaseFeatureParser(feature));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DatabaseFeature} from a {@link File}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final File file)
    throws TaskException {

    return configure(feature, file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DatabaseFeature} from a {@link File}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final File file, final boolean validate)
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
   ** Factory method to configure a {@link DatabaseFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final InputStream stream)
    throws TaskException {

    return configure(feature, stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DatabaseFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final InputStream stream, final boolean validate)
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
   ** Factory method to configure a {@link DatabaseFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final InputSource source)
    throws TaskException {

    return configure(feature, source, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   configure
  /**
   ** Factory method to configure a {@link DatabaseFeature} from an
   ** {@link InputSource}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **
   ** @return                    the {@link DatabaseFeature} to allow method
   **                            chaining.
   **                            Possible object {@link DatabaseFeature}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static DatabaseFeature configure(final DatabaseFeature feature, final InputSource source, final boolean validate)
    throws TaskException {

    try {
      final DatabaseFeatureFactory factory = new DatabaseFeatureFactory(feature);
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
   ** Factory method to configure a {@link DatabaseFeature} from an
   ** {@link InputStream}.
   **
   ** @param  feature            the {@link DatabaseFeature} to be configured by
   **                            this {@link FeatureFactory}.
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static void configure(final DatabaseFeature feature, final PDocument document)
    throws TaskException {

    try {
      final DatabaseFeatureFactory factory = new DatabaseFeatureFactory(feature);
      factory.unmarshal(document);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}