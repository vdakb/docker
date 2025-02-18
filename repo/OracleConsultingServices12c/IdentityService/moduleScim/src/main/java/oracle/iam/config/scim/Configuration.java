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

    System      :   Oracle Identity Service Extension
    Subsystem   :   Generic SCIM Service

    File        :   Configuration.java

    Compiler    :   Java Development Kit 8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Configuration.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-07-10  DSteding    First release version
*/

package oracle.iam.config.scim;

public interface Configuration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the classes for the JMX client are part of a Web application, the JNDI name
  // for the Runtime MBeanServer is: java:comp/env/jmx/runtime
  // see https://docs.oracle.com/cd/E12839_01/web.1111/e13729/instmbeans.htm#JMXPG154
  static final String RUNTIME = "java:comp/env/jmx/runtime"; //"java:comp/jmx/runtime"

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   reset
  /**
   ** Reset the entire <code>Configuration</code> to be empty.
   */
  void reset();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   load
  /**
   ** Load the configuration from the file system.
   **
   ** @return                    the <code>Configuration</code>.
   **                            <br>
   **                            Possible object is <code>Configuration</code>.
   */
  Configuration load();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   store
  /**
   ** Save the configuration to the file system.
   */
  void store();
}
