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

    Copyright © 2021 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Password Reset

    File        :   AbstractDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2021-13-09  DSteding    First release version
*/

package bka.iam.identity.password.schema;

import java.util.Locale;
import java.util.Hashtable;

import javax.naming.Context;

import oracle.adf.share.ADFContext;

import oracle.iam.platform.OIMClient;

import oracle.iam.ui.platform.utils.SystemUtils;

import oracle.iam.ui.platform.model.common.ModelAdapterBean;

import oracle.iam.ui.platform.model.config.ConstantsDefinition;

////////////////////////////////////////////////////////////////////////////////
// abstract class AbstractDataProvider
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** Data Provider Object used by any customization.
 ** <p>
 ** Define an instance variable for each VO attribute, with the data type that
 ** corresponds to the VO attribute type. The name of the instance variable must
 ** match the name of the VO attribute, and the case must match.
 ** <p>
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class AbstractDataProvider<T extends ModelAdapterBean> extends oracle.iam.ui.platform.model.common.AbstractDataProvider<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractDataProvider</code> data access
   ** object that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AbstractDataProvider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   create (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object creation.
   **
   ** @param  mab                the model adapter bean, with attributes set.
   */
  @Override
  public void create(final T mab) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   update (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object update.
   **
   ** @param  mab                the model adapter bean, with attributes set.
   */
  @Override
  public void update(final T mab) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   delete (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** B-Object delete.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public void delete(final T mab) {
    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   exists (DataProvider)
  /**
   ** Implement this method in the data provider to provide an implementation
   ** to check if the B-Object exists
   **
   ** @param  mab                the model adapter bean, with key fields set.
   */
  @Override
  public boolean exists(final T mab) {
    // intentionally left blank
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   lookup (DataProvider)
  /**
   ** Return a specific backend object identified by the name.
   ** <p>
   ** The ModelAdapterBean argument implementation class must correspond to the
   ** DataProvider implementation. In other words, this method implemented in
   ** AttributeDataProvider expects a AttributeAdapterBean as an argument.
   ** <br>
   ** If the DataProvider and ModelAdapterBean types do not match, and exception
   ** will be thrown.
   ** <p>
   ** The ModelAdapterBean must has its "key" fields set. All other fields are
   ** ignored. In most cases, the field field is simply the name.
   **
   ** @param  mab                the model adapter bean, with key fields set.
   **
   ** @return                    object identified by the given name.
   */
  @Override
  public T lookup(final T mab) {
    // intentionally left blank
    return mab;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   locale
  /**
   ** Returns a locale for the ADF context of the current thread.
   ** <p>
   ** This locale will only be returned if an environment has not been defined
   ** for the {@link ADFContext}. The environment may not be defined if the
   ** {@link ADFContext} is acquired from a java application.
   **
   ** @return                    the current locale.
   */
  public static Locale locale() {
    return current().getLocale();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   current
  /**
   ** Returns the ADF context for the current thread.
   ** <p>
   ** <code>current</code> will automatically initialize a
   ** <code>DefaultContext</code> if one does not exist. This may result in
   ** memory leaks if the calling thread is reused before
   ** #resetADFContext(Object) is invoked.
   ** <p>
   ** An improper {@link ADFContext} is initialized if that context
   ** implementation does not match the environment that is using it. For
   ** example, if a caller is executing in a web environment than the proper
   ** {@link ADFContext} implementation would be <code>ServletADFContext</code>.
   ** The <code>DefaultContext</code> implementation would be improper for use
   ** in the web environment. The proper context may be initialized by
   ** invoking <code>initADFContext</code> with the required external context
   ** (the <code>ServletContext</code>, etc. when used in a web environment).
   ** <p>
   ** The use of an improper {@link ADFContext} could result in memory leaks if
   ** the {@link ADFContext} is not properly reset when the calling thread is
   ** done using it. An improper use could also lead to unexpected application
   ** behaviour if an {@link ADFContext} client initializes an
   ** {@link ADFContext} state that is required by another thread which is using
   ** the proper {@link ADFContext}. Using <code>initADFContext</code> with
   ** <code>findCurrent</code> can help diagnose/avoid these potential issues.
   **
   ** @return                    the ADF context for the current thread.
   */
  public static ADFContext current() {
    return ADFContext.getCurrent();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   service
  /**
   ** Returns an instance of a Business Facade by invoking the method platform
   ** service resolver to return the appropriate instance of the desired
   ** Business Facade.
   ** <br>
   ** The utility factory keeps track of created Business Facade and on
   ** execution of close() will free all aquired resources of the created
   ** Business Facade.
   ** <br>
   ** This implementation will stop the execution if the Business Facade cannot
   ** be instantiated. An appropriate message will be logged in the Application
   ** Server Log in this case.
   **
   ** @param <T>                 the expected class type.
   ** @param  serviceClass       the class of the Business Facade to create.
   **                            Typically it will be of the sort:
   **                            <code>Thor.API.tcNameUtilityIntf.class</code>.
   **
   ** @return                    the Business Facade.
   **                            It needs not be cast to the requested Business
   **                            Facade.
   */
  public static <T> T service(final Class<T> serviceClass) {
    final Hashtable environment = new Hashtable();
    final String URL = SystemUtils.getProperty(ConstantsDefinition.PROVIDER_URL);
    if (URL != null) {
      environment.put(Context.PROVIDER_URL, URL);
    }
    final OIMClient client = new OIMClient(environment);
    return client.getService(serviceClass);
  }
}