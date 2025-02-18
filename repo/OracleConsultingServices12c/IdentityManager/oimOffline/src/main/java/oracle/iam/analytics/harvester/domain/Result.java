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

    Copyright © 2013. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Analytics Extension Library
    Subsystem   :   Offline Target Connector

    File        :   Result.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    Result.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    3.0.0.0      2013-08-23  DSteding    First release version
*/

package oracle.iam.analytics.harvester.domain;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.xml.datatype.XMLGregorianCalendar;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;

import oracle.hst.foundation.xml.XMLException;
import oracle.hst.foundation.xml.XMLOutputNode;

import oracle.hst.foundation.utility.DateUtility;

////////////////////////////////////////////////////////////////////////////////
// class Result
// ~~~~~ ~~~~~~
/**
 ** Java class for result complex type.
 ** <p>
 ** The following schema fragment specifies the expected content contained
 ** within this class.
 ** <pre>
 ** &lt;complexType&gt;
 **   &lt;complexContent&gt;
 **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **       &lt;sequence&gt;
 **         &lt;element name="message" maxOccurs="unbounded" minOccurs="0"&gt;
 **           &lt;complexType&gt;
 **             &lt;complexContent&gt;
 **               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 **                 &lt;sequence&gt;
 **                   &lt;element name="severity"  type="{http://service.api.oia.iam.ots}severity"/&gt;
 **                   &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 **                   &lt;element name="location"  type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                   &lt;element name="text"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 **                 &lt;/sequence&gt;
 **               &lt;/restriction&gt;
 **             &lt;/complexContent&gt;
 **           &lt;/complexType&gt;
 **         &lt;/element&gt;
 **       &lt;/sequence&gt;
 **       &lt;attribute name="status" use="required" type="{http://service.api.oia.iam.ots}status"/&gt;
 **     &lt;/restriction&gt;
 **   &lt;/complexContent&gt;
 ** &lt;/complexType&gt;
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 3.0.0.0
 ** @since   3.0.0.0
 */
@XmlRootElement(name=Result.LOCAL)
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"message"})
public class Result {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  public final static String     LOCAL             = "result";

  /**
   ** the XML attribute specifies the name the timestamp of a message record.
   */
  final static String            MESSAGE_TIMESTAMP = "timestamp";

  /**
   ** the XML attribute specifies the location context of a message record.
   */
  final static String            MESSAGE_LOCATION  = "location";

  /**
   ** the XML attribute specifies the name the text of a message record.
   */
  final static String            MESSAGE_TEXT      = "text";

  /** the component identifier of any message event */
  static final String            COMPONENT         = "OIM";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @XmlAttribute(required=true)
  protected Status               status            = Status.SUCCESS;

  @XmlElement(required=true, name=Result.Message.LOCAL, namespace=ObjectFactory.NAMESPACE, nillable=false)
  protected List<Result.Message> message;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Message
  // ~~~~~ ~~~~~~~
  /**
   ** Java class for anonymous complex type.
   ** <p>
   ** The following schema fragment specifies the expected content contained
   ** within this class.
   ** <pre>
   ** &lt;complexType&gt;
   **   &lt;complexContent&gt;
   **     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
   **       &lt;sequence&gt;
   **         &lt;element name="severity"  type="{http://service.api.oia.iam.ots}severity"/&gt;
   **         &lt;element name="timestamp" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
   **         &lt;element name="location"  type="{http://service.api.oia.iam.ots}token"/&gt;
   **         &lt;element name="text"      type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
   **       &lt;/sequence&gt;
   **     &lt;/restriction&gt;
   **   &lt;/complexContent&gt;
   ** &lt;/complexType&gt;
   ** </pre>
   */
  @XmlAccessorType(XmlAccessType.FIELD)
  @XmlType(name="", propOrder={Severity.LOCAL, MESSAGE_TIMESTAMP, MESSAGE_LOCATION, MESSAGE_TEXT})
  public static class Message {

    ////////////////////////////////////////////////////////////////////////////
    // static final attributes
    ////////////////////////////////////////////////////////////////////////////

    public final static String     LOCAL     = "message";

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    @XmlElement(required=true, nillable=false, namespace=ObjectFactory.NAMESPACE, name="severity")
    protected Severity             severity;
    @XmlSchemaType(name="dateTime")
    @XmlElement(required=true, nillable=false, namespace=ObjectFactory.NAMESPACE, name="timestamp")
    protected XMLGregorianCalendar timestamp;
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlElement(required=true, nillable=false, namespace=ObjectFactory.NAMESPACE, name="location")
    protected String               location;
    @XmlElement(required=true, nillable=false, namespace=ObjectFactory.NAMESPACE, name="text")
    protected String               text;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a empty <code>Message</code> that allows use as a JavaBean.
     ** <p>
     ** Zero argument constructor required by the framework.
     ** <p>
     ** Default Constructor
     */
    public Message() {
      // ensure inheritance
      super();
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs an <code>Message</code> with the specified namespace and
     ** endpoint.
     **
     ** @param  severity         the {@link Severity} of the message event.
     ** @param  timestamp        the timestamp the message event occured.
     ** @param  location         the location context of the message event.
     ** @param  text             the message text of the event.
     */
    public Message(final Severity severity, final Date timestamp, final String location, final String text) {
      // ensure inheritance
      super();

      // ensure inheritance
      this.severity  = severity;
      this.text      = text;
      setTimestamp(timestamp);
      setLocation(location);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: setSeverity
    /**
     ** Sets the value of the severity property.
     **
     ** @param  value            allowed object is {@link String}.
     */
    public void setSeverity(final String value) {
      this.severity = Severity.fromValue(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setSeverity
    /**
     ** Sets the value of the severity property.
     **
     ** @param  value            allowed object is {@link Severity}.
     */
    public void setSeverity(final Severity value) {
      this.severity = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getSeverity
    /**
     ** Returns the value of the severity property.
     **
     ** @return                  possible object is {@link Severity}.
     */
    public Severity getSeverity() {
      return this.severity;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setTimestamp
    /**
     ** Sets the value of the timestamp property.
     **
     ** @param  value            allowed object is {@link Date}.
     */
    public void setTimestamp(final Date value) {
      this.timestamp = DateUtility.toXMLGregorianCalendar(value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setTimestamp
    /**
     ** Sets the value of the timestamp property.
     **
     ** @param  value            allowed object is {@link XMLGregorianCalendar}.
     */
    public void setTimestamp(final XMLGregorianCalendar value) {
      this.timestamp = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getTimestamp
    /**
     ** Returns the value of the timestamp property.
     **
     ** @return                  possible object is {@link XMLGregorianCalendar}.
     */
    public XMLGregorianCalendar getTimestamp() {
      return this.timestamp;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setLocation
    /**
     ** Sets the value of the location property.
     **
     ** @param  value            allowed object is {@link String}.
     */
    public void setLocation(final String value) {
      this.location = String.format("%s::%s", COMPONENT, value);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getLocation
    /**
     ** Returns the value of the location property.
     **
     ** @return                  possible object is {@link String}.
     */
    public String getLocation() {
      return this.location;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: setText
    /**
     ** Sets the value of the text property.
     **
     ** @param  value            allowed object is {@link String}.
     */
    public void setText(final String value) {
      this.text = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: getText
    /**
     ** Returns the value of the text severity.
     **
     ** @return                  possible object is {@link String}.
     */
    public String getText() {
      return this.text;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods grouped by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: toXMLOutputNode
    /**
     ** This is used to create this instance as a child element within the
     ** passed in parent element.
     ** <p>
     ** The created {@link XMLOutputNode} object can be used to add attributes
     ** to the child element as well as other elements.
     **
     ** @param  parent           the parent {@link XMLOutputNode}.
     **
     ** @return                  the created {@link XMLOutputNode}.
     **
     ** @throws XMLException     in the event of misconfiguration (such as
     **                          failure to set an essential property) or if
     **                          initialization fails.
     */
    public XMLOutputNode toXMLOutputNode(final XMLOutputNode parent)
      throws XMLException {

      final XMLOutputNode node = parent.element(LOCAL);
      node.element(Severity.LOCAL).value(getSeverity().value());
      node.element(MESSAGE_TIMESTAMP).value(getTimestamp().toString());
      node.element(MESSAGE_LOCATION).value(getLocation());
      node.element(MESSAGE_TEXT).value(getText());

      return node;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Result</code> that allows use as a JavaBean.
   ** <p>
   ** Zero argument constructor required by the framework.
   ** <p>
   ** Default Constructor
   */
  public Result() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a empty <code>Result</code> with the specified status.
   **
   ** @param  status             the initial {@link Status} of this
   **                            <code>Result</code>.
   */
  public Result(final Status status) {
    // ensure inheritance
    super();

    // initialize instance attributtes
    this.status = status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setStatus
  /**
   ** Sets the value of the status property.
   **
   ** @param  value              allowed object is {@link String}.
   */
  public void setStatus(final Status value) {
    // override the status only if its still succes to reflect in the
    // response any higher level
    if (this.status.isLess(value))
      this.status = value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getStatus
  /**
   ** Returns the value of the status property.
   **
   ** @return                    possible object is {@link String}.
   */
  public Status getStatus() {
    return this.status;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getMessage
  /**
   ** Returns the value of the message property.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JAXB object.
   ** <br>
   ** This is why there is not a <code>set</code> method for the resource
   ** property.
   ** <p>
   ** For example, to add a new item, do as follows:
   ** <pre>
   **    getMessage().add(newItem);
   ** </pre>
   ** Objects of the following type(s) are allowed in the list
   ** {@link Result.Message}.
   **
   ** @return                    the value of the message property.
   */
  public List<Result.Message> getMessage() {
    if (this.message == null)
      this.message = new ArrayList<Result.Message>();

    return this.message;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toXMLOutputNode
  /**
   ** This is used to create this instance as a child element within the
   ** passed in parent element.
   ** <p>
   ** The created {@link XMLOutputNode} object can be used to add attributes
   ** to the child element as well as other elements.
   **
   ** @param  parent             the parent {@link XMLOutputNode}.
   **
   ** @return                    the created {@link XMLOutputNode}.
   **
   ** @throws XMLException       in the event of misconfiguration (such as
   **                            failure to set an essential property) or if
   **                            initialization fails.
   */
  public XMLOutputNode toXMLOutputNode(final XMLOutputNode parent)
    throws XMLException {

    final XMLOutputNode node = parent.element(LOCAL);
    node.attribute(Status.LOCAL, getStatus().value());
    for (Message line : getMessage())
      line.toXMLOutputNode(node);
    return node;
  }
}