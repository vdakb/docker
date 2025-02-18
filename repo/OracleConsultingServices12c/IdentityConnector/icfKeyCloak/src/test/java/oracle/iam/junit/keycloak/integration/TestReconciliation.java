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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Red Hat Keycloak Connector

    File        :   TestReconciliation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    TestReconciliation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-06-11  DSteding    First release version
*/

package oracle.iam.junit.keycloak.integration;

import java.util.Set;
import java.util.Map;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.io.Serializable;

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.framework.common.objects.EmbeddedObject;
import org.identityconnectors.framework.common.objects.ConnectorObject;
import org.identityconnectors.framework.common.objects.OperationalAttributes;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.iam.identity.foundation.AttributeTransformation;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.Reconciliation;

////////////////////////////////////////////////////////////////////////////////
// class TestReconciliation
// ~~~~~ ~~~~~~~~~~~~~~~~~~
/**
 ** The <code>TestReconciliation</code> emulates the Red Hat Keycloak Server
 ** Reconciliation.
 ** <p>
 ** Implemented by an extra class to keep it outside of the test case classes
 ** itself.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestReconciliation extends TestBaseIntegration {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Default format to prefix <code>Lookup Definition</code> encoded entries.
   */
  protected static final String     PATTREN_ENCODED_VALUE = "%d~%s";

  /**
   ** Default format to prefix <code>Lookup Definition</code> decoded entries.
   */
  protected static final String     PATTREN_DECODED_VALUE = "%s~%s";

  /**
   ** The value of an entity status if its reconciled as <code>Active</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is only applicable for identities like users,
   **              roles and organizations.
   */
  protected static final String     STATUS_ACTIVE         = "Active";

  /**
   ** The value of an entity status if its reconciled as <code>Active</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is only applicable for accounts.
   */
  protected static final String     STATUS_ENABLED        = "Enabled";

  /**
   ** The value of an entity status if its reconciled as <code>Inactive</code>
   ** from a <code>Service Provider</code>.
   ** <br>
   ** <b>Note</b>: The status is applicable on any entity.
   */
  protected static final String     STATUS_DISABLED       = "Disabled";

  /**
   ** Attribute tag which can be defined on a scheduled task to advicepreventing
   ** creation and processing of reconciliation events for target system data
   ** that already exists in Identity Manager.
   ** <br>
   ** This attribute is optional and defaults to <code>true</code>.
   */
  protected static final String     IGNORE_DUBLICATES     = "Ignore Dublicates";

  private static final List<String> supportedFormat       = CollectionUtility.list("yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyyMMddHHmmss'Z'", "yyyyMMddHHmmss'z'", "yyyy-MM-dd");

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
    private Event(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor, final boolean strict)  {
      // ensure inheritance
      super();

      // initialize instance attributes
      transform(subject, endpoint, descriptor, strict);
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
     */
    protected void transform(final ConnectorObject subject, final Long endpoint, final Descriptor descriptor, final boolean strict) {
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
        this.master.put(rule.id, interprete(rule, AttributeUtil.getSingleValue(attribute)));
      }
      this.master.put(Reconciliation.IT_RESOURCE, endpoint);
      transform(subject, endpoint, descriptor.reference());
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
        final Set<Descriptor.Attribute> mapping    = descriptor.attribute();
        if (attribute != null) {
          for (Object row : attribute.getValue()) {
            final EmbeddedObject            embed  = (EmbeddedObject)row;
            final Map<String, Serializable> record = new LinkedHashMap<String, Serializable>();
            for (Descriptor.Attribute rule : mapping) {
              final Attribute value = embed.getAttributeByName(rule.source);
              Serializable latch = interprete(rule, AttributeUtil.getSingleValue(value));
              // apply the rule to build an entitlement if the descriptor
              // attribute is declared as
              if (rule.entitlement())
                latch = String.format(PATTREN_ENCODED_VALUE, endpoint, latch);
              // put all attributes in the mapping regardless if they are empty
              // or not.
              record.put(rule.id, latch);
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
     **
     ** @return                  a value object that Identity Manager is able to
     **                          understand.
     **                          <br>
     **                          Possible object {@link Serializable}.
     */
    private Serializable interprete(final Descriptor.Attribute specification, final Object value) {
      if (value == null)
        return SystemConstant.EMPTY;

      if (OperationalAttributes.ENABLE_NAME.equals(specification.source)) {
        // Introduced because of #11737223 -- @TODO remove once #11737223 fixed
        if (value instanceof Boolean) {
          final Boolean enabled = (Boolean)value;
          return enabled ? STATUS_ENABLED : STATUS_DISABLED;
        }
        else {
          final Boolean enabled = STATUS_ACTIVE.equals(value);
          return enabled ? STATUS_ENABLED : STATUS_DISABLED;
        }
      }
      // format as date
      if (Date.class.equals(specification.type.clazz)) {
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
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestReconciliation</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestReconciliation() {
    // ensure inheritance
    super();
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
  public static Event buildEvent(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor) {
    return buildEvent(endpoint, subject, descriptor, false);
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
  public static Event buildEvent(final Long endpoint, final ConnectorObject subject, final Descriptor descriptor, final boolean strict) {
    return new Event(endpoint, subject, descriptor, strict);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   searchControl
  /**
   ** Factory method to create a new &amp; initialized control configurator
   ** instance.
   **
   ** @param  token              a string containing lexical representation of
   **                            a date.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    a new &amp; initialized control configurator
   **                            instance.
   **                            <br>
   **                            Possible object is
   **                            <code>OperationContext</code>.
   */
  protected OperationOptionsBuilder searchControl(final Set<String> returning) {
    final OperationOptionsBuilder factory = new OperationOptionsBuilder();
    final Map<String, Object>     option  = factory.getOptions();
    option.put(BATCH_SIZE,        BATCH_SIZE_DEFAULT);
    option.put(BATCH_START,       0);
    factory.setAttributesToGet(returning);
    return factory;
  }
}