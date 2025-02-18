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
    Subsystem   :   Common Shared Offline Application Management

    File        :   Application.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Application.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// abstract class Application
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~
/**
 ** A <code>Application</code> that wrappes the custom level type
 ** <code>Generic</code>.
 ** <br>
 ** <b>Note</b>:
 ** <br>
 ** Class is package protected
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="account" maxOccurs="unbounded" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="entitlements" maxOccurs="unbounded" minOccurs="0"&gt;
 **                     &lt;complexType&gt;
 **                       &lt;complexContent&gt;
 **                         &lt;extension base="{http://www.oracle.com/schema/oim/offline}entity"&gt;
 **                           &lt;sequence&gt;
 **                             &lt;element name="entitlement" type="{http://www.oracle.com/schema/oim/offline}entitlement" maxOccurs="unbounded" minOccurs="0"/&gt;
 **                           &lt;/sequence&gt;
 **                         &lt;/extension&gt;
 **                       &lt;/complexContent&gt;
 **                     &lt;/complexType&gt;
 **                   &lt;/element&gt;
 **                 &lt;/sequence&gt;
 **                 &lt;attribute name="action" default="assign"&gt;
 **                   &lt;simpleType&gt;
 **                     &lt;restriction base="{http://www.oracle.com/schema/oim/offline}token"&gt;
 **                       &lt;enumeration value="assign"/&gt;
 **                       &lt;enumeration value="revoke"/&gt;
 **                       &lt;enumeration value="enable"/&gt;
 **                       &lt;enumeration value="disable"/&gt;
 **                     &lt;/restriction&gt;
 **                   &lt;/simpleType&gt;
 **                 &lt;/attribute&gt;
 **               &lt;/extension&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="id" use="required" type="{http://www.oracle.com/schema/oim/offline}token" /&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @see     Entity
 ** @see     EntitlementEntity
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Application extends EntitlementEntity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6985938090996606433")
  private static final long                            serialVersionUID = -9162925224927751035L;

  private String                                       status;

  /**
   ** The container what {@link EntitlementEntity}'s the application should
   ** have.
   ** <br>
   ** We use a TreeMap to ensure that the order in which the collection will
   ** return the KeySet is always the same.
   */
  private final Map<String, List<EntitlementEntity>>   entitlement = new TreeMap<String, List<EntitlementEntity>>();

  /**
   ** The container which <code>Identity</code>s should assigned to, revoked
   ** from enabled or disabled for this <code>Application</code>.
   */
  private final Map<Identity, List<EntitlementEntity>> account     = new TreeMap<Identity, List<EntitlementEntity>>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> with the specified name but without
   ** an valid identifier.
   ** <p>
   ** The identifier the <code>Application</code> belongs to has to be populated
   ** manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  protected Application(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> with the specified identifier and
   ** name.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  protected Application(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> which is associated with the
   ** specified task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifyingname of the
   **                            <code>Application</code>.
   ** @param  status             the status of the <code>Application</code>.
   */
  protected Application(final long identifier, final String name, final String status) {
    // ensure inheritance
    super(identifier, name);
    // initialize instannce attributes
    this.status = status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   status
  /**
   ** Returns the status of the <code>Application</code>.
   **
   ** @return                    the status of the <code>Application</code>.
   */
  public final String status() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the String that is passed to the filter to retrieve the requested
   ** type of a <code>Application</code>.
   **
   ** @return                    the String that is passed to the filter to
   **                            retrieve the requested type of a
   **                            <code>Application</code>.
   */
  public String type() {
    return ResourceObject.TYPE_APPLICATION;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   orderableFor
  /**
   ** Returns the String that is passed to the filter to retrieve the requested
   ** order for of a <code>Application</code>.
   **
   ** @return                    the String that is passed to the filter to
   **                            retrieve the requested order for of a
   **                            <code>Application</code>.
   */
  public abstract String orderableFor();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Creates an empty namespace for {@link EntitlementEntity}'s in this
   ** <code>Application</code>.
   **
   ** @param  name               the name if the namespace for
   **                            {@link EntitlementEntity}'s assignable to this
   **                            <code>Application</code>.
   */
  public final void namespace(final String name) {
    // prevent bogus input
    if (StringUtility.isEmpty(name))
      throw new IllegalArgumentException(TaskBundle.format(TaskError.ARGUMENT_IS_NULL, "name"));

    // validate if the namespace allready exists
    if (this.entitlement.keySet().contains(name))
      return;

    // put an association in the entitlement namespace
    this.entitlement.put(name, new LinkedList<EntitlementEntity>());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   namespace
  /**
   ** Returns the {@link List} of {@link EntitlementEntity} namespaces of this
   ** <code>Application</code>.
   **
   ** @return                    the {@link List} of {@link EntitlementEntity}
   **                            namespaces assigned to this
   **                            <code>Application</code>; returns never
   **                            <code>null</code>.
   */
  public final Set<String> namespace() {
    return this.entitlement.keySet();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   entitlement
  /**
   ** Returns the {@link List} of {@link EntitlementEntity} for a specific
   ** <code>Application</code> if the <code>Application</code> contained in the
   ** collection of <code>Application</code>s to assign.
   **
   ** @param  namespace          the namespace where the
   **                            {@link EntitlementEntity}s are requested for.
   **
   ** @return                    the {@link List} of {@link EntitlementEntity}
   **                            of the specified namespace of this
   **                            <code>Application</code>; returns never
   **                            <code>null</code>.
   */
  public final List<EntitlementEntity> entitlement(final String namespace) {
    return this.entitlement.get(namespace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: entity (Entity)
  /**
   ** Returns the XML name used for informational purpose with an end user.
   **
   ** @return                    the XML name used for informational purpose
   **                            with an end user.
   */
  @Override
  public String entity() {
    return TaskBundle.string(TaskMessage.ENTITY_RESOURCE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
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
    final StringBuilder builder = new StringBuilder(TaskBundle.format(TaskMessage.ENTITLEMENT_ACTION, this.action().displayName(), entity()));
    final Risk          risk    = this.risk();
    if (risk != null)
      builder.append(TaskBundle.format(TaskMessage.ENTITLEMENT_RISK, risk.displayName()));
    builder.append(super.toString());
    if (!CollectionUtility.empty(namespace())) {
      for (String id : namespace()) {
        final List<EntitlementEntity> entitlement = entitlement(id);
        builder.append(entitlement.toString());
      }
    }
    return builder.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link EntitlementEntity}'s to the list of entitlements
   ** that should be provisioned to an <code>Identity</code> on this
   ** <code>Application</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            {@link EntitlementEntity}'s that is mapped on
   **                            the specified <code>Application</code> handled
   **                            inside of Oracle Identity Manager.
   ** @param  value              the {@link EntitlementEntity} that should be
   **                            provisioned on this <code>Application</code>
   **                            to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object add(final String entitlement, final EntitlementEntity value) {
    List<EntitlementEntity> list = entitlement(entitlement);
    if (list == null) {
      list = new LinkedList<EntitlementEntity>();
      this.entitlement.put(entitlement, list);
    }
    list.add(value);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link EntitlementEntity}'s to the list of entitlements
   ** that should be provisioned to an <code>Identity</code> on this
   ** <code>Application</code>.
   **
   ** @param  entitlement        the internal name of the
   **                            {@link EntitlementEntity}'s that is mapped on
   **                            the specified <code>Application</code> handled
   **                            inside of Oracle Identity Manager.
   ** @param  values             the {@link EntitlementEntity}'s that should be
   **                            provisioned on this <code>Application</code>
   **                            to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object add(final String entitlement, final List<EntitlementEntity> values) {
    List<EntitlementEntity> list = entitlement(entitlement);
    if (list == null) {
      list = new LinkedList<EntitlementEntity>();
      this.entitlement.put(entitlement, list);
    }
    list.addAll(values);
    return list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds the specified {@link EntitlementEntity}'s to the list of entitlements
   ** that should be provisioned to an <code>Account</code> on this
   ** <code>Application</code>.
   **
   ** @param  account            the the {@link ApplicationAccount} that is
   **                            mapped on the specified
   **                            <code>Application</code> handled inside of
   **                            Oracle Identity Manager.
   ** @param  values             the {@link EntitlementEntity}'s that should be
   **                            provisioned an this <code>Application</code>
   **                            to add.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final Object add(final Identity account, final List<EntitlementEntity> values) {
    List<EntitlementEntity> list = this.account.get(account);
    if (list == null) {
      list = new LinkedList<EntitlementEntity>();
      this.account.put(account, list);
    }
    list.addAll(values);
    return list;
  }
}