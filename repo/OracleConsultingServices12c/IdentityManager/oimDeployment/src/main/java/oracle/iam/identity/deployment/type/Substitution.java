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
    Subsystem   :   Deployment Utilities 12c

    File        :   Substitution.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Substitution.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.identity.deployment.type;

////////////////////////////////////////////////////////////////////////////////
// class Substitution
// ~~~~~ ~~~~~~~~~~~~
/**
 ** <code>Substitution</code> defines the value holder of subtitutions that can
 ** be applied on a {@link ImportSet}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Substitution extends Category {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private String replacement = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Substitution</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Substitution() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>Substitution</code> to initialize the instance.
   **
   ** @param  physicalType       the name of the category aka the physical type
   **                            of the Identity Manager.
   ** @param  origin             the subject of substitution.
   ** @param  replacement        the substitution value.
   */
  public Substitution(final String physicalType, final String origin, final String replacement) {
    // ensure inheritance
    super(physicalType, origin);

    this.replacement = replacement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setReplace
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>replacement</code>.
   **
   ** @param  replacement        the substitution value.
   */
  public void setReplacement(final String replacement) {
    this.replacement = replacement;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   replacement
  /**
   ** Returns the substitution value in the created deployment.
   **
   ** @return                    the substitution value in the created
   **                            deployment.
   */
  public final String replacement() {
    return this.replacement;
  }
}