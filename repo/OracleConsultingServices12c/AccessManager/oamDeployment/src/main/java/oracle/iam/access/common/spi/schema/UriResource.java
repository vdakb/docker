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

    Copyright © 2010. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager Utility Library
    Subsystem   :   Deployment Utilities 12c

    File        :   UriResource.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    UriResource.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

////////////////////////////////////////////////////////////////////////////////
// class UriResource
// ~~~~~ ~~~~~~~~~~~
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
 **         &lt;element name="uri"         type="{http://www.w3.org/2001/XMLSchema}anyURI"/&gt;
 **         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **         &lt;element name="queryString" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 **       &lt;sequence&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=UriResource.LOCAL)
@XmlType(name="", propOrder={"uri", "description", "queryString"})
public class UriResource {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "uriResource";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlElement(required=true)
  protected String uri;
  protected String description;
  protected String queryString;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>UriResource</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public UriResource() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>UriResource</code> with the specified properties.
   **
   ** @param  uri                the uri od the resource.
   **                            Allowed object is {@link String}.
   ** @param  description        the description od the resource.
   **                            Allowed object is {@link String}.
   ** @param  queryString        the query string od the resource.
   **                            Allowed object is {@link String}.
   */
  public UriResource(final String uri, final String description, final String queryString) {
    // ensure inheritance
    super();

    // initialize instance
    this.uri         = uri;
    this.description = description;
    this.queryString = queryString;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUri
  /**
   ** Sets the value of the <code>uri</code> property.
   **
   ** @param  value              the value of the <code>uri</code> property.
   **                            Allowed object is {@link String}.
   */
  public void setUri(final String value) {
    this.uri = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUri
  /**
   ** Returns the value of the <code>uri</code> property.
   **
   ** @return                    the value of the <code>uri</code> property.
   **                            Possible object is {@link String}.
   */
  public String getUri() {
    return this.uri;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setDescription
  /**
   ** Sets the value of the <code>description</code> property.
   **
   ** @param  value              the value of the <code>description</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setDescription(final String value) {
    this.description = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getDescription
  /**
   ** Returns the value of the <code>description</code> property.
   **
   ** @return                    the value of the <code>description</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getDescription() {
    return this.description;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setQueryString
  /**
   ** Sets the value of the <code>queryString</code> property.
   **
   ** @param  value              the value of the <code>queryString</code>
   **                            property.
   **                            Allowed object is {@link String}.
   */
  public void setQueryString(final String value) {
    this.queryString = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getQueryString
  /**
   ** Returns the value of the <code>queryString</code> property.
   **
   ** @return                    the value of the <code>queryString</code>
   **                            property.
   **                            Possible object is {@link String}.
   */
  public String getQueryString() {
    return this.queryString;
  }
}