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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Presistence Foundation Shared Library
    Subsystem   :   JPA Unit Testing

    File        :   DataSourceProxy.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    DataSourceProxy.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2014-02-23  DSteding    First release version
*/

package oracle.hst.platform.jpa.bootstrap;

import javax.sql.DataSource;

import net.ttddyy.dsproxy.support.ProxyDataSource;

////////////////////////////////////////////////////////////////////////////////
// enum DataSourceProxy
// ~~~~ ~~~~~~~~~~~~~~~
/**
 ** Wrapper around the {@link DataSource} configuration.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public enum DataSourceProxy {
    NONE {
      @Override
      public DataSource dataSource(final DataSource dataSource) {
        return dataSource;
      }
    }
  , DSSPY {
      @Override
      public DataSource dataSource(final DataSource dataSource) {
        return new ProxyDataSource(dataSource);
      }
    }
  , P6SPY {
      @Override
      public DataSource dataSource(final DataSource dataSource) {
        return dataSource;
      }
    }
  ;

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dataSource
  /**
   ** Factory method to create a proxied {@link DataSource} for a specific
   ** flavor.
   **
   ** @return                    the proxied {@link DataSource} created for
   **                            a specific flavor.
   **                            <br>
   **                            Possible object is {@link DataSource}.
   */
  public abstract DataSource dataSource(DataSource dataSource);
}