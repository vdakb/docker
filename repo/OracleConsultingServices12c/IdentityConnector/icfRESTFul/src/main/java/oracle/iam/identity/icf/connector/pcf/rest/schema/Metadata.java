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

    Copyright © 2015. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Pivotal Cloud Foundry Connector

    File        :   Metadata.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Metadata.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf.rest.schema;

import java.util.Calendar;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

////////////////////////////////////////////////////////////////////////////////
// class Metadata
// ~~~~~ ~~~~~~~~
/**
 ** Stores metadata about a REST object.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Metadata {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonIgnore
  /** The prefix to identitify this resource */
  public static final String PREFIX    = "metadata";

  @JsonIgnore
  /** The identifier of the resource */
  public static final String ID        = "guid";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty(ID)
  private String             guid;

  @JsonProperty("url")
  private URI                location;

  @JsonProperty("created_at")
  private Calendar           created;

  @JsonProperty("updated_at")
  private Calendar           modified;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Metadata</code> REST Resource that allows use as
   ** a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Metadata() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guid
  /**
   ** Sets the guid of the REST object.
   **
   ** @param  value              the guid of the REST object.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata guid(final String value) {
    this.guid = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   guid
  /**
   ** Returns the resourceType of the REST object.
   **
   ** @return                    the resourceType of the REST object.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String guid() {
    return this.guid;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Sets the timestamp of when the REST object was created.
   **
   ** @param  value              the date and time the REST object was created.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata created(final Calendar value) {
    this.created = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   created
  /**
   ** Returns the timestamp of when the REST object was created.
   **
   ** @return                    the date and time the REST object was created.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar created() {
    return this.created;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modified
  /**
   ** Sets the timestamp of when the REST object was last modified.
   **
   ** @param  value              the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Allowed object is {@link Calendar}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata modified(final Calendar value) {
    this.modified = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modified
  /**
   ** Returns the timestamp of when the REST object was last modified.
   **
   ** @return                    the date and time the REST object was last
   **                            modified.
   **                            <br>
   **                            Possible object is {@link Calendar}.
   */
  public final Calendar modified() {
    return this.modified;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Sets the location URI of the REST object.
   **
   ** @param  value              the location URI of the REST object.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Metadata</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>Metadata</code>.
   */
  public final Metadata location(final URI value) {
    this.location = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   location
  /**
   ** Returns the location URI of the REST object.
   **
   ** @return                    the location URI of the REST object.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI location() {
    return this.location;
  }
}