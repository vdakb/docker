/*
    Oracle Deutschland GmbH

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license  agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Foundation Shared Library
    Subsystem   :   Common Shared Offline Resource Management

    File        :   EntitlementEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntitlementEntity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class EntitlementEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A <code>EntitlementEntity</code> that wrappes the details table information.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entitlement"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **       &lt;attribute name="action" default="assign"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://www.oracle.com/schema/oim/offline}token"&gt;
 **             &lt;enumeration value="assign"/&gt;
 **             &lt;enumeration value="revoke"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @see     Entity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class EntitlementEntity extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String   MULTIPLE         = "entitlements";
  public static final String   SINGLE           = "entitlement";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:2349385179501947639")
  private static final long    serialVersionUID = 1040534716599165663L;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  private static int           errorState       = 0;
  private static String        errorText        = null;
  private static MessageDigest digester         = null;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The {@link Action} to apply. */
  private Action               action;

  /** The {@link Risk} to apply. */
  private Risk                 risk;

  /** The application entity the entitlement is assigned to. */
  protected ApplicationEntity  application;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Action
  // ~~~~ ~~~~~~~
  /**
   ** This enum store the grammar's constants of {@link Action}s.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   **   &lt;simpleType name="action"&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}token"&gt;
   **       &lt;enumeration value="assign"/&gt;
   **       &lt;enumeration value="revoke"/&gt;
   **       &lt;enumeration value="enable"/&gt;
   **       &lt;enumeration value="disable"/&gt;
   **     &lt;/restriction&gt;
   **   &lt;/simpleType&gt;
   ** </pre>
   */
  public static enum Action {

      /** the encoded action values that can by applied on entitlements. */
      assign(TaskMessage.ENTITLEMENT_ACTION_ASSIGN)
    , revoke(TaskMessage.ENTITLEMENT_ACTION_REVOKE)
    , enable(TaskMessage.ENTITLEMENT_ACTION_ENABLE)
    , disable(TaskMessage.ENTITLEMENT_ACTION_DISABLE)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attribute
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:7206500452373026912")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the name of the resource key for this <code>Action</code>. */
    private final String      resourceKey;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Action</code> with a single parent state.
     **
     ** @param  resourceKey      the name of the resource key for this
     **                          <code>Action</code>.
     */
    Action(final String resourceKey) {
      this.resourceKey = resourceKey;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: displayName
    /**
     ** Translate the instance.
     **
     ** @return                  the <code>String</code> of this enumeration
     **                          value for display and logging purpose.
     */
    public final String displayName() {
      return TaskBundle.string(this.resourceKey);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // enum Risk
  // ~~~~ ~~~~
  /**
   ** The <code>Risk</code> translate the semantic representation of an item
   ** risk level to the physical representation in the persistence layer.
   */
  public enum Risk {

      /** the encoded risk values that can by applied on entitlements. */
      none(0,   TaskMessage.ENTITLEMENT_RISK_NONE)
    , low(3,    TaskMessage.ENTITLEMENT_RISK_LOW)
    , medium(5, TaskMessage.ENTITLEMENT_RISK_MEDIUM)
    , high(7,   TaskMessage.ENTITLEMENT_RISK_HIGH)
    ;

    ////////////////////////////////////////////////////////////////////////////
    // static final attribute
    ////////////////////////////////////////////////////////////////////////////

    // the official serial version ID which says cryptically which version we're
    // compatible with
    @SuppressWarnings("compatibility:-8085084305670959675")
    private static final long serialVersionUID = -1L;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /**
     ** the physical value stored in the persistence layer for this risk level
     */
    private final Integer     level;

    /** the name of the resource key for this <code>Action</code>. */
    private final String      resourceKey;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Risk</code> with the specified physical risk
     ** level.
     **
     ** @param  level            the physical level of an item risk
     ** @param  resourceKey      the name of the resource key for this
     **                          <code>Risk</code>.
     */
    Risk(final int level, final String resourceKey) {
      this.level       = new Integer(level);
      this.resourceKey = resourceKey;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: level
    /**
     ** Returns the physical risk level.
     **
     ** @return                  the physical level of an item risk
     */
    public final Integer level() {
      return this.level;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: displayName
    /**
     ** Translate the instance.
     **
     ** @return                  the <code>String</code> of this enumeration
     **                          value for display and logging purpose.
     */
    public final String displayName() {
      return TaskBundle.string(this.resourceKey);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    try {
      digester = MessageDigest.getInstance("MD5");
    }
    catch (NoSuchAlgorithmException e) {
      errorState = -20;
      errorText  = e.getLocalizedMessage();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>EntitlementEntity</code> with the specified name but
   ** without an valid identifier.
   ** <p>
   ** The identifier the <code>EntitlementEntity</code> belongs to has to be
   ** populated manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>EntitlementEntity</code>.
   */
  public EntitlementEntity(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Evaluatess the MD5 hash of the given criteria.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>EntitlementEntity</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>EntitlementEntity</code>.
   */
  public EntitlementEntity(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Evaluatess the MD5 hash of the given criteria.
   **
   ** @param  application        the application entity the entitlement is
   **                            assigned to.
   ** @param  identifier         the internal system identifier of the
   **                            <code>EntitlementEntity</code> to load.
   ** @param  field              the identifying names of the
   **                            <code>EntitlementEntity</code>.
   */
  public EntitlementEntity(final ApplicationEntity application, final long identifier, final String[] field) {
    // Ensure the digest's buffer is empty.
    digester.reset();

    // Fill the digest's buffer with provided data to compute a message digest
    // from.
    for (int i = 0; i < field.length; i++) {
      if (!StringUtility.isEmpty(field[i]))
        digester.update(field[i].getBytes());
    }

    this.key         = identifier;
    // Generate the digest.
    // This does any necessary padding required by the algorithm.
    this.name        = StringUtility.bytesToHex(digester.digest());
    this.application = application;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this entitlement.
   **
   ** @param  value              the encoded action to be applied on this
   **                            entitlement.
   **
   ** @throws TaskException     if the passed <code>value</code> cannot be
   **                           matched with any value of the enumeration
   **                           {@link Action}.
   */
  public void action(final String value)
    throws TaskException {

    if (StringUtility.isEmpty(value))
      action(Action.assign);
    else {
      final Action action = Action.valueOf(value);
      if (action == null)
        throw new TaskException(TaskError.ARGUMENT_BAD_VALUE, value);

      action(action);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Sets the {@link Action} to be applied on this entitlement.
   **
   ** @param  action             the {@link Action} to be applied on this
   **                            entitlement.
   */
  public void action(final Action action) {
    this.action = action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   action
  /**
   ** Returns the {@link Action} to be applied on this entitlement.
   **
   ** @return                    the {@link Action} to be applied on this
   **                            entitlement.
   */
  public Action action() {
    return this.action;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   risk
  /**
   ** Sets the {@link Risk} to be applied on this entitlement.
   **
   ** @param  value              the encoded action to be applied on this
   **                            entitlement.
   **
   ** @throws TaskException     if the passed <code>value</code> cannot be
   **                           matched with any value of the enumeration
   **                           {@link Action}.
   */
  public void risk(final String value)
    throws TaskException {

    if (StringUtility.isEmpty(value))
      this.risk = Risk.none;
    else
      try {
        // the value is constrained by the enumeration hence no conversion
        this.risk = Risk.valueOf(value);
      }
      catch (IllegalArgumentException e) {
        throw new TaskException(TaskError.ARGUMENT_BAD_VALUE, value);
      }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   risk
  /**
   ** Sets the {@link Risk} to be applied on this entitlement.
   **
   ** @param  risk               the {@link Risk} to be applied on this
   **                            entitlement.
   */
  public void risk(final Risk risk) {
    this.risk = risk;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   risk
  /**
   ** Returns the {@link Risk} to be applied on this entitlement
   **
   ** @return                    the {@link Risk} to be applied on this
   **                            entitlement.
   */
  public Risk risk() {
    return this.risk;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  @Override
  public String elements() {
    return MULTIPLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element (XMLSerializable)
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  @Override
  public String element() {
    return SINGLE;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entity (Entity)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_ENTITLEMENT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns a string representation of the object.
   ** <p>
   ** In general, the <code>toString</code> method returns a string that
   ** "textually represents" this object. The result is a concise but
   ** informative representation that is easy for a person to read.
   ** <p>
   ** The <code>toString</code> method for class <code>Entity</code> returns a
   ** string consisting of the name of the class of which the object is an
   ** instance.
   **
   ** @return                    a string representation of the object.
   */
  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder(TaskBundle.format(TaskMessage.ENTITLEMENT_ACTION, this.action.displayName(), entity()));
    if (this.risk != null)
      builder.append(TaskBundle.format(TaskMessage.ENTITLEMENT_RISK, this.risk.displayName()));
    builder.append(super.toString());
    return builder.toString();
  }
}