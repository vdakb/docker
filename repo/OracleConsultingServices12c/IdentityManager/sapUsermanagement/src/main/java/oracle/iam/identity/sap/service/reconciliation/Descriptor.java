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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   AccountTargetReconciliation.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AccountTargetReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.service.reconciliation;

import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.AttributeMapping;
import oracle.iam.identity.foundation.AttributeTransformation;

////////////////////////////////////////////////////////////////////////////////
// class Descriptor
// ~~~~~ ~~~~~~~~~~
/**
 ** The <code>ReconciliationDescriptor</code> is intend to use where outbound
 ** attributes of a reconciliation target are mapped to the inbound Oracle
 ** Identity Manager Object (core or user defined).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
class Descriptor extends oracle.iam.identity.foundation.reconciliation.Descriptor {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which must be defined on this Metadata Descriptor to hold
   ** the name of the attribute which refers to the RFC table to read the
   ** change log.
   */
  public static final String CHANGELOG = "changelog";

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum ChangeLog
  // ~~~ ~~~~~~~~~~
  enum ChangeLog {

      USR02("USR02")
    , USR04("USR04")
    , USH02("USH02")
    ;
    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-7978550205497894102")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>ChangeLog</code> that allows use as a JavaBean.
     **
     ** @param  value            the external representation if this
     **                          <code>ChangeLog</code>.
     */
    ChangeLog(final String value) {
      // initialize instance attributes
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   value
    /**
     ** Returns the value of the value property.
     **
     ** @return                  the value of the value property.
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods groupd by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: fromValue
    /**
     ** Factory method to create a proper status from the given string value.
     **
     ** @param  value             the string value the status should be returned
     **                          for.
     **
     ** @return                  the relation.
     */
    public static ChangeLog fromValue(final String value) {
      for (ChangeLog cursor : ChangeLog.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Descriptor</code> which is associated with the
   ** specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   */
  public Descriptor(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Descriptor</code> which is associated with the
   ** specified logging provider <code>loggable</code>.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  mapping            the {@link AttributeMapping} of this reference
   **                            descriptor.
   ** @param  transformation     the {@link AttributeTransformation} of this
   **                            reference descriptor.
   */
  public Descriptor(final Loggable loggable, final AttributeMapping mapping, final AttributeTransformation transformation) {
    // ensure inheritance
    super(loggable, mapping, transformation);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLog
  /**
   ** Sets the name of the attribute which refers to the RFC table to read the
   ** change log.
   **
   ** @param  changeLog          the name of the attribute which refers to the
   **                            RFC table to read the change log.
   */
  public final void changeLog(final ChangeLog changeLog) {
    this.put(CHANGELOG, changeLog);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   changeLog
  /**
   ** Returns the name of the attribute which refers to the RFC table to read the
   ** change log.
   ** <p>
   ** Convenience method to shortens the access to the configuration attribute
   ** {@link #CHANGELOG}.
   **
   ** @return                    the name of the attribute which refers to the
   **                            RFC table to read the change log.
   */
  public final Enum<ChangeLog> changeLog() {
    return (Enum<ChangeLog>)enumValue(CHANGELOG);
  }
}