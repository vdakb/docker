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
    Subsystem   :   Branding Customization

    File        :   Component.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Component.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      18.01.2021  SBernet    First release version
*/

package bka.iam.identity.ui.shell.branding.model;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.Serializable;

import bka.iam.identity.ui.BrandingError;
import bka.iam.identity.ui.BrandingException;

import bka.iam.identity.ui.shell.branding.Adapter;

////////////////////////////////////////////////////////////////////////////////
// class Component
// ~~~~~ ~~~~~~~~~
/**
 * Declares methods Component.
 * <br>
 * This class is simple named list of {@link bka.iam.identity.ui.shell.branding.model.Rule} s.
 * <p>
 * The following schema fragment specifies the expected content contained
 * within this class.
 * <pre>
 * &lt;complexType name="environment"&gt;
 * &lt;complexContent&gt;
 * &lt;extension base="{http://schemas.bka.bund.de/indentity/account/efbs}entity"&gt;
 * &lt;sequence&gt;
 * &lt;element name="template" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0" maxOccurs="unbounded"/&gt;
 * &lt;/sequence&gt;
 * &lt;/extension&gt;
 * &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * @author sylvert.bernet@oracle.com
 * @version 1.0.0.0
 * @since 1.0.0.0
 */
public class Component implements Serializable {

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

  final transient String name;
  // use a map of strings to prevent dublicate rules
  Map<String, Rule>             rule;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Component</code> object that represents a list of
   ** Rules that should be evaluated.
   **
   ** @param  name               the name of the component
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws BrandingException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Component(String name)
    throws BrandingException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (name == null)
      throw Adapter.exception(BrandingError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_NAME);

    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRule
  /**
   * Return a {@link Map} of rules that can be applied for this component
   * instance.
   * <p>
   * This accessor method returns a reference to the live map, not a snapshot.
   * Therefore any modification you make to the returned list will be present
   * inside the JAXB object.
   * <br>
   * <p>
   * For example, to add a new item, do as follows:
   * <pre>
   * getRule().put(newName, newRule);
   * </pre>
   * Objects of the following type(s) are allowed in the map
   * {@link String} as the key and {@link bka.iam.identity.ui.shell.branding.model.Rule} as the value.
   *
   * @return the templates that can be requested by this
   * environment instance.
   * <br>
   * Possible object is {@link Map} where each
   * element is of type {@link String} as the key
   * and {@link bka.iam.identity.ui.shell.branding.model.Rule} as the value.
   * <br>
   * Never <code>null</code>.
   */
  public Map<String, Rule> getRule() {
    if (this.rule == null)
      this.rule = new LinkedHashMap<String, Rule>();

    return this.rule;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add rule on the component that will be evaluated for users.
   **
   ** @param  value              the rule instance of the component to add.
   **
   */
  public void add(final Rule value) {
    if (this.rule == null)
      this.rule = new LinkedHashMap<String, Rule>();

    this.rule.put(value.id, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getDefault
  /**
   ** Return the default rule associated to the component. If no default rule
   ** is found, null is returned.
   **
   ** @return                  the default rule
   **                          <br>
   **                          Possible object is {@link Rule}.
   */
  public Rule getDefault() {
    for (Map.Entry<String, Rule> rule : this.rule.entrySet()) {
      if (rule.getValue().isDefault()) {
        return rule.getValue();
      }
    }
    return null;
  }
}