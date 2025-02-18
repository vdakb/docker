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
    Subsystem   :   Connector Bundle Framework

    File        :   EntityReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    EntityReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.connector.service;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Hashtable;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.Serializable;

import javax.security.auth.login.LoginException;

import javax.xml.parsers.ParserConfigurationException;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.QualifiedUid;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ResultsHandler;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationOptions;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import oracle.mds.core.MDSSession;

import oracle.mds.naming.DocumentName;
import oracle.mds.naming.ReferenceException;

import oracle.mds.persistence.PManager;
import oracle.mds.persistence.PDocument;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcObjectOperationsIntf;
import Thor.API.Operations.tcReconciliationOperationsIntf;
import Thor.API.Operations.tcITResourceInstanceOperationsIntf;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcIDNotFoundException;
import Thor.API.Exceptions.tcDataNotProvidedException;
import Thor.API.Exceptions.tcMultipleMatchesFoundException;

import oracle.iam.platform.Platform;
import oracle.iam.platform.OIMInternalClient;

import oracle.iam.platform.context.ContextManager;
import oracle.iam.platform.context.ContextAwareString;
import oracle.iam.platform.context.ContextAwareNumber;

import oracle.iam.scheduler.api.SchedulerConstants;

import oracle.iam.reconciliation.api.InputData;
import oracle.iam.reconciliation.api.ChangeType;
import oracle.iam.reconciliation.api.FailedInputData;
import oracle.iam.reconciliation.api.BatchAttributes;
import oracle.iam.reconciliation.api.ReconciliationResult;
import oracle.iam.reconciliation.api.ReconOperationsService;

import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemThreadPool;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.TableFormatter;

import oracle.hst.foundation.utility.StringUtility;
import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AttributeTransformation;

import oracle.iam.identity.foundation.resource.TaskBundle;

import oracle.iam.identity.foundation.naming.ITResource;
import oracle.iam.identity.foundation.naming.ResourceObject;

import oracle.iam.identity.connector.integration.FrameworkError;
import oracle.iam.identity.connector.integration.FrameworkBundle;
import oracle.iam.identity.connector.integration.FrameworkMessage;

////////////////////////////////////////////////////////////////////////////////
// abstract class EntityReconciliation
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>EntityReconciliation</code> acts as the service end point for
 ** Identity Manager to reconcile metadata information from any
 ** Service Provider.
 ** <p>
 ** The following scheduled task parameters are expected to be defined:
 ** <ul>
 **   <li>IT Resource              - The IT Resource used to establish the
 **                                  connection to the target system
 **   <li>Reconciliation Object    - the name of the
 **                                  <code>Resource Object</code> to reconcile.
 **   <li>Batch Size               - Specifies the size of a block read from the
 **                                  reconciliation source.
 ** </ul>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public abstract class EntityReconciliation extends Reconciliation {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The value of an entity status if its reconciled as <code>Active</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is only applicable for identities like users,
   **              roles and organizations.
   */
  protected static final String       STATUS_ACTIVE     = "Active";

  /**
   ** The value of an entity status if its reconciled as <code>Active</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is only applicable for accounts.
   */
  protected static final String       STATUS_ENABLED    = "Enabled";

  /**
   ** The value of an entity status if its reconciled as <code>Inactive</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is applicable on any entity.
   */
  protected static final String       STATUS_DISABLED   = "Disabled";

  /**
   ** Attribute tag which can be defined on a scheduled task to advicepreventing
   ** creation and processing of reconciliation events for target system data
   ** that already exists in Identity Manager.
   ** <br>
   ** This attribute is optional and defaults to <code>true</code>.
   */
  protected static final String       IGNORE_DUBLICATES = "Ignore Dublicates";

  private static final String         internalFormat    = "yyyy/MM/dd HH:mm:ss z";

  private static final List<String>   supportedFormat   = CollectionUtility.list("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyyMMddHHmmss'Z'", "yyyyMMddHHmmss'z'", "yyyy-MM-dd");

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the mapping of attribute name and their transformation for reconciliation */
  protected Descriptor.Reconciliation descriptor;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Event
  // ~~~~~ ~~~~~
  /**
   ** <code>Event</code> is a helper class for mapping {@link ConnectorObject}s
   ** to Identity Manager friendly data structure (aka {@link Map}s).
   */
  public static class Event {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Map<String, Serializable>                    master   = new LinkedHashMap<String, Serializable>();
    private final Map<String, List<Map<String, Serializable>>> multiple = new LinkedHashMap<String, List<Map<String, Serializable>>>();

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>ReconcilationSubject</code> which obtains the data
     ** to reconcile from the specified {@link ConnectorObject}
     ** <code>subject</code> that is parsed with the attribute mapping of the
     ** {@link Descriptor} <code>descriptor</code>.
     ** <br>
     ** The attributes obtained from the given {@link ConnectorObject} are
     ** filtered by the <code>source</code> attribute of the Identity Manager
     ** Metadata Descriptor specified.
     **
     ** @param  endpoint         the <code>IT Resource</code> source of this
     **                          event belongs to.
     **                          <br>
     **                          Allowed object is {@link Long}.
     ** @param  subject          the complete collection of attribute values
     **                          gathered from a data source.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     ** @param  descriptor       the attribute mappping.
     **                          <br>
     **                          Allowed object is {@link Descriptor}.
     ** @param  authoritative    <code>true</code> if the event to build belongs
     **                          to an authoritative source.
     **                          <br>
     **                          The difference beteween authoritative and
     **                          non-authoritative reconcilaition belongs
     **                          mainly to the <code>IT Resource</code> which
     **                          needs to be put in the reconciled data if the
     **                          event to build is non-authoritative.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  strict           <code>true</code> if the result should
     **                          contain attribute mappings only for those
     **                          definitions that defined in the descriptor
     **                          but also contained in the provided
     **                          collection. If <code>false</code> if
     **                          specified than the result has a value for
     **                          each attribute name defined in the
     **                          descriptor.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    private Event(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor, final boolean authoritative, final boolean strict)  {
      // ensure inheritance
      super();

      // initialize instance attributes
      transform(subject, endpoint, descriptor, strict, authoritative);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: master
    /**
     ** Returns the attribut value mapping of the identity/account data to
     ** reconcile.
     **
     ** @return                  the attribut value mapping of the
     **                          identity/account data to reconcile.
     **                          <br>
     **                          Possible object {@link Map} where each element
     **                          is as of type {@link String} for the key and
     **                          {@link Object} for the value.
     */
    public Map<String, Serializable> master() {
      return this.master;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: multiple
    /**
     ** Returns the attribut value mapping of the account multi-valued data to
     ** reconcile.
     **
     ** @return                  the attribut value mapping of the
     **                          account multi-valued data to reconcile.
     **                          <br>
     **                          Possible object {@link Map} where each
     **                          element is as of type {@link String} for the
     **                          multi-valued target and {@link Object} for the
     **                          value.
     */
    public Map<String, List<Map<String, Serializable>>> multiple() {
      return this.multiple;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toString (overridden)
    /**
     ** Returns a string representation of this instance.
     ** <br>
     ** Adjacent elements are separated by the character " " (space).
     ** Elements are converted to strings as by String.valueOf(Object).
     **
     ** @return                  the string representation of this instance.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    @Override
    public String toString() {
      final StringBuilder builder = new StringBuilder();
      builder.append(this.master.toString());
      builder.append(this.multiple.toString());
      return builder.toString();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: transform
    /**
     ** Returns the {@link Map} which contains only the attributes registered by
     ** this mapping.
     ** <br>
     ** The attributes obtained from the given {@link ConnectorObject} are
     ** filtered by the <code>Code</code> column of the Identity Manager Metadata
     ** Descriptor used by this instance.
     ** <p>
     ** There is no need to maintain the predictable iteration order provided by
     ** the source mapping if the source mapping implementation is a
     ** <code>LinkedHashMap</code>.
     **
     ** @param  subject          the complete collection of attribute values
     **                          gathered from a data source.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     ** @param  endpoint         the <code>IT Resource</code> source of this
     **                          event belongs to.
     **                          <br>
     **                          Allowed object is {@link Long}.
     ** @param  descriptor       the attribute mapping descriptor.
     **                          <br>
     **                          Allowed object is {@link Descriptor}.
     ** @param  strict           <code>true</code> if the result should
     **                          contain attribute mappings only for those
     **                          definitions that defined in the descriptor
     **                          but also contained in the provided
     **                          collection. If <code>false</code> if
     **                          specified than the result has a value for
     **                          each attribute name defined in the
     **                          descriptor.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  authoritative    <code>true</code> if the event to build
     **                          belongs to an authoritative source.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    protected void transform(final ConnectorObject subject, final Long endpoint, final Descriptor descriptor, final boolean strict, final boolean authoritative) {
      // process every single-valued attribute
      final Set<Descriptor.Attribute> mapping = descriptor.attribute();
      // create a new mapping which is big enough to hold each value
      // there is no need to maintain the predictable iteration order provided
      // by the source mapping if the source mapping implementation is a
      // LinkedHashMap
      for (Descriptor.Attribute rule : mapping) {
        final Attribute attribute = subject.getAttributeByName(rule.source);
        // check if the provided attribute collection has a mapping for the
        // source attribute name of this mapping
        if (!strict && attribute == null)
          continue;
        // gets the attributes that the request is trying to reconcile from the
        // Target System. One field can drive more than one attributes but
        // necessarily not all the attributes mapped may not be reconciled.
        // Put all attributes in the mapping regardless if they are empty or
        // not.
        this.master.put(rule.id, interprete(rule, AttributeUtil.getSingleValue(attribute), authoritative));
      }
      final Logger logger = descriptor.logger();
      // evaluate any template attribute if required
      if (descriptor.template != null && descriptor.template.size() > 0) {
        apply(descriptor.template, this.master);
      }
      if (logger != null && logger.debugLevel()) {
        logger.debug(formatCollection(TaskMessage.ATTRIBUT_MAPPING, this.master));
      }
      // evaluate any transformation if required
      if (descriptor.transformation()) {
        apply(descriptor.transformer(), this.master);
        if (logger != null && logger.debugLevel()) {
          logger.debug(formatCollection(TaskMessage.ATTRIBUT_TRANSFORMATION, this.master));
        }
      }
      // only non-authoritive event can have embedded values that targeted to the
      // process child forms
      if (!authoritative) {
        this.master.put(Reconciliation.IT_RESOURCE, endpoint);
        transform(subject, endpoint, descriptor.reference);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: transform
    /**
     ** Creates the {@link Map} which contains only the multi-valued attributes
     ** registered by this mapping.
     ** <br>
     ** The attributes obtained from the given {@link ConnectorObject} are
     ** filtered by the <code>name</code> column of the Identity Manager Metadata
     ** Descriptor used by this instance.
     ** <p>
     ** There is no need to maintain the predictable iteration order provided by
     ** the source mapping if the source mapping implementation is a
     ** <code>LinkedHashMap</code>.
     **
     ** @param  subject          the complete collection of attribute values
     **                          gathered from a data source.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     ** @param  endpoint         the <code>IT Resource</code> source of this
     **                          event belongs to.
     **                          <br>
     **                          Allowed object is {@link Long}.
     ** @param  reference        the multi-valued attribute mapping descriptor.
     **                          <br>
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link Pair} for the key
     **                          and {@link Descriptor} as the value.
     */
    protected void transform(final ConnectorObject subject, final Long endpoint, final Map<Pair<String, String>, Descriptor> reference) {
      for (Map.Entry<Pair<String, String>, Descriptor> cursor : reference.entrySet()) {
        final List<Map<String, Serializable>> collector = new ArrayList<Map<String, Serializable>>();
        final Pair<String, String>            section   = cursor.getKey();
        this.multiple.put(section.tag, collector);
        // process every multi-valued attribute
        final Attribute                 attribute  = subject.getAttributeByName(section.value);
        final Descriptor                descriptor = cursor.getValue();
        final Logger                    logger     = descriptor.logger();
        final Set<Descriptor.Attribute> mapping    = descriptor.attribute();
        if (attribute != null) {
          for (Object row : attribute.getValue()) {
            final EmbeddedObject            embed  = (EmbeddedObject)row;
            final Map<String, Serializable> record = new LinkedHashMap<String, Serializable>();
            for (Descriptor.Attribute rule : mapping) {
              final Attribute value = embed.getAttributeByName(rule.source);
              Serializable latch = interprete(rule, AttributeUtil.getSingleValue(value), false);
              // apply the rule to build an entitlement if the descriptor
              // attribute is declared as
              if (rule.entitlement())
                latch = String.format(PATTREN_ENCODED_VALUE, endpoint, latch);
              // put all attributes in the mapping regardless if they are empty
              // or not.
              record.put(rule.id, latch);
            }
            // evaluate any template attribute if required
            if (descriptor.template != null && descriptor.template.size() > 0) {
              apply(descriptor.template, record);
            }
            if (logger != null && logger.debugLevel()) {
              logger.debug(formatCollection(TaskMessage.ATTRIBUT_MAPPING,record));
            }
            // evaluate any transformation if required
            if (descriptor.transformation()) {
              apply(descriptor.transformer, record);
              if (logger != null && logger.debugLevel()) {
                logger.debug(formatCollection(TaskMessage.ATTRIBUT_TRANSFORMATION,record));
              }
            }
            collector.add(record);
          }
        }
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Aplies configured transformations
     **
     ** @param  rule             the transformation rules configured.
     **                          <br>
     **                          Allowed object is
     **                          {@link AttributeTransformation}.
     ** @param  source           the source of the transformation.
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Serializable} for the value.
     */
    protected void apply(final Set<Descriptor.Template> rule, final Map<String, Serializable> source) {
      final DescriptorShell shell = DescriptorShell.instance();
      Map<String, Object> target = new HashMap<String, Object>();
      for (Map.Entry<String, Serializable> cursor : source.entrySet()) {
        target.put(cursor.getKey(), cursor.getValue());
      }
      for (Descriptor.Template cursor : rule) {
        // single attribute
        final Object value = shell.execute(cursor.source, target);
        target.put(cursor.id, value);
      }
      for (Map.Entry<String, Object> cursor : target.entrySet()) {
        source.put(cursor.getKey(), (Serializable)cursor.getValue());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply
    /**
     ** Aplies configured transformations
     **
     ** @param  rule             the transformation rules configured.
     **                          <br>
     **                          Allowed object is
     **                          {@link AttributeTransformation}.
     ** @param  source           the source of the transformation.
     **                          Allowed object is {@link Map} where each
     **                          element is of type {@link String} for the key
     **                          and {@link Serializable} for the value.
     */
    protected void apply(final AttributeTransformation rule, final Map<String, Serializable> source) {
      Map<String, Object> target = new HashMap<String, Object>();
      for (Map.Entry<String, Serializable> cursor : source.entrySet()) {
        target.put(cursor.getKey(), cursor.getValue());
      }
      target = rule.transform(target);
      for (Map.Entry<String, Object> cursor : target.entrySet()) {
        source.put(cursor.getKey(), (Serializable)cursor.getValue());
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: interprete
    /**
     ** Identity Manager specific formating based on FieldFlag
     **
     ** @param  specification    the attribute specificatin descriptor.
     **                          <br>
     **                          Allowed object is {@link Descriptor.Attribute}.
     ** @param  authoritative    <code>true</code> if the event to build
     **                          belongs to an authoritative source.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     **
     ** @return                  a value object that Identity Manager is able to
     **                          understand.
     **                          <br>
     **                          Possible object {@link Serializable}.
     */
    private Serializable interprete(final Descriptor.Attribute specification, final Object value, final boolean authoritative) {
      if (value == null)
        return SystemConstant.EMPTY;

      if (OperationalAttributes.ENABLE_NAME.equals(specification.source)) {
        // Introduced because of #11737223 -- @TODO remove once #11737223 fixed
        if (value instanceof Boolean) {
          final Boolean enabled = (Boolean)value;
          return enabled ? authoritative ? STATUS_ACTIVE : STATUS_ENABLED : STATUS_DISABLED;
        }
        else {
          final Boolean enabled = STATUS_ACTIVE.equals(value);
          return enabled ? authoritative ? STATUS_ACTIVE : STATUS_ENABLED : STATUS_DISABLED;
        }
      }
      // format as date
      else if (Date.class.equals(specification.type)) {
        // to do the stupid limitation of the connector framework that isn't
        // able to serialize a Date value over the wire; Date's are serialized
        // as Long, hence we try at first to transform a Long to a Date value.
        // If this isn't possible because of the value may be a String the
        // convertion is following a best guess to figure out the date format of
        // the value
        try {
          return new Date(Long.class.cast(value));
        }
        // the value wasn't a Long hence could not be cast
        catch (ClassCastException e) {
          for (String cursor : supportedFormat) {
            SimpleDateFormat formatter = new SimpleDateFormat(cursor);
            try {
              formatter = new SimpleDateFormat(cursor);
              // if no ParseException, return internal date
              return formatter.parse((String)value);
            }
            catch (ParseException x) {
              // try for all supported formats
              continue;
            }
          }
          // if date is none of supported formats, throw original exception
          throw e;
        }
      }
      else {
        return (value instanceof Boolean) ? (Boolean)value ? "1" : "0" : value.toString();
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // abstract class Handler
  // ~~~~~~~~ ~~~~~ ~~~~~~~
  static abstract class Handler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** the size of the thread pool */
    protected final int                   size;
    /** the job task thats instanciated this reconciliation handler */
    protected final EntityReconciliation  task;
    /** the transfer object to the reconciliation enging */
    protected final BatchAttributes       batchAttrs;

    protected ReconOperationsService      facade;
    protected boolean                     closed = false;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a reconciliation <code>Process</code> executer that allows
     ** use as a JavaBean.
     **
     ** @param  task             the {@link Reconciliation} task which has
     **                          instantiated this event handler.
     **                          <br>
     **                          Allowed object is {@link EntityReconciliation}.
     ** @param  size             the value for the initial number of threads
     **                          available in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  ignorable        <code>true</code> to prevent event creation and
     **                          processing of target system data that already
     **                          exists in Identity Manager.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    public Handler(final EntityReconciliation task, final int size, final boolean ignorable) {
      // ensure inheritance
      super();

      // prevent bogus input
      if (size < 1)
        throw new IllegalArgumentException(FrameworkBundle.string(FrameworkError.RECONCILIATION_POOL_SIZE));

      // initialize instance attributes
      this.size       = size;
      this.task       = task;
      this.batchAttrs = new BatchAttributes(task.reconcileObject(), internalFormat, ignorable);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: facade
    /**
     ** Returns an instance of a Business Facade by creating an internal client
     ** connection to return the appropriate instance of the desired
     ** {@link ReconOperationsService}.
     ** <br>
     ** This implementation will stop the execution if the Business Facade
     ** cannot be instantiated. An appropriate message will be logged in the
     ** Application Server Log in this case.
     **
     ** @return                    the {@link ReconOperationsService}.
     **
     ** @throws  LoginException    If there is an error during login.
     */
    protected ReconOperationsService facade()
      throws LoginException {

      if (this.facade == null) {
        final OIMInternalClient client = new OIMInternalClient(new Hashtable<String, String>());
        // bug 18127455 - logging in as oim internal
        // oimClient.loginAsAdmin();
        client.loginAsOIMInternal();
        this.facade = client.getService(ReconOperationsService.class);
      }
      return this.facade;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: submit
    /**
     ** Executes the reconcilaition events sometime in the future.
     ** <p>
     ** If the task cannot be submitted for execution, either because this
     ** executor has been shutdown or because its capacity has been reached, the
     ** task is handled by the current <code>RejectedExecutionHandler</code>.
     */
    protected abstract void submit();

    ////////////////////////////////////////////////////////////////////////////
    // Method: summary
    /**
     ** Generate the metric repost about the reconciliation events processed.
     **
     ** @param  result           the results of a reconciliation process batch.
     **                          <br>
     **                          Allowed object is {@link ReconciliationResult}.
     */
    protected void summary(final ReconciliationResult result) {
      final String method = "summary";
      this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_EVENT_CREATED, result.getSuccessResult().size()));
      final ArrayList<FailedInputData> failed = result.getFailedResult();
      if (!failed.isEmpty()) {
        this.task.error(method, FrameworkBundle.format(FrameworkMessage.RECONCILIATION_EVENT_FAILED, failed.size()));
        for (FailedInputData failure : failed) {
          this.task.error(method, failure.getFailure().getLocalizedMessage());
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Batch
  // ~~~~~ ~~~~~
  /**
   ** <code>Batch</code> reconciliation service, which can be used to create
   ** reconciliation events in Identity Manager, it needs to be used in the
   ** following way:
   ** <ul>
   **   <li>Add all reconciliation events representing the objects on the target
   **       resource by using {@link #handle(ConnectorObject)} method.
   **   <li>Call {@link #finish()} method to tell the service that there won't
   **       be any other reconciliation events added.
   ** </ul>
   */
  public static class Batch extends    Handler
                            implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final SystemThreadPool executor;

    private int                    slot;
    private int                    timeOut;
    private InputData[]            pool;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Batch</code> executer that allows use as a
     ** JavaBean.
     **
     ** @param  task             the {@link Reconciliation} task which has
     **                          instantiated this event handler.
     **                          <br>
     **                          Allowed object is {@link EntityReconciliation}.
     ** @param  size             the value for the initial number of threads
     **                          available in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  ignorable        <code>true</code> to prevent event creation and
     **                          processing of target system data that already
     **                          exists in Identity Manager.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    public Batch(final EntityReconciliation task, final int size, final boolean ignorable) {
      // ensure inheritance
      this(task, size, ignorable, SystemThreadPool.Config.build().build(), 24);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Batch</code> executer that allows use as a
     ** JavaBean.
     **
     ** @param  task             the {@link Reconciliation} task which has
     **                          instantiated this event handler.
     **                          <br>
     **                          Allowed object is {@link EntityReconciliation}.
     ** @param  size             the value for the initial number of threads
     **                          available in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  ignorable        <code>true</code> to prevent event creation and
     **                          processing of target system data that already
     **                          exists in Identity Manager.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  config           the configuration properties of the thread
     **                          pool.
     **                          Allowed object is
     **                          {@link SystemThreadPool.Config}.
     ** @param  timeOut          the batch size for the reconciliation source.
     **                          Allowed object is <code>int</code>.
     */
    public Batch(final EntityReconciliation task, final int size, final boolean ignorable, final SystemThreadPool.Config config, final int timeOut) {
      // ensure inheritance
      super(task, size, ignorable);

      // initialize instance attributes
      this.pool     = new InputData[size];
      this.timeOut  = timeOut;
      this.executor = new SystemThreadPool(config, Executors.defaultThreadFactory());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object           each object return from the search.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      final String method = "handle";
      this.task.trace(method, SystemMessage.METHOD_ENTRY);
      if (this.closed) {
        this.task.stop();
        this.task.error(method, FrameworkBundle.string(FrameworkError.RECONCILIATION_POOL_CLOSED));
        this.task.trace(method, SystemMessage.METHOD_EXIT);
        return false;
      }
      
      // check if reconciliation events wanted
      if (this.task.gatherOnly()) {
        this.task.info(TaskBundle.string(TaskMessage.RECONCILE_SKIP));
        this.task.trace(method, SystemMessage.METHOD_EXIT);
        return true;
      }

      final Event event = buildEvent(this.task.resource.instance(), object, this.task.descriptor, false);
      try {
        this.pool[this.slot] = new InputData(event.master(), event.multiple(), true, ChangeType.CHANGELOG, null);
        this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_POOL_ADD, this.slot));
        this.slot++;
        if (this.slot == this.size) {
          submit();
        }
        return !this.task.stopped();
      }
      finally {
        this.task.trace(method, SystemMessage.METHOD_EXIT);
      }
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: submit (Process)
    /**
     ** Executes the reconcilaition events sometime in the future.
     ** <p>
     ** If the task cannot be submitted for execution, either because this
     ** executor has been shutdown or because its capacity has been reached, the
     ** task is handled by the current <code>RejectedExecutionHandler</code>.
     */
    @Override
    protected void submit() {
      final String method = "submit";
      this.task.trace(method, SystemMessage.METHOD_ENTRY);
      // prepare the variable that later on will be puched to the execution
      // context for audit purpose
      final Long        id   = (Long)ContextManager.getValue(SchedulerConstants.JOB_HISTROY_ID);
      final String      name = (String)ContextManager.getValue(SchedulerConstants.JOB_NAME_CONTEXT);
      // copy the data gathered so far into a new array due to the
      // asynchronously batch submitted below
      final InputData[] data = (this.slot == this.size) ? this.pool : Arrays.copyOf(this.pool, this.slot);
      this.executor.submit(
        new Runnable() {
          @Override
          public void run() {
            task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_POOL_COMPLETE, data.length));
            try {
              // login internally to Identity Manager
              final ReconOperationsService facade = facade();
              // prepare the environment for auditing purpose
              ContextManager.pushContext("EntityReconciliation", ContextManager.ContextTypes.ADMIN, "Batch");
              ContextManager.setValue(SchedulerConstants.JOB_HISTROY_ID,   new ContextAwareNumber(id));
              ContextManager.setValue(SchedulerConstants.JOB_NAME_CONTEXT, new ContextAwareString(name));
              summary(facade.createReconciliationEvents(batchAttrs, data));
            }
            catch (LoginException e) {
              task.error(method, FrameworkBundle.format(FrameworkError.RECONCILIATION_EVENT_LOGIN, data.length));
            }
            catch (Throwable t) {
              task.fatal(method, t);
            }
            finally {
              ContextManager.popContext();
            }
          }
        }
      );
      // cleanup allocated resources
      this.slot = 0;
      this.pool = new InputData[this.size];
      this.task.trace(method, SystemMessage.METHOD_EXIT);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: finish
    /**
     ** Finish processing of all the reconciliation events added by calling
     ** {@link #handle(ConnectorObject)}.
     ** <br>
     ** Calling {@link #handle(ConnectorObject)} is not allowed after this call.
     */
    public void finish() {
      final String method = "finish";
      if (this.closed)
        throw new IllegalStateException(FrameworkBundle.string(FrameworkError.RECONCILIATION_POOL_FINISHED));

      if (this.slot > 0) {
        submit();
      }
      this.closed = true;
      if (this.executor != null) {
        this.executor.shutdown();
        try {
          this.executor.awaitTermination(this.timeOut, TimeUnit.HOURS);
        }
        catch (Exception e) {
          this.task.fatal(method, e);
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Concurrent
  // ~~~~~ ~~~~~~~~~~
  /**
   ** <code>Concurrent</code> batch reconciliation service, which can be used to
   ** create reconciliation events in Identity Manager.
   ** <br>
   ** The way this service works is that it keeps collecting
   ** {@link ConnectorObject}s from connector and adds it to a batch (size is
   ** configurable in feature configuration).
   ** <br>
   ** Once the batch is full, this class uses one of the threads part of the
   ** pool created by the scheduled task. Each thread will convert all
   ** {@link ConnectorObject}s (part of each batch) into {@link Event}, checks
   ** if it has to be excluded, validates and transforms it.
   ** <br>
   ** Once all {@link ConnectorObject}s are converted to {@link Event}s, the
   ** same thread sends the entire batch to teh reconcilitaion engine.
   ** <p>
   ** The task which  calls this service should send pool of threads using
   ** Executors.newFixedThreadPool(int nThreads).
   ** <br>
   ** It needs to be used in the following way:
   ** <ul>
   **   <li>Add {@link ConnectorObject}s to the batch.
   **   <li>Call {@link #finish()} method to tell the service that there won't
   **       be any other reconciliation events added
   ** </ul>
   ** The task which calls this service should also make sure to wait until all
   ** threads complete calling reconciliation API.
   */
  public static class Concurrent extends    Handler
                                 implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final ExecutorService executor;

    private int                   slot;
    private String                dateFormat;
    private ConnectorObject[]     pool;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Concurrent</code> executer that allows use as
     ** a JavaBean.
     **
     ** @param  task             the {@link Reconciliation} task which has
     **                          instantiated this event handler.
     **                          <br>
     **                          Allowed object is {@link EntityReconciliation}.
     ** @param  size             the value for the initial number of threads
     **                          available in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     ** @param  ignorable        <code>true</code> to prevent event creation and
     **                          processing of target system data that already
     **                          exists in Identity Manager.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     ** @param  dateFormat       the format pattern of the date values to
     **                          reconcile.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  executor         An {@link ExecutorService} that provides
     **                          methods to manage termination and methods that
     **                          can produce a <code>Future</code> for tracking
     **                          progress of one or more asynchronous tasks.
     **                          <br>
     **                          Allowed object is {@link ExecutorService}.
     */
    public Concurrent(final EntityReconciliation task, final int size, final boolean ignorable, final String dateFormat, final ExecutorService executor) {
      // ensure inheritance
      super(task, size, ignorable);

      // initialize instance attributes
      this.dateFormat = dateFormat;
      this.pool       = new ConnectorObject[size];
      this.executor   = executor;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object           each object return from the search.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      final String method = "handle";
      this.task.trace(method, SystemMessage.METHOD_ENTRY);
      if (this.closed) {
        this.task.stop();
        this.task.error(method, FrameworkBundle.string(FrameworkError.RECONCILIATION_POOL_CLOSED));
        this.task.trace(method, SystemMessage.METHOD_EXIT);
      }
      this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_POOL_ADD, this.slot));
      this.pool[this.slot++] = object;
      if (this.slot == this.size) {
        submit();
      }
      this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_POOL_ADDED, object.getUid().getName()));
      return !this.task.stopped();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: submit (Process)
    /**
     ** Executes the reconcilaition events sometime in the future.
     ** <p>
     ** If the task cannot be submitted for execution, either because this
     ** executor has been shutdown or because its capacity has been reached, the
     ** task is handled by the current <code>RejectedExecutionHandler</code>.
     */
    @Override
    protected void submit() {
      this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_POOL_LIMITS, this.slot));
      // we need a finla variable here because its used inside the anonymous
      // class below
      final StringBuffer buffer = new StringBuffer();
      try  {
        buffer.append(ContextManager.wrapContext());
      }
      catch(ParserConfigurationException e) {
        this.task.warning(String.format("Cannot get current context.\nReason: %s", e.getMessage()));
      }
      final ConnectorObject[] data = this.slot == this.size ? this.pool : Arrays.copyOf(this.pool, this.slot);
      this.executor.execute(
        new Runnable() {
          public void run() {
            final String context = buffer.toString();
            ContextManager.pushContext("EntityReconciliation", ContextManager.ContextTypes.ADMIN, "Concurrent");
            try {
              if (!StringUtility.isBlank(context)){
                ContextManager.loadWrappedContext(context);
              }
              process(data);
            }
            finally {
              ContextManager.popContext();
            }
          }
        }
      );
      // set batchIndex back to 0;
      this.slot = 0;
      this.pool = new ConnectorObject[this.size];
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: finish
    /**
     ** Finish processing of all the reconciliation events added by calling
     ** {@link #handle(ConnectorObject)}.
     ** <br>
     ** Calling {@link #handle(ConnectorObject)} is not allowed after this call.
     */
    public void finish() {
      if (this.closed)
        throw new IllegalStateException(FrameworkBundle.string(FrameworkError.RECONCILIATION_POOL_FINISHED));

      if (this.slot > 0)
        submit();

      this.closed = true;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: process
    private void process(final ConnectorObject[] subject) {
      final int         size  = subject.length;
      final InputData[] data  = new InputData[size];
      int               index = 0;
      for (int i = 0; i < size; i++) {
        final Event event = new Event(this.task.resource.instance(), subject[i], this.task.descriptor, false, false);
        this.task.debug("process", FrameworkBundle.format(FrameworkMessage.RECONCILIATION_EVENT_REGULAR, subject[i].getUid().getUidValue()));
        data[index++] = new InputData(event.master(), event.multiple(), true, ChangeType.CHANGELOG, null);
      }
      final ReconOperationsService facade = Platform.getService(ReconOperationsService.class);
      summary(facade.createReconciliationEvents(this.batchAttrs, size == this.size ? data : Arrays.copyOf(data, index)));
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Delete
  // ~~~~~ ~~~~~~
  /**
   ** <code>Delete</code> reconciliation service, which can be used to delete
   ** objects in Identity Manager, it needs to be used in the
   ** following way:
   ** <ul>
   **   <li>Add all reconciliation events representing the objects on the target
   **       resource by using {@link #handle(ConnectorObject)} method.
   **   <li>Call {@link #finish()} method to delete accounts which are present
   **       in Identity Manager but where not added by
   **       {@link #handle(ConnectorObject)}.
   ** </ul>
   */
  public static class Delete implements ResultsHandler {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final int                  size;
    private final EntityReconciliation task;
    private final Set<Object>          matched  = new HashSet<Object>();
    private final Map<String, Long>    endpoint;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an empty <code>Delete</code> executer that allows use as a
     ** JavaBean.
     **
     ** @param  task             the {@link Reconciliation} task which has
     **                          instantiated this event handler.
     **                          <br>
     **                          Allowed object is {@link EntityReconciliation}.
     ** @param  size             the value for the initial number of threads
     **                          available in the pool.
     **                          <br>
     **                          Allowed object is <code>int</code>.
     */
    public Delete(final EntityReconciliation task, final int size) {
      // ensure inheritance
      super();

      // initialize instance attributes
      this.task     = task;
      this.size     = size;
      this.endpoint = CollectionUtility.map(Reconciliation.IT_RESOURCE, this.task.resource.instance());
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: handle (ResultsHandler)
    /**
     ** Call-back method to do whatever it is the caller wants to do with each
     ** {@link ConnectorObject} that is returned in the result of
     ** <code>SearchApiOp</code>.
     **
     ** @param  object           each object return from the search.
     **                          <br>
     **                          Allowed object is {@link ConnectorObject}.
     **
     ** @return                  <code>true</code> if we should keep processing;
     **                          otherwise <code>false</code> to cancel.
     **                          <br>
     **                          Allowed object is <code>boolean</code>.
     */
    @Override
    public boolean handle(final ConnectorObject object) {
      final String method = "handle";
      this.task.trace(method, SystemMessage.METHOD_ENTRY);
      // build the event like a trusted source due to only account attriibutes
      // required for the reconcilaitaion event to create
      final EntityReconciliation.Event     event  = EntityReconciliation.buildEvent(this.task.resource.instance(), object, this.task.descriptor, false);
      final tcReconciliationOperationsIntf facade = Platform.getService(tcReconciliationOperationsIntf.class);
      try {
        final Set<?> matchedObject = facade.provideDeletionDetectionData(this.task.reconcileObject(), new Map[]{event.master}, this.endpoint);
        this.matched.addAll(matchedObject);
      }
      catch (tcIDNotFoundException | tcMultipleMatchesFoundException | tcAPIException e) {
        this.task.stop();
        this.task.fatal(method, e);
      }
      finally {
        this.task.trace(method, SystemMessage.METHOD_EXIT);
      }
      return !this.task.stopped();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: finish
    /**
     ** Finish processing of all the reconciliation events which where not added
     ** by calling {@link #handle(ConnectorObject)}.
     ** <br>
     ** Calling {@link #handle(ConnectorObject)} is not allowed after this call.
     **
     ** @throws TaskException    in case an error does occur.
     */
    public void finish()
      throws TaskException {

      final tcReconciliationOperationsIntf facade = Platform.getService(tcReconciliationOperationsIntf.class);
      try {
        final tcResultSet missing = facade.getMissingAccounts(this.task.reconcileObject(), this.matched, this.endpoint);
        if (missing.isEmpty()) {
          this.task.info(FrameworkBundle.string(FrameworkMessage.RECONCILIATION_EVENT_NOTHING));
        }
        else {
          final long[] event = facade.deleteDetectedAccounts(missing);
          for (long cursor : event) {
            // process the event; rule matching; revoke account
            facade.processReconciliationEvent(cursor);
            this.task.info(FrameworkBundle.format(FrameworkMessage.RECONCILIATION_EVENT_DELETE, cursor));
          }
        }
      }
      catch (tcDataNotProvidedException | tcIDNotFoundException e) {
        throw TaskException.abort(e);
      }
      catch (tcAPIException e) {
        throw TaskException.unhandled(e);
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>EntityReconciliation</code> with the specified
   ** logging category.
   **
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  protected EntityReconciliation(final String loggerCategory) {
    // ensure inheritance
    super(loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gatherOnly
  /**
   ** Returns <code>true</code> if event creation and processing of target
   ** system data that already exists in Identity Manager should be prevented.
   **
   ** @return                    <code>true</code> if event creation and
   **                            processing of target system data that already
   **                            exists in Identity Manager should be prevented.
   **                            <br>
   **                            Possiblle object is <code>boolean</code>.
   */
  public final boolean ignoreDublicates() {
    return booleanValue(IGNORE_DUBLICATES);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   returningAttributes
  /**
   ** Returns the {@link Set} of attribute names that will be passed to a Target
   ** System search operation to specify which attributes the Service Provider
   ** should return.
   **
   ** @return                    the {@link Set} of attribute names that will be
   **                            passed to a Target System search operation to
   **                            specify which attributes the Service Provider
   **                            should return.
   **                            <br>
   **                            Possiblle object is {@link Set} where each
   **                            element is of type {@link String}.
   */
  protected Set<String> returningAttributes() {
    return this.descriptor.source();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of abstract base classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   operationBuilder (Reconciliation)
  /**
   ** Factory method used in the main reconciliation search to configure the
   ** {@link OperationOptions} passed to the bundle from the default parameters
   ** of a job like <code>Incremental</code>.
   **
   ** @return                    the {@link OperationOptionsBuilder} containing
   **                            the defaults to query the target system.
   **                            <br>
   **                            Possible object is
   **                            {@link OperationOptionsBuilder}.
   */
  @Override
  protected OperationOptionsBuilder operationBuilder() {
    // add filter attributes if filter is specified
    Set<String> emit = filterAttributes();
    if (emit == null) {
      emit = new HashSet<String>();
    }

    emit.addAll(returningAttributes());
    emit.addAll(this.descriptor.referenceSource());
    // build it
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    factory.setAttributesToGet(emit);
    if (this.conatinerClass != null) {
      factory.setContainer(new QualifiedUid(this.conatinerClass, new Uid(stringValue(SEARCH_BASE))));
    }
    final Map<String, Object> option = factory.getOptions();
    option.put(BATCH_SIZE,            batchSize());
    option.put(SEARCH_ORDER,          searchOrder());
    option.put(INCREMENTAL,           incremental());
    option.put(SYNCHRONIZATION_TOKEN, synchronizationToken());
    return factory;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildEvent
  /**
   ** Factory method to create a <code>Event</code> which is associated with the
   ** specified task and belongs to the IT Resource specified by the given
   ** identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  endpoint           the <code>IT Resource</code> source of this
   **                            event belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  subject            the complete collection of attribute values
   **                            gathered from a data source.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   ** @param  descriptor         the attribute mappping.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  authoritative      <code>true</code> if the event to build
   **                            belongs to an authoritative source.
   **                            The difference beteween authoritative and
   **                            non-authoritative reconcilaition belongs mainly
   **                            to the <code>IT Resource</code> which needs to
   **                            be put in the reconciled data if the event to
   **                            build is non-authoritative.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a populated <code>Reconciliation.Event</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityReconciliation.Event}.
   */
  public static Event buildEvent(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor, final boolean authoritative) {
    return buildEvent(endpoint, subject, descriptor, authoritative, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildEvent
  /**
   ** Factory method to create a <code>Event</code> which is associated with the
   ** specified task and belongs to the IT Resource specified by the given
   ** identifier.
   ** <p>
   ** The <code>IT Resource</code> configuration will be populated from the
   ** repository of the Identity Manager and also all well known attributes
   ** using the specified <code>instance</code> key.
   **
   ** @param  endpoint           the <code>IT Resource</code> source of this
   **                            event belongs to.
   **                            <br>
   **                            Allowed object is {@link Long}.
   ** @param  subject            the complete collection of attribute values
   **                            gathered from a data source.
   **                            <br>
   **                            Allowed object is {@link ConnectorObject}.
   ** @param  descriptor         the attribute mappping.
   **                            <br>
   **                            Allowed object is {@link Descriptor}.
   ** @param  authoritative      <code>true</code> if the event to build
   **                            belongs to an authoritative source.
   **                            The difference beteween authoritative and
   **                            non-authoritative reconcilaition belongs mainly
   **                            to the <code>IT Resource</code> which needs to
   **                            be put in the reconciled data if the event to
   **                            build is non-authoritative.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  strict             <code>true</code> if the result should
   **                            contain attribute mappings only for those
   **                            definitions that defined in the descriptor
   **                            but also contained in the provided
   **                            collection. If <code>false</code> if
   **                            specified than the result has a value for
   **                            each attribute name defined in the
   **                            descriptor.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    a populated <code>Reconciliation.Event</code>.
   **                            <br>
   **                            Possible object is
   **                            {@link EntityReconciliation.Event}.
   */
  public static Event buildEvent(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor, final boolean authoritative, final boolean strict) {
    return new Event(endpoint, subject, descriptor, authoritative, strict);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   initialize (overridden)
  /**
   ** The initialization task.
   **
   ** @throws TaskException      in case an error does occur.
   */
  @Override
  protected void initialize()
    throws TaskException {

    // ensure inheritance
    // this will produce the trace of the configured task parameter
    super.initialize();

    unmarshal(reconcileDescriptor());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   unmarshal
  /**
   ** Factory method to create a {@link Descriptor} from a path.
   **
   ** @param  path               the absolute path for the descriptor in the
   **                            Metadata Store that has to be parsed.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws TaskException      in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  protected void unmarshal(final String path)
    throws TaskException {

    final String method = "unmarshal";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);
    // create the task descriptor that provides the attribute mapping and
    // transformations to be applied on the mapped attributes
    this.descriptor = Descriptor.buildReconciliation(this);
    try {
      // prevent bogus input
      if (StringUtility.isEmpty(path))
        TaskException.argumentIsNull("path");

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_FETCH, path));
      final MDSSession session  = createSession();
      final PManager   manager  = session.getPersistenceManager();
      final PDocument  document = manager.getDocument(session.getPContext(), DocumentName.create(path));

      debug(method, TaskBundle.format(TaskMessage.METADATA_OBJECT_UNMARSHAL, path));
      DescriptorFactory.configure(this.descriptor, document);
    }
    catch (ReferenceException e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupObject
  /**
   ** Lookups an {@link ApplicationInstance} from Identity Manager.
   **
   ** @param  objectName         the name of a <code>Resource Object</code> to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the internal identifier of the desired
   **                            <code>Resource Object</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected long lookupObject(final String objectName)
    throws TaskException {

    final String method = "lookupObject";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ResourceObject.NAME, objectName);
    try {
      final tcObjectOperationsIntf facade    = objectFacade();
      final tcResultSet            resultSet = facade.findObjects(filter);
      int count = resultSet.getRowCount();
      if (count == 0) {
        // if a Resource Object is not mapped we have no chance to fix it hence
        // stop any further processing
        throw new TaskException(TaskError.RESOURCE_NOT_FOUND, objectName);
      }
      else if (count > 1) {
        // if a Resource Resource is mapped which returns multiple entries then
        // we have a either a wildcard in the specified IT Resource name or a
        // corrupted repository; regardless what the reason will be we have no
        // chance to fix it hence stop any further processing
        throw new TaskException(TaskError.RESOURCE_AMBIGUOUS, objectName);
      }
      return resultSet.getLongValue(ResourceObject.KEY);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookupResource
  /**
   ** Lookups an {@link ApplicationInstance} from Identity Manager.
   **
   ** @param  resourceName       the name of a <code>IT Resource</code> to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the internal identifier of the desired
   **                            <code>IT Resource</code>.
   **                            <br>
   **                            Possible object is <code>long</code>.
   **
   ** @throws TaskException      in case an error does occur.
   */
  protected long lookupResource(final String resourceName)
    throws TaskException {

    final String method = "lookupResource";
    trace(method, SystemMessage.METHOD_ENTRY);
    timerStart(method);

    final Map<String, String> filter = new HashMap<String, String>();
    filter.put(ITResource.NAME, resourceName);
    try {
      final tcITResourceInstanceOperationsIntf facade    = resourceFacade();
      final tcResultSet                        resultSet = facade.findITResourceInstances(filter);
      int count = resultSet.getRowCount();
      if (count == 0) {
        // if an IT Resource is not mapped we have no chance to fix it hence
        // stop any further processing
        throw new TaskException(TaskError.ITRESOURCE_NOT_FOUND, resourceName);
      }
      else if (count > 1) {
        // if an IT Resource is mapped which returns multiple entries then we
        // have a either a wildcard in the specified IT Resource name or a
        // corrupted repository; regardless what the reason will be we have no
        // chance to fix it hence stop any further processing
        throw new TaskException(TaskError.ITRESOURCE_AMBIGUOUS, resourceName);
      }
      return resultSet.getLongValue(ITResource.KEY);
    }
    catch (Exception e) {
      throw new TaskException(e);
    }
    finally {
      timerStop(method);
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   formatCollection
  /**
   ** Formats a {@link Set} of {@link Attribute}s as an output for debugging
   ** purpose.
   **
   ** @param  mapping            the {@link Set} of {@link Attribute}s to format
   **                            for debugging output.
   **
   ** @return                    the formatted string representation
   */
  private static String formatCollection(final String header, final Map<String, Serializable> mapping) {
    final TableFormatter table  = new TableFormatter()
    .header("Attribute")
    .header("Value")
    ;
    for (Map.Entry<String, Serializable> cursor : mapping.entrySet()) {
      StringUtility.formatValuePair(table, cursor.getKey(), String.valueOf(cursor.getValue()));         
    }
    final StringBuilder buffer = new StringBuilder();
    table.print(buffer);
    return TaskBundle.format(header, buffer.toString());
  }
}