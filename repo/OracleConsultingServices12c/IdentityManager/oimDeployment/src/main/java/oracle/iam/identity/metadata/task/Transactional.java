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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Library
    Subsystem   :   Metadata Service Utilities 11g

    File        :   Transactional.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Transactional.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.metadata.task;

import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.tools.ant.BuildException;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.iam.identity.metadata.type.Path;

////////////////////////////////////////////////////////////////////////////////
// abstract class Transactional
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~
/**
 ** Invokes the Runtime JMX Bean to maintain documents of Identity Manager
 ** metadata repository.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Transactional extends Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** A list of fully qualified metadata documents to export.
   ** <p>
   ** This can be specified by a comma-separated list of fully qualified
   ** metadata documents or a document name pattern that is used to identify
   ** which documents to export, import or delete.
   */
  private List<String> documentPath            = new ArrayList<String>();

  /**
   ** A comma-separated list of names of the specified customization layers from
   ** which only customization documents will be exported, imported or deleted.
   ** This parameter is ignored if "excludeAllCust" is specified.
   */
  private List<String> restrictedPath          = new ArrayList<String>();

  /**
   ** A Boolean value indicating whether to exclude all customization metadata
   ** documents from being exported. If specified, this parameter causes
   ** "restrictCustTo" to be ignored.
   */
  private boolean      excludeAllCustomization = false;

  /**
   ** A Boolean value indicating whether to exclude base metadata documents from
   ** being exported.
   */
  private boolean      excludeBaseDocuments    = false;

  /**
   ** A Boolean value indicating whether to exclude extended metadata documents
   ** from being exported, imported or deleted.
   */
  private boolean      excludeExtendedMetadata = false;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Transactional</code> event handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Transactional() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Transactional</code> event handler that use the specified
   ** {@link Metadata} task providing the JMX bean properties.
   **
   ** @param  other              {@link Metadata} task providing the JMX bean
   **                            properties.
   */
  protected Transactional(final Metadata other) {
    // ensure inheritance
    super(other);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDocs
  /**
   ** Call by the ANT kernel to inject the value for argument
   ** <code>documentPath</code>.
   **
   ** @param  documentPath       a list of fully qualified metadata documents to
   **                            export, import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   */
  public void setDocs(final String documentPath) {
    documentPath(translatePath(documentPath));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addConfiguredPath
  /**
   ** Call by the ANT kernel to inject values for argument
   ** <code>documentPath</code>.
   **
   ** @param  documentPath       a fully qualified metadata document to export,
   **                            import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   */
  public void addConfiguredPath(final Path documentPath) {
    documentPath(documentPath.list());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentPath
  /**
   ** Call internally to inject the value for argument
   ** <code>documentPath</code>.
   **
   ** @param  documentPath       a list of fully qualified metadata documents to
   **                            export, import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   */
  protected void documentPath(final String... documentPath) {
    for (int i = 0; i < documentPath.length; i++)
      this.documentPath.add(documentPath[i]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   documentPath
  /**
   ** Returns the array of metadata documents to export, import or delete.
   **
   ** @return                    an array of fully qualified metadata documents
   **                            to export, import or delete.
   */
  protected final String[] documentPath() {
    return this.documentPath.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRestrictCustTo
  /**
   ** Call by the ANT kernel to inject the value for argument
   ** <code>restrictCustTo</code>.
   **
   ** @param  restrictedPath        a comma-separated list of names of the
   **                               specified customization layers from which
   **                               only customization documents will be
   **                               exported or deleted from or imported to.
   **                               The parameter set by this method is ignored
   **                               if "excludeAllCust" is also specified.

   */
  public void setRestrictCustTo(final String restrictedPath) {
    restrictedPath(translatePath(restrictedPath));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   restrictedPath
  /**
   ** Call internally to inject the value for argument
   ** <code>restrictedPath</code>.
   **
   ** @param  restrictedPath     a list of fully qualified metadata documents to
   **                            restrict in export, import or delete.
   **                            <p>
   **                            This can be specified by a comma-separated list
   **                            of fully qualified metadata documents or a
   **                            document name pattern that is used to identify
   **                            which documents to handled.
   */
  protected void restrictedPath(final String[] restrictedPath) {
    for (int i = 0; i < restrictedPath.length; i++)
      this.restrictedPath.add(restrictedPath[i]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   restrictedPath
  /**
   ** Returns the array of metadata documents to restrict to in export, import
   ** or delete.
   **
   ** @return                    an array of fully qualified metadata documents
   **                            to restrict in export, import or delete.
   */
  protected final String[] restrictedPath() {
    return this.restrictedPath.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExcludeAllCust
  /**
   ** Call by the ANT kernel to inject the value for argument
   ** <code>excludeAllCustomization</code>.
   **
   ** @param  excludeAllCustomization a Boolean value indicating whether to
   **                                 exclude all customization metadata
   **                                 documents from the operation.
   **                                 If specified, this parameter causes
   **                                 "restrictCustTo" to be ignored.
   */
  public void setExcludeAllCust(final boolean excludeAllCustomization) {
    this.excludeAllCustomization = excludeAllCustomization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeAllCustomization
  /**
   ** Returns the value indicating whether to exclude all customization metadata
   ** documents from the operation.
   **
   ** @return                    the value indicating whether to exclude all
   **                            customization metadata documents from the
   **                            operation.
   */
  protected final boolean excludeAllCustomization() {
    return this.excludeAllCustomization;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExcludeBaseDocs
  /**
   ** Call by the ANT kernel to inject the value for argument
   ** <code>excludeBaseDocuments</code>.
   **
   ** @param  excludeBaseDocuments a Boolean value indicating whether to exclude
   **                              base metadata documents from operation.
   */
  public void setExcludeBaseDocs(final boolean excludeBaseDocuments) {
    this.excludeBaseDocuments = excludeBaseDocuments;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeBaseDocuments
  /**
   ** Returns the value indicating whether to exclude base metadata documents
   ** from operation.
   **
   ** @return                    the value indicating whether to exclude base
   **                            metadata documents from operation.
   */
  protected final boolean excludeBaseDocuments() {
    return this.excludeBaseDocuments;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setExcludeExtendedMetadata
  /**
   ** Call by the ANT kernel to inject the value for argument
   ** <code>excludeExtendedMetadata</code>.
   **
   ** @param  excludeExtendedMetadata a Boolean value indicating whether to
   **                                 exclude extended metadata documents from
   **                                 operation.
   */
  public void setExcludeExtendedMetadata(final boolean excludeExtendedMetadata) {
    this.excludeExtendedMetadata = excludeExtendedMetadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   excludeExtendedMetadata
  /**
   ** Returns the value indicating whether to exclude extended metadata
   ** documents from operation.
   **
   ** @return                    the value indicating whether to exclude
   **                            extended metadata documents from operation.
   */
  protected final boolean excludeExtendedMetadata() {
    return this.excludeExtendedMetadata;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   translatePath
  /**
   ** Splits a Document Path with <code>,</code> into its parts.
   **
   ** @param  source             a <code>String</code> value.
   **
   ** @return                    an array of strings, one for each path element
   */
  public static String[] translatePath(final String source) {
    if (source == null || source.length() == 0)
      return new String[0];

    final List<String>    result = new ArrayList<String>();
    final StringTokenizer tokenizer = new StringTokenizer(source);
    while (tokenizer.hasMoreTokens())
      result.add(tokenizer.nextToken());

    return result.toArray(new String[0]);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate (overridden)
  /**
   ** The entry point to validate the task to perform.
   **
   ** @throws BuildException   in case a validation error does occur.
   */
  @Override
  protected void validate()
    throws BuildException {

    if (this.documentPath.size() == 0) {
      final String message = ServiceResourceBundle.format(ServiceError.TASK_ATTRIBUTE_MISSING, "documentPath");
      throw new BuildException(message);
    }

    // ensure inheritance
    super.validate();
  }
}