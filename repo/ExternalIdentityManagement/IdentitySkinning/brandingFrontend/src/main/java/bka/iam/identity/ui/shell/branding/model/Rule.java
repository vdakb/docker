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

    File        :   Rule.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Rule.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      25.01.2020  SBernet    First release version
*/
package bka.iam.identity.ui.shell.branding.model;

import java.io.Serializable;

import oracle.hst.foundation.utility.StringUtility;

import bka.iam.identity.ui.BrandingError;
import bka.iam.identity.ui.BrandingException;

import bka.iam.identity.ui.shell.branding.Adapter;

//////////////////////////////////////////////////////////////////////////////
// class Rule
// ~~~~ ~~~~~
/**
 ** Declares <code>Rule</code>  applied for the <code>Component</code>
 ** <p>
 ** A <code>rule</code> defines the value of <code>Component</code> that
 ** will be applied if it matches with the defined user attribute and filter
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://schemas.bka.bund.de/identity/ui/custom}"&gt;
 **       &lt;attribute name="attribute" use="required" &gt;
 **       &lt;attribute name="filter"    use="required"&gt;
 **         &lt;simpleType&gt;
 **           &lt;restriction base="{http://schemas.bka.bund.de/indentity/account/efbs}token"&gt;
 **             &lt;enumeration value="Byte"/&gt;
 **             &lt;enumeration value="Long"/&gt;
 **             &lt;enumeration value="Date"/&gt;
 **             &lt;enumeration value="Short"/&gt;
 **             &lt;enumeration value="Double"/&gt;
 **             &lt;enumeration value="String"/&gt;
 **             &lt;enumeration value="Integer"/&gt;
 **             &lt;enumeration value="Boolean"/&gt;
 **           &lt;/restriction&gt;
 **         &lt;/simpleType&gt;
 **       &lt;/attribute&gt;
 **     &lt;/extension&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 */
public class Rule implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-7317395049474314300")
  private static final long serialVersionUID = 1357227268013815938L;
  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final String  id;
  final String  attribute;
  final String  filter;
  String        value;
  final boolean _default;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Rule</code> that allows use as a JavaBean.
   **
   ** @param  id                 the model identifier of the attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  attribute          the user attribute that will be used to apply
   **                            the filer expression
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param filter              the regex expression to match
   **                            <br>
   **                            Allowed object is {@link String}.

   **
   ** @throws BrandingException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Rule(String id, String attribute, String filter)
    throws BrandingException {

    //ensure inheritance
    super();
    // prevent bogus input
    if (StringUtility.isEmpty(id))
      throw Adapter.exception(BrandingError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_ID);

    this._default = StringUtility.isEmpty(attribute) && StringUtility.isEmpty(filter);

    if (!this._default && StringUtility.isEmpty(attribute))
      throw Adapter.exception(BrandingError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_ATTRIBUTE);
    if (!this._default && StringUtility.isEmpty(filter))
      throw Adapter.exception(BrandingError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_FILTER);

    this.id = id;
    this.attribute = attribute;
    this.filter = filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: getId
  /**
   ** Returns the id of the rule.
   **
   ** @return                  the identifier of the rule.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String getId() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getAttribute
  /**
   ** Returns the user attribute that will be used to applied the regex
   ** expression.
   **
   ** @return                  the user attribute that will be used to applied
   **                          the regex expression.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String getAttribute() {
    return this.attribute;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: getFilter
  /**
   ** Return egex expression that defines that rule.
   **
   ** @return                  the regex expression that defines that rule.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String getFilter() {
    return this.filter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method: isDefault
  /**
   * Return <code>true</code> if the rule is the default rule applied on the
   * {@link bka.iam.identity.ui.shell.branding.model.Component}
   *
   * @return          <code>true</code> if the intanciate rule is a default rule
   *                  Possible object is {@link boolean}.
   */
  public final boolean isDefault() {
    return this._default;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the expression value that will be use if the {@link Rule} matches.
   **
   ** @param value             the value for this {@link Rule}.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  public final void setValue(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue
  /**
   ** Return the expression value of this {@link Rule}.
   **
   ** @return                the expression value for this {@link Rule}.
   **                        <br>
   **                        Possible object is {@link String}.
   */
  public final String getValue() {
    return this.value;
  }
}