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
    Subsystem   :   Identity Governance Service

    File        :   Named.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Named.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.jpa.provider;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.MappedSuperclass;

import oracle.hst.platform.jpa.converter.Boolean2Integer;

////////////////////////////////////////////////////////////////////////////////
// abstract class Named
// ~~~~~~~~ ~~~~~ ~~~~~
/**
 ** The base class for persited metadata of resources that have a name.
 **
 ** @param  <I>                  the type of the identifiying value
 **                              implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@MappedSuperclass
public abstract class Named<I extends Comparable<I> & Serializable> extends Base<I> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8562885297768153061")
  private static final long serialVersionUID = 3049561861090223382L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Column(name=NAME, unique=true, nullable=false, length=64)
  protected String          name;

  @Convert(converter=Boolean2Integer.class)
  @Column(name=ACTIVE, nullable=false)
  protected Boolean         active;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty persistable <code>Named</code> instance that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Named() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Sets the <code>name</code> property of the <code>Named</code> entity.
   **
   ** @param  value              the <code>name</code> property of the
   **                            <code>Named</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final void setName(final String value) {
    this.name = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getName
  /**
   ** Returns the <code>name</code> property of the <code>Named</code> entity.
   **
   ** @return                    the <code>name</code> property of the
   **                            <code>Named</code> entity.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getName() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setActive
  /**
   ** Sets the <code>active</code> property of the <code>Named</code> entity.
   **
   ** @param  value              the <code>active</code> property of the
   **                            <code>Named</code> entity to set.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   */
  public final void setActive(final Boolean value) {
    this.active = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getActive
  /**
   ** Returns the <code>active</code> property of the <code>Named</code> entity.
   **
   ** @return                    the <code>active</code> property of the
   **                            <code>Named</code> entity.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean getActive() {
    return this.active;
  }
}