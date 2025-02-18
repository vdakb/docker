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

    File        :   XMLOperation.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    XMLOperation.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import oracle.hst.foundation.logging.Logger;
import oracle.hst.foundation.logging.Loggable;

import oracle.hst.foundation.utility.ClassUtility;

import oracle.hst.foundation.resource.XMLStreamBundle;

////////////////////////////////////////////////////////////////////////////////
// class XMLOperation
// ~~~~~ ~~~~~~~~~~~~
/**
 ** The base class of XML document operations.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public class XMLOperation implements Loggable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** these are for convenience the avoid a specific getter for this instance
   ** attributes.
   ** <p>
   ** Subclasses must not change these instance attributes.
   */
  protected final String category = ClassUtility.shortName(this.getClass().getName());
  protected final Logger logger;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a <code>XMLOperation</code> that use the specifid {@link Logger}
   ** for output purpose.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML document operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** If the <code>XMLOperation</code> object is created with a logger initally
   ** set to <code>null</code> logging is disabled.
   **
   ** @param  loggable           the instance providing the {@link Logger};
   **                            to disable, set to <code>null</code>.
   */
  public XMLOperation(final Loggable loggable) {
    // ensure inheritance
    super();

    // instialize intance attributes
    this.logger = (loggable == null) ? null : loggable.logger();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessors methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   logger
  /**
   ** Returns the log writer for this XML operation.
   ** <br>
   ** The logger is an object to which all logging and tracing messages for this
   ** XML file operation object instance will be printed. This includes
   ** messages printed by the methods of this object, messages printed by
   ** methods of other objects manufactured by this object, and so on.
   ** <br>
   ** When a <code>XMLOperation</code> object is created the logger is
   ** initially <code>null</code>, in other words, logging is disabled.
   **
   ** @return                    the log writer for this XML operation.
   */
  public final Logger logger() {
    return this.logger;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   fatal (Loggable)
  /**
   ** Logs an fatal error to the associated <code>Logger</code>.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  what               the exception as the reason to log.
   */
  @Override
  public final void fatal(final String method, final Throwable what) {
    if (this.logger != null)
      this.logger.fatal(this.category, method, what);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   error (Loggable)
  /**
   ** Writes a error message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  messageKey         the resource key of the message to write to the
   **                            log.
   */
  @Override
  public void error(final String method, final String messageKey) {
    if (this.logger != null)
      this.logger.error(this.category, method, XMLStreamBundle.string(messageKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Writes a warning message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  messageKey         the resource key of the message to write to the
   **                            log.
   */
  @Override
  public void warning(final String method, final String messageKey) {
    if (this.logger != null)
      this.logger.warn(this.category, method, XMLStreamBundle.string(messageKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   warning (Loggable)
  /**
   ** Writes an warning message to the associated <code>Logger</code>.
   **
   **
   ** @param  messageKey         the resource key of the message to write to the
   **                            log.
   */
  @Override
  public void warning(final String messageKey) {
    if (this.logger != null)
      this.logger.warn(XMLStreamBundle.string(messageKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   info (Loggable)
  /**
   ** Writes a informational message to the associated <code>Logger</code> if
   ** once is available.
   **
   ** @param  messageKey        the resource key of the message to write to the
   **                            log.
   */
  @Override
  public void info(final String messageKey) {
    if (this.logger != null)
      this.logger.info(XMLStreamBundle.string(messageKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   debug (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  messageKey         the resource key of the message to write to the
   **                            log.
   */
  @Override
  public void debug(final String method, final String messageKey) {
    if (this.logger != null)
      this.logger.debug(this.category, method, XMLStreamBundle.string(messageKey));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trace (Loggable)
  /**
   ** Writes a debug message to the associated <code>Logger</code> if once is
   ** available.
   **
   ** @param  method             the name of the method where the logging
   **                            event was occurred.
   ** @param  message            the message to write to the log.
   */
  @Override
  public void trace(final String method, final String message) {
    if (this.logger != null)
      this.logger.trace(this.category, method, message);
  }
}