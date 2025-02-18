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

    File        :   Configuration.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Configuration.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/
package bka.iam.identity.ui.shell.branding.model;

import java.io.Serializable;

import java.util.LinkedHashMap;

import org.w3c.dom.Document;

import bka.iam.identity.ui.BrandingException;

////////////////////////////////////////////////////////////////////////////////
// class Configuration
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Java class for configuration complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="configuration"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="environment" type="{http://schemas.bka.bund.de/indentity/account/efbs}environment" minOccurs="0" maxOccurs="unbounded"/&gt;
 **       &lt;/sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@SuppressWarnings("oracle.jdeveloper.java.semantic-warning")
public class Configuration extends    LinkedHashMap<String, Component>
                           implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-2343536760171787822")
  private static final long  serialVersionUID = 1793252556275417739L;

  //////////////////////////////////////////////////////////////////////////////
  // private attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the system id of the XML document loaded */
  final String               id;

  //////////////////////////////////////?////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Configuration</code> that allows use as a
   ** JavaBean.
   **
   ** @param id                  the configuration identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Configuration(final String id)
    throws BrandingException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (id == null)
      throw new BrandingException("Argument null. Id Configuration");
      //throw Adapter.exception(ServiceError.ARGUMENT_IS_NULL, "" /* Serializer.ATTRIBUTE_ID */);

    // initialize arguments
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Sets the configuration identifier.
   **
   ** @return                    the configuration identifier.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public final String id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Sets the configuration identifier.
   **
   ** @return                    the configuration identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getId() {
    return this.id;
  }


  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to unmarshal a {@link Configuration} from a
   ** {@link Document}.
   **
   ** @param  document           the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link Document}.
   **
   ** @return                    the populated {@link Configuration}.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Configuration}.
   **
   ** @throws BrandingException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Configuration build(final Document document)
    throws BrandingException {

    // prevent bogus input
    if (document == null)
      throw new BrandingException("Argument null. document");
      //throw Adapter.exception(ServiceError.ARGUMENT_IS_NULL, "document");

    // create the template descriptor that provides the attribute mapping and
    // transformations to be applied on the requestable accounts and entitlements
    final Configuration model = new Configuration(document.getDocumentURI());
    Serializer.unmarshal(model, document);
    return model;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to unmarshal a {@link Configuration} from a
   ** {@link Document}.
   **
   ** @param  document           the configuration source for the descriptor to
   **                            create by parsing the underlying XML.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the populated {@link Configuration}.
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Configuration}.
   **
   ** @throws BrandingException  in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public static Configuration build(final String document)
    throws BrandingException {

    // prevent bogus input
    if (document == null)
      throw new BrandingException("Argument null. document");
      // throw Adapter.exception(ServiceError.ARGUMENT_IS_NULL, "document");

    // create the template descriptor that provides the component attribute
    // value according to the defined rules
    final Configuration model = new Configuration(document);
    //Serializer.unmarshal(model, document);
    return model;
  }

  ////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add environment on the configuration that will be requestable for users.
   **
   ** @param  value              the value expression of the component to add.
   **
   */
  public void add(final Component value) {
    put(value.name, value);
  }
}