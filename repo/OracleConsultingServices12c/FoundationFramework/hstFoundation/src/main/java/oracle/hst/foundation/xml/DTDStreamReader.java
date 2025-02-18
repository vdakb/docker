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

    File        :   DTDStreamReader.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the interface
                    DTDStreamReader.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamConstants;

////////////////////////////////////////////////////////////////////////////////
// interface DTDStreamReader
// ~~~~~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Optional interface implemented by {@link XMLStreamReader} implementations
 ** that provide additional data about {@link XMLStreamConstants#DTD} events.
 ** <p>
 ** All the requirements outlined in {@link oracle.hst.foundation.xml} apply to
 ** this extension interface. In particular, to get a reference to the
 ** extension, the consumer MUST call {@link XMLStreamReader#getProperty(String)}
 ** with {@link #PROPERTY} as the property name.
 */
public interface DTDStreamReader {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The name of the property used to look up this extension interface from a
   ** {@link XMLStreamReader} implementation.
   */
  static final String PROPERTY = DTDStreamReader.class.getName();

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   rootName
  /**
   ** Returns the root name of the DTD, i.e. the name immediately following the
   ** <code>DOCTYPE</code> keyword.
   **
   ** @return                    the root name; must not be <code>null</code>.
   **
   ** @throws IllegalStateException if the current event is not
   **                               {@link XMLStreamConstants#DTD}
   */
  String rootName();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   publicId
  /**
   ** Returns the public ID of the external subset.
   **
   ** @return                    the public ID, or <code>null</code> if there is
   **                            no external subset or no public ID has been
   **                            specified for the external subset.
   **
   ** @throws IllegalStateException if the current event is not
   **                               {@link XMLStreamConstants#DTD}
   */
  String publicId();

  //////////////////////////////////////////////////////////////////////////////
  // Method:   systemId
  /**
   ** Returns the system ID of the external subset.
   **
   ** @return                    the system ID, or <code>null</code> if there is
   **                            no external subset.
   **
   ** @throws IllegalStateException if the current event is not
   **                               {@link XMLStreamConstants#DTD}
   */
  String systemId();
}