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

    File        :   Streamable.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Streamable.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.1.0      2012-03-11  DSteding    First release version
*/

package oracle.hst.foundation.xml;

import javax.xml.stream.XMLStreamException;

////////////////////////////////////////////////////////////////////////////////
// abstract class Streamable
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~
/**
 ** This interface identifies classes supporting XML serialization
 ** (XML serialization is still possible for classes not implementing this
 ** interface through dynamic XML Binding though).
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.1.0
 ** @since   1.0.1.0
 */
public abstract class Streamable {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of the tag */
  private final String tag;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Creates a new instance of <code>Streamable</code>.
   **
   ** @param  tag               the name of the tag
   */
  public Streamable(final String tag) {
    // ensure inheritance
    super();

    // initiaize instance
    this.tag = tag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tag
  /**
   ** Returns the name of the tag.
   **
   ** @return                    the name of the tag.
   */
  public String tag() {
    return this.tag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Compares two <code>XMLSerialized</code>s.
   ** <p>
   ** Two <code>XMLSerialized</code> objects are equal if they are the same
   ** object or if their name are the equal.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **
   ** @see    Object#equals(Object)
   */
  @Override
  public boolean equals(final Object other) {
    if (other instanceof Streamable) {
      final Streamable tag = (Streamable)other;
      return this.tag.equals(tag.tag);
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   open
  /**
   ** Factory method to store a <code>XMLSerialized</code> in a XML stream.
   **
   ** @param  stream             the {@link StreamOutput} used to write the
   **                            start tag.
   **
   ** @return                    this instance to allow method chaining.
   **
   ** @throws XMLStreamException if the instance cannot be written to the
   **                            stream.
   */
  public Streamable open(final StreamOutput stream)
    throws XMLStreamException {

    stream.writeStartElement(this.tag);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   write
  /**
   ** Write the content of this <code>Streamable</code> to a XML stream.
   **
   ** @param  stream             the {@link StreamOutput} used to write the
   **                            content.
   **
   ** @return                    this instance to allow method chaining.
   **
   ** @throws XMLStreamException if the instance cannot be written to the
   **                            stream.
   */
  public abstract Streamable write(final StreamOutput stream)
    throws XMLStreamException;

  //////////////////////////////////////////////////////////////////////////////
  // Method:   close
  /**
   ** Factory method to store a <code>XMLSerialized</code> in a XML stream.
   **
   ** @param  stream             the {@link StreamOutput} used to write the
   **                            end tag.
   **
   ** @return                    this instance to allow method chaining.
   **
   ** @throws XMLStreamException if the instance cannot be written to the
   **                            stream.
   */
  public Streamable close(final StreamOutput stream)
    throws XMLStreamException {

    stream.writeEndElement();
    return this;
  }
}