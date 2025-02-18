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

    File        :   ApplicationEntity.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ApplicationEntity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2010-02-10  DSteding    First release version
*/

package oracle.iam.identity.foundation.offline;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Collection;

import oracle.iam.identity.foundation.naming.ResourceObject;

////////////////////////////////////////////////////////////////////////////////
// class ApplicationEntity
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** A <code>ApplicationEntity</code> that wrappes the custom level type
 ** <code>Application</code>.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
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
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class ApplicationEntity extends Application {

  //////////////////////////////////////////////////////////////////////////////
  // static final attribute
  //////////////////////////////////////////////////////////////////////////////

  public static final String             SINGLE           = "application";
  public static final String             MULTIPLE         = "applications";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-1454136397410056932")
  private static final long              serialVersionUID = -4687624170057211729L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attribute
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the {@link TreeSet} of <code>Application Instance</code>s that are
   ** designed as {@link Application}s and has a dependendy on this
   ** <code>Application Instance</code>
   */
  protected Set<ApplicationEntity>       parents         = new TreeSet<ApplicationEntity>();

  /**
   ** The {@link Map} the stores the depended Applications in the way
   ** <ul>
   **   <li> keyEntry is the internal identifier of Oracle Identity Manager
   **                 for the <code>Application Instance</code>
   **   <li> value    is the public name of Oracle Identity Manager for the
   **                 <code>Application Instance</code>
   ** </ul>
   */
  private Map<String, ApplicationEntity> forward         = new TreeMap<String, ApplicationEntity>();

  /**
   ** The {@link Map} the stores the depended Applications in the way
   ** <ul>
   **   <li> keyEntry is the public name of Oracle Identity Manager for the
   **                 <code>Application Instance</code>
   **   <li> value    is the internal identifier of Oracle Identity Manager
   **                 for the <code>Application Instance</code>
   ** </ul>
   */
  private Map<String, ApplicationEntity> reverse         = new TreeMap<String, ApplicationEntity>();

  private ProcessForm                    mainForm;
  private ProcessForm[]                  childForm;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationEntity</code> with the specified name but
   ** without an valid identifier.
   ** <p>
   ** The identifier the <code>ApplicationEntity</code> belongs to has to be
   ** populated manually.
   **
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  public ApplicationEntity(final String name) {
    // ensure inheritance
    super(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationEntity</code> with the specified identifier
   ** and name.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifying name of the
   **                            <code>Application</code>.
   */
  public ApplicationEntity(final long identifier, final String name) {
    // ensure inheritance
    super(identifier, name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ApplicationEntity</code> which is associated with the
   ** specified task.
   **
   ** @param  identifier         the internal system identifier of the
   **                            <code>Application</code> to load.
   ** @param  name               the identifyingname of the
   **                            <code>Application</code>.
   ** @param  status             the status of the <code>Application</code>.
   */
  public ApplicationEntity(final long identifier, final String name, final String status) {
    // ensure inheritance
    super(identifier, name, status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hasParents
  /**
   ** Returns <code>true</code> if this resource dependends on other
   ** <code>Application</code>s.
   **
   ** @return                    <code>true</code> if this resource dependends
   **                            on other <code>Application</code>s.
   */
  public final boolean hasParents() {
    return this.parents.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependsOn
  /**
   ** Returns <code>true</code> if this resource dependends on the specified
   ** <code>Application</code>.
   **
   ** @param  parent             the {@link Application} to verify for
   **                            dependencies.
   **
   ** @return                    <code>true</code> if this resource dependends
   **                            on the specified <code>Application</code>.
   */
  public final boolean dependsOn(final Application parent) {
    return hasParents() ? this.parents.contains(parent) : false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   isOwner
  /**
   ** Returns <code>true</code> if this resource has dependend
   ** <code>Application</code>s.
   **
   ** @return                    <code>true</code> if this resource has
   **                            dependend <code>Application</code>s.
   */
  public final boolean isOwner() {
    return !this.forward.isEmpty();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   owns
  /**
   ** Returns <code>true</code> if the specified resource is enlist of dependend
   ** <code>Application</code>s.
   **
   ** @param  resource           the name of the <code>Application</code> to
   **                            lookup.
   **
   ** @return                    the dependend {@link ApplicationEntity} if any.
   */
  public final boolean owns(final long resource) {
    return this.forward.containsKey(String.valueOf(resource));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   owns
  /**
   ** Returns <code>true</code> if the specified resource is enlist of dependend
   ** <code>Application</code>s.
   **
   ** @param  resource           the name of the <code>Application</code> to
   **                            lookup.
   **
   ** @return                    the dependend {@link ApplicationEntity} if any.
   */
  public final boolean owns(final String resource) {
    return this.reverse.containsKey(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependency
  /**
   ** Returns the <code>ApplicationEntity</code> if the specified resource is
   ** enlisted of dependend <code>ApplicationEntity</code>s.
   **
   ** @param  resource           the <code>ApplicationEntity</code> instance or
   **                            <code>null</code> if the specified name does
   **                            not match to a depended
   **                            <code>ApplicationEntity</code>.
   **
   ** @return                    the dependend {@link ApplicationEntity} if any.
   */
  public final ApplicationEntity dependency(final long resource) {
    return this.forward.get((String.valueOf(resource)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   dependency
  /**
   ** Returns the <code>ApplicationEntity</code> if the specified resource is
   ** enlisted of dependend <code>ApplicationEntity</code>s.
   **
   ** @param  resource           the <code>ApplicationEntity</code> instance or
   **                            <code>null</code> if the specified name does
   **                            not match to a depended
   **                            <code>ApplicationEntity</code>.
   **
   ** @return                    the dependend {@link ApplicationEntity} if any.
   */
  public final ApplicationEntity dependency(final String resource) {
    return this.reverse.get(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mainForm
  /**
   ** Sets a the internal system identifier of the process form that is used to
   ** provision this <code>Application</code>s.
   **
   ** @param  mainForm           the internal system identifier of the process
   **                            form that is used to provision this
   **                            <code>Application</code>s.
   */
  public final void mainForm(final ProcessForm mainForm) {
    this.mainForm = mainForm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   mainForm
  /**
   ** Returns the internal system identifier of the process form that is used
   ** to provision this <code>Application</code>s.
   **
   ** @return                    the internal system identifier of the process
   **                            form that is used to provision this
   **                            <code>Application</code>s.
   */
  public final ProcessForm mainForm() {
    return this.mainForm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   childForm
  /**
   ** Sets a the internal system identifier of the process form that is used
   ** to provision details of this <code>Application</code>s.
   **
   ** @param  childForm          the internal system identifier of the process
   **                            forms that is used to provision details of
   **                            this <code>Application</code>s.
   */
  public final void childForm(final ProcessForm[] childForm) {
    this.childForm = childForm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   childForm
  /**
   ** Returns the internal system identifier of the process form that is used
   ** to provision details of this <code>Application</code>s.
   **
   ** @return                    the internal system identifier of the process
   **                            form that is used to provision details of
   **                            this <code>Application</code>s.
   */
  public final ProcessForm[] childForm() {
    return this.childForm;
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
  // Method:   orderableFor (Application)
  /**
   ** Returns the String that is passed to the filter to retrieve the requested
   ** order for of a <code>Application</code>.
   **
   ** @return                    the String that is passed to the filter to
   **                            retrieve the requested order for of an
   **                            <code>Application</code>.
   */
  public final String orderableFor() {
    return ResourceObject.ORDER_FOR_USER;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Adds the specified <code>ApplicationEntity</code> to the list of parent
   ** object that where this <code>ApplicationEntity</code> depends on.
   **
   ** @param  collection         the {@link Collection} of
   **                            <code>ApplicationEntity</code> to be added to
   **                            the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean parent(final Collection<ApplicationEntity> collection) {
    for (ApplicationEntity entity : collection)
      parent(entity);
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parent
  /**
   ** Adds the specified <code>ApplicationEntity</code> to the list of parent
   ** object where this <code>ApplicationEntity</code> depends on.
   **
   ** @param  instance           the <code>ApplicationEntity</code> to be
   **                            added to the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call.
   */
  public final boolean parent(final ApplicationEntity instance) {
    if (this.parents == null)
      this.parents = new TreeSet<ApplicationEntity>();

    // add the resource themselve
    if (!parents.contains(instance)) {
      instance.forward.put(String.valueOf(this.key), this);
      instance.reverse.put(this.name, this);
      return this.parents.add(instance);
    }
    else
      return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeParent
  /**
   ** Removes the specified <code>ApplicationEntity</code>s from the list of
   ** parent object where this <code>ApplicationEntity</code> depends on.
   **
   ** @param  collection         the {@link Collection} of
   **                            <code>ApplicationEntity</code>s to be removed
   **                            from the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean removeParent(final Collection<ApplicationEntity> collection) {
    for (ApplicationEntity entity : collection)
      removeParent(entity);
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeParent
  /**
   ** Removes the specified <code>ApplicationEntity</code> from the list of
   ** parent object where this <code>ApplicationEntity</code> depends on.
   **
   ** @param  instance           the <code>ApplicationEntity</code> to be
   **                            removed from the set of parents.
   **
   ** @return                    <code>true</code> if the collection changed as
   **                            a result of the call
   */
  public final boolean removeParent(final ApplicationEntity instance) {
    if (this.parents == null)
      return false;

    // remove the resource themselve
    if (parents.contains(instance)) {
      instance.forward.remove(String.valueOf(this.key));
      instance.reverse.remove(this.name);
      return this.parents.remove(instance);
    }
    else
      return false;
  }
}