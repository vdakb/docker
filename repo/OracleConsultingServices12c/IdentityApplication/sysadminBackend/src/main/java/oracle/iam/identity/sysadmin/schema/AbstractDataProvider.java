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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   System Administration Management

    File        :   AbstractDataProvider.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    AbstractDataProvider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    2.0.0.0     2017-03-06  DSteding    First release version
*/

package oracle.iam.identity.sysadmin.schema;

import java.util.Locale;
import java.util.Hashtable;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import javax.naming.Context;

import oracle.javatools.resourcebundle.BundleFactory;

import oracle.adf.share.ADFContext;

import oracle.iam.platform.OIMClient;

import oracle.iam.platform.context.ContextManager;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

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
 **
 ** @author  Dieter.Steding@oracle.com
 ** @version 2.0.0.0
 ** @since   2.0.0.0
 */
public abstract class AbstractDataProvider<T extends ModelAdapterBean> extends oracle.iam.ui.platform.model.common.AbstractDataProvider<T> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String MISSING_RESOURCE = "???%s???";
  private static final String BUNDLE           = "oracle.iam.identity.sysadmin.bundle.Backend";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:3420486421727608428")
  private static final long   serialVersionUID = -4773893541804649804L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AbstractDataProvider</code> data access object
   ** that allows use as a JavaBean.
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
  // Method:   resourceBackendValue
  /**
   ** Pulls a String resource from the adminstration resource bundle with
   ** respect to the current <code>Locale</code>.
   **
   ** @param  key                string message key.
   ** @param  args               the arguments referenced by the format
   **                            specifiers in the format string given by
   **                            <code>key</code>. If there are more arguments
   **                            than format specifiers, the extra arguments are
   **                            ignored. The number of arguments is variable
   **                            and may be zero. The maximum number of
   **                            arguments is limited by the maximum dimension
   **                            of a Java array as defined by
   **                            <cite>The Java&trade; Virtual Machine Specification</cite>.
   **                            The behaviour on a <code>null</code> argument
   **                            depends on the
   **                            <a href="../util/Formatter.html#syntax">conversion</a>.
   **
   ** @return                    resource choice or placeholder error String.
   */
  public static String resourceBackendValue(final String key, final Object... args) {
    return String.format(resourceBackendValue(key), args);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBackendValue
  /**
   ** Pulls a String resource from the adminstration resource bundle with
   ** respect to the current <code>Locale</code>.
   **
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String.
   */
  public static String resourceBackendValue(final String key) {
    return resourceBundleValue(BUNDLE, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Backend" or {PACKAGE_NAME} + "Backend".
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key) {
    return stringSafely(resourceBundle(clazzName), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleKey
  public static String resourceBundleKey(final String value, final String prefix) {
    String bundleKey = "";
    if (value != null) {
      bundleKey = prefix + "." + value.toLowerCase().replace(" ", ".");
    }
    return bundleKey;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   ** <br>
   ** The resulting string will be obtained from the resource bundle for the
   ** current Locale.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Backend" or {PACKAGE_NAME} + "Backend".
   ** @param  key                string message key.
   ** @param  defaultValue       the default value which will be returned if the
   **                            given <code>key</code> is not mapped in the
   **                            resuorce bundle.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key, final String defaultValue) {
    final ResourceBundle bundle = ResourceBundle.getBundle(clazzName, ContextManager.getResolvedLocale());
    String value = bundle.getString(key);
    if (value == null || value.trim().length() == 0) {
            value = defaultValue;
    }
    return value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the specified resource bundle.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Backend" or {PACKAGE_NAME} + "Backend".
   ** @param  key                string message key.
   ** @param  locale             the locale to look for.
   **
   ** @return                    resource choice or placeholder error String
   */
  public static String resourceBundleValue(final String clazzName, final String key, Locale locale) {
    return stringSafely(resourceBundle(clazzName, locale), key, null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Method for fetching a {@link ResourceBundle}.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Backend" or {PACKAGE_NAME} + "Backend".
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle resourceBundle(final String clazzName) {
    return resourceBundle(clazzName, locale());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundle
  /**
   ** Method for fetching a {@link ResourceBundle}.
   **
   ** @param  clazzName          the bundleId to use.
   **                            This will only be significant if the project is
   **                            set for one bundle per artifact. The bundle ID
   **                            is up to the client, however is generally
   **                            either {PACKAGE_NAME} + {OBJECT_NAME} +
   **                            "Backend" or {PACKAGE_NAME} + "Backend".
   ** @param locale              the locale to look for.
   **
   ** @return                    the {@link ResourceBundle}.
   */
  public static ResourceBundle resourceBundle(final String clazzName, final Locale locale) {
    // load the resource bundle
    return BundleFactory.getBundle(clazzName, locale);
  }

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

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   queryString
  /**
   ** Return the query literal of the second argument of the
   ** {@link SearchCriteria} thta match the specified <code>name</code> (aka
   ** view attribute).
   **
   ** @param  criteria           the OIM search criteria to invastigate.
   ** @param  name               the name (aka view attribute) to lookup.
   **
   ** @return                    the query string of the second argument of the
   **                            specified {@link SearchCriteria} that match the
   **                            specified name.
   */
  public static String queryString(final SearchCriteria criteria, final String name) {
    // this code is based on how the OIMProgramaticVO assembles a series of
    // SearchCriteria object using the ADF Query data as it's input
    final SearchCriteria filter = findCriteria(criteria, name);
    return (filter == null) ? null : queryString(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   queryString
  /**
   ** Return the query literal of the second argument of the
   ** {@link SearchCriteria}.
   **
   ** @param  criteria           the OIM search criteria to invastigate.
   **
   ** @return                    the query string of the second argument of the
   **                            specified {@link SearchCriteria}.
   */
  public static String queryString(final SearchCriteria criteria) {
    final SearchCriteria.Operator operator = criteria.getOperator();
    switch (operator) {
      case CONTAINS    : return String.format("*%s*", criteria.getSecondArgument());
      case BEGINS_WITH : return String.format("%s*",  criteria.getSecondArgument());
      case ENDS_WITH   : return String.format("*%s",  criteria.getSecondArgument());
    }
    return (String)criteria.getSecondArgument();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   findCriteria
  /**
   ** Return the search criteria list of backend objects based on the given
   ** filter criteria.
   **
   ** @param  criteria           the OIM search criteria to invastigate.
   ** @param  name               the name (aka view attribute) to lookup.
   **
   ** @return                    the {@link SearchCriteria} that match the
   **                            specified name.
   */
  public static SearchCriteria findCriteria(final SearchCriteria criteria, final String name) {
    // this code is based on how the OIMProgramaticVO assembles a series of
    // SearchCriteria object using the ADF Query data as it's input

    // start by checking the second argument.
    // this will be either:
    //   * SearchCriteria, with no child criteria
    //   * if not a SearchCriteria, then the end of the criteria list has been
    //     reached
    // if the second argument does not contain what we are looking for then
    // traverse down the first argument. the first arg will be either:
    //   * SearchCriteria, with or without child criteria
    if (criteria != null) {
      final Object second = criteria.getSecondArgument();
      if (second instanceof SearchCriteria) {
        final SearchCriteria secondCriteria = (SearchCriteria)second;
        final String firstParam = (String)secondCriteria.getFirstArgument();
        if (firstParam.equalsIgnoreCase(name)) {
          // Match found, returning
          return secondCriteria;
        }
      }
      // if we get here then the secondArg did not contain what we are looking
      // for see if we are at the end of the chain
      final Object first = criteria.getFirstArgument();
      if (first instanceof String) {
        final String value = String.valueOf(first);
        if (value.equalsIgnoreCase(name))
          return criteria;
      }
      else if (first instanceof SearchCriteria) {
        return findCriteria((SearchCriteria)first, name);
      }
    }
    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the adminstration resource bundle with
   ** respect to the current <code>Locale</code>.
   **
   ** @param  key                string message key.
   ** @param  args               the arguments referenced by the format
   **                            specifiers in the format string given by
   **                            <code>key</code>. If there are more arguments
   **                            than format specifiers, the extra arguments are
   **                            ignored. The number of arguments is variable
   **                            and may be zero. The maximum number of
   **                            arguments is limited by the maximum dimension
   **                            of a Java array as defined by
   **                            <cite>The Java&trade; Virtual Machine Specification</cite>.
   **                            The behaviour on a <code>null</code> argument
   **                            depends on the
   **                            <a href="../util/Formatter.html#syntax">conversion</a>.
   **
   ** @return                    resource choice or placeholder error String.
   */
  public static String resourceBundleValue(final String key, final Object... args) {
    return String.format(resourceBundleValue(key), args);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   resourceBundleValue
  /**
   ** Pulls a String resource from the adminstration resource bundle with
   ** respect to the current <code>Locale</code>.
   **
   ** @param  key                string message key.
   **
   ** @return                    resource choice or placeholder error String.
   */
  public static String resourceBundleValue(final String key) {
    return resourceBundleValue(BUNDLE, key);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   stringSafely
  /**
   ** Internal method to proxy for resource keys that don't exist.
   **
   ** @param  bundle             the {@link ResourceBundle} the desired resource
   **                            is provided by.
   ** @param  key                string message key.
   ** @param  defaultValue       placeholder string if the key isn't mapped in
   **                            the resource bundle.
   **
   ** @return                    resource choice or placeholder error String
   */
  private static String stringSafely(final ResourceBundle bundle, final String key, final String defaultValue) {
    String resource = null;
    try {
      resource = bundle.getString(key);
    }
    catch (MissingResourceException e) {
      resource = (defaultValue != null) ? defaultValue : String.format(MISSING_RESOURCE, key);
    }
    return resource;
  }
}