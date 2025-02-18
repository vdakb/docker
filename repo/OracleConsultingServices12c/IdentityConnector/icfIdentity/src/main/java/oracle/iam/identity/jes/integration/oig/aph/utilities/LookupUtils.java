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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   BKA Access Policy Holder

    File        :   LookupUtils.java

    Compiler    :   JDK 1.8

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implement the class
                    LookupUtils.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.09.2024  TSebo    First release version
*/
package oracle.iam.identity.jes.integration.oig.aph.utilities;

import Thor.API.Exceptions.tcAPIException;
import Thor.API.Exceptions.tcColumnNotFoundException;
import Thor.API.Exceptions.tcInvalidLookupException;
import Thor.API.tcResultSet;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

////////////////////////////////////////////////////////////////////////////////
// class LookupUtils
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
/**
 ** The <code>LookupUtils</code> is a utility class related to OIM Lookups
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class LookupUtils {
  
  private static final String className = LookupUtils.class.getName();
  private static final Logger logger = Logger.getLogger(className);
  
  private Thor.API.Operations.tcLookupOperationsIntf lookupService;
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   * Construct LookupUtils
   * @param lookupService
   */
  public LookupUtils(Thor.API.Operations.tcLookupOperationsIntf lookupService) {
    super();
    this.lookupService = lookupService;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLookupValues
  /**
   ** Read Lookup values from OIM
   ** @param lookupName Lookup name
   ** @return Map where key represent "lookup code" and value represents "lookup decode".
   */
  public Map<String, String> getLookupValues(String lookupName){
    String methodName = "getLookupValues";
    logger.entering(className, methodName);
      
    Map<String, String> response = new HashMap<>();
    try{
      tcResultSet lookupValues = lookupService.getLookupValues(lookupName);
      for (int i = 0; i < lookupValues.getRowCount(); i++) {
        lookupValues.goToRow(i);
        response.put(lookupValues.getStringValue("Lookup Definition.Lookup Code Information.Code Key"),
                     lookupValues.getStringValue("Lookup Definition.Lookup Code Information.Decode"));
      }
    }
    catch(tcInvalidLookupException | tcAPIException | tcColumnNotFoundException e){
      logger.warning("Lookup definition '"+lookupName+"' doesn't exist. Error message:"+e);
    }
    logger.exiting(className, methodName);
    return response;
  }
}
