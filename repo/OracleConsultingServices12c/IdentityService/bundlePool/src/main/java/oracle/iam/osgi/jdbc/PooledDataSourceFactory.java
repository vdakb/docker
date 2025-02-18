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
    Subsystem   :   Generic Persistence Interface

    File        :   PooledDataSourceFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    PooledDataSourceFactory

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.osgi.jdbc;

import java.util.Properties;

import java.sql.SQLException;

import javax.sql.DataSource;

////////////////////////////////////////////////////////////////////////////////
// class PooledDataSourceFactory
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** JDBC specific extension to standard OSGi/JDBC concept of
 ** {@link DataSourceFactory}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface PooledDataSourceFactory {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Method similar to {@link DataSourceFactory} factory methods.
   ** <br>
   ** It creates pooled {@link DataSource} using OSGi JDBC standard
   ** {@link DataSourceFactory}.
   **
   ** @param  factory            an existing {@link DataSourceFactory} that can
   **                            be used to create {@link DataSource},
   **                            {@link javax.sql.XADataSource} or
   **                            {@link javax.sql.ConnectionPoolDataSource}
   **                            depending on configuration properties.
   **                            <br>
   **                            Allowed object is {@link DataSourceFactory}.
   ** @param  config             the pooling and connection factory
   **                            configuration.
   **                            <br>
   **                            Allowed object is {@link Properties}.
   **
   ** @return                    an poolable {@link DataSource}.
   **                            <br>
   **                            Possible object is {@link DataSource}.
   **
   ** @throws SQLException       if the {@link DataSource} cannot be created.
   */
  DataSource create(final DataSourceFactory factory, final Properties config)
    throws SQLException;
}
