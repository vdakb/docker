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
    Subsystem   :   Generic Directory Connector

    File        :   TestConnection.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    TestConnection.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.gds.connector;

import java.util.List;

import java.text.SimpleDateFormat;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;

import oracle.iam.identity.connector.integration.FilterFactory;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.foundation.utility.DateUtility;

import oracle.iam.identity.icf.connector.DirectoryFilter;
import oracle.iam.identity.icf.connector.DirectoryFilterTranslator;

////////////////////////////////////////////////////////////////////////////////
// class TestFilterTranslator
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~
/**
 ** The test case to connect to the target system.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestFilterTranslator extends TestCase {


  public TestFilterTranslator() {
    super();
  }

  public static void main(String[] args) {
    Filter filter = FilterFactory.build("startsWith('uid', 'B')");
    if (true) {
      final SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddhhmmss.SSS'Z'");
      final String           criteria  = formatter.format(DateUtility.now());
      filter = FilterBuilder.and(
        filter
      , FilterBuilder.or(
          FilterBuilder.greaterThan(AttributeBuilder.build("createTimestamp", criteria))
        , FilterBuilder.greaterThan(AttributeBuilder.build("modifyTimestamp", criteria))
        )
      );
    }
    try {
      final List<DirectoryFilter> expression = DirectoryFilterTranslator.build(ENDPOINT.schema(), ObjectClass.ACCOUNT).translate(filter);
      CONSOLE.info(expression.toString());
    }
    catch (SystemException e) {
      CONSOLE.error(e.getClass().getSimpleName(), e.code().concat("::").concat(e.getLocalizedMessage()));
    }
  }
}