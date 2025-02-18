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

    File        :   AttributeMapping.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    AttributeMapping.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2010-28-06  DSteding    First release version
*/

package oracle.iam.identity.foundation;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

import oracle.hst.foundation.SystemConstant;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class AttributeMapping
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The <code>AttributeMapping</code> is intend to use where inbound attributes
 ** of an Oracle Identity Manager Object (core or user defined) are mapped to
 ** the outbound provisioning or reconciliation target.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 */
public class AttributeMapping extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>AttributeMapping</code> which is associated
   ** with the specified logging provider <code>loggable</code>.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Lookup Definition</code>
   **                            configuration wrapper.
   */
  public AttributeMapping(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AttributeMapping</code> which is associated
   ** with the specified logging provider <code>loggable</code> and belongs to
   ** the Metadata Descriptor specified by the given name.
   ** <br>
   ** The Lookup Definition will be populated from the repository of the Oracle
   ** Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Lookup Definition</code>
   **                            configuration wrapper.
   ** @param  instanceName       the name of the Lookup Definition instance
   **                            where this wrapper belongs to.
   **
   ** @throws TaskException      if the Lookup Definition is not defined in the
   **                            Oracle Identity manager meta entries or one or
   **                            more attributes are missing on the
   **                            Lookup Definition.
   */
  public AttributeMapping(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterByEncoded
  /**
   ** Returns the {@link Map} which contains only the attributes registered by
   ** this mapping.
   ** <br>
   ** The attributes obtained from the given {@link Map} are filtered by the
   ** <code>Decode</code> column of the Oracle Identity Manager Lookup
   ** Definition used by this instance.
   ** <p>
   ** There is no need to maintain the predictable iteration order provided by
   ** the source mapping if the source mapping implementation is a
   ** <code>LinkedHashMap</code>.
   **
   ** @param  subject            the complete collection of attribute values
   **                            gathered from a data source.
   **
   ** @return                    the filterd map.
   */
  public Map<String, Object> filterByEncoded(final Map<String, Object> subject) {
    return filterByEncoded(subject, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filterByEncoded
  /**
   ** Returns the {@link Map} which contains only the attributes registered by
   ** this mapping.
   ** <br>
   ** The attributes obtained from the given {@link Map} are filtered by the
   ** <code>Decode</code> column of the Oracle Identity Manager Lookup
   ** Definition used by this instance.
   ** <p>
   ** There is no need to maintain the predictable iteration order provided by
   ** the source mapping if the source mapping implementation is a
   ** <code>LinkedHashMap</code>.
   **
   ** @param  subject            the complete collection of attribute values
   **                            gathered from a data source.
   ** @param  strict             <code>true</code> if the result should contain
   **                            attribute mappings only for those definitions
   **                            that defined in the descriptor but also
   **                            contained in the provided collection.
   **                            if <code>false</code> if specified than the
   **                            result has a value for each attribute name
   **                            defined in the descriptor.
   **
   ** @return                    the filterd map.
   */
  public Map<String, Object> filterByEncoded(final Map<String, Object> subject, final boolean strict) {
    final String method = "filterByEncoded";
    // create a new mapping which is big enough to hold each value
    // there is no need to maintain the predictable iteration order provided by
    // the source mapping if the source mapping implementation is a
    // LinkedHashMap
    final Map<String, Object> result = new LinkedHashMap<String, Object>(this.attribute.size());
    for (Map.Entry<String, Object> entry : this.attribute.entrySet()) {
      final String outboundKey = (String)entry.getValue();
      if (StringUtility.isEmpty(outboundKey)) {
        error(method, TaskBundle.format(TaskError.ATTRIBUTE_KEY_NOTMAPPED, entry.getKey()));
      }
      else {
        // check if the provided attribute collection has a mapping for the
        // source attribute name of this mapping
        if (!strict && !subject.keySet().contains(entry.getKey()))
          continue;
        final Object value = subject.get(entry.getKey());
        // gets the attributes that the request is trying to provision or
        // reconcile to or from the Target System. One field can drive more than
        // one attributes but necessarily not all the attributes mapped may not
        // be provisioned or reconciled.
        final StringTokenizer tokenizer = new StringTokenizer(outboundKey.trim(), "|");
        while (tokenizer.hasMoreTokens()) {
          // put all attributes in the mapping regardless if they are empty or
          // not.
          result.put(tokenizer.nextToken().trim(), value == null ? SystemConstant.EMPTY : value);
        }
      }
    }
    return result;
  }
}