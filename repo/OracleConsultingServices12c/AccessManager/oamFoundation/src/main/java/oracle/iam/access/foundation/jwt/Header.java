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

    Copyright © 2017. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Authentication Plug-In Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   Token.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Token.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.1.0.0      2014-28-10  DSteding    First release version
*/

package oracle.iam.access.foundation.jwt;

import java.util.Map;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;

////////////////////////////////////////////////////////////////////////////////
// class Header
// ~~~~~ ~~~~~~
/**
 ** JSON Object Signing and Encryption (JOSE) Header
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Header {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("typ")
  protected Type                type       = Type.JWT;
  @JsonProperty("alg")
  protected Algorithm           algorithm  = Algorithm.HS384;
  @JsonIgnore
  protected Map<String, String> properties = new LinkedHashMap<>();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Header</code> task that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Header() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Header</code> with the specified {@link Algorithm}.
   **
   ** @param  algorithm          the {@link Algorithm} supported by the
   **                            <code>Header</code>.
   */
  public Header(final Algorithm algorithm) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.algorithm = algorithm;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   set
  /**
   ** Add a property to the JSON Web Token header.
   **
   ** @param  name               the name of the header property.
   ** @param  value              the value of the header property.
   **
   ** @return                    this <code>Header</code> for chaining
   **                            invocations.
   */
  @JsonAnySetter
  public Header set(String name, String value) {
    properties.put(name, value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   get
  /**
   ** Returns the value to which the specified <code>name</code> is mapped, or
   ** <code>null</code> if the properties contains no mapping for the
   ** <code>name</code>.
   **
   ** @param  name               the name whose associated value is to be
   **                            returned.
   **
   ** @return                    the value to which the specified
   **                            <code>name</code> is mapped, or
   **                            <code>null</code> if the properties contains no
   **                            mapping for the <code>name</code>.
   */
  public String get(final String name) {
    return this.properties.get(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   any
  /**
   ** Special getter used to flatten additional header properties into top level
   ** values. Necessary to correctly serialize this object.
   **
   ** @return                    a map of properties to be serialized as if they
   **                            were actual properties of this class.
   */
  @JsonAnyGetter
  public Map<String, String> any() {
    return this.properties;
  }
}