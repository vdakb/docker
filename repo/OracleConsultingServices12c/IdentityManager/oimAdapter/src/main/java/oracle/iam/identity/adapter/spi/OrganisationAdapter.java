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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   OrganisationAdapter.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    StringAdapter.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-07-06  DSteding    Fixed DE-000130
                                         Constant used to demarcate method
                                         entry/exit etc. needs to be part of
                                         product independent interfaces
    1.0.0.0      2010-10-01  DSteding    First release version
*/

package oracle.iam.identity.adapter.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;

import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.dataobj.util.tcOrganizationHierarchy;

import com.thortech.xl.util.adapters.tcUtilXellerateOperations;

import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.SystemConstant;
import oracle.hst.foundation.SystemException;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.identity.foundation.TaskError;
import oracle.iam.identity.foundation.TaskMessage;
import oracle.iam.identity.foundation.AbstractLookup;
import oracle.iam.identity.foundation.EntityAdapter;

import oracle.iam.identity.foundation.naming.LookupValue;

import oracle.iam.identity.foundation.resource.TaskBundle;

////////////////////////////////////////////////////////////////////////////////
// class OrganisationAdapter
// ~~~~~ ~~~~~~~~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on the Oracle Identity Manager Organization
 ** Entity.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   1.0.0.0
 */
public class OrganisationAdapter extends EntityAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrganisationAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public OrganisationAdapter(final tcDataProvider provider) {
    super(provider);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>OrganisationAdapter</code> task adpater that
   ** allows use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public OrganisationAdapter(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   processOrdered
  /**
   ** Validates if the specified process is ordered for the specified user
   ** profile
   **
   ** @param  organizationKey    the internal identifier of the organization
   **                            profile to validate.
   ** @param  processName        the name of the process to validate.
   **
   ** @return                    {@link #SUCCESS}  if the specified oraginaziion
   **                            is provisioned to the specified process;
   **                            otherwise {@link #FAILURE}.
   */
  public String processOrdered(final String organizationKey, final String processName) {
    final String method = "processOrdered";
    trace(method, SystemMessage.METHOD_ENTRY);
    try {
      return Integer.parseInt(tcUtilXellerateOperations.checkProcessOrderedForAct(this.provider(), processName, organizationKey)) > 0 ? SUCCESS : FAILURE;
    }
    catch (Exception e) {
      fatal(method, e);
      return FAILURE;
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLdapName
  /**
   ** Return the hierarchy name of an organization.
   **
   ** @param  lookupDefinition   the name of the Lookup Definition where the
   **                            <code>organizationName</code> is mapped to a
   **                            LDAP DN.
   ** @param  organizationName   the key for the desired organization name.
   ** @param  reverse            if <code>true</code> is specified the passed
   **                            name of the oragnization is defined in the
   **                            decoded column of the Lookup Definition; if
   **                            <code>false</code> is specified the
   **                            organization should be defined in the encoded
   **                            column of the Lookup Definition.
   **
   ** @return                    the desired value for the Organization.
   **                            if no value is found for
   **                            <code>oragnaizationName</code> an empty String
   **                            will be returned.
   */
  public String getLdapName(final String lookupDefinition, final String organizationName, final boolean reverse) {
    final String method = "getLdapName";
    trace(method, SystemMessage.METHOD_ENTRY);

    String dn = SystemConstant.EMPTY;
    try {
      // query the lookup and request only recods where the passed organization
      // name belongs to
      Map filter = new HashMap(1);
      filter.put(reverse ? LookupValue.DECODED : LookupValue.ENCODED, organizationName);
      tcResultSet resultSet = lookupFacade().getLookupValues(lookupDefinition, filter);
      if (resultSet.getRowCount() == 0) {
        String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), lookupDefinition, organizationName };
        error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
      }
      if (resultSet.getRowCount() > 1) {
        String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_ORGANIZATION), lookupDefinition, organizationName };
        error(method, TaskBundle.format(TaskError.RESOURCE_AMBIGUOUS, parameter));
      }
      else {
        resultSet.goToRow(0);
        dn = resultSet.getStringValue(reverse ? LookupValue.ENCODED : LookupValue.DECODED);
      }
    }
    catch (tcColumnNotFoundException e) {
      error(method, TaskBundle.format(TaskError.COLUMN_NOT_FOUND, reverse ? LookupValue.ENCODED : LookupValue.DECODED));
    }
    catch (tcInvalidLookupException e) {
      String[] parameter = { TaskBundle.string(TaskMessage.ENTITY_LOOKUP), lookupDefinition};
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    catch (tcAPIException e) {
      error(method, TaskBundle.format(TaskError.UNHANDLED, e.getLocalizedMessage()));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return dn;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   distinguishedName
  /**
   ** Buils a distinhuished name.
   **
   ** @param  organizationKey    the internal system identifier of a
   **                            organization.
   ** @param  typeMapping        the mapping that defines the tranlastion of the
   **                            organizational types to the name prifix of the
   **                            relative distinguished names
   **
   ** @return                    a full qualified distinguished name that may be
   **                            relative to the root context configured on a
   **                            IT Resource instance.
   */
  public final String distinguishedName(final String organizationKey, final String typeMapping) {
    final String method = "distinguishedName";
    trace(method, SystemMessage.METHOD_ENTRY);

    final String path = compose(hierarchy(organizationKey, typeMapping));

    trace(method, SystemMessage.METHOD_EXIT);
    return path;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildLookupKey
  /**
   ** Buils a full qualified Lookup Definition key.
   **
   ** @param  distinguishedName  a relative distinguished name that has to be
   **                            extended with the specified
   **                            <code>rootContext</code> and prefixed with the
   **                            internal system identifier of an IT Resource
   **                            instance specified by <code>instanceKey</code>.
   ** @param  rootContext        the root conext defined on an IT Resource
   **                            instance that has to be appended to the
   **                            specified <code>distinguishedName</code>.
   ** @param  instanceKey        the internal system identifier of an IT
   **                            Resource instance that is used to prefix the
   **                            resulting name.
   **
   ** @return                    a full qualified Lookup Definition key.
   */
  public final String buildLookupKey(final String distinguishedName, final String rootContext, final long instanceKey) {
    return buildLookupKey(distinguishedName, rootContext, String.valueOf(instanceKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   buildLookupKey
  /**
   ** Buils a full qualified Lookup Definition key.
   **
   ** @param  distinguishedName  a relative distinguished name that has to be
   **                            extended with the specified
   **                            <code>rootContext</code> and prefixed with the
   **                            internal system identifier of an IT Resource
   **                            instance specified by <code>instanceKey</code>.
   ** @param  rootContext        the root conext defined on an IT Resource
   **                            instance that has to be appended to the
   **                            specified <code>distinguishedName</code>.
   ** @param  instanceKey        the internal system identifier of an IT
   **                            Resource instance that is used to prefix the
   **                            resulting name.
   **
   ** @return                    a full qualified Lookup Definition key.
   */
  public final String buildLookupKey(final String distinguishedName, final String rootContext, final String instanceKey) {
    // if we don'T have a valid name return immediately
    if (StringUtility.isEmpty(distinguishedName))
      return SystemConstant.EMPTY;

    // build the stupid encoded value
    Object[] parameter = {instanceKey, distinguishedName, rootContext};
    return String.format("%1$s~%2$s,%3$s", parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hierarchy
  /**
   ** Build the hierarchy of organizations.
   ** <p>
   ** The returning {@link List} contains the entries of the ordered hierarchy.
   ** The first element (zero based index) is the organization passed in. Each
   ** further entry in the container is the superordinated organization of the
   ** preceeding entry.
   ** <p>
   ** The building of the hierarchy stoppes at the first organization that type
   ** is not mapped to a prefix of relative distinguished name specified by the
   ** Lookup Definiton <code>nameMapping</code>
   **
   ** @param  organizationKey    the internal system identifier of a
   **                            organization.
   ** @param  typeMapping        the mapping that defines the translastion of
   **                            the organizational types to the name prefix of
   **                            the relative distinguished names
   **
   ** @return                    a full qualified distinguished name that may be
   **                            relative to the root context configured on a
   **                            IT Resource instance.
   */
  private final List hierarchy(final String organizationKey, final String typeMapping) {
    final String method = "hierarchy";
    trace(method, SystemMessage.METHOD_ENTRY);

    final List                    component = new ArrayList();
    final tcOrganizationHierarchy resolver  = new tcOrganizationHierarchy(this.provider());

    try {
      // query the lookup that defines the type mapping
      final AbstractLookup mapping   = new AbstractLookup(this, typeMapping);
      final Vector         hierarchy = resolver.getHierarchyVector(organizationKey);
      if (mapping != null) {
        for (int i = 0; i < hierarchy.size(); ++i) {
          String[] typemap = (String[])hierarchy.elementAt(i);
          String   prefix  = (String)mapping.get(typemap[1]);
          if ((prefix == null) || (prefix.trim().equals(SystemConstant.EMPTY)))
            break;

          String[] pair    = new String[2];
          pair[0] = prefix;
          pair[1] = typemap[0];
          component.add(pair);
        }
      }
    }
    catch (SystemException e) {
      String[] parameter = {TaskBundle.string(TaskMessage.ENTITY_LOOKUP), typeMapping };
      error(method, TaskBundle.format(TaskError.RESOURCE_NOT_FOUND, parameter));
    }
    finally {
      trace(method, SystemMessage.METHOD_EXIT);
    }
    return component;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   compose
  /**
   ** Composits a distinguished name from the individual component contained in
   ** the specified {@link List}.
   ** <p>
   ** The {@link List} of components is expected as an array of Strings, where
   ** each element is ["object , class", "object name"], e.g.
   ** ["ou", "Oracle Consulting Services"]
   **
   ** @param  component          contains the elements in the tree, deepest one
   **                            first. The String array must be of format
   **                            <code>["object prefix", "object value"]</code>
   **                            where:
   **                            <ul>
   **                              <li><code>object prefix</code> is the objects
   **                                   class type ["CN" | "OU" | ...)
   **                              <li><code>object value</code> is the LDAP
   **                                  objects attribute value
   **                                  ("DSteding" | "finance group" |  ... ).
   **                                  Basically whatever is assigned to the
   **                                  mandatory property "cn" or "ou"
   **                            </ul>
   **
   ** @return                    the distinguished name of the canonical path
   **                           (including the root context), e.g.
   **                           OU=Users,OU=abc,OU=Companies,DC=corpdev,DC=com
   */
  public static String compose(final List component) {
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < component.size(); i++) {
      if (i > 0)
        buffer.append(SystemConstant.COMMA);
      buffer.append(composeName((String[])component.get(i)));
    }
    return buffer.toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   composeName
  /**
   ** This method gives the composed name object value for the given value
   ** string.
   **
   ** @param  component          the attribute prefix, e.g <code>cn</code> and
   **                            the value
   **
   ** @return                    the composed name
   */
  public static String composeName(final Object[] component) {
    return String.format("%1$s=%2$s", component);
  }
}