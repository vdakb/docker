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

    System      :   Oracle Consulting Services Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   ServicePlatformSupport.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    ServicePlatformSupport.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment;

import oracle.dms.util.DMSUtil;
import oracle.dms.util.Platform;
import oracle.dms.util.DMSProperties;
import oracle.dms.util.PlatformSupport;

import oracle.hst.foundation.utility.StringUtility;

////////////////////////////////////////////////////////////////////////////////
// class ServicePlatformSupport
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~
/**
 ** This is the platform simulation to create a proper DMS Execution Context.
 ** <p>
 ** <b>Note</b>:
 ** Class needs to be declared <code>public</code> to allow ANT introspection.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class ServicePlatformSupport implements PlatformSupport {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String PROPERTY_NAME   = "oracle.server.name";
  public static final String PROPERTY_CONFIG = "oracle.dms.config.file";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final String serverName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServicePlatformSupport</code> platform support that
   ** allows use as a JavaBean.
   */
  protected ServicePlatformSupport() {
    // ensure inheritance
    super();
    
    // initialize instance
    String serverName = DMSProperties.getProperty(PROPERTY_NAME);
    if (StringUtility.isEmpty(serverName)) {
      serverName = "proc" + DMSUtil.getProcessID();
    }
    this.serverName = serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final Platform getPlatform() {
    return Platform.Default;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getServerName (PlatformSupport)
  @Override
  public final String getServerName() {
    return this.serverName;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getDomainConfigDirectory() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getServerConfigDirectory(String paramString1, String paramString2) {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getServerConfigDirectory() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getConfigFilename() {
    return DMSProperties.getProperty("oracle.dms.config.file");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getServerLogPath() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getClusterName() {
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final boolean isAdminServer() {
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPlatform (PlatformSupport)
  @Override
  public final String getApplicationName() {
    return null;
  }
}