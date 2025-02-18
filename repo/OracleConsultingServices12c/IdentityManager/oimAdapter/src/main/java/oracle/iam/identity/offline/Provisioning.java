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

    System      :   Oracle Identity Manager Adapter Shared Library
    Subsystem   :   Common Shared Adapter

    File        :   Provisioning.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Provisioning.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2015-10-01  DSteding    First release version
*/

package oracle.iam.identity.offline;

import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import com.thortech.xl.dataaccess.tcDataProvider;

import com.thortech.xl.dataobj.tcDataSet;
import com.thortech.xl.dataobj.PreparedStatementUtil;

import java.io.StringWriter;

import javax.xml.bind.JAXBElement;

import javax.xml.namespace.QName;

import oracle.hst.foundation.utility.StringUtility;

import oracle.iam.platform.Platform;

import oracle.iam.identity.foundation.EntityAdapter;

import oracle.iam.identity.offline.schema.Entity;
import oracle.iam.identity.offline.schema.Payload;
import oracle.iam.identity.offline.schema.Identity;
import oracle.iam.provisioning.adapters.ManualProvisioningPayLoad;

////////////////////////////////////////////////////////////////////////////////
// class Provisioning
// ~~~~~ ~~~~~~~~~~~~
/**
 ** Adpapter dedicated to operate on offline resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.1.0.0
 ** @since   3.1.0.0
 */
public class Provisioning extends EntityAdapter {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the category of the logging facility to use */
  static final String LOGGER_CATEGORY = "OCS.OTS.PROVISIONING";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor

  /**
   ** Constructs an empty <code>Provisioning</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   */
  public Provisioning(final tcDataProvider provider) {
    // ensure inheritance
    this(provider, LOGGER_CATEGORY);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Provisioning</code> task adpater that allows
   ** use as a JavaBean.
   **
   ** @param  provider           the session provider connection
   ** @param  loggerCategory     the category of the logging facility under
   **                            which the log messages will be spooled to the
   **                            output devices.
   */
  public Provisioning(final tcDataProvider provider, final String loggerCategory) {
    // ensure inheritance
    super(provider, loggerCategory);
  }

  /**
   ** Generate {@link Payload} based on parameters passed
   ** @param  instanceKey
   ** @param  operation : Provisioning Operation
   ** @return
   ** @throws Exception
   */
  private Payload createPayload(final String instanceKey, final String operation, final String compositeURL, final String operationKey, final String entityType)
    throws Exception {

    Payload payload = null;
    final String query = "SELECT orc.request_key, orc.orc_tos_instance_key, oiu.oiu_key, oiu.usr_key, oiu.app_instance_key FROM orc orc, oiu oiu WHERE orc.orc_key = ? AND oiu.orc_key = orc.orc_key";
    try {
      PreparedStatementUtil statement = new PreparedStatementUtil();
      statement.setStatement(this.provider(), query);
      statement.setString(1, instanceKey);
      statement.execute(tcDataProvider.DEFAULT);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        dataSet.goToRow(0);
        final String   identityKey = dataSet.getString("usr_key");
        final Identity beneficiary = createIdentity(identityKey);
        final Identity Manager     = createManager(identityKey);
        final Identity requester   = createIdentity(identityKey);
      }
    }
    catch (Exception e) {
      throw e;
    }
    return payload;
  }

  private String generatePayload(final Payload payload) {
    String xml = null;
    /*
    try {
      final JAXBContext  context    = JAXBContext.newInstance(Payload.class);
      final Marshaller   marshaller = context.createMarshaller();
      final StringWriter writer     = new StringWriter();
      final JAXBElement element     = new JAXBElement(new QName(payload.DEFAULT_COMPOSITE_URL, "process", "ns1"), Payload.class, payload);
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      marshaller.marshal(element, writer);
      xml = writer.toString();
      String toReplace = "xmlns:ns2=\"" + payload.DEFAULT_COMPOSITE_URL;
      xml = xml.replace(toReplace, "xmlns:ns1=\"" + payload.getCompositeURL());
      xml = xml.replace("<ns2:", "<ns1:");
      xml = xml.replace("</ns2:", "</ns1:");
    }
    catch (JAXBException e) {
      error("generatePayload", e.getMessage());
      error("generatePayload", "Recovering by generating xml manually instead of JAXB.");
      //In case of jaxb related exceptions, generate xml manually.
      xml = payload.toXML();
    }
    */
    return xml;
  }
  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountName
  /**
   ** Returns the value of the process form field which is tagged as
   ** <code>AccountName</code>.
   **
   ** @param  instanceKey        the system identifier of the provisioning
   **                            process.
   **
   ** @return                    the value of the process form field which is
   **                            tagged as <code>AccountName</code>.
   */
  private String accountName(final String instanceKey)
    throws Exception {

    PreparedStatementUtil pstmt = new PreparedStatementUtil();
    String query1 = "select sdk.sdk_key, sdk.sdk_name from orc, tos, sdk where orc.tos_key = tos.tos_key and tos.sdk_key = sdk.sdk_key and orc.orc_key = ?";
    String query2 = "select sdc.sdc_name from sdk, sdc, sdp where sdk.sdk_key=sdc.sdk_key and sdk.sdk_active_version=sdc.sdc_version and sdc.sdc_key=sdp.sdc_key and sdp.sdp_property_name = 'AccountName' and upper(sdp.sdp_property_value) = 'TRUE' and sdk.sdk_key=?";

    String sdkName  = "";
    String sdcName  = "";
    String sdcValue = "";
    try {
      pstmt.setStatement(this.provider(), query1);
      pstmt.setString(1, instanceKey);
      pstmt.execute(tcDataProvider.DEFAULT);
      tcDataSet ds = pstmt.getDataSet();
      if (ds.getRowCount() > 0) {
        ds.goToRow(0);
        sdkName = ds.getString("sdk_name");
        pstmt = new PreparedStatementUtil();
        pstmt.setStatement(this.provider(), query2);
        pstmt.setString(1, ds.getString("sdk_key"));
        ds = pstmt.getDataSet();
        if (ds.getRowCount() > 0) {
          ds.goToRow(0);
          sdcName = ds.getString("sdc_name");
          if (!StringUtility.isEmpty(sdcName)) {
            String query3 = "select " + sdcName + " as accountname from " + sdkName + " where " + sdkName + ".orc_key = ?";
            pstmt.setStatement(this.provider(), query3);
            pstmt.setString(1, instanceKey);
            pstmt.execute(tcDataProvider.DEFAULT);
            if(ds.getRowCount()>0) {
              ds.goToRow(0);
              sdcValue = ds.getString("accountname");
            }
          }
        }
      }
    }
    catch (Exception e) {
      fatal("accountName", e);
    }
    return sdcValue;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   noteData
  /**
   ** Returns the OSI_NOTE value for the <code>operationKey</code> (sch_key).
   **
   ** @return                    the OSI_NOTE value for the
   **                            <code>operationKey</code> (sch_key).
   */
  private String noteData(final String operationKey)
    throws Exception {

    final String query = "select osi_note from osi where sch_key=?";
    String retVal = null;
    try {
      final PreparedStatementUtil statement = new PreparedStatementUtil();
      statement.setStatement(this.provider(), query);
      statement.setString(1, operationKey);
      statement.execute(tcDataProvider.DEFAULT);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        dataSet.goToRow(0);
        retVal = dataSet.getString("osi_note");
      }
    }
    catch (Exception e) {
      fatal("noteData", e);
      throw e;
    }
    return retVal;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createManager
  /**
   ** Factory method to create an {@link Identity} wrapper for specified system
   ** identifier.
   **
   ** @param  identityKey        the system identifier of the {@link Identity}
   **                            to create.
   **
   ** @return                    the {@link Identity} wrapper for the provided
   **                            system identifier <code>identityKey</code>.
   */
  private Identity createManager(final String identityKey)
    throws Exception {

    final String query = "SELECT usr_manager_key FROM usr WHERE usr_key = ?";
    Identity identity = null;
    try {
      final PreparedStatementUtil statement = new PreparedStatementUtil();
      statement.setStatement(this.provider(), query);
      statement.setString(1, identityKey);
      statement.execute(tcDataProvider.DEFAULT);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        dataSet.goToRow(0);
        identity = createIdentity(dataSet.getString("usr_manager_key"));
      }
    }
    catch (Exception e) {
      fatal("createIdentity", e);
      throw e;
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createIdentity
  /**
   ** Factory method to create an {@link Identity} wrapper for specified system
   ** identifier.
   **
   ** @param  identityKey        the system identifier of the {@link Identity} to
   **                            create.
   **
   ** @return                    the {@link Identity} wrapper for the provided
   **                            system identifier <code>identityKey</code>.
   */
  private Identity createIdentity(final String identityKey)
    throws Exception {

    final String query = "SELECT usr.usr_login, usr.usr_first_name, usr.usr_last_name, usr.usr_display_name, act_name from usr, act where usr.usr_key = ? AND usr.act_key = act.act_key";
    Identity identity = new Identity();
    try {
      final PreparedStatementUtil statement = new PreparedStatementUtil();
      statement.setStatement(this.provider(), query);
      statement.setString(1, identityKey);
      statement.execute(tcDataProvider.DEFAULT);
      final tcDataSet dataSet = statement.getDataSet();
      if (dataSet.getRowCount() > 0) {
        dataSet.goToRow(0);
        identity.setLoginName(dataSet.getString(1));
        identity.setFirstName(dataSet.getString(2));
        identity.setLastName(dataSet.getString(3));
        identity.setOrganizationName(dataSet.getString(4));
      }
    }
    catch (Exception e) {
      fatal("createIdentity", e);
      throw e;
    }
    return identity;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createEntity
  /**
   ** Returns the {@link Entity} wrapper for the provided data.
   **
   ** @param  entityKey          the system identifier of the {@link Entity} to
   **                            create.
   ** @param  entityName         the public name of the {@link Entity} to
   **                            create.
   **
   ** @return                    the {@link Entity} wrapper for the provided
   ** data.
   */
  private Entity createEntity(final long entityKey, final String entityName) {
    return new Entity(entityKey, entityName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   callbackURL
  /**
   ** Returns the callback URL for SOA to call back provisioning webservice.
   **
   ** @return                    the callback URL for SOA to call back
   **                            provisioning webservice.
   */
  private String callbackURL() {
    return Platform.getConfiguration().getDiscoveryConfig() + "/provisioning-callback/ProvisioningCallbackService";
  }
}
