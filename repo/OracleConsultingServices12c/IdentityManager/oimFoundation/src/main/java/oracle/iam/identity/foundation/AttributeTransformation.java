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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   AttributeTransformation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeTransformation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.HashMap;

import oracle.hst.foundation.SystemMessage;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AttributeTransformation
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>AttributeTransformation</code> is intend to use where inbound
 ** attributes of an Oracle Identity Manager Object (core or user defined) are
 ** needs transformation to the outbound provisioning or reconciliation target.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class AttributeTransformation extends AttributeMapping {

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Transformer
  private class Transformer {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    // the instance of an {@link AbstractAttributeTransformer}
    private AttributeTransformer instance;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    private Transformer(final AttributeTransformer instance) {
      this.instance  = instance;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   transform (AttributeTransformer)
    /**
     ** Returns the specified <code>origin</code> as an appropriate
     ** transformation.
     ** <br>
     ** The {@link Map} <code>origin</code> contains all untouched values. The
     ** {@link Map} <code>subject</code> contains all transformed values
     **
     ** @param  attributeName      the specific attribute in the {@link Map}
     **                            <code>origin</code> that has to be transformed.
     ** @param  origin             the {@link Map} to transform.
     ** @param  subject            the transformation of the specified
     **                            {@link Map} <code>origin</code>.
     */
    public void transform(final String attributeName, final Map<String, Object> origin, final Map<String, Object> subject) {
      this.instance.transform(attributeName, origin, subject);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method:   toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character "\n" (line feed).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                  the string representation of this instance.
     */
    public String toString() {
      return this.instance.getClass().getName();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributeTransformation</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   */
  public AttributeTransformation(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeTransformation</code> which is associated
   ** with the specified logging provider <code>loggable</code> and belongs to
   ** the Lookup Definition specified by the given name.
   ** <br>
   ** The Lookup Definition will be populated from the repository of the Oracle
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  instanceName       the name of the Lookup Definition instance
   **                            where this wrapper belongs to.
   **
   ** @throws TaskException      if the Metadata Descriptor is not defined in the
   **                            Oracle Identity manager metadata entries or one
   **                            or more attributes are missing on the
   **                            Metadata Descriptor.
   */
  public AttributeTransformation(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   populateAttributes (overridden)
  /**
   ** Obtains the Lookup Definition definition from Oracle Identity Manager.
   **
   ** @param  instanceName       the name of the Oracle Identity Manager
   **                            Lookup Definition where this wrapper belongs
   **                            to.
   **
   ** @throws TaskException      in case of
   **                            <ul>
   **                              <li>the Lookup Definition is not defined in
   **                                  the Oracle Identity Manager meta data
   **                                  entries with the name passed to the
   **                                  constructor of this wrapper
   **                              <li>more than one instance was found for the
   **                                  name; seems to that data corruption has
   **                                  been occurred;
   **                              <li>a generell error has occurred.
   **                            </ul> or a generell error has occurred.
   */
  // TODO: Remove it's stable
  @Override
  public void populateAttributes(final String instanceName)
    throws TaskException {

    // ensure inheritance
    super.populateAttributes(instanceName);

    final String method = "populateAttributes";
    trace(method, SystemMessage.METHOD_ENTRY);

    // instanciate all transformer
    for (String attributeName : this.attribute.keySet()) {
      final String className = (String)this.attribute.get(attributeName);
      try {
        put(attributeName, className);
      }
      catch (RuntimeException e) {
        error(method, e.getLocalizedMessage());
        // to avoid runtime exception we remove the failed instance
        this.attribute.remove(attributeName);
      }
    }
    trace(method, SystemMessage.METHOD_EXIT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   put (overridden)
  /**
   ** Obtains the Lookup Definition definition from Oracle Identity Manager.
   **
   ** @param  attributeName      the name of the attribute where
   **                            <code>transformer</code> has to be applied on.
   ** @param  value              the name of the class implementing the
   **                            transformation.
   **
   ** @throws RuntimeException   in case of the transformer class cannot be
   **                            instantiated.
   */
  @Override
  public Object put(final String attributeName, final Object value) {

    final String className = (String)value;
    try {
      // a little bit reflection
      @SuppressWarnings("unchecked")
      final Class<Transformer>       transformer = (Class<Transformer>)Class.forName(className);
      final Object[]                 parameter   = { this.logger};
      final Class[]                  types       = { Logger.class };
      final Constructor<Transformer> constructor = transformer.getConstructor(types);
      return super.put(attributeName, new Transformer((AttributeTransformer)constructor.newInstance(parameter)));
    }
    catch (ClassNotFoundException e) {
      throw new RuntimeException(TaskBundle.format(TaskError.CLASSNOTFOUND, className));
    }
    catch (NoSuchMethodException e) {
      throw new RuntimeException(TaskBundle.format(TaskError.CLASSNOTFOUND, className));
    }
    catch (InstantiationException e) {
      throw new RuntimeException(TaskBundle.format(TaskError.CLASSNOTCREATE, className));
    }
    catch (InvocationTargetException e) {
      throw new RuntimeException(TaskBundle.format(TaskError.CLASSNOTCREATE, className));
    }
    catch (IllegalAccessException e) {
      throw new RuntimeException(TaskBundle.format(TaskError.CLASSNOACCESS, className));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transform
  /**
   ** Returns the {@link Map} which contains the attributes after the
   ** transformation has take place.
   ** <br>
   ** The attributes obtained from the given {@link Map} are filtered by the
   ** <code>Encode</code> column of the Oracle Identity Manager Lookup
   ** Definition used by this instance.
   ** <p>
   ** There is no need to maintain the predictable iteration order provided by
   ** the source mapping if the source mapping implementation is a
   ** <code>LinkedHashMap</code>.
   **
   ** @param  subject            the complete collection of attribute values
   **                            gathered from a data source.
   **
   ** @return                    the transformed attribute mapping.
   */
  public Map<String, Object> transform(final Map<String, Object> subject) {
    // create a new mapping which is big enough to hold each value
    final Map<String, Object> result = new HashMap<String, Object>(subject);
    // create a new mapping which is big enough to hold each value
    for(String attributeName : this.attribute.keySet()) {
      final Transformer transformer = (Transformer)this.attribute.get(attributeName);
      transformer.transform(attributeName, subject, result);
    }
    return result;
  }
}