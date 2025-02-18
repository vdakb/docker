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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Offline Target Connector

    File        :   CreateResponse.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    CreateResponse.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.request.role;

import javax.xml.bind.annotation.XmlRootElement;

////////////////////////////////////////////////////////////////////////////////
// class CreateResponse
// ~~~~ ~~~~~~~~~~~~~~~
/**
 ** Java class for anonymous complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element ref="{http://service.api.oia.iam.ots/base}result"/&gt;
 **       &lt;/sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlRootElement(namespace=ObjectFactory.NAMESPACE, name=CreateResponse.LOCAL)
public class CreateResponse extends TransactionResponse {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String LOCAL = "createResponse";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>CreateResponse</code> that allows use as a
   ** JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public CreateResponse() {
    // ensure inheritance
    super();
  }
}