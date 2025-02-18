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

    Copyright © 2018. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Manager Connector Library
    Subsystem   :   Generic SCIM Interface

    File        :   InstantMessaging.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    InstantMessaging.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.scim.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import oracle.iam.system.simulation.scim.annotation.Attribute;
import oracle.iam.system.simulation.scim.annotation.Definition;

////////////////////////////////////////////////////////////////////////////////
// class InstantMessaging
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** Stores address for the user.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class InstantMessaging {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The prefix to identitify this resource */
  public static final String PREFIX    = "ims";

  /**
   ** The canonical value of a instant messaging belonging to Tencent QQ.
   ** <p>
   ** Tencent QQ, also known as QQ, is an instant messaging software service and
   ** web portal developed by the Chinese tech giant Tencent.
   ** <br>
   ** QQ offers services that provide online social games, music, shopping,
   ** microblogging, movies, and group and voice chat software.
   ** <br>
   ** The logo of the software is a winking penguin wearing a red scarf.
   ** <br>
   ** It is the world's 7th most visited website, according to Alexa.
   */
  public static final String QQ        = "qq";

  /**
   ** The canonical value of a instant messaging belonging to AOL Instant
   ** Messenger purpose.
   ** <p>
   ** AIM (AOL Instant Messenger) was an instant messaging and presence computer
   ** program created by AOL, which used the proprietary OSCAR instant messaging
   ** protocol and the TOC protocol to allow registered users to communicate in
   ** real time.
   ** <p>
   ** In June 2015, AOL was acquired by Verizon Communications.
   ** <br>
   ** In June 2017, Verizon combined AOL and Yahoo into its subsidiary Verizon
   ** Media (formally Oath Inc).
   ** <bR>
   ** The company discontinued AIM as a service on December 15, 2017.
   */
  public static final String AIM       = "aim";

  /**
   ** The canonical value of a instant messaging belonging to Google Talk
   ** purpose.
   ** <p>
   ** Google Talk, also known as Google Chat, was an instant messaging service
   ** that provided both text and voice communication.
   ** <br>
   ** The instant messaging service was variously referred to colloquially as
   ** Gchat, Gtalk, or Gmessage among its users.
   */
  public static final String GTALK     = "gtalk";

  /**
   ** The canonical value of a instant messaging belonging to ICQ purpose.
   ** <p>
   ** ICQ is a cross-platform instant messaging and VoIP client.
   ** <br>
   ** The name ICQ derives from the English phrase "I Seek You".
   ** <br>
   ** Originally developed by the Israeli company Mirabilis in 1996, the client
   ** was bought by AOL in 1998, and then by Mail.Ru Group in 2010.
   */
  public static final String ICQ       = "icq";

  /** The canonical value of a instant messaging belonging to Microsoft
   ** Messenger purpose.
   ** <p>
   ** Messenger (formerly MSN Messenger Service, .NET Messenger Service and
   ** Windows Live Messenger Service) was an instant messaging and presence
   ** system developed by Microsoft in 1999 for use with its MSN Messenger
   ** software. It was used by instant messaging clients including Windows 8,
   ** Windows Live Messenger, Microsoft Messenger for Mac, Outlook.com and
   ** Xbox Live.
   ** <br>
   ** Third-party clients also connected to the service. It communicated using
   ** the Microsoft Notification Protocol, a proprietary instant messaging
   ** protocol. The service allowed anyone with a Microsoft account to sign in
   ** and communicate in real time with other people who were signed in as well.
   ** <p>
   ** In April 2013, Microsoft merged this service into Skype network; existing
   ** users were able to sign into Skype with their existing accounts and access
   ** their contact list. As part of the merger, Skype instant messaging
   ** functionality is now running on the backbone of the former Messenger
   ** service.
   */
  public static final String MSN       = "msn";

  /**
   ** The canonical value of a instant messaging belonging to XMMPP purpose.
   ** <p>
   ** Extensible Messaging and Presence Protocol (XMPP) is a communication
   ** protocol for message-oriented middleware based on XML (Extensible Markup
   ** Language).
   ** <br>
   ** It enables the near-real-time exchange of structured yet extensible data
   ** between any two or more network entities. Originally named Jabber, the
   ** protocol was developed by the eponymous open-source community in 1999 for
   ** near real-time instant messaging (IM), presence information, and contact
   ** list maintenance.
   ** <br>
   ** Designed to be extensible, the protocol has been used also for
   ** publish-subscribe systems, signalling for VoIP, video, file transfer,
   ** gaming, the Internet of Things (IoT) applications such as the smart grid,
   ** and social networking services.
   */
  public static final String XMPP      = "xmpp";

  /**
   ** The canonical value of a instant messaging belonging to Skype purpose.
   ** <p>
   ** Skype is a telecommunications application that specializes in providing
   ** video chat and voice calls between computers, tablets, mobile devices, the
   ** Xbox One console, and smartwatches via the Internet.
   ** <br>
   ** Skype also provides instant messaging services.
   ** <br>
   ** Users may transmit text, video, audio and images.
   ** <br>
   ** Skype allows video conference calls.
   ** <p>
   ** Skype originally featured a hybrid peer-to-peer and client-server system.
   ** <br>
   ** Skype has been powered entirely by Microsoft-operated supernodes since May
   ** 2012. The 2013 mass surveillance disclosures revealed that Microsoft had
   ** granted intelligence agencies unfettered access to supernodes and Skype
   ** communication content.
   */
  public static final String SKYPE     = "skype";

  /**
   ** The canonical value of a instant messaging belonging to Yahoo! Messenger
   ** purpose.
   ** <p>
   ** Yahoo! Messenger (sometimes abbreviated Y!M) was an
   ** advertisement-supported instant messaging client and associated protocol
   ** provided by Yahoo!.
   ** <br>
   ** Yahoo! Messenger was provided free of charge and could be downloaded and
   ** used with a generic "Yahoo ID" which also allowed access to other Yahoo!
   ** services, such as Yahoo! Mail.
   ** <br>
   ** The service also offered VoIP, file transfers, webcam hosting, a text
   ** messaging service, and chat rooms in various categories.
   ** <p>
   ** Yahoo! Messenger dates back to Yahoo! Chat, which was a public chat room
   ** service. The actual client, originally called Yahoo! Pager, launched on
   ** March 9, 1998 and renamed to Yahoo! Messenger in 1999.
   ** <p>
   ** The chat room service shut down in 2012.
   */
  public static final String YAHOO     = "yahoo";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty("primary")
  @Attribute(description="A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred messenger or primary messenger. The primary attribute value 'true' MUST appear no more than once.", required=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT)
  private Boolean primary;

  @JsonProperty("type")
  @Attribute(description="A label indicating the attribute's function; e.g., 'aim', 'gtalk', 'mobile' etc.", required=false, caseExact=false, canonical={AIM, GTALK, ICQ, XMPP, MSN, SKYPE, QQ, YAHOO}, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  type;

  @JsonProperty("value")
  @Attribute(description="Instant messaging address for the User.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  value;

  @JsonProperty("display")
  @Attribute(description="A human readable name, primarily used for display purposes.", required=false, caseExact=false, mutability=Definition.Mutability.READ_WRITE, returned=Definition.Returned.DEFAULT, uniqueness=Definition.Uniqueness.NONE)
  private String  display;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>InstantMessaging</code> SCIM Resource that
   ** allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public InstantMessaging() {
	  // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @param  value              the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    the <code>InstantMessaging</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>InstantMessaging</code>.
   */
  public final InstantMessaging primary(final Boolean value) {
    this.primary = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   primary
  /**
   ** Whether the 'primary' or preferred attribute value for this attribute.
   ** <p>
   ** The primary attribute value 'true' MUST appear no more than once.
   **
   ** @return                    the 'primary' or preferred attribute value for
   **                            this attribute.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public final Boolean primary() {
    return this.primary;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Sets the label indicating the attribute's function.
   **
   ** @param  value              the label indicating the attribute's function.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>InstantMessaging</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>InstantMessaging</code>.
   */
  public final InstantMessaging type(final String value) {
    this.type = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   type
  /**
   ** Returns the label indicating the attribute's function.
   **
   ** @return                    the label indicating the attribute's function.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String type() {
    return this.type;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Sets the instant messaging address for the User.
   **
   ** @param  value              the instant messaging address for the User.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>InstantMessaging</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>InstantMessaging</code>.
   */
  public final InstantMessaging value(final String value) {
    this.value = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   value
  /**
   ** Returns the instant messaging address for the User.
   **
   ** @return                    the instant messaging address for the User.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String value() {
    return this.value;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Sets the display name, primarily used for display purposes.
   **
   ** @param  value              the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the <code>InstantMessaging</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>InstantMessaging</code>.
   */
  public final InstantMessaging display(final String value) {
    this.display = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   display
  /**
   ** Returns the display name, primarily used for display purposes.
   **
   ** @return                    the display name, primarily used for display
   **                            purposes.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String display() {
    return this.display;
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
   */
  @Override
  public int hashCode() {
    int result = this.value != null ? this.value.hashCode() : 0;
    result = 31 * result + (this.display != null ? this.display.hashCode() : 0);
    result = 31 * result + (this.type    != null ? this.type.hashCode()    : 0);
    result = 31 * result + (this.primary != null ? this.primary.hashCode() : 0);
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>InstantMessaging</code>s are considered equal if and only if
   ** they represent the same properties. As a consequence, two given
   ** <code>InstantMessaging</code>s may be different even though they contain
   ** the same set of names with the same values, but in a different order.
   **
   ** @param  other              the reference object with which to compare.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final InstantMessaging that = (InstantMessaging)other;
    if (this.value != null ? !this.value.equals(that.value) : that.value != null)
      return false;

    if (this.display != null ? !this.display.equals(that.display) : that.display != null)
      return false;

    if (this.type != null ? !this.type.equals(that.type) : that.type != null)
      return false;

    return !(this.primary != null ? !this.primary.equals(that.primary) : that.primary != null);
  }
}