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

    Copyright © 2019. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Identity Frontend Extension
    Subsystem   :   Embedded Credential Collector

    File        :   OTPTemplate.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   tomas.t.sebo@oracle.com

    Purpose     :   This file implements the class
                    OTPTemplate.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2003-05-17  TSebo    First release version
*/


package bka.iam.platform.access.idp.type;


////////////////////////////////////////////////////////////////////////////////
// class OTPTemplate
// ~~~~~ ~~~~~~
/**
 ** POJO class which hold information about OTP Email template
 **
 **
 ** @author  tomas.t.sebo@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class OTPTemplate {
    
    //////////////////////////////////////////////////////////////////////////////
    // instance attributes
    //////////////////////////////////////////////////////////////////////////////
    private String            locale;
    private String            subject;
    private String            body;
    
    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>OTPTemplate</code> which create object, properties needs
     ** to be provided via getter and setter methods.
     **/
    public OTPTemplate() {
        super();
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>OTPTemplate</code> which populates its values from
     ** the specified properties.
     **/
    public OTPTemplate( String locale) {
        this(locale,null,null);
    }

    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>OTPTemplate</code> which populates its values from
     ** the specified properties.
     **/
    public OTPTemplate( String locale, String subject, String body) {
        super();
        this.locale  = locale;
        this.subject = subject;
        this.body = body;
    }
    





    //////////////////////////////////////////////////////////////////////////////
    // Methode:   getLocale
    /**
     ** Returns the value of the <code>locale</code> property.
     **
     ** @return                    the value of the <code>locale</code>
     **                            property.
     **                            Possible object is {@link String}.
     */
    public String getLocale() {
        return locale;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methode:   getSubject
    /**
     ** Returns the value of the <code>subject</code> property.
     **
     ** @return                    the value of the <code>subject</code>
     **                            property.
     **                            Possible object is {@link String}.
     */
    public String getSubject() {
        return subject;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methode:   getBody
    /**
     ** Returns the value of the <code>body</code> property.
     **
     ** @return                    the value of the <code>body</code>
     **                            property.
     **                            Possible object is {@link String}.
     */
    public String getBody() {
        return body;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methode:   setLocale
    /**
     ** Set the value of the <code>locale</code> property.
     **
     ** @param locale             the value of the <code>locale</code>
     **                            property.
     **                            Possible object is {@link String}.
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    //////////////////////////////////////////////////////////////////////////////
    // Methode:   setSubject
    /**
     ** Set the value of the <code>subject</code> property.
     **
     ** @param subject             the value of the <code>subject</code>
     **                            property.
     **                            Possible object is {@link String}.
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }


    //////////////////////////////////////////////////////////////////////////////
    // Methode:   setBody
    /**
     ** Set the value of the <code>body</code> property.
     **
     ** @param body             the value of the <code>body</code>
     **                         property.
     **                         Possible object is {@link String}.
     */
    public void setBody(String body) {
        this.body = body;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof OTPTemplate)) {
            return false;
        }
        final OTPTemplate other = (OTPTemplate) object;
        if (!(locale == null ? other.locale == null : locale.equals(other.locale))) {
            return false;
        }
        return true;
    }


    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((locale == null) ? 0 : locale.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return String.format("Locale: %s, Subject: %s, Body: %s",locale, subject, body);      
    }

}
