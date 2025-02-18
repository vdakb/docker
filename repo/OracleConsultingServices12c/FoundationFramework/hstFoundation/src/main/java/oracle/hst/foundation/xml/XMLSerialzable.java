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

    Copyright © 2012. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Foundation Shared Library
    Subsystem   :   Common Shared XML Stream Facilities

    File        :   XMLSerialzable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    XMLSerialzable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import java.io.Serializable;

////////////////////////////////////////////////////////////////////////////////
// interface XMLSerialzable
// ~~~~~~~~~ ~~~~~~~~~~~~~~
/**
 ** XML serializability of a class is enabled by the class implementing the
 ** oracle.hst.foundation.xml.XMLSerializable interface.
 ** <p>
 ** Classes that do not implement this interface will not have any of their
 ** state serialized or deserialized.
 */
public interface XMLSerialzable extends Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:6771932002964727397")
  static final long serialVersionUID = -5407287476022114236L;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   elements
  /**
   ** Returns the XML element tag to marshal/unmarshal multiple entities of
   ** this type.
   **
   ** @return                    the XML element tag to marshal/unmarshal
   **                            multiple entity of this type.
   */
  String elements();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   element
  /**
   ** Returns the XML element tag to marshal/unmarshal a single entity of this
   ** type.
   **
   ** @return                    the XML element tag to marshal/unmarshal a
   **                            single entity of this type.
   */
  String element();
}