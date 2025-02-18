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

    Copyright © 2014. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Security Foundation Library
    Subsystem   :   Common Shared Runtime Facilities

    File        :   JCAContext.java

    Compiler    :   Java Development Kit 8

    Author      :   Dieter Steding

    Purpose     :   This file implements the interface
                    JCAContext.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2014-28-10  DSteding    First release version
*/

package oracle.hst.platform.jca;

import java.security.Provider;
import java.security.SecureRandom;

////////////////////////////////////////////////////////////////////////////////
// class JCAContext
// ~~~~~ ~~~~~~~~~~
/**
 ** Java Cryptography Architecture (JCA) context, consisting of a JCA
 ** {@link Provider provider} and {@link SecureRandom secure random generator}.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class JCAContext {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	/** The JCA provider. */
	protected final Provider     provider;

	/** The secure random generator. */
	protected final SecureRandom random;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new JCA context.
	 **
   ** @param  provider           the JCA provider.
   **                            <br>
   **                            May be <code>null</code> to use the default
   **                            system one.
   **                            <br>
   **                            Allowed object is {@link Provider}.
	 ** @param  random             the specific secure random generator.
   **                            <br>
   **                            May be <code>null</code> to use the default
   **                            system one.
   **                            <br>
   **                            Allowed object is {@link SecureRandom}.
	 */
	protected JCAContext(final Provider provider, final SecureRandom random) {
    // ensure inheritance
    super();

    // initialize instance attributes
		this.provider = provider;
		this.random   = random;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   provider
  /**
   ** Returns the security provider to be asked for the encryption algorithm.
   ** <br>
   ** The provider may be registered at the security infrastructure beforehand,
   ** and its being used here will not result in its being registered.
   ** <p>
   ** If no provider name / provider is returned, the default JVM provider will
   ** be used.
   ** <p>
   ** <b>Importatnt</b>
   ** <br>
   ** Implememting classes have to ensure thread safity on this method by
   ** declaring it <code>synchronized</code>.
   **
   ** @return                    the {@link Provider} to be asked for the chosen
   **                            algorithm.
   **                            <br>
   **                            Possible object is {@link Provider}.
   */
  public final Provider provider() {
    return this.provider;
  }
  public final SecureRandom random() {
    return this.random;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>JCAContext</code> with the
   ** {@link Provider} and {@link SecureRandom} specified.
	 **
   ** @param  provider           the JCA provider.
   **                            <br>
   **                            May be <code>null</code> to use the default
   **                            system one.
   **                            <br>
   **                            Allowed object is {@link Provider}.
	 ** @param  random             the specific secure random generator.
   **                            <br>
   **                            May be <code>null</code> to use the default
   **                            system one.
   **                            <br>
   **                            Allowed object is {@link SecureRandom}.
   **
   ** @return                    the <code>JCAContext</code> populated with the
   **                            {@link Provider} and {@link SecureRandom}
   **                            specified.
   **                            <br>
   **                            Possible object is <code>JCAContext</code>.
   */
  public static JCAContext build(final Provider provider, final SecureRandom random) {
    return new JCAContext(provider, random);
  }
}