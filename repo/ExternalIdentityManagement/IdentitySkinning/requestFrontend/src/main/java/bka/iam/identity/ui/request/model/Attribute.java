package bka.iam.identity.ui.request.model;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestException;

import bka.iam.identity.ui.request.Adapter;

//////////////////////////////////////////////////////////////////////////////
// class Attribute
// ~~~~ ~~~~~~~~~~
/**
 ** Declares <code>Application Instance</code> or <code>Entitlement</code>
 ** attribute.
 ** <p>
 ** An <code>Attribute</code> defines the mapping between physical and logical
 ** <code>Account</code> attribute access for an <code>Identity</code>.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;extension base="{http://schemas.bka.bund.de/indentity/account/efbs}entity"&gt;
 **       &lt;attribute name="mapping" use="required" type="{http://schemas.bka.bund.de/indentity/account/efbs}token" /&gt;
 **       &lt;attribute name="type" use="required"&gt;
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
public class Attribute extends Entity {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  String       value;
  final String type;
  final String mapping;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
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
  public Attribute(final String id, final String label, final String mapping, final String type)
      throws RequestException {

      // ensure inheritance
      this(Pair.of(id, label), mapping, type);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Attribute</code> that allows use as a JavaBean.
   **
   ** @param id                  the attribute identifier associated with a
   **                            label expression to be displayed along with
   **                            the attribute in the user interface.
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
  public Attribute(final Pair<String, String> id, final String mapping, final String type)
    throws RequestException {

    // ensure inheritance
    super(id);

    // prevent bogus input
    if (StringUtility.isEmpty(mapping))
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_MAPPING);

    if (StringUtility.isEmpty(type))
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_TYPE);

    // initialize arguments
    this.type    = type;
    this.mapping = mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method: mapping
  /**
   ** Return the attribute mapping value.
   **
   ** @return                  the value mapping expression for this
   **                          attribute.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String mapping() {
    return this.mapping;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Return the attribute mapping type.
   **
   ** @return                  the value type expression for this attribute.
   **                          <br>
   **                          Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the attribute value expression.
   **
   ** @param value             the value expression for this attribute.
   **                          <br>
   **                          Allowed object is {@link String}.
   */
  public final void value(final String value) {
    this.value = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getValue
  /**
   ** Return the attribute value expression.
   **
   ** @return                the value expression for this attribute.
   **                        <br>
   **                        Possible object is {@link String}.
   */
  public final String getValue() {
    return this.value;
  }
}