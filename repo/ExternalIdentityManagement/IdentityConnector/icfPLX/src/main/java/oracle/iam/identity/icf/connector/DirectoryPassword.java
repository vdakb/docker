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
    Subsystem   :   Generic Directory Connector

    File        :   DirectoryPassword.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the class
                    DirectoryPassword.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.identity.icf.connector;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;

import oracle.iam.identity.icf.foundation.utility.CredentialAccessor;

import org.identityconnectors.common.security.GuardedString;
///////////////////////////////////////////////////////////////////////////////
// abstract class DirectoryPassword
// ~~~~~~~~ ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** The <code>DirectoryPassword</code> implements the base functionality
 ** for password in a Directory Service.
 ** <br>
 ** An Directory Service object may have different algorithm for password. Some
 ** of them are mandatory for the functionality.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class DirectoryPassword {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  protected final String name;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Accessor
  // ~~~~~ ~~~~~~~~
  public static abstract interface Accessor {
    public abstract void access(final Attribute attribute);
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Empty
  // ~~~~~ ~~~~~
  /**
   ** The <code>Empty</code> implements the empty password attribute object for
   ** a Directory Service.
   */
  private static class Empty extends DirectoryPassword {

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Empty</code> password attribute which has a name but
     ** no password.
     **
     ** @param  name             the name of the attribute to build.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Empty(final String name) {
      // ensure inheritance
      super(name);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: build (DirectoryPassword)
    /**
     ** Accessor of the password attribute.
     **
     ** @return                  an instance of <code>Attribute</code>.
     **                          <br>
     **                          Possible object {@link DirectoryPassword}.
     */
    @Override
    public Attribute build() {
      return new BasicAttribute(this.name);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Simple
  // ~~~~~ ~~~~~~
  /**
   ** The <code>Simple</code> implements the plain text password attribute
   ** object for a Directory Service.
   */
  private static final class Simple extends DirectoryPassword {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final String        encoding;
    private final GuardedString password;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Simple</code> password attribute which has a name
     ** and a password.
     ** <br>
     ** The encoding is specified by <code>encoding</code>.
     **
     **
     ** @param  name             the name of the attribute to lookup.
     **                          <br>
     **                          Allowed object is {@link String}.
     ** @param  password         the value of the password.
     **                          <br>
     **                          Allowed object is {@link GuardedString}.
     ** @param  encoding         the encoding of the password.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    private Simple(final String name, final GuardedString password, final String encoding) {
      // ensure inheritance
      super(name);

      // initialize instance attributes
      this.password = password;
      this.encoding = encoding;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods of abstract base classes
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: access (DirectoryPassword)
    public Attribute build() {
      CredentialAccessor.string(this.password);
      final ByteBuffer buffer = Charset.forName(this.encoding).encode(CredentialAccessor.string(this.password));
      buffer.rewind();
      byte[] bytes = new byte[buffer.limit()];
      buffer.get(bytes);
      final BasicAttribute attribute = new BasicAttribute(this.name, bytes);
      buffer.rewind();
      while (buffer.remaining() > 0)
        buffer.put((byte) 0);
      return attribute;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Empty</code> password attribute which has a name
   ** but no password.
   **
   **
   ** @param  name               the name of the attribute to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private DirectoryPassword(final String name) {
    // ensure inheritance
    super();

    // initialize instance attributes
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a simple password attribute with the specified
   ** name but no password value.
   **
   ** @param  attributeName      the name of the attribute to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryPassword}.
   */
  public static DirectoryPassword build(final String attributeName) {
    return new Empty(attributeName);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a simple password attribute with the specified
   ** name and password value.
   ** <br>
   ** Per default the encoding is UTF-8.
   **
   **
   ** @param  attributeName      the name of the attribute to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the value of the password.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            Possible object is {@link DirectoryPassword}.
   */
  public static DirectoryPassword build(final String attributeName, final GuardedString password) {
    return build(attributeName, password, "UTF-8");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a simple password attribute with the specified
   ** name and password value.
   ** The encoding is specified by <code>encoding</code>.
   **
   **
   ** @param  attributeName      the name of the attribute to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  password           the value of the password.
   **                            <br>
   **                            Allowed object is {@link GuardedString}.
   ** @param  encoding           the encoding of the password.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            <br>
   **                            Possible object is {@link DirectoryPassword}.
   */
  public static DirectoryPassword build(final String attributeName, final GuardedString password, final String encoding) {
    return new Simple(attributeName, password, encoding);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an {@link Attribute} for the password.
   **
   ** @return                    an instance of <code>Attribute</code>.
   **                            <br>
   **                            Possible object {@link Attribute}.
   */
  public abstract Attribute build();
}
