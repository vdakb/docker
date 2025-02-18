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

    File        :   Embed.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    Entity.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2021-22-05  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.jira.schema;

import java.util.List;

import java.net.URI;

import oracle.iam.identity.icf.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// interface Entity
// ~~~~~~~~~ ~~~~~~
/**
 ** The base REST entity.
 ** <br>
 ** This object contains all of the attributes required of PCF REST entity
 ** objects.
 ** <p>
 ** <code>Entity</code> is used when the domain is known ahead of time. In that
 ** case a developer can derive a class from <code>Embed</code> and annotate
 ** the class. The class should be a Java bean. This will make it easier to work
 ** with the REST object since you will just have plain old getters and setters
 ** for core attributes.
 **
 ** @param  <T>                  the type of the implementation.
 **                              <br>
 **                              This parameter is used for convenience to allow
 **                              better implementations of the entities
 **                              extending this class (entities can return their
 **                              own specific type instead of type defined by
 **                              this class only).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public interface Entity<T extends Entity> extends Resource<T> {

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static void jsonify(final String tag, final Boolean value, final StringBuilder collector) {
    collector.append("\"").append(tag).append("\":\"").append(value).append("\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static void jsonify(final String tag, final Integer value, final StringBuilder collector) {
    collector.append("\"").append(tag).append("\":\"").append(value).append("\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static void jsonify(final String tag, final URI value, final StringBuilder collector) {
    jsonify(tag, value.toString(), collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static void jsonify(final String tag, final String value, final StringBuilder collector) {
    collector.append("\"").append(tag).append("\":\"").append(value).append("\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static void jsonify(final String tag, final Entity value, final StringBuilder collector) {
    collector.append("\"").append(tag).append("\":\"").append(value).append("\"");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   jsonify
  static <T extends Entity> void jsonify(final String tag, final List<T> collection, final StringBuilder collector) {
    collector.append("\"").append(tag).append("\":[").append(collection).append("]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  int hashCode();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  String toString();
}