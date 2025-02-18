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

    System      :   Oracle Access Manager
    Subsystem   :   Custom Rest Service

    File        :   OAMUtil.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    OAMUtil.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/
package bka.iam.access.oidc.extension.utils;

import java.util.Map;
import java.util.HashMap;

import java.util.logging.Logger;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;

////////////////////////////////////////////////////////////////////////////////
// class OAMConstants
// ~~~~~ ~~~~~~~~~~~~
public class OAMUtil {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Class name captured for logging purpose
   */
  private static final String   CLASS  = OAMUtil.class.getName();
  /**
   ** Logger created based on the class name
   */
  private static final Logger   LOGGER = Logger.getLogger(CLASS);
  /**
   ** Logger created based on the class name
   */
  private static final String   QUERY  = "select a.attr_name, a.attr_values from entities e, entity_attrs a where e.name_path = ? and a.entity_id = e.id";

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entityAttributes
  public static Map<String, String> entityAttributes(final String path) {
    Connection        conn = null;
    PreparedStatement ps   = null;

    final Map<String, String> collector = new HashMap<String, String>();
    try {
      conn = WLSUtil.dataSource("jdbc/oamds").getConnection();
      ps = conn.prepareStatement(QUERY);
      ps.setString(1, path);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        collector.put(rs.getString("ATTR_NAME").replace("~", ""), rs.getString("ATTR_VALUES").replace("~", ""));
      }
      rs.close();
      LOGGER.info("Entity Attributes for " + path + ":" + collector);
    }
    catch (Exception e) {
      e.printStackTrace();
      LOGGER.warning(e.getLocalizedMessage());
    }
    finally {
      if (ps != null) {
        try {
          ps.close();
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
      if (conn != null) {
        try {
          conn.close();
        }
        catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    return collector;
  }
}