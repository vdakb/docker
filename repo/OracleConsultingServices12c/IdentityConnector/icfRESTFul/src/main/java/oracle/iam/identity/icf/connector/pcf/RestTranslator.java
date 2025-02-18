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

    File        :   RestTranslator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    RestTranslator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector.pcf;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.OperationalAttributeInfos;

////////////////////////////////////////////////////////////////////////////////
// class RestTranslator
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** PCF REST implementation to translate ICF Filter in the PCF REST syntax.
 ** <p>
 ** A search filter may contain operators (such as 'contains' or 'in') or may
 ** contain logical operators (such as 'AND', 'OR' or 'NOT') that a connector
 ** cannot implement using the native API of the target system or application. A
 ** connector developer should subclass <code>RestTranslator</code> in order to
 ** declare which filter operations the connector does support. This allows
 ** the <code>RestTranslator</code> instance to analyze a specified search
 ** filter and reduce the filter to its most efficient form. The default (and
 ** worst-case) behavior is to return a <code>null</code> expression, which
 ** means that the connector should return "everything" (that is, should return
 ** all values for every requested attribute) and rely on the common code in the
 ** framework to perform filtering. This "fallback" behavior is good (in that it
 ** ensures consistency of search behavior across connector implementations) but
 ** it is obviously better for performance and scalability if each connector
 ** performs as much filtering as the native API of the target can support.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
class RestTranslator {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public static final String EQ = ":";
  public static final String GT = ">=";
  public static final String GE = ">";
  public static final String LT = "<=";
  public static final String LE = "<";
  public static final String IN = " IN ";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a PCF REST filter <code>RestTranslator</code> that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private RestTranslator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a ICF to REST filter translator.
   **
   ** @return                    the path to the root of the JSON object that
   **                            represents the REST resource.
   **                            <br>
   **                            Possible object is <code>RestTranslator</code>.
   */
  static RestTranslator build() {
    return new RestTranslator();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   filter
  static String filter(final String name, final String value, final String operator, final boolean not) {
    String normalized = name;
    if (Uid.NAME.equals(name)) {
      normalized = RestMarshaller.IDENTIFIER;
    }
    else if (Name.NAME.equals(name)) {
      normalized = RestMarshaller.NAME;
    }
    else if (OperationalAttributeInfos.ENABLE.equals(name)) {
      normalized = RestMarshaller.STATUS;
    }

    final StringBuilder builder = new StringBuilder();
    if (not)
      builder.append("not (");
    builder.append(normalized).append(operator).append(value);
    if (not)
      builder.append(")");
    return builder.toString();
  }
}