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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   JobStatus.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    JobStatus.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Locale;

import java.io.Serializable;

import oracle.jbo.common.StringManager;
import oracle.jbo.common.ResourceBundleDef;

import oracle.iam.ui.platform.model.common.OIMProgrammaticVO;

////////////////////////////////////////////////////////////////////////////////
// class JobStatus
// ~~~~~ ~~~~~~~~~
/**
 ** Data Access Object used by System Administration customization.
 ** <br>
 ** Represents the status within the context of a particular Scheduler Job or
 ** Scheduler Job History.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class JobStatus implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final int     STARTED          = 1;
  public static final int     STOPPED          = 2;
  public static final int     RUNNING          = 5;

  private static final int    LENGTH           = 8;
  private static final String STATUS           = "oracle.iam.identity.sysadmin.model.view.JobStatusVO.SL_%d_1";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:105373429977419621")
  private static final long   serialVersionUID = -7319956179969381516L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  private final long          encoded;
  private final String        decoded;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>JobStatus</code> values object which belongs to a
   ** trigget status of a job and use the specified properties to populate its
   ** value.
   **
   ** @param  encoded              the encoded value of a trigger status to
   **                              populate.
   ** @param  decoded              the localized value of a trigger status to
   **                              populate for display purpose.
   */
  private JobStatus(final long encoded, final String decoded) {
    // ensure inheritance
    super();

    // initalize instance
    this.encoded = encoded;
    this.decoded = decoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getEncoded
  /**
   ** Returns the value of the encoded property.
   **
   ** @return                      the value of the encoded property.
   **                              Possible object is <code>long</code>.
   */
  public long getEncoded() {
    return this.encoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   getDecoded
  /**
   ** Returns the value of the decoded property.
   **
   ** @return                      the value of the decoded property.
   **                              Possible object is {@link String}.
   */
  public String getDecoded() {
    return this.decoded;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create <code>JobStatus</code> values objects which
   ** belongs to the specified view object.
   **
   **
   ** @param  viewDefinition     the {@link OIMProgrammaticVO} used to localize
   **                            static value lists.
   **                            <br>
   **                            Allowed object is {@link OIMProgrammaticVO}.
   **
   ** @return                    the array <code>JobStatus</code> populated with
   **                            the native value as <code>encoded</code> and
   **                            the localized string as <code>encoded</code>
   **                            value.
   **                            <br>
   **                            Possible object is array of {@link JobStatus}.
   */
  public static JobStatus[] build(final OIMProgrammaticVO viewDefinition) {
    final JobStatus[]       status = new JobStatus[LENGTH];
    final Locale            locale = viewDefinition.getApplicationModule().getSession().getLocale();
    final ResourceBundleDef bundle = viewDefinition.getResourceBundleDef();
    for (int i = 0; i < LENGTH; i++) {
      status[i] = new JobStatus(i, StringManager.getLocalizedStringFromResourceDef(bundle, String.format(STATUS, i), null, locale, null, false));
    }
    return status;
  }
}