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

    Copyright © 2021. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Atlassian Jira Server Connector

    File        :   Adressable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Adressable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// abstract class Adressable
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** Any REST resource representation with URI to itself (or to its complete
 ** version - when partial representation is embedded in other resources)
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Adressable<T extends Adressable> implements Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty
  protected URI self;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Adressable</code> REST Resource that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Adressable() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   self
  /**
   ** Sets the {@link URI} to the REST resource or (usually) its complete
   ** version - when partial representation is embedded in other resources.
   **
   ** @param  value              the {@link URI} to the REST resource or
   **                            (usually) its complete version - when partial
   **                            representation is embedded in other resources.
   **                            <br>
   **                            Allowed object is {@link URI}.
   **
   ** @return                    the <code>Adressable</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>T</code>.
   */
  public final T self(final URI value) {
    this.self = value;
    return (T)this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   self
  /**
   ** Returns the {@link URI} to the REST resource or (usually) its complete
   ** version - when partial representation is embedded in other resources.
   **
   ** @return                    the {@link URI} to the REST resource or
   **                            (usually) its complete version - when partial
   **                            representation is embedded in other resources.
   **                            <br>
   **                            Possible object is {@link URI}.
   */
  public final URI self() {
    return this.self;
  }
}