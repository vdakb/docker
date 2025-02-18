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
    Subsystem   :   System Authorization Management

    File        :   IdentityAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentityAdapter.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysauthz.schema;

////////////////////////////////////////////////////////////////////////////////
// class IdentityAdapter
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Data Access Object used by <code>Catalog</code> customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 ** When the instance of this class is passed to the methods in the
 ** corresponding <code>Application Module</code> class, the
 ** <code>Application Module</code> can call the getter methods to retrieve the
 ** values from the AdapterBean and pass them to the OIM APIs.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public class IdentityAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Long    id;
  private String  name;
  private String  displayName;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>IdentityAdapter</code> value object that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentityAdapter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   id
  /**
   ** Sets the value of the id property.
   **
   ** @param  value              the value of the id property.
   **                            Allowed object is {@link Long}.
   */
  public void id(final Long value) {
    this.id = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   id
  /**
   ** Returns the value of the id property.
   **
   ** @return                    the value of the id property.
   **                            Possible object is {@link Long}.
   */
  public Long id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   name
  /**
   ** Sets the value of the name property.
   **
   ** @param  value              the value of the name property.
   **                            Allowed object is {@link String}.
   */
  public void name (final String value) {
    this.name  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   name
  /**
   ** Returns the value of the name property.
   **
   ** @return                    the value of the name property.
   **                            Possible object is {@link String}.
   */
  public String name () {
    return this.name ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   displayName
  /**
   ** Sets the value of the displayName property.
   **
   ** @param  value              the value of the displayName property.
   **                            Allowed object is {@link String}.
   */
  public void displayName (final String value) {
    this.displayName  = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   displayName
  /**
   ** Returns the value of the displayName property.
   **
   ** @return                    the value of the displayName property.
   **                            Possible object is {@link String}.
   */
  public String displayName () {
    return this.displayName ;
  }
}