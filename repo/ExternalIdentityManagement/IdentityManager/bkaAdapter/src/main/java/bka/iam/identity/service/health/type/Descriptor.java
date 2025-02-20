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

    Copyright 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   BKA Identity Manager
    Subsystem   :   Access Policy House Keeping

    File        :   Descriptor.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the class
                    Descriptor.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.service.health.type;

import java.util.List;
import java.util.ArrayList;

////////////////////////////////////////////////////////////////////////////////
// class Descriptor
// ~~~~~ ~~~~~~~~~~
/**
 ** <code>Descriptor</code> represents extended attributes of an IT Resource.
 ** <p>
 ** This class is required to be able to describe the object classes each ICF
 ** connector used to read from or write to any target system.
 ** <br>
 ** Nearly every connector implementation hides such information but alos don't
 ** handle missing values inside Identity Manager.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Descriptor {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /** the name of an <code>IT Resource</code>. */
  protected final String name;

  /**
   ** the type of object (eg., Account/Group/...) for this
   ** <code>IT Resource</code>.
   */
  protected List<Type>   type;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  public static class Type {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    /** The name of the <code>ObjectClass</code>. */
    protected String name;

    /**
     ** The value fetched from the target system needs to be prefixed with the
     ** internal identifier of the <code>IT Resource</code>.
     ** <br>
     ** Default is <code>true</code> because connector developer are mostly not
     ** take care about that not each multi-valued attribute needs to be
     ** prefixed because it isn't always an entitlement.
     */
    protected boolean prefix = true;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Type</code> that allows use as a JavaBean.
     ** <br>
     ** Zero argument constructor required by the framework.
     ** <br>
     ** Default Constructor
     ** <br>
     ** The lifetime of any entry defaulted to 10 minutes.
     */
    public Type() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Methode: name
    /**
     ** Returns the attribute value for <code>NAME</code> using the alias name
     ** <code>name</code>.
     **
     ** @return                  the attribute value for <code>NAME</code>
     **                          using the alias name <code>name</code>.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    public String name() {
      return this.name;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methode: prefix
    /**
     ** Returns the attribute value for <code>PREFIX</code> using the alias name
     ** <code>prefix</code>.
     **
     ** @return                  whether the value fetched from the target
     **                          system needs to be prefixed with the internal
     **                          identifier of the <code>IT Resource</code>.
     **                          <br>
     **                          Default is <code>true</code> because connector
     **                          developer are mostly not take care about that
     **                          not each multi-valued attribute needs to be
     **                          prefixed because it isn't always an
     **                          entitlement.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    public boolean prefix() {
      return this.prefix;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Descriptor</code> for the specified endpoint name.
   **
   ** @param  name               the name of the endpoint this descriptor
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  private Descriptor(final String name) {
    // ensure inheritance
    super();

    // initialize instanc attributes
    this.name = name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   name
  /**
   ** Returns the attribute value for <code>NAME</code> using the alias name
   ** <code>name</code>.
   **
   ** @return                    the attribute value for <code>NAME</code>
   **                            using the alias name <code>name</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String name() {
    return this.name;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methode:   type
  /**
   ** Returns the attribute value for <code>TYPE</code> using the alias name
   ** <code>type</code>.
   **
   ** @return                    the attribute value for <code>TYPE</code> using
   **                            the alias name <code>type</code>.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public List<Type> type() {
    if (this.type == null)
      this.type = new ArrayList<Type>();

    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create a <code>Descriptor</code> for the specified
   ** endpoint name.
   **
   ** @param  name               the name of the endpoint this descriptor
   **                            belongs to.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the created <code>Descriptor</code>.
   **                            <br>
   **                            Possible object is
   **                            <code>WobbleRippleFilterFactory</code>.
   */
  public static Descriptor build(final String name) {
    return new Descriptor(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
   ** Add a new {@link Type} reference to the instance's list of
   ** {@link Type}s.
   **
   ** @param  value              the {@link Type} reference to add to the
   **                            instance's list of {@link Type}s.
   **                            <br>
   **                            Allowed object is {@link Type}.
   */
  public void add(final Type value) {
    if (this.type == null)
      this.type = new ArrayList<Type>();

    this.type.add(value);
  }
}