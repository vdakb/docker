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

    System      :   Identity Manager Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Sandbox.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Sandbox.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2012-08-31  DSteding    First release version
*/

package oracle.iam.identity.common.adf.marshal;

import java.io.File;

import java.text.SimpleDateFormat;

import oracle.hst.foundation.logging.Loggable;
import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLProcessor;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.iam.identity.common.spi.SandboxMetadata;

////////////////////////////////////////////////////////////////////////////////
// abstract class Sandbox
// ~~~~~~~~ ~~~~~ ~~~~~~~
/**
 ** The marshaller to spool out the sandbox metadata descriptor.
 ** <p>
 ** The class is kept abstract due to it expose only static methods.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** The tag names of the created XML file are not declared by constants. This
 ** violations is an exception regarding the coding guidelines but is acceptable
 ** due to those tags are only used inside of this class and occurs mostly only
 ** once.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Sandbox {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String            PATH       = "mdssys/sandbox";

  private static final String           ROOT       = "sandbox_metadata";
  private static final String           NAMESPACE  = "http://xmlns.oracle.com/mds/sandbox";
  private static final String           FILENAME   = "active_mdsSandboxMetadata.xml";
  private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("HH:mm:ss");

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   marshal
  /**
   ** Marshals the sandbox metadata descriptor.
   **
   ** @param  loggable           the instance providing the logging
   **                            capabilities; to disable, set to
   **                            <code>null</code>.
   ** @param  path               the {@link File} pathname to create or override
   **                            the file within.
   ** @param  sandbox            the {@link SandboxMetadata} as the data
   **                            provider.
   **
   ** @throws XMLException       if the an error occurs.
   */
  public static void marshal(final Loggable loggable, final File path, final SandboxMetadata sandbox)
    throws XMLException {

    final File             file = new File(path, FILENAME);
    final XMLOutputNode    root = Metadata.marshalSandbox(loggable, file, ROOT, NAMESPACE, sandbox.version().sandbox);

    XMLOutputNode meta = root.element("user_metadata");
    meta.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    meta.element("comments").value(sandbox.comment());
    meta.element("description").value(sandbox.description());
    meta.element("createdBy").value(sandbox.user());
    meta.element("createdOn").value(String.valueOf(sandbox.created().getTime()));
    meta.element("lastModifiedBy").value("mdsInternal");
    meta.element("lastModifiedOn").value(String.valueOf(sandbox.updated().getTime()));
    meta.element("mainlineLabel").value(String.format("Creation_IdM_%s_%s", sandbox.name(), DATEFORMAT.format(sandbox.created())));
    meta.element("mergedBy");
    meta.element("mergedOn");
    meta.element("postMergeLabel");
    meta.element("preMergeLabel");
    meta.element("preUndoLabel");
    meta.element("postUndoLabel");
    meta.element("undoneBy");
    meta.element("finalizedBy");
    meta.element("name").value(String.format("IdM_%s", sandbox.name()));
    //    root.element("tenantId").value("0");
    meta = root.element("custom_metadata");
    meta.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    meta = root.element("system_metadata");
    meta.attribute(XMLProcessor.ATTRIBUTE_XMLNS, NAMESPACE);
    meta.element("listOfDocsInSandbox");

    root.close();
  }
}