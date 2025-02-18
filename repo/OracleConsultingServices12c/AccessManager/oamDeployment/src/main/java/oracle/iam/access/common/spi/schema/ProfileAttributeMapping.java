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

    File        :   ProfileAttributeMapping.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    ProfileAttributeMapping.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2010-10-07  DSteding    First release version
*/

package oracle.iam.access.common.spi.schema;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

////////////////////////////////////////////////////////////////////////////////
// class ProfileAttributeMapping
// ~~~~~ ~~~~~~~~~~~~~~~~~~~~~~~
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
 **         &lt;element ref="{}attributeMapping" minOccurs="0" maxOccurs="unbounded"/&gt;
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
@XmlType(name="", propOrder={"attributeMapping"})
@XmlRootElement(name=ProfileAttributeMapping.LOCAL)
public class ProfileAttributeMapping {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String LOCAL = "profileAttributeMapping";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected List<AttributeMapping> attributeMapping;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>ProfileAttributeMapping</code> that allows use
   ** as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public ProfileAttributeMapping() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getAttributeMapping
  /**
   ** Returns the value of the <code>attributeMapping</code> property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the custom
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getAttributeMapping().add(newItem);
   ** </pre>
   ** <p>
   ** Objects of the following type(s) are allowed in the list
   ** {@link AttributeMapping}s.
   **
   ** @return                    the associated {@link List} of
   **                            {@link AttributeMapping}s.
   **                            Returned value is never <code>null</code>.
   */
  public List<AttributeMapping> getAttributeMapping() {
    if (this.attributeMapping == null)
      this.attributeMapping = new ArrayList<AttributeMapping>();

    return this.attributeMapping;
  }
}