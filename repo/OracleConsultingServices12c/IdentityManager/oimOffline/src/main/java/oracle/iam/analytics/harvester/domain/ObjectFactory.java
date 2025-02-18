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

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   ObjectFactory.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    ObjectFactory.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain;

import java.util.Date;

import javax.xml.bind.annotation.XmlRegistry;

////////////////////////////////////////////////////////////////////////////////
// class ObjectFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** This object contains factory methods for each Java content interface and
 ** Java element interface generated in the
 ** oracle.iam.analytics.harvester.domain package.
 ** <p>
 ** An ObjectFactory allows you to programatically construct new instances of
 ** the Java representation for XML content. The Java representation of XML
 ** content can consist of schema derived interfaces and classes representing
 ** the binding of schema type definitions, element declarations and model
 ** groups. Factory methods for each of these are provided in this class.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlRegistry
public class ObjectFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String NAMESPACE = "http://service.api.oia.iam.ots/base";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Create a new <code>ObjectFactory</code> that can be used to create new
   ** instances of schema derived classes for package:
   ** <code>oracle.iam.identity.ots.domain</code>
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ObjectFactory() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResult
  /**
   ** Create an instance of {@link Result}.
   **
   ** @return                    an instance of {@link Result}.
   */
  public Result createResult() {
    return new Result();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResult
  /**
   ** Create an instance of {@link Result} with the specified status.
   **
   ** @param  status             the initial {@link Status} of the created
   **                            {@link Result}.
   **
   ** @return                    an instance of {@link Result}.
   */
  public Result createResult(final Status status) {
    return new Result(status);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResultMessage
  /**
   ** Create an instance of {@link Result.Message}.
   **
   ** @return                    an instance of {@link Result.Message}.
   */
  public Result.Message createResultMessage() {
    return new Result.Message();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   createResultMessage
  /**
   ** Create an instance of {@link Result.Message}.
   **
   ** @param  severity           the {@link Severity} of the message event.
   ** @param  timestamp          the timestamp the message event occured.
     ** @param  location         the location context of the message event.
   ** @param  message            the message text of the event.
   **
   ** @return                    an instance of {@link Result.Message}.
   */
  public Result.Message createResultMessage(final Severity severity, final Date timestamp, final String location, final String message) {
    return new Result.Message(severity, timestamp, location, message);
  }
}