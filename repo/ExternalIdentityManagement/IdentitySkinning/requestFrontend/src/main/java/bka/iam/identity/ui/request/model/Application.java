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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Application.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Application.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.util.List;
import java.util.ArrayList;

import oracle.hst.foundation.object.Pair;

import bka.iam.identity.ui.RequestException;

////////////////////////////////////////////////////////////////////////////////
// class Application
// ~~~~~ ~~~~~~~~~~~
/**
 ** Declares <code>Application Instance</code> account template.
 ** <p>
 ** An <code>Application</code> defines the attributes needed to be request an
 ** <code>Account</code> for an <code>Identity</code> if its tagged as an
 ** <code>application</code>.
 ** <p>
 ** If the <code>Application</code> its tagged as a <code>predecessor</code> the
 ** instance defines an <code>Account</code> that needs to be provisioned
 ** for an <code>Identity</code> to be able to request the
 ** <code>Application</code>s tagged as <code>application</code> or the
 ** <code>Permission</code>s tagged as <code>entitlement</code> in the same
 ** template.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="application"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="attribute" maxOccurs="unbounded" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;extension base="{http://schemas.bka.bund.de/indentity/account/efbs}entity"&gt;
 **                 &lt;attribute name="mapping" use="required" type="{http://schemas.bka.bund.de/indentity/account/efbs}token" /&gt;
 **                 &lt;attribute name="type"    use="required"&gt;
 **                   &lt;simpleType&gt;
 **                     &lt;restriction base="{http://schemas.bka.bund.de/indentity/account/efbs}token"&gt;
 **                       &lt;enumeration value="Byte"/&gt;
 **                       &lt;enumeration value="Long"/&gt;
 **                       &lt;enumeration value="Date"/&gt;
 **                       &lt;enumeration value="Short"/&gt;
 **                       &lt;enumeration value="Double"/&gt;
 **                       &lt;enumeration value="String"/&gt;
 **                       &lt;enumeration value="Integer"/&gt;
 **                       &lt;enumeration value="Boolean"/&gt;
 **                     &lt;/restriction&gt;
 **                   &lt;/simpleType&gt;
 **                 &lt;/attribute&gt;
 **               &lt;/extension&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Application extends Permission {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:7568087788899872725")
  private static final long  serialVersionUID = 1103237700738760856L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** indicates that an application already exists for an account and needs to
   ** be modified instead of request a new account
   */
  String                     accountID   = null;

  // use a set of strings to prevent dublicate permissions
  transient List<Permission> permission;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Application</code> object that represents a template
   ** for an account.
   **
   ** @param  id                 the name of the application instance that will
   **                            be represented by this wrapper.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Application(final String id)
    throws RequestException {

    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** Determines how to handle the request operation.
   ** <p>
   ** If a certain account does not exists the appilcation needs to be tagged as
   ** <code>RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION</code>.
   ** If such account exists the model to use is
   ** <code>RequestConstants.MODEL_MODIFY_ACCOUNT_OPERATION</code>.
   ** <br>
   ** The string is set in the evaluation phase of the request and used later on
   ** in the submit phase to set the correct model value.
   **
   ** @param  value              a valid account instance id if the account
   **                            evaluation has an account matched with the same
   **                            properties; otherwise <code>null</code>.
   */
  public final void accountID(final String value) {
    this.accountID = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountID
  /**
   ** Determines how to handle the request operation.
   ** <p>
   ** If a certain account does not exists the appilcation needs to be tagged as
   ** <code>RequestConstants.MODEL_PROVISION_APPLICATION_INSTANCE_OPERATION</code>.
   ** If such account exists the model to use is
   ** <code>RequestConstants.MODEL_MODIFY_ACCOUNT_OPERATION</code>.
   ** <br>
   ** The string is set in the evaluation phase of the request and used later on
   ** in the submit phase to set the correct model value.
   **
   ** @return                    a valid account instance id if the account
   **                            evaluation has an account matched with the same
   **                            properties; otherwise <code>null</code>.
   */
  public final String accountID() {
    return this.accountID;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getPermission
  /**
   ** Return permissions that will requested with the application instance.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the permission
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getPermission().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link String}.
   **
   ** @return                    the permissions that will be requested with
   **                            the application instances.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link Pair}.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public List<Permission> getPermission() {
    if (this.permission == null)
      this.permission = new ArrayList<Permission>();

    return this.permission;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Adds permission on the template that will be to user with the application
   ** instance
   **
   ** @param  permission         the permission to add.
   **                            <br>
   **                            Allowed object is {@link Permission}.
   */
  public void add(final Permission permission) {
    if (this.permission == null)
      this.permission = new ArrayList<Permission>();

    this.permission.add(permission);
  }
}