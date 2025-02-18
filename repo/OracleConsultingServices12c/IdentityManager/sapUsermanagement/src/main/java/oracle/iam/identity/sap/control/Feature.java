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

    System      :   Oracle Identity Manager Foundation Library
    Subsystem   :   SAP/R3 Usermanagement Connector

    File        :   Feature.java

    Compiler    :   Oracle JDeveloper 11g

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Feature.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-06-19  DSteding    First release version
*/

package oracle.iam.identity.sap.control;

import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.security.GuardedString;

import oracle.iam.identity.foundation.TaskException;
import oracle.iam.identity.foundation.AbstractLookup;

////////////////////////////////////////////////////////////////////////////////
// class Feature
// ~~~~~ ~~~~~~~
/**
 ** The <code>Feature</code> provides the base feature description of a SAP/R3
 ** System.
 ** <br>
 ** Implementation of a system may vary in locations of certain informations and
 ** object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class Feature extends AbstractLookup {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** Attribute tag which may be defined for a Feature Configuration to specify
   ** the name of the partner system within a secured communication with the
   ** SAP/R3 System.
   ** <p>
   ** The attribute will be mandatory if the attribute
   ** {@link Resource#SECURE_SOCKET} is set to
   ** [<code>true</code> | <code>yes</code>]
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.snc_partnername
   ** {@link DestinationProvider#JCO_SNC_PARTNERNAME}
   **
   ** @see DestinationProvider
   */
  public static final String SECURE_NAME_REMOTE             = DestinationProvider.JCO_SNC_PARTNERNAME;

  /**
   ** Attribute tag which may be defined for a Feature Configuration to specify
   ** the name of the local system within a secured communication with the
   ** SAP/R3 System.
   ** <p>
   ** The attribute will be mandatory if the attribute
   ** {@link Resource#SECURE_SOCKET} is set to
   ** [<code>true</code> | <code>yes</code>]
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.snc_myname {@link DestinationProvider#JCO_SNC_MYNAME}
   **
   ** @see DestinationProvider
   */
  public static final String SECURE_NAME_LOCAL              = DestinationProvider.JCO_SNC_MYNAME;

  /**
   ** Attribute tag which may be defined for a Feature Configuration to specify
   ** the level of security within a secured communication with the SAP/R3
   ** System.
   ** <p>
   ** The attribute will be mandatory if the attribute
   ** {@link Resource#SECURE_SOCKET} is set to
   ** [<code>true</code> | <code>yes</code>]
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.snc_qop {@link DestinationProvider#JCO_SNC_QOP}
   ** <p>
   ** The default SNC Quality of service (Default: 8)
   ** <br>
   ** The following options are available:
   ** <ul>
   **   <li>Value 1
   **       <br>
   **       With this protection level, the system verifies the identity of the
   **       communication partners. This is the minimum protection level offered
   **       by SNC. No actual data protection is provided.
   **   <li>Value 2
   **       <br>
   **       With this protection level, the system detects any changes or
   **       manipulation of the data which may have occurred between the two end
   **       points of a communication. Integrity protection also includes
   **       authentication.
   **   <li>Value 3
   **       <br>
   **       With this protection level, the system encrypts the messages being
   **       transferred to make eavesdropping useless. Privacy protection also
   **       includes integrity protection of the data. This is the maximum level
   **       of protection provided by SNC.
   **   <li>Value 8
   **       <br>
   **       This is default protection
   **   <li>Value 9
   **       <br>
   **       This is maximum protection
   ** </ul>
   **
   ** @see DestinationProvider
   */
  public static final String SECURE_LEVEL                   = DestinationProvider.JCO_SNC_QOP;

  /**
   ** Attribute tag which may be defined for a Feature Configuration to specify
   ** the location of the security libraries to use to secure the communication
   ** with the SAP/R3 System.
   ** <p>
   ** The attribute will be mandatory if the attribute
   ** {@link Resource#SECURE_SOCKET} is set to
   ** [<code>true</code> | <code>yes</code>]
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.snc_library {@link DestinationProvider#JCO_SNC_LIBRARY}
   **
   ** @see DestinationProvider
   */
  public static final String SECURE_LIBRARY_PATH            = DestinationProvider.JCO_SNC_LIBRARY;

  /**
   ** Attribute tag which may be defined on an Feature Configuration to
   ** enable/disable RFC trace (0 or 1) to the SAP/R3 System.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.trace {@link DestinationProvider#JCO_R3NAME}
   **
   ** @see DestinationProvider
   */
  public static final String TRACE_ENABLED                  = DestinationProvider.JCO_TRACE;

  /**
   ** Attribute tag which may be defined on an Feature Configuration to specify
   ** the file system path the trace file will be located within.
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.trace_level.
   **
   ** @see DestinationProvider
   */
  public static final String TRACE_LEVEL                    = "jco.trace_level";

  /**
   ** Attribute tag which may be defined on an Feature Configuration to
   ** enable/disable CPIC trace(-1 [take over environment value ], 0 no trace,
   ** 1,2,3 different trace levels)
   ** <p>
   ** This attribute will be mapped in the destination properties to
   ** jco.client.cpic_trace.
   **
   ** @see DestinationProvider
   */
  public static final String TRACE_LEVEL_CPIC               = "jco.client.cpic_trace";

  /**
   ** Attribute tag which may be defined on an Feature Configuration to
   ** determine whether future dated roles have to be reconciled form the
   ** SAP/R3 System.
   */
  public static final String ROLE_FUTURE_DATED              = "role.dated.future";

  /**
   ** Attribute tag which may be defined on an Feature Configuration to
   ** determine whether past dated roles have to be reconciled form the
   ** SAP/R3 System.
   */
  public static final String ROLE_PAST_DATED               = "role.dated.past";

  /**
   ** Default value of the role objectClass of an entry in a SAP/R3 System.
   */
  public static final String ROLE_OBJECT_CLASS             = "role";

  /**
   ** Default value of the role objectClass of an entry in a SAP/R3 System.
   */
  public static final String ROLE_OBJECT_CLASS_DEFAULT      = "ACTIVITYGROUPS~SUBSYSTEM;AGR_NAME;TO_DAT;FROM_DAT";

  /**
   ** Default value of the profile objectClass of an entry in a SAP/R3 System.
   */
  public static final String PROFILE_OBJECT_CLASS           = "profile";

  /**
   ** Default value of the profile objectClass of an entry in a SAP/R3 System.
   */
  public static final String PROFILE_OBJECT_CLASS_DEFAULT   = "PROFILES~SUBSYSTEM;PROFILE";

  /**
   ** Default value of the group objectClass of an entry in a SAP/R3 System.
   */
  public static final String GROUP_OBJECT_CLASS             = "group";

  /**
   ** Default value of the group objectClass of an entry in a SAP/R3 System.
   */
  public static final String GROUP_OBJECT_CLASS_DEFAULT     = "GROUPS~USERGROUP";

  /**
   ** Default value of the parameter objectClass of an entry in a SAP/R3 System.
   */
  public static final String PARAMETER_OBJECT_CLASS         = "parameter";

  /**
   ** Default value of the parameter objectClass of an entry in a SAP/R3 System.
   */
  public static final String PARAMETER_OBJECT_CLASS_DEFAULT = "PARAMETER1~PARID;PARVA";

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

	private String[]      filteredAccounts              = { "DDIC", "SAP*", "SAPCPIC" };
	private GuardedString password                      = new GuardedString("Nr&v8tP(%s".toCharArray());

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Feature</code> which is associated with the
   ** specified loggable.
   ** <p>
   ** The instance created through this constructor has to populated manually
   ** and does not belongs to an Oracle Identity Manager Object.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   */
  public Feature(final Loggable loggable) {
    // ensure inheritance
    super(loggable);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Feature</code> which is associated with the
   ** specified task and belongs to the Metadata Descriptor specified by the
   ** given name.
   ** <br>
   ** The Metadata Descriptor will be populated from the repository of the
   ** Oracle Identity Manager and also all well known attributes.
   **
   ** @param  loggable           the {@link Loggable} which has instantiated
   **                            this <code>Metadata Descriptor</code>
   **                            configuration wrapper.
   ** @param  instanceName       the name of the <code>Metadata Descriptor</code>
   **                            instance where this configuration wrapper will
   **                            obtains the values.
   **
   ** @throws TaskException      if the <code>Metadata Descriptor</code> is not
   **                            defined in the Oracle Identity Manager metadata
   **                            entries.
   */
  public Feature(final Loggable loggable, final String instanceName)
    throws TaskException {

    // ensure inheritance
    super(loggable, instanceName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureNameRemote
  /**
   ** Returns the name of the remote system within a secured communication
   ** with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_NAME_REMOTE}.
   **
   ** @return                    the name of the remote system within a
   **                            secured communication with the SAP/R3 System.
   */
  public final String secureNameRemote() {
    return stringValue(SECURE_NAME_REMOTE);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureLevel
  /**
   ** Returns the level of security level within a secured communication with
   ** the SAP/R3 System.
   ** <p>
   ** The default SNC Quality of service (Default: 8)
   ** <br>
   ** The following options are available:
   ** <ul>
   **   <li>Value 1
   **       <br>
   **       With this protection level, the system verifies the identity of the
   **       communication partners. This is the minimum protection level offered
   **       by SNC. No actual data protection is provided.
   **   <li>Value 2
   **       <br>
   **       With this protection level, the system detects any changes or
   **       manipulation of the data which may have occurred between the two end
   **       points of a communication. Integrity protection also includes
   **       authentication.
   **   <li>Value 3
   **       <br>
   **       With this protection level, the system encrypts the messages being
   **       transferred to make eavesdropping useless. Privacy protection also
   **       includes integrity protection of the data. This is the maximum level
   **       of protection provided by SNC.
   **   <li>Value 8
   **       <br>
   **       This is default protection
   **   <li>Value 9
   **       <br>
   **       This is maximum protection
   ** </ul>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_LEVEL}.
   **
   ** @return                     the level of security within a secured
   **                             communication with the SAP/R3 System.
   */
  public final int secureLevel() {
    return integerValue(SECURE_LEVEL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureNameLocal
  /**
   ** Returns the name of the local system within a secured communication
   ** with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_NAME_LOCAL}.
   **
   ** @return                    the name of the local system within a
   **                            secured communication with the SAP/R3 System.
   */
  public final String secureNameLocal() {
    return stringValue(SECURE_NAME_LOCAL);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   secureLibraryPath
  /**
   ** Returns the location of the security libraries to use to secure the
   ** communication with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #SECURE_LIBRARY_PATH}.
   **
   ** @return                    the location of the security libraries to use
   **                            to secure the communication with the SAP/R3
   **                            System.
   */
  public final String secureLibraryPath() {
    return stringValue(SECURE_LIBRARY_PATH);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traceEnabled
  /**
   ** Returns the mode of tracing the RFC connection with the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #TRACE_ENABLED}.
   ** <p>
   ** If {@link #TRACE_ENABLED} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns <code>false</code>.
   **
   ** @return                    <code>true</code> if trace is enabled;
   **                            otherwise <code>false</code>.
   */
  public final boolean traceEnabled() {
    return booleanValue(TRACE_ENABLED, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   traceLevel
  /**
   ** Returns the level of tracing from 0 to 10 for the RFC connection with the
   ** SAP/R3 System.
   ** <ul>
   **   <li><code>0</code> - no trace
   **   <li><code>1</code> - JCo version and runtime environment info
   **                        + important public API calls
   **   <li><code>2</code> - + additional public API calls (e.g. getClient
   **                          and releaseClient)
   **   <li><code>3</code> - + internal middleware calls (JNI / JRfc layer)
   **   <li><code>4</code> - + more internal middleware details (e.g.
   **                          enter/leave API info)
   **   <li><code>5</code> - + record memory allocation info
   **                        + important caller stack trace info (e.g. for
   **                          removePool, setTraceLevel)
   **   <li><code>6</code> - + RFC meta data (name, type, offset, length,
   **                          import/export-options)
   **                        + ASCII content data (first 1000 chars of structs /
   **                          first 5 rows of tables)
   **   <li><code>7</code> - + additional Hex values for content data
   **   <li><code>8</code> - + full content data dump (no char or row limit)
   **   <li><code>9</code> - + Java to/from C marshalling field data and
   **                          codepage converter calls
   **   <li><code>10</code> - + memory leak analysis info (Record ObjectIDs &
   **                           detailed freeRecord)
   ** </ul>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #TRACE_LEVEL}.
   ** <p>
   ** If {@link #TRACE_LEVEL} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns <code>0</code>.
   **
   ** @return                    the level of tracing for the RFC connection
   **                            with the SAP/R3 System.
   */
  public final int traceLevel() {
    return integerValue(TRACE_LEVEL, 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleFutureDated
  /**
   ** Whether future dated roles have to be reconciled form the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #ROLE_FUTURE_DATED}.
   ** <p>
   ** If {@link #ROLE_FUTURE_DATED} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns <code>true</code>.
   **
   ** @return                    <code>true</code> if future dated roles have to
   **                            be reconciled form the SAP/R3 System; otherwise
   **                            <code>false</code>.
   */
  public final boolean roleFutureDated() {
    return booleanValue(ROLE_FUTURE_DATED, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rolePastDated
  /**
   ** Whether future dated roles have to be reconciled form the SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #ROLE_PAST_DATED}.
   ** <p>
   ** If {@link #ROLE_PAST_DATED} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns <code>true</code>.
   **
   ** @return                    <code>true</code> if future dated roles have to
   **                            be reconciled form the SAP/R3 System; otherwise
   **                            <code>false</code>.
   */
  public final boolean rolePastDated() {
    return booleanValue(ROLE_PAST_DATED, true);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   roleObjectClass
  /**
   ** Returns the schema definition of the objectClass role of a SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #ROLE_OBJECT_CLASS}.
   ** <p>
   ** If {@link #ROLE_OBJECT_CLASS} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link #ROLE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass role
   **                            of a SAP/R3 System.
   */
  public final String roleObjectClass() {
    return stringValue(ROLE_OBJECT_CLASS, ROLE_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   groupObjectClass
  /**
   ** Returns the schema definition of the objectClass group of a SAP/R3 System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #GROUP_OBJECT_CLASS}.
   ** <p>
   ** If {@link #GROUP_OBJECT_CLASS} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link #GROUP_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass group
   **                            of a SAP/R3 System.
   */
  public final String groupObjectClass() {
    return stringValue(GROUP_OBJECT_CLASS, GROUP_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   profileObjectClass
  /**
   ** Returns the schema definition regarding objectClass profile of a SAP/R3
   ** System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PROFILE_OBJECT_CLASS}.
   ** <p>
   ** If {@link #PROFILE_OBJECT_CLASS} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link #PROFILE_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass
   **                            profile of a SAP/R3 System.
   */
  public final String profileObjectClass() {
    return stringValue(PROFILE_OBJECT_CLASS, PROFILE_OBJECT_CLASS_DEFAULT);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parameterObjectClass
  /**
   ** Returns the schema definition regarding objectClass parameter of a SAP/R3
   ** System.
   ** <p>
   ** Convenience method to shortens the access to the resource attribute
   ** {@link #PARAMETER_OBJECT_CLASS}.
   ** <p>
   ** If {@link #PARAMETER_OBJECT_CLASS} is not mapped in the underlying
   ** {@link AbstractLookup} this method returns
   ** {@link #PARAMETER_OBJECT_CLASS_DEFAULT}.
   **
   ** @return                    the schema definition of the objectClass
   **                            parameter of a SAP/R3 System.
   */
  public final String parameterObjectClass() {
    return stringValue(PARAMETER_OBJECT_CLASS, PARAMETER_OBJECT_CLASS_DEFAULT);
  }
}