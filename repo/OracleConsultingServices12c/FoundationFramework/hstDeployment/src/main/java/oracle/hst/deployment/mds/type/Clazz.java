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

    System      :   Foundation Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   Clazz.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Clazz.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.1.0.0     2014-11-29  DSteding    First release version
*/

package oracle.hst.deployment.mds.type;

import java.lang.reflect.Constructor;

import org.apache.tools.ant.BuildException;

import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.Reference;

import oracle.mds.cust.CustomizationClass;

import oracle.hst.foundation.utility.StringUtility;

import oracle.hst.deployment.ServiceError;
import oracle.hst.deployment.ServiceResourceBundle;

import oracle.hst.deployment.spi.AbstractInstance;

////////////////////////////////////////////////////////////////////////////////
// class Clazz
// ~~~~~ ~~~~~
/**
 ** The environment wrapper of a specific customization class in a Oracle
 ** Metadata Store.
 ** <p>
 ** A Customization Class (CC) evaluates the current context and the current
 ** metadata object and returns a String result to be used in customizations.
 ** <p>
 ** A customization class has a name, as returned by the getName() method, and a
 ** value, as returned by the getValue(RestrictedSession, MetadataObject)
 ** method. The name is combined with the value to select a customization layer
 ** to apply. For example, a UserCustomizationClass may have the name "user" and
 ** getValue() returns the current user to facilitate user customizations.
 ** <br>
 ** Depending on the implementation, the getValue() method could return a "user"
 ** property from the context, the current database username, a user stored on
 ** the servlet session, or the OS user etc.
 ** <p>
 ** The Customization Class also defines a CacheHint which indicates to MDS how
 ** widely visible a metadata object with this customization will be and hence
 ** its likely duration in memory. This information may be used internally by
 ** MDS to decide whether to cache a customization and where to cache it, and
 ** any Customization Layers constructed using this CC will have this cache
 ** hint.
 ** <p>
 ** Implementations should provide at most a single, parameter-less constructor.
 ** Implementations must not attempt to recursively load other MOs.
 ** Implementations must be thread-safe
 ** Implementations should be as efficient as possible as each registered
 ** Customization Class is re-evaluated for every call to get a MetadataObject.
 ** <p>
 ** The Customization Class also has a method called getIDPrefix. Components
 ** those want to generate unique id, for newly added elements as
 ** customizations, across customization layers, can use this method to get
 ** id-prefix based on the current tip customization layer name/value pair. When
 ** a new element is added as customization for a layer name/value pair,
 ** components can prefix this generated id-prefix to their actual auto
 ** generated id to make sure that their id is unique across customization
 ** layers. Please read getIDPrefix for more details.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.1.0.0
 ** @since   1.1.0.0
 */
public class Clazz extends DataType {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private CustomizationClass delegate = null;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Clazz</code> type that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Clazz() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setRefid
  /**
   ** Call by the ANT kernel to inject the argument for parameter refid.
   ** <p>
   ** Makes this instance in effect a reference to another
   ** <code>MDSNamespacePathList</code> instance.
   ** <p>
   ** You must not set another attribute or nest elements inside this element
   ** if you make it a reference.
   **
   ** @param  reference          the id of this instance.
   **
   ** @throws BuildException     if any other instance attribute is already set.
   */
  @Override
  public void setRefid(final Reference reference)
    throws BuildException {

    Object other = reference.getReferencedObject(getProject());
    if(other instanceof Clazz) {
      this.delegate = ((Clazz)other).delegate;
      // ensure inheritance
      super.setRefid(reference);
    }
    else {
      final Object[] parameter = {reference.getRefId(), "class", reference.getRefId(), other.getClass().getName() };
      throw new BuildException(ServiceResourceBundle.format(ServiceError.TYPE_REFERENCE_MISMATCH, parameter), getLocation());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setName
  /**
   ** Call by the ANT deployment to inject the argument for parameter
   ** <code>clazz</code>.
   **
   ** @param  name               the name of the class.
   */
  public void setName(final String name)
    throws BuildException {

    if (StringUtility.isEmpty(name))
      AbstractInstance.handleAttributeMandatory("name");

    this.delegate = create(name, CustomizationClass.class);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   delegate
  /**
   ** Returns the folder where the import of this set has to be written
   ** to.
   **
   ** @return                    the folder where the import of this set
   **                            has to be written to.
   */
  public final CustomizationClass delegate() {
    return this.delegate;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   validate
  /**
   ** The entry point to validate the type to use.
   **
   ** @throws BuildException     in case the instance does not meet the
   **                            requirements.
   */
  public void validate()
    throws BuildException {

    if (this.delegate == null)
      AbstractInstance.handleAttributeMissing("class");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** Creates an object of the class name and supertype.
   **
   ** @param  <T>                the class type of the instance to create.
   ** @param  name               the class name of the instance to create.
   ** @param  superType          the type of the super class of the instance to
   **                            create.
   **
   ** @return                    the created instance.
   **
   ** @throws BuildException     if the class couldnot be instantiated.
   */
  public static <T> T create(final String name, final Class<T> superType) {
    try {
      final Class<? extends T> clazz       = Class.forName(name).asSubclass(superType);
      final Constructor<T>     constructor = (Constructor<T>)clazz.getDeclaredConstructor();
      if (constructor == null)
        throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSCONSTRUCTORNOARG, clazz.getName()));

      // if private, make it accessible
      if (!constructor.isAccessible()) {
        constructor.setAccessible(true);
      }
      return constructor.newInstance();
    }
    catch (ClassNotFoundException e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSNOTFOUND, name));
    }
    catch (NoSuchMethodException e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSCONSTRUCTOR, name));
    }
    catch (Exception e) {
      throw new BuildException(ServiceResourceBundle.format(ServiceError.CLASSNOTCREATE, name));
    }
  }
}