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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Foundation Shared Library

    File        :   OperationContext.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    OperationContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.foundation;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import oracle.iam.identity.icf.foundation.utility.StringUtility;
import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.OperationOptions;

////////////////////////////////////////////////////////////////////////////////
// final class OperationContext
// ~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Utility class, which helps to configure {@link OperationOptions} instances.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public final class OperationContext {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the size of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_SIZE         = "batchSize";
  /**
   ** The option a connector service consumer will put in the operation options
   ** of a search operation to configure the start index of a batch of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String BATCH_START        = "batchStart";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search base(s) of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_BASE        = "searchBase";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search filter of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_FILTER      = "searchFilter";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the search order of resources
   ** returned from a <code>Service Provider</code>.
   */
  public static final String SEARCH_ORDER       = "searchOrder";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to obtain entries from the Service Provider incrementally means based
   ** on a synchronization token like <code>changeNumber</code> of timestamps.
   */
  public static final String INCREMENTAL        = "incremental";
  /**
   ** The name a connector service consumer will put in the operation options
   ** of a reconciliation process to configure the synchronization strategy of
   ** resources returned from a <code>Service Provider</code>.
   */
  public static final String SYNCHRONIZE        = "synchronization";
  /**
   ** The value a connector service consumer will put in the operation options
   ** to specifiy the value of a synchronization token.
   */
  public static final String SYNCHRONIZE_TOKEN  = "synchronizationToken";

  /**
   ** The default start inedx of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int           BATCH_START_DEFAULT = 1;
  /**
   ** The default size limit of a search result obtained from a
   ** <code>Service Provier</code>.
   ** <br>
   ** This value is aligned with the default limits of Identity Manager.
   */
  static final int           BATCH_SIZE_DEFAULT  = 500;

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  protected static Date      zero               = null;

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.MILLISECOND, 0);
    calendar.set(1900,0,1,0,0,0);
    // should be result in a string of 19000101000000.0Z
    zero = calendar.getTime();
  };

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // don't worry about the access modifier 'puplic' all attributes are final
  // hence not modifiable
  public final int             start;
  public final int             limit;
  public final String          scope;
  public final String          filter;
  public final Set<String>     emit;
  public final Set<String>     order;
  public final QualifiedUid    base;

  public final boolean         incremental;
  public final Synchronization synchronization;
  public final String          synchronizationToken;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Synchronization
  /**
   ** <code>Synchronization</code> classifies the type the synchronization mode
   ** with a Service Provider.
   */
  public static enum Synchronization {
      /** the synchronization mode for <code>cookie</code> based change log */
      COOKIE("cookie")
      /** the synchronization mode for <code>timestamp</code> filters */
   ,  RESOURCE("resource")
      /** the synchronization mode for <code>standard</code> based change log */
   ,  CHANGELOG("changeLog")
   ;

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the lower case string value for this filter type. */
    private final String value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructor for <code>Synchronization</code> with a constraint value.
     **
     ** @param  value            the synchronization mode of the object.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    Synchronization(final String value) {
      this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Synchronization</code> constraint.
     **
     ** @return                  the value of the <code>Synchronization</code>
     **                          constraint.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: from
    /**
     ** Factory method to create a proper <code>Synchronization</code>
     ** constraint from the given string value.
     **
     ** @param  value            the string value the type constraint should be
     **                          returned for.
     **                          <br>
     **                          Allowed object is {@link String}.
     **
     ** @return                  the <code>Synchronization</code> constraint.
     **                          <br>
     **                          Possible object is
     **                          <code>Synchronization</code>.
     */
    public static Synchronization from(final String value) {
      for (Synchronization cursor : Synchronization.values()) {
        if (cursor.value.equals(value))
          return cursor;
      }
      throw new IllegalArgumentException(value);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new operation control configurator instance which populates
   ** its properties from the specified {@link OperationOptions}.
   **
   ** @param  option             the options that affect the way an operation is
   **                            run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   */
  private OperationContext(final OperationOptions option) {
    // ensure inheritance
    super();

    // initialize intsnace attributes
    this.scope = option.getScope();
    this.base  = (option.getContainer() != null) ? option.getContainer() : null;
    this.emit  = CollectionUtility.set(option.getAttributesToGet());

    final Map<String, Object> control = option.getOptions();
    this.order                = (control.containsKey(SEARCH_ORDER))      ? CollectionUtility.set((String[])control.get(SEARCH_ORDER)) : null;
    this.filter               = (control.containsKey(SEARCH_FILTER))     ? (String)control.get(SEARCH_FILTER) : null;
    this.incremental          = (control.containsKey(INCREMENTAL))       ? (Boolean)control.get(INCREMENTAL) : Boolean.FALSE;
    this.synchronization      = (control.containsKey(SYNCHRONIZE))       ? Synchronization.from((String)control.get(SYNCHRONIZE)) : Synchronization.RESOURCE;
    this.synchronizationToken = (control.containsKey(SYNCHRONIZE_TOKEN)) ? (String)control.get(SYNCHRONIZE_TOKEN) : null;

    // take special care for the start index so that we later on have a valid
    // value always
    int num = BATCH_START_DEFAULT;
    Integer start  = (Integer)control.get(BATCH_START);
    if (start != null) {
      num = start.intValue();
      if (num < 1)
        // fallback to the default if violating
        num = BATCH_START_DEFAULT;
    }
    this.start = num;
    // take special care for the limit so that we later on have a valid value
    // always
    num = BATCH_SIZE_DEFAULT;
    Integer size  = (Integer)control.get(BATCH_SIZE);
    if (size != null) {
      num = size.intValue();
      if (num <= 0)
        // fallback to the default if violating
        num = BATCH_SIZE_DEFAULT;
    }
    this.limit = num;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a new &amp; initialized control configurator
   ** instance.
   **
   ** @param  option             the options that affect the way an operation is
   **                            run.
   **                            <br>
   **                            Allowed object is {@link OperationOptions}.
   **
   ** @return                    a new &amp; initialized control configurator
   **                            instance.
   **                            <br>
   **                            Possible object is
   **                            <code>OperationContext</code>.
   */
  public static OperationContext build(final OperationOptions option) {
    return new OperationContext(option);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   transformToDate
  /**
   ** Returns the specified <code>attributeValue</code> as an appropriate
   ** Date transformation.
   ** <p>
   ** <code>Service Provider</code> timestamp is in the form of
   ** "yyyyMMddHHmmss'Z'". The date and time specified in
   ** <code>Service Provider</code> timestamp is based on the Coordinated
   ** Universal Time (UTC). The date and time "Mon Jul 30 17:42:00 2001" is
   ** represented in <code>Service Provider</code> timestamp as
   ** "20010730174200'Z'".
   **
   ** @param  attributeValue     the specific attribute value that has to be
   **                            transformed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  pattern            the pattern describing the date and time
   **                            format.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the transformed {@link Date} value.
   **                            <br>
   **                            Possible object is {@link Date}.
   */
  public static Date transformToDate(final String attributeValue, final String pattern) {
    final SimpleDateFormat formatter = new SimpleDateFormat(pattern);

    Date timestamp = zero;
    // apply tranformation only if we don't have an empty value
    if (!StringUtility.empty(attributeValue)) {
      try {
        timestamp = formatter.parse(attributeValue);
      }
      catch (ParseException e) {
        ;
      }
    }
    return timestamp;
  }
}