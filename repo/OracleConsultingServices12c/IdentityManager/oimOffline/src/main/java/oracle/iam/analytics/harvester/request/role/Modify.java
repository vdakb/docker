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

    File        :   Modify.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    Modify.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.request.role;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

import oracle.iam.analytics.harvester.domain.role.Policy;
import oracle.iam.analytics.harvester.domain.role.Context;

////////////////////////////////////////////////////////////////////////////////
// class Modify
// ~~~~ ~~~~~~~
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
 **         &lt;element name="context" type="{http://service.api.oia.iam.ots}context"/&gt;
 **         &lt;element name="policy"  type="{http://service.api.oia.iam.ots}policy"/&gt;
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
@XmlRootElement(name=Modify.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={Context.LOCAL, Policy.LOCAL})
public class Modify {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String LOCAL = "modify";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true, nillable = false, namespace=oracle.iam.analytics.harvester.domain.role.ObjectFactory.NAMESPACE, name=Context.LOCAL)
  protected Context context;
  @XmlElement(required=true, nillable = false, namespace=oracle.iam.analytics.harvester.domain.role.ObjectFactory.NAMESPACE, name="policy")
  protected Policy  policy;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Modify</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Modify() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Modify</code> request that wraps the specified
   ** {@link Policy} and the invocation parameter.
   **
   ** @param  context            the transactional {@link Context} determining
   **                            the behavior of the servive.
   ** @param  policy             the {@link Policy} of this request.
   */
  public Modify(final Context context, final Policy policy) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.context = context;
    this.policy  = policy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setContext
  /**
   ** Sets the value of the context property.
   **
   ** @param  value              allowed object is {@link Context}.
   */
  public final void setContext(final Context value) {
    this.context = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getContext
  /**
   ** Returns the value of the context property.
   **
   ** @return                    possible object is {@link Context}.
   */
  public final Context getContext() {
    return context;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setPolicy
  /**
   ** Sets the value of the policy property.
   **
   ** @param  value              allowed object is {@link Policy}.
   */
  public final void setPolicy(final Policy value) {
    this.policy = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPolicy
  /**
   ** Returns the value of the policy property.
   **
   ** @return                    possible object is {@link Policy}.
   */
  public final Policy getPolicy() {
    return this.policy;
  }
}