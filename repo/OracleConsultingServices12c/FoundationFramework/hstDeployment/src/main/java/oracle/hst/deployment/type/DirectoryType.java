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
    Subsystem   :   Deployment Utilities

    File        :   DirectoryType.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryType.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.hst.deployment.type;

import org.apache.tools.ant.types.EnumeratedAttribute;

////////////////////////////////////////////////////////////////////////////////
// class DirectoryType
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** <code>DirectoryType</code> defines the attribute restriction on values that
 ** can be passed to a {@link DirectoryContext}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class DirectoryType extends EnumeratedAttribute {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String OUD         = "unifiedDirectory";
  private static final String OID         = "internetDirectory";
  private static final String OVD         = "virtualDirectory";
  private static final String ADS         = "activeDirectory";
  private static final String NED         = "novellDirectory";

  // the names of the allowed server types in alphabetical order
  private static final String[] registry = {
    OUD
  , OID
  , OVD
  , ADS
  , NED
  };

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class UnifiedDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>UnifiedDirectory</code> defines the attribute restriction to
   ** {@link #OUD}.
   */
  public static final class UnifiedDirectory extends DirectoryType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>UnifiedDirectory</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public UnifiedDirectory(){
      // ensure inheritance
      super(OUD);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class InternetDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~~
  /**
   ** <code>InternetDirectory</code> defines the attribute restriction to
   ** {@link #OID}.
   */
  public static final class InternetDirectory extends DirectoryType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>InternetDirectory</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public InternetDirectory(){
      // ensure inheritance
      super(OID);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class VirtualDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~~
  /**
   ** <code>VirtualDirectory</code> defines the attribute restriction to
   ** {@link #OVD}.
   */
  public static final class VirtualDirectory extends DirectoryType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>VirtualDirectory</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public VirtualDirectory(){
      // ensure inheritance
      super(OVD);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class NovellDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** <code>NovellDirectory</code> defines the attribute restriction to
   ** {@link #NED}.
   */
  public static final class NovellDirectory extends DirectoryType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>NovellDirectory</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public NovellDirectory(){
      // ensure inheritance
      super(NED);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class ActiveDirectory
  // ~~~~~ ~~~~~~~~~~~~~~~
  /**
   ** <code>ActiveDirectory</code> defines the attribute restriction to
   ** {@link #ADS}.
   */
  public static final class ActiveDirectory extends DirectoryType {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>ActiveDirectory</code> type that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public ActiveDirectory(){
      // ensure inheritance
      super(ADS);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryType</code> type that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public DirectoryType() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>DirectoryType</code> with the specified value property.
   **
   ** @param  type               the type property value to set.
   */
  protected DirectoryType(final String type){
    // ensure inheritance
    super();

    // initialize instance
    setValue(type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the type of the server where a task will connecting to.
   **
   ** @return                    the type of the server where a task will
   **                            connecting to.
   */
  public final String type() {
    return super.getValue();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValues (EnumeratedAttribute)
  /**
   ** The only method a subclass needs to implement.
   **
   ** @return                    an array holding all possible values of the
   **                            enumeration. The order of elements must be
   **                            fixed so that indexOfValue(String) always
   **                            return the same index for the same value.
   */
  @Override
  public String[] getValues() {
    return registry;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isUnifiedDirectory
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle Unified Directory.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle Unified Directory.
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean isUnifiedDirectory() {
    return OUD.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isInternetDirectory
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle Internet Directory.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle Internet Directory.
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean isInternetDirectory() {
    return OID.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isVirtualDirectory
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Oracle Virtual Directory.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Oracle Virtual Directory.
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean isVirtualDirectory() {
    return OVD.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isNovellDirectory
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Novell eDirectory.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Novell eDirectory.
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean isNovellDirectory() {
    return NED.equals(this.value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isActiveDirectory
  /**
   ** Returns <code>true</code> if the type of the configured server is
   ** Microsoft Active Directory.
   **
   ** @return                    <code>true</code> if the type of the configured
   **                            server is Microsoft Active Directory.
   **                            Allowed object is <code>boolean</code>.
   */
  public boolean isActiveDirectory() {
    return ADS.equals(this.value);
  }
}