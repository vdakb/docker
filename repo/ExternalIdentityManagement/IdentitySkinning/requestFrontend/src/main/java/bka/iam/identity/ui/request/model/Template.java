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

    File        :   Template.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Template.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.CollectionUtility;

import bka.iam.identity.ui.RequestException;

////////////////////////////////////////////////////////////////////////////////
// class Template
// ~~~~~ ~~~~~~~~
/**
 ** Declares methods Template.
 ** <br>
 ** This class is simple named list of {@link Application}s.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="template"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://schemas.bka.bund.de/indentity/account/efbs}entity"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="predecessor" type="{http://schemas.bka.bund.de/indentity/account/efbs}application" minOccurs="0"/&gt;
 **         &lt;element name="application" type="{http://schemas.bka.bund.de/indentity/account/efbs}application" maxOccurs="unbounded"/&gt;
 **         &lt;element name="entitlement" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 **       &lt;/sequence&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Template extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final List<String>      EMPTY_ENTITLEMENT = Collections.EMPTY_LIST;
  public static final List<Application> EMPTY_APPLICATION = Collections.EMPTY_LIST;

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6732240368077911084")
  private static final long             serialVersionUID  = 5730477344542423037L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  Application                          predecessor;
  // use a map of strings to prevent dublicate applications
  Map<String, Application>             application;
  // use a set of strings to prevent dublicate entitlements
  List<Pair<String, String>>           entitlement;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Template</code> object that represents a list of
   ** applications and entitlements wich is named.
   **
   ** @param  id                 the model identifier of the template.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  label              the label expression for id to be displayed
   **                            along with the template in the user interface.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  Template(final String id, final String label)
    throws RequestException {

    // ensure inheritance
    super(id, label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Template</code> object that represents a list of
   ** template wich is named.
   **
   ** @param id                  the template identifier associated with a label
   **                            expression for id to be displayed along with
   **                            the template in the user interface.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Template(final Pair<String, String> id)
    throws RequestException {

    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   predecessor
  /**
   ** Returns the predecssor account constraint of the bulk.
   **
   ** @return                    the predecssor account constraint of the bulk.
   **                            <br>
   **                            Possible object {@link Application}.
   */
  public Application predecessor() {
    return this.predecessor;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getApplication
  /**
   ** Return applications that will requested by this template instance.
   ** <p>
   ** This accessor method returns a reference to the live map, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the entitlement
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getApplication().put(newName, newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the map
   ** {@link String} as the key and {@link Application} as the value.
   **
   ** @return                    the applications that will be requested by this
   **                            template instance.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Application} as the value.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public Map<String, Application> getApplication() {
    if (this.application == null)
      this.application = new LinkedHashMap<String, Application>();

    return this.application;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAccount
  /**
   ** Return applications that will requested by this template instance.
   ** <p>
   ** This accessor method returns a reference to the live map, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the entitlement
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getApplication().put(newName, newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the map
   ** {@link String} as the key and {@link Application} as the value.
   **
   ** @return                    the applications that will be requested by this
   **                            template instance.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Application} as the value.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public List<Application> getAccount() {
    if (this.application == null)
      this.application = new LinkedHashMap<String, Application>();

    return CollectionUtility.list(this.application.values());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getEntitlement
  /**
   ** Return entitlements that will requested with the application instance.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the entitlement
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **   getEntitlement().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link String}.
   **
   ** @return                    the entitlements that will be requested with
   **                            the application instances.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public List<Pair<String, String>> getEntitlement() {
    if (this.entitlement == null) {
      this.entitlement = new ArrayList<Pair<String, String>>();
    }

    return this.entitlement;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add application on the template that will be requested for users.
   **
   ** @param  value              the value expression of the application to add.
   **
   */
  public void add(final Application value) {
    if (this.application == null)
      this.application = new LinkedHashMap<String, Application>();

    this.application.put(value.id.tag, value);
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add entitlement on the template that will be to user with the application
   ** instance
   **
   ** @param  value              the value expression of the entitlement to add.
   **
   */
  public void add(final Pair<String, String> value) {
    getEntitlement().add(value);
  }
}