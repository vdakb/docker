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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Directory Service Utility Library
    Subsystem   :   Deployment Utilities 11g

    File        :   EntryHandler.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    EntryHandler.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0     2012-12-09  DSteding    First release version
*/

package oracle.iam.directory.common.spi.handler;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.NameNotFoundException;
import javax.naming.NameAlreadyBoundException;
import javax.naming.OperationNotSupportedException;

import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.naming.directory.SearchControls;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SchemaViolationException;
import javax.naming.directory.InvalidAttributesException;
import javax.naming.directory.InvalidAttributeIdentifierException;

import javax.naming.ldap.Control;
import javax.naming.ldap.LdapContext;

import org.apache.tools.ant.BuildException;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceFrontend;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

import oracle.hst.deployment.type.ODSServerContext;

import oracle.iam.directory.common.FeatureError;
import oracle.iam.directory.common.FeatureMessage;
import oracle.iam.directory.common.FeatureException;
import oracle.iam.directory.common.FeatureResourceBundle;

import oracle.iam.directory.common.task.FeatureDirectoryTask;

////////////////////////////////////////////////////////////////////////////////
// class EntryHandler
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Invokes an operation on LDAP Server to managed entries in a Directory
 ** Information Tree (DIT).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class EntryHandler extends AbstractFeatureHandler {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final List<EntryInstance> workload = new ArrayList<EntryInstance>();

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Name
  // ~~~~~ ~~~~
  public static abstract class Name extends AbstractInstance {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Name</code> task that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public Name() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>Name</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public Name(final String name) {
      // ensure inheritance
      super();

      // initialize instance attributes
      name(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overriden)
    /**
     ** Compares this instance with the specified object.
     ** <p>
     ** The result is <code>true</code> if and only if the argument is not
     ** <code>null</code> and is a <code>Name</code> object that represents
     ** the same <code>name</code> and <code>action</code> as this instance.
     **
     ** @param other             the object to compare this <code>Name</code>
     **                          with.
     **
     ** @return                  <code>true</code> if the <code>Name</code>s
     **                          are equal; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object other) {
      if (!(other instanceof Name))
        return false;

      final Name another = (Name)other;
      return another.name().equals(this.name());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class EntryInstance
  // ~~~~~ ~~~~~~~~~~~~~
  public static class EntryInstance extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private String          value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>EntryInstance</code> task that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public EntryInstance() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>EntryInstance</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public EntryInstance(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Called to inject the argument for parameter <code>rdn</code>.
     **
     ** @param  value            the context value of the relative
     **                            distinguished name (RDN) of the entry.
     */
    public final void value(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the context value of the relative distinguished name (RDN) of
     ** the entry.
     **
     ** @return                    the context value of the relative
     **                            distinguished name (RDN) of the entry.
     */
    public final String value() {
      return this.value;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class AttributeInstance
  // ~~~~~ ~~~~~~~~~~~~~~~~~
  public static class AttributeInstance extends Name {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AttributeInstance</code> task that allows use as a
     ** JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     */
    public AttributeInstance() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AttributeInstance</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     */
    public AttributeInstance(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>AttributeInstance</code> with the specified name.
     **
     ** @param  name               the value set for the name property.
     ** @param  value              the value object to associate with the name.
     */
    public AttributeInstance(final String name, final String value) {
      // ensure inheritance
      super(name);

      // initialize instance attributes
      addParameter(value, value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>EntryHandler</code> to initialize the instance.
   **
   ** @param  frontend           the {link ServiceFrontend} that instantiated
   **                            this service.
   */
  public EntryHandler(final ServiceFrontend frontend) {
    // ensure inheritance
    super(frontend);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   name (AbstractHandler)
  /**
   ** Called to inject the argument for parameter <code>name</code>.
   **
   ** @param  name               the name of the alias.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void name(final String name)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pair to the parameters that has to be applied
   ** after an import operation.
   **
   ** @param  name               the name of the parameter of the Oracle
   **                            WebLogic Domain entity instance.
   ** @param  value              the value for <code>name</code> to set on the
   **                            LDAP Context instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final String name, final Object value)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addParameter (AbstractHandler)
  /**
   ** Add the specified value pairs to the parameters that has to be applied.
   **
   ** @param  parameter          the named value pairs to be applied on the
   **                            LDAP Context instance.
   **
   ** @throws BuildException     always.
   */
  @Override
  public void addParameter(final Map<String, Object> parameter)
    throws BuildException {

    throw new BuildException(ServiceResourceBundle.string(ServiceError.NOTIMPLEMENTED));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createAttributes
  /**
   ** Creates {@link Attributes} object for a given {@link Map} containing key,
   ** values.
   ** <p>
   ** This method create the attributes only for mapping entries that has
   ** non-empty values.
   **
   ** @param  mapping            the {@link Map} containing the name to value
   **                            attribute mapping.
   **
   ** @return                    the {@link Attributes} object created from the
   **                            given {@link Map}.
   **
   ** @throws FeatureException   if the operation fails.
   */
  public static Attributes createAttributes(final Map<String, Object> mapping)
    throws FeatureException {

    final Attributes attributes = new BasicAttributes(false);
    // avoid bogus input
    if (mapping == null)
      return attributes;

    for (String name : mapping.keySet()) {
      final Object value = mapping.get(name);
      // we have to decide if we put a value in the attribute or not
      // this is necessary to prevent the delivery of attributes to the
      // backend server that don't have a value
      if (value != null) {
        final Attribute attribute = new BasicAttribute(name);
        for (Object cursor : CollectionUtility.list((Collection)value))
          attribute.add(cursor);

        attributes.put(attribute);
      }
    }
    return attributes;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add the specified entry to the workload.
   **
   ** @param  object           the {@link EntryInstance} to add.
   **
   ** @throws BuildException   if the specified {@link EntryInstance} is already
   **                          assigned to this task.
   */
  public void add(final EntryInstance object)
    throws BuildException {

    this.workload.add(object);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates a new new entry in the LDAP Server through the discovered
   ** {@link LdapContext}.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the registered
   ** entries to delete.
   **
   ** @throws FeatureException     in case an error does occur.
   */
  public void create()
    throws FeatureException {

    // initialize the business logic layer to operate on
    this.facade = ((FeatureDirectoryTask)frontend()).context().unwrap();

    for (EntryInstance cursor : this.workload) {
      final Attributes attributes = createAttributes(cursor.parameter());
      attributes.put(new BasicAttribute(cursor.name(), cursor.value()));
      createEntry(ODSServerContext.composeName(cursor.name(), cursor.value()), attributes);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Invokes an operation on the connected Directory Information Tree (DIT) to
   ** modify entries.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the registered
   ** entries to modify.
   **
   ** @throws FeatureException     a operational problem occurred when talking
   **                            to the LDAP server.
   */
  public final void modify()
    throws FeatureException {

    // initialize the business logic layer to operate on
    this.facade = ((FeatureDirectoryTask)frontend()).context().unwrap();

    for (EntryInstance cursor : this.workload) {
      modifyEntry(ODSServerContext.composeName(cursor.name(), cursor.value()), createAttributes(cursor.parameter()));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** Invokes an operation on the connected Directory Information Tree (DIT) to
   ** delete entries.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the registered
   ** entries to delete.
   **
   ** @throws FeatureException   a operational problem occurred when talking
   **                            to the LDAP server.
   */
  public final void delete()
    throws FeatureException {

    // initialize the business logic layer to operate on
    this.facade = ((FeatureDirectoryTask)frontend()).context().unwrap();

    for (EntryInstance cursor : this.workload) {
      deleteEntry(ODSServerContext.composeName(cursor.name(), cursor.value()), null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntry
  /**
   ** Creates object and binds to the passed relative distinguished name and the
   ** attributes.
   ** <br>
   ** Calls the context.createSubcontext() to create object and bind it with the
   ** passed distinguished name and the attributes.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the entry to create.
   **
   ** @param  objectRDN          the relative distinguished name of the object
   **                            to create, e.g. <code>cn=george</code>.
   ** @param  attributes         the attributes of the object.
   **
   ** @throws FeatureException   if the operation fails
   */
  protected void createEntry(final String objectRDN, final Attributes attributes)
    throws FeatureException {

    final String[] arguments = {contextName(), objectRDN};
    info(FeatureResourceBundle.format(FeatureMessage.OBJECT_CREATE_BEGIN, arguments));
    try {
      // create object
      final Context context = this.facade.createSubcontext(objectRDN, attributes);
      contextClose(context);
      warning(FeatureResourceBundle.format(FeatureMessage.OBJECT_CREATE_SUCCESS, arguments));
    }
    catch (NameAlreadyBoundException e) {
      error(FeatureResourceBundle.format(FeatureError.OBJECT_EXISTS, arguments));
    }
    catch (InvalidAttributesException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.ATTRIBUTE_DATA, arguments, e);
      else
        error(FeatureResourceBundle.format(FeatureError.ATTRIBUTE_DATA, arguments));
    }
    catch (InvalidAttributeIdentifierException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.ATTRIBUTE_TYPE, arguments, e);
      else
        error(FeatureResourceBundle.format(FeatureError.ATTRIBUTE_TYPE, arguments));
    }
    catch (SchemaViolationException e) {
      error(FeatureResourceBundle.format(FeatureError.ATTRIBUTE_SCHEMA, arguments));
      error(e.getLocalizedMessage());
    }
    catch (OperationNotSupportedException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.NOT_SUPPORTED, arguments, e);
      else
        error(FeatureResourceBundle.format(FeatureError.NOT_SUPPORTED, arguments));
    }
    catch (NamingException e) {
      if (failonerror()) {
        throw new FeatureException(FeatureError.OBJECT_CREATE, arguments, e);
      }
      else
        error(FeatureResourceBundle.format(FeatureError.OBJECT_CREATE, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modifyEntry
  /**
   ** Modifies passed attributes to a existing object at the relative
   ** distinguished name.
   ** <br>
   ** Calls the <code>context.modifyAttributes()</code> to replace the
   ** attributes.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the entry to modify.
   **
   ** @param  objectRDN          the relative distinguished name of the object,
   **                            e.g. <code>'cn=george'</code>
   ** @param  attributes         the new attributes of the object
   **
   ** @throws FeatureException   if the operation fails
   */
  protected void modifyEntry(final String objectRDN, final Attributes attributes)
    throws FeatureException {

    final String[] arguments = {contextName(), objectRDN};
    info(FeatureResourceBundle.format(FeatureMessage.OBJECT_MODIFY_BEGIN, arguments));
    // Specify the changes to make
    final List<ModificationItem> set = new ArrayList<ModificationItem>();
    try {
      // first pass detect modifications
      final Attributes                       existing = lookupEntry(objectRDN);
      NamingEnumeration<? extends Attribute> names    = existing.getAll();
      while(names.hasMoreElements()) {
        final Attribute lhs = names.nextElement();
        final Attribute rhs = attributes.get(lhs.getID());
        if (rhs != null) {
          // verify if a change is really happens
          if (!equal(lhs, rhs))
            set.add(new ModificationItem(LdapContext.REPLACE_ATTRIBUTE, rhs));
        }
      }
      // second pass detect additions
      names = attributes.getAll();
      while(names.hasMoreElements()) {
        final Attribute lhs = names.nextElement();
        final Attribute rhs = existing.get(lhs.getID());
        if (rhs == null)
          set.add(new ModificationItem(LdapContext.ADD_ATTRIBUTE, lhs));
      }
      if (set.size() == 0) {
        warning(FeatureResourceBundle.format(FeatureMessage.OBJECT_MODIFY_SKIPPED, arguments));
      }
      else {
        final ModificationItem[] mod = set.toArray(new ModificationItem[0]);
        // modify object
        this.facade.modifyAttributes(objectRDN, mod);
        warning(FeatureResourceBundle.format(FeatureMessage.OBJECT_MODIFY_SUCCESS, arguments));
      }
    }
    catch (InvalidAttributesException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.ATTRIBUTE_DATA, arguments, e);
      else
        error(FeatureResourceBundle.format(FeatureError.ATTRIBUTE_DATA, arguments));
    }
    catch (SchemaViolationException e) {
      error(FeatureResourceBundle.format(FeatureError.ATTRIBUTE_SCHEMA, arguments));
      error(e.getLocalizedMessage());
    }
    catch (NameNotFoundException e) {
      error(FeatureResourceBundle.format(FeatureError.OBJECT_MODIFY, arguments));
      error(e.getLocalizedMessage());
    }
    catch (NamingException e) {
      if (failonerror())
        throw new FeatureException(FeatureError.OBJECT_MODIFY, arguments, e);
      else
        error(FeatureResourceBundle.format(FeatureError.OBJECT_MODIFY, arguments));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   deleteEntry
  /**
   ** Deletes object of the passed relative distinguished name.
   ** <br>
   ** Calls the context.destroySubcontext() to delete object
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context must be the superior context of the entry to delete.
   **
   ** @param  objectRDN          the relative distinguished name of the object,
   **                            e.g. <code>'cn=george'</code>
   ** @param  deleteControl      ...
   **
   ** @throws FeatureException   if the operation fails
   */
  protected void deleteEntry(final String objectRDN, final Control[] deleteControl)
    throws FeatureException {

    final String[] arguments = {contextName(), objectRDN};
    info(FeatureResourceBundle.format(FeatureMessage.OBJECT_DELETE_BEGIN, arguments));
    try {
      if (!existsEntry(objectRDN))
        error(FeatureResourceBundle.format(FeatureError.OBJECT_NOT_EXISTS, arguments));
      else {
        if (deleteControl != null)
          this.facade.setRequestControls(deleteControl);

        // delete object
        this.facade.destroySubcontext(objectRDN);
        warning(FeatureResourceBundle.format(FeatureMessage.OBJECT_DELETE_SUCCESS, arguments));
      }
    }
    catch (OperationNotSupportedException e) {
      error(FeatureResourceBundle.format(FeatureError.NOT_SUPPORTED, arguments));
      error(e.getLocalizedMessage());
    }
    catch (NamingException e) {
      error(FeatureResourceBundle.format(FeatureError.OBJECT_DELETE, arguments));
      error(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   existsEntry
  /**
   ** Determines the existance of an entry in the named context leveraging the
   ** given name.
   ** <br>
   ** Performs the search in the one-level.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context acts as the search base.
   **
   ** @param  objectRDN          the relative distinguished name of the object,
   **                            e.g. <code>'cn=george'</code>.
   **
   ** @return                    <code>true</code> if the given relative
   **                            distinguished name exists; otherwise
   **                            <code>false</code>.
   **
   ** @throws FeatureException   if the operation fails
   */
  protected boolean existsEntry(final String objectRDN)
    throws FeatureException {

    final List<SearchResult> result = searchEntry(objectRDN);
    if (result.size() > 1) {
      final String[] arguments = {contextName(), objectRDN};
      throw new FeatureException(FeatureError.OBJECT_AMBIGUOUS, arguments);
    }
    return result.size() == 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupEntry
  /**
   ** Returns the attributes of an entry in the named context leveraging the
   ** given name.
   ** <br>
   ** Performs the search in the one-level.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context acts as the search base.
   **
   ** @param  objectRDN          the relative distinguished name of the object,
   **                            e.g. <code>'cn=george'</code>
   **
   ** @return                    the {@link Attributes} for the given relative
   **                            distinguished name.
   **
   ** @throws FeatureException   if the operation fails
   */
  protected Attributes lookupEntry(final String objectRDN)
    throws FeatureException {

    final List<SearchResult> result = searchEntry(objectRDN);
    if (result.size() > 1) {
      final String[] arguments = {contextName(), objectRDN};
      throw new FeatureException(FeatureError.OBJECT_AMBIGUOUS, arguments);
    }
    else if (result.size() == 0) {
      final String[] arguments = {contextName(), objectRDN};
      throw new FeatureException(FeatureError.OBJECT_NOT_EXISTS, arguments);
    }
    return result.get(0).getAttributes();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchEntry
  /**
   ** Searches in the named context for the entry with the given name.
   ** <br>
   ** Performs the search in the one-level.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** The connected context acts as the search base.
   **
   ** @param  objectRDN          the relative distinguished name of the object,
   **                            e.g. <code>'cn=george'</code>
   **
   ** @return                    the {@link List} of {@link SearchResult}s for
   **                            the given relative distinguished name.
   **
   ** @throws FeatureException   if the operation fails
   */
  protected List<SearchResult> searchEntry(final String objectRDN)
    throws FeatureException {

    final SearchControls controls = new SearchControls(SearchControls.ONELEVEL_SCOPE, 0, 0, null, false, false);
    // seems to be we are ok; transform the SearchResult contained in the
    // NamingEnumeration in a list of strings to return this list to the caller
    final List<SearchResult> result   = new ArrayList<SearchResult>();
    // The NamingEnumeration that results from context.search() using
    // SearchControls contains elements of objects from the subtree (including
    // the named context) that satisfy the search filter specified in
    // context.search().
    // The names of elements in the NamingEnumeration are either relative to the
    // named context or is a URL string.
    // If the named context satisfies the search filter, it is included in the
    // enumeration with the empty string as its name.
    final NamingEnumeration<SearchResult> names = search(SystemConstant.EMPTY, objectRDN, controls);
    while(names.hasMoreElements())
      result.add(names.nextElement());
    contextClose(names);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals
  /**
   ** Equality function that properly handles {@link Attribute}s.
   **
   ** @param  a1                 the first {@link Attribute}.
   **                            May be <code>null</code>.
   ** @param  a2                 the second {@link Attribute}.
   **                            May be <code>null</code>.
   **
   ** @return                    <code>true</code> if the two objects are equal.
   */
  private boolean equal(final Attribute a1, final Attribute a2)
    throws FeatureException {

    //same object or both null
    if (a1 == a2)
      return true;
    else if (a1 == null)
      return false;
    else if (a2 == null)
      return false;
    if (a1.size() != a2.size())
      return false;
    else
      try {
        final List l1 = CollectionUtility.list(a1.getAll());
        final List l2 = CollectionUtility.list(a2.getAll());
        return CollectionUtility.haveSameElements(l1, l2);
      }
      catch (NamingException e) {
        throw new FeatureException(FeatureError.UNHANDLED, e);
      }
  }
}