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

    Copyright © 2023. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Account Provisioning Service Model

    File        :   Risk.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the enum
                    Risk.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-06-30  DSteding    First release version
*/

package oracle.iam.identity.igs.model;


////////////////////////////////////////////////////////////////////////////////
// enum Risk
// ~~~~ ~~~~
/**
 ** The <code>Risk</code> translate the semantic representation of an item risk
 ** level to the physical representation in the persistence layer.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;simpleType name="risk"&gt;
 **   &lt;restriction base="{http://www.oracle.com/schema/igs}token"&gt;
 **   &lt;enumeration value="none"/&gt;
 **   &lt;enumeration value="low"/&gt;
 **   &lt;enumeration value="medium"/&gt;
 **   &lt;enumeration value="high"/&gt;
 **   &lt;/restriction&gt;
 ** &lt;/simpleType&gt;
 ** </pre>
 */
public enum Risk {
    /** the encoded risk values that can by applied on entitlements. */
    none(0)
  , low(3)
  , medium(5)
  , high(7)
  ;

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8085084305670959675")
  private static final long serialVersionUID = -1L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the physical value stored in the persistence layer for this risk level
   */
  private final Integer     level;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Risk</code> with the specified physical risk
   ** level.
   **
   ** @param  level            the physical level of an item risk
   **                          <br>
   **                          Allowed object is <code>int</code>.
   */
  Risk(final int level) {
    this.level = new Integer(level);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   level
  /**
   ** Returns the physical risk level.
   **
   ** @return                    the physical level of an item risk.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public final Integer level() {
    return this.level;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  ////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method: from
  /**
   ** Factory method to create a proper <code>Risk</code> from the given
   ** <code>level</code> value.
   **
   ** @param  level            the integer value the <code>Risk</code> should
   **                          be returned for.
   **                          <br>
   **                          Allowed object is {@link Integer}.
   **
   ** @return                  the <code>Risk</code> mapped at
   **                          <code>id</code>.
   **                          <br>
   **                          Possible object is <code>Risk</code>.
   */
  public static Risk from(final int level) {
    for (Risk cursor : Risk.values()) {
      if (cursor.level.equals(level))
        return cursor;
    }
    return null;
  }
}