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

    File        :   Permission.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Permission.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import oracle.hst.foundation.object.Pair;

import bka.iam.identity.ui.RequestException;

////////////////////////////////////////////////////////////////////////////////
// class Permission
// ~~~~~ ~~~~~~~~~~
/**
 ** Declares <code>Application Instance</code> entitlement template.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="permission"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Permission extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-4009833273751949728")
  private static final long serialVersionUID = -5389334132338245564L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // use a set of tagged value pairs to prevent dublicate attribute name
  transient List<Attribute>  attribute;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Permission</code> object that represents a template
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
  public Permission(final String id)
    throws RequestException {

    // ensure inheritance
    super(id, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttribute
  /**
   ** Return attributes that will requested with the application instance.
   **
   ** @return                    the attributes that will be requested with the
   **                            application instances.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            is of type {@link Attribute}.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public List<Attribute> getAttribute() {
    return this.attribute == null ? Collections.emptyList() : this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add attribute on the template that will be to user with the application
   ** instance.
   ** <br>
   ** Convinience wrapper for add(Pair()).
   **
   ** @param  id                 the model identifier of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  label              the label expression for id to be displayed
   **                            along with the attribute in the user interface.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param mapping             the attribute mapping (aka the attribute name in
   **                            the datamodel) in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param type                the attribute type of the attribute in Identity
   **                            Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void add(final String id, final String label, final String mapping, final String type)
    throws RequestException {

    add(new Attribute(Pair.of(id, label), mapping, type));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add attribute on the template that will be to user with the application
   ** instance
   **
   ** @param id                  the attribute identifier associated with a
   **                            label expression for id to be displayed along
   **                            with the entity in the user interface.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   ** @param mapping             the attribute mapping (aka the attribute name in
   **                            the datamodel) in Identity Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param type                the attribute type of the attribute in Identity
   **                            Manager.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public void add(final Pair<String, String> id, final String mapping, final String type)
    throws RequestException {

    add(new Attribute(id, mapping, type));
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add attribute on the template that will be to user with the application
   ** instance
   **
   ** @param  attribute          the tagged-value pair to add.
   **                            <br>
   **                            Allowed object is {@link Attribute}.
   */
  public void add(final Attribute attribute) {
    // lazy init the attribute container
    if (this.attribute == null)
      // linked hash set to keep order of attributes fetched from the file
      this.attribute = new ArrayList<Attribute>();

    this.attribute.add(attribute);
  }
}