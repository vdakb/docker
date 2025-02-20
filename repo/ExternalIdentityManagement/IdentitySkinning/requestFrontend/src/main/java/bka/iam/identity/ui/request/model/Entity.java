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

    Copyright © 2020 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Frontend Extension
    Subsystem   :   Special Account Request

    File        :   Entity.java

    Compiler    :   JDK 1.8

    Author      :   sylvert.bernet@oracle.com

    Purpose     :   This file implements the abstract
                    class Entity.

    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      15.02.2020  SBernet    First release version
*/

package bka.iam.identity.ui.request.model;

import java.io.Serializable;

import oracle.hst.foundation.object.Pair;

import oracle.hst.foundation.utility.StringUtility;

import bka.iam.identity.ui.RequestError;
import bka.iam.identity.ui.RequestException;

import bka.iam.identity.ui.request.Adapter;

////////////////////////////////////////////////////////////////////////////////
// abstract class Entity
// ~~~~~~~~ ~~~~~ ~~~~~~
/**
 ** Declares parameters and methods that represents any entity in Identity
 ** Manager.
 ** <p>
 ** An entity is identified by an <code>id</code> in the model and should have a
 ** label expression that can be displayed along with the entity in the UI.
 ** <p>
 ** <b>Note</b>:
 ** <br>
 ** This class access modifier cannot be higher than public so that reflection
 ** can access the methods of this class.
 ** <br>
 ** If this class were armed with a higher access modifier, it would no longer
 ** be possible to access the methods by reflection, even if they were declared
 ** public.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType name="entity"&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;attribute name="id"    use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 **       &lt;attribute name="label" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public abstract class Entity implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-3257478299605269106")
  private static final long serialVersionUID = 5558338913055441797L;

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  final transient Pair<String, String> id;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> that allows use as a JavaBean.
   **
   ** @param  id                 the model identifier of the template.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  label              the label expression for id to be displayed
   **                            along with the template in the user interface.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  Entity(final String id, final String label)
    throws RequestException {

    // ensure inheritance
    this(Pair.of(id, label));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Entity</code> that allows use as a JavaBean.
   **
   ** @param id                  the entity identifier associated with a label
   **                            expression for id to be displayed along with
   **                            the entity in the user interface.
   **                            <br>
   **                            Allowed object is {@link Pair}.
   **
   ** @throws RequestException   in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public Entity(final Pair<String, String> id)
    throws RequestException {

    // ensure inheritance
    super();

    // prevent bogus input
    if (id == null)
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_ID);

    if (StringUtility.isEmpty(id.tag))
      throw Adapter.exception(RequestError.ARGUMENT_IS_NULL, Serializer.ATTRIBUTE_LABEL);

    // initialize arguments
    this.id = id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   id
  /**
   ** Return the entity identifier.
   **
   ** @return                    the entity identifier.
   **                            <br>
   **                            Possible object is {@link Pair}.
   */
  public final Pair<String, String> id() {
    return this.id;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getId
  /**
   ** Return the entity identifier.
   **
   ** @return                    the entity identifier.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String getId() {
    return this.id.tag;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getLabel
  /**
   ** Return the entity label.
   **
   ** @return                    the entity label.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String getLabel() {
    return this.id.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

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
  public int hashCode() {
    return this.id.hashCode();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Entities</code> are considered equal if and only if they
   ** represent the same id value. As a consequence, two given
   ** <code>Entities</code> may be different even though they contain the same
   ** id value.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    // test identity
    if (this == other)
      return true;

    // test for null and exact class matches
    if (other == null || getClass() != other.getClass())
      return false;

    final Entity that = (Entity)other;
    return this.id.equals(that.id);
  }
}