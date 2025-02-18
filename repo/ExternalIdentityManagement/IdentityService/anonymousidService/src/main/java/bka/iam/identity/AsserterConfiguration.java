/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Anonymous Identifier Service

    File        :   AsserterConfiguration.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AsserterConfiguration.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity;

import javax.inject.Singleton;

import bka.iam.identity.jmx.BearerAsserterConfiguration;

////////////////////////////////////////////////////////////////////////////////
// class AsserterConfiguration
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~
/**
 ** The configuration provider as a JMX management implementation.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Singleton
@SuppressWarnings({"oracle.jdeveloper.cdi.not-proxyable-bean", "oracle.jdeveloper.cdi.uncofig-project"})
public class AsserterConfiguration extends BearerAsserterConfiguration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4522323665128329570")
  private static final long serialVersionUID = -5557804332400158669L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AsserterConfiguration</code> that use the
   ** <code>pidresource</code> later for realm that will be sent via the
   ** <code>WWW-Authenticate</code> header.
   ** <br>
   ** <b>Note</b>
   ** <br>
   ** This <code>pidresource</code> <b>does not</b> couple a named identity
   ** store configuration to the authentication mechanism.
   ** <p>
   ** The <code>pidresource</code> its also used to maintain the persisted
   ** configuration.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AsserterConfiguration() {
    // ensure inheritance
    super("pidresource");
  }
}