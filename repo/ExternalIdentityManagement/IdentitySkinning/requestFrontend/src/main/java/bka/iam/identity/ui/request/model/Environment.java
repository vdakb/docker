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

    File        :   Environment.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Environment.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.util.Map;
import java.util.LinkedHashMap;

import oracle.hst.foundation.object.Pair;

import bka.iam.identity.ui.RequestException;

////////////////////////////////////////////////////////////////////////////////
// class Environment
// ~~~~~ ~~~~~~~~~~~
/**
 ** Declares methods Environment.
 ** <br>
 ** This class is simple named list of {@link Application}s.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="environment"&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://schemas.bka.bund.de/indentity/account/efbs}entity"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="template" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0" maxOccurs="unbounded"/&gt;
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
public class Environment extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:4028788087846418358")
  private static final long serialVersionUID = -3106596773659207447L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  // use a map of strings to prevent dublicate applications
  Map<String, Template>             template;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Environment</code> object that represents a list of
   ** templates wich is named.
   **
   ** @param  id                 the model identifier of the environment.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  label              the label expression for id to be displayed
   **                            along with the environment in the user
   **                            interface.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  Environment(final String id, final String label)
    throws RequestException {

    // ensure inheritance
    super(id, label);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Environment</code> object that represents a list of
   ** templates wich is named.
   **
   ** @param id                  the environment identifier associated with a
   **                            label expression for id to be displayed along
   **                            with the environment in the user interface.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Environment(final Pair<String, String> id)
    throws RequestException {

    // ensure inheritance
    super(id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getTemplate
  /**
   ** Return templates that can requested for this environment instance.
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
   **   getTemplate().put(newName, newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the map
   ** {@link String} as the key and {@link Application} as the value.
   **
   ** @return                    the templates that can be requested by this
   **                            environment instance.
   **                            <br>
   **                            Possible object is {@link Map} where each
   **                            element is of type {@link String} as the key
   **                            and {@link Template} as the value.
   **                            <br>
   **                            Never <code>null</code>.
   */
  public Map<String, Template> getTemplate() {
    if (this.template == null)
      this.template = new LinkedHashMap<String, Template>();

    return this.template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add template on the environment that will be requestable for users.
   **
   ** @param  value              the value expression of the template to add.
   **
   */
  public void add(final Template value) {
    if (this.template == null)
      this.template = new LinkedHashMap<String, Template>();

    this.template.put(value.id.tag, value);
  }
}