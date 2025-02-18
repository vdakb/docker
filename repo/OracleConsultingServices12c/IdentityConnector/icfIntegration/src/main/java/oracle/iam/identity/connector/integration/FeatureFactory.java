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

    File        :   FeatureFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    FeatureFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.integration;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;
import oracle.mds.persistence.MDSIOException;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractMetadataTask;

////////////////////////////////////////////////////////////////////////////////
// class FeatureFactory
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** This is the class to create meta information of feature description for
 ** a <code>IT Resource</code> instance.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class FeatureFactory extends XMLProcessor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final FeatureParser parser;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>FeatureFactory</code>.
   **
   ** @param  parser             the {@link FeatureParser} that the factory use
   **                            to populate the {@link TargetFeature} passed in.
   **                            <br>
   **                            Allowed object is {@link FeatureParser}.
   */
  protected FeatureFactory(final FeatureParser parser) {
    // ensure inheritance
    super();

    // initialize instance
    this.parser  = parser;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from a {@link File}.
   **
   ** @param  file               the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link File}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final File file)
    throws TaskException {

    unmarshal(file, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from a {@link File}.
   **
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
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final File file, final boolean validate)
    throws TaskException {

    try {
      unmarshal(new FileInputStream(file), validate);
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from an
   ** {@link InputStream}.
   **
   ** @param  stream             the mapping configuration file for the
   **                            descriptor to create as a XML stream.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final InputStream stream)
    throws TaskException {

    unmarshal(stream, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from an
   ** {@link InputStream}.
   **
   ** @param  stream             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputStream}.
   ** @param  validate           <code>true</code> if the provided
   **                            <code>source</code> has to be validated against
   **                            the XML Schema.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final InputStream stream, final boolean validate)
    throws TaskException {

    // prevent bogus input
    if (stream == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "stream");

    unmarshal(new InputSource(stream), validate);
    try {
      stream.close();
    }
    catch (IOException e) {
      throw new TaskException(TaskError.ABORT, e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from an
   ** {@link InputSource}.
   **
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
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final InputSource source, final boolean validate)
    throws TaskException {

    // validate the provided stream against the schema
    if (validate) {
      try {
        validate(TargetFeature.class, source);
      }
      catch (XMLException e) {
        throw new TaskException(e);
      }
    }
    unmarshal(source);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link TargetFeature} from a path.
   **
   ** @param  task               the {@link AbstractMetadataTask} where the
   **                            object to create will belong to.
   **                            <br>
   **                            Allowed object is {@link AbstractMetadataTask}.
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final AbstractMetadataTask task, final String path)
    throws TaskException {

    try {
      final MDSSession session  = task.createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));
      unmarshal(document);
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from an
   ** {@link InputStream}.
   **
   ** @param  document           the configuration for the descriptor to create
   **                            by parsing the specified {@link PDocument}.
   **                            <br>
   **                            Allowed object is {@link PDocument}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final PDocument document)
    throws TaskException {

    // prevent bogus input
    if (document == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "document");

    try {
      unmarshal(document.read());
    }
    catch (MDSIOException e) {
      throw new TaskException(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to configure a {@link TargetFeature} from an
   ** {@link InputSource}.
   **
   ** @param  source             the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link InputSource}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  private void unmarshal(final InputSource source)
    throws TaskException {

    // prevent bogus input
    if (source == null)
      throw new TaskException(TaskError.ARGUMENT_IS_NULL, "source");

    try {
      this.parser.processDocument(source);
    }
    catch (SAXException e) {
      throw new TaskException(e);
    }
  }
}