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

    System      :   Identity Service Library
    Subsystem   :   Generic SCIM Interface

    File        :   TestTypeEvaluator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TestTypeEvaluator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.scim.v2;

import org.junit.Test;

import org.junit.runner.JUnitCore;

import oracle.iam.junit.TestBase;

import oracle.iam.platform.scim.entity.Filter;

import oracle.iam.platform.scim.schema.Email;
import oracle.iam.platform.scim.schema.Photo;
import oracle.iam.platform.scim.schema.Support;
import oracle.iam.platform.scim.schema.Address;
import oracle.iam.platform.scim.schema.PhoneNumber;
import oracle.iam.platform.scim.schema.InstantMessaging;

import oracle.iam.platform.scim.entity.TypeEvaluator;

////////////////////////////////////////////////////////////////////////////////
// class TestTypeEvaluator
// ~~~~~ ~~~~~~~~~~~~~~~~~
/**
 ** Test coverage for filtering entity types.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestTypeEvaluator extends TestBase {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TestTypeEvaluator</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TestTypeEvaluator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **                            <br>
   **                            Allowed object is array of {@link String}.
   */
  @SuppressWarnings("unused")
  public static void main(final String[] args) {
    final String[] parameter = {TestTypeEvaluator.class.getName()};
    JUnitCore.main(parameter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testEmailTypeWork
  /**
   ** Tests e-Mail match the expected type <code>work</code>.
   */
  @Test
  public void testEmailTypeWork() {
    try {
      final Email candidate = Support.objectMapper().readValue("{\"primary\" : true, \"type\" : \"work\",  \"value\" : \"123\"}", Email.class);
      assertNotNull(candidate.type());
      assertNotNull(candidate.value());
      assertEquals(candidate.type(), "work");
      
      assertTrue("type eq \"wORk\"", TypeEvaluator.evaluate(Filter.from("type eq \"wORk\""), candidate.type()));
      assertTrue("type pr",          TypeEvaluator.evaluate(Filter.from("type pr"),          candidate.type()));
      assertTrue("type sw \"wO\"",   TypeEvaluator.evaluate(Filter.from("type sw \"wO\""),   candidate.type()));
      assertTrue("type ew \"Rk\"",   TypeEvaluator.evaluate(Filter.from("type ew \"Rk\""),   candidate.type()));
      assertTrue("type co \"or\"",   TypeEvaluator.evaluate(Filter.from("type co \"or\""),   candidate.type()));
      assertTrue("type lt \"x\"",    TypeEvaluator.evaluate(Filter.from("type lt \"x\""),    candidate.type()));
      assertTrue("type le \"work\"", TypeEvaluator.evaluate(Filter.from("type le \"work\""), candidate.type()));
      assertTrue("type gt \"a\"",    TypeEvaluator.evaluate(Filter.from("type gt \"a\""),    candidate.type()));
      assertTrue("type ge \"v\"",    TypeEvaluator.evaluate(Filter.from("type ge \"v\""),    candidate.type()));

      assertFalse(TypeEvaluator.evaluate(Filter.eq("type", "none"), candidate.type()));
      assertFalse(TypeEvaluator.evaluate(Filter.sw("type", "nONe"),   candidate.type()));
      assertFalse(TypeEvaluator.evaluate(Filter.ew("type", "noNE"),   candidate.type()));
      assertFalse(TypeEvaluator.evaluate(Filter.co("type", "NOne"),   candidate.type()));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testPhoneNumberTypeMobile
  /**
   ** Tests phone number match the expected type <code>mobile</code>.
   */
  @Test
  public void testPhoneNumberTypeMobile() {
    try {
      final PhoneNumber candidate = Support.objectMapper().readValue("{\"primary\" : true, \"type\" : \"mobile\",  \"value\" : \"555-555-4444\"}", PhoneNumber.class);
      assertNotNull(candidate.type());
      assertNotNull(candidate.value());
      assertEquals(candidate.type(), "mobile");

      assertTrue("type eq MoBilE", TypeEvaluator.evaluate(Filter.eq("type", "MoBilE"), candidate.type()));
      assertTrue("type pr ",       TypeEvaluator.evaluate(Filter.pr("type"),           candidate.type()));
      assertTrue("type sw mO",     TypeEvaluator.evaluate(Filter.sw("type", "mO"),     candidate.type()));
      assertTrue("type ew eL",     TypeEvaluator.evaluate(Filter.ew("type", "Le"),     candidate.type()));
      assertTrue("type co bI",     TypeEvaluator.evaluate(Filter.co("type", "bI"),     candidate.type()));
      assertTrue("type lt x",      TypeEvaluator.evaluate(Filter.lt("type", "N"),      candidate.type()));
      assertTrue("type le mobile", TypeEvaluator.evaluate(Filter.le("type", "mobile"), candidate.type()));
      assertTrue("type gt a",      TypeEvaluator.evaluate(Filter.gt("type", "a"),        candidate.type()));
      assertTrue("type ge l",      TypeEvaluator.evaluate(Filter.ge("type", "l"),        candidate.type()));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   testAddressTypeHome
  /**
   ** Tests address match the expected type <code>home</code>.
   */
  @Test
  public void testAddressTypeHome() {
    try {
      final Address candidate = Support.objectMapper().readValue("{\"primary\" : true, \"type\" : \"home\", \"locality\" : \"Hollywood\", \"region\" : \"CA\", \"postalCode\" : \"91608\", \"streetAddress\" : \"456 Hollywood Blvd\"}", Address.class);
      assertNotNull(candidate.type());
      assertNotNull(candidate.locality());
      assertNotNull(candidate.region());
      assertNotNull(candidate.postalCode());
      assertNotNull(candidate.streetAddress());
      assertEquals(candidate.type(), "home");

      assertTrue("type eq hOme", TypeEvaluator.evaluate(Filter.eq("type", "hOme"), candidate.type()));
      assertTrue("type pr ",     TypeEvaluator.evaluate(Filter.pr("type"),         candidate.type()));
      assertTrue("type sw Ho",   TypeEvaluator.evaluate(Filter.sw("type", "Ho"),   candidate.type()));
      assertTrue("type ew ME",   TypeEvaluator.evaluate(Filter.ew("type", "ME"),   candidate.type()));
      assertTrue("type co me",   TypeEvaluator.evaluate(Filter.co("type", "me"),   candidate.type()));
      assertTrue("type lt i",    TypeEvaluator.evaluate(Filter.lt("type", "i"),    candidate.type()));
      assertTrue("type le home", TypeEvaluator.evaluate(Filter.le("type", "home"), candidate.type()));
      assertTrue("type gt a",    TypeEvaluator.evaluate(Filter.gt("type", "a"),    candidate.type()));
      assertTrue("type ge b",    TypeEvaluator.evaluate(Filter.ge("type", "b"),    candidate.type()));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tesPhotoTypeThumbnail
  /**
   ** Tests photo match the expected type <code>thumbnail</code>.
   */
  @Test
  public void tesPhotoTypeThumbnail() {
    try {
      final Photo candidate = Support.objectMapper().readValue("{\"type\" : \"thumbnail\", \"value\" : \"https://photos.example.com/profilephoto/72930000000Ccne/T\"}", Photo.class);
      assertNotNull(candidate.type());
      assertNotNull(candidate.value());
      assertEquals(candidate.type(), "thumbnail");

      assertTrue("type eq thUmbNail", TypeEvaluator.evaluate(Filter.eq("type", "thUmbNail"), candidate.type()));
      assertTrue("type pr ",          TypeEvaluator.evaluate(Filter.pr("type"),              candidate.type()));
      assertTrue("type sw thUmb",     TypeEvaluator.evaluate(Filter.sw("type", "thUmb"),     candidate.type()));
      assertTrue("type ew Nail",      TypeEvaluator.evaluate(Filter.ew("type", "Nail"),      candidate.type()));
      assertTrue("type co uMb",       TypeEvaluator.evaluate(Filter.co("type", "uMb"),       candidate.type()));
      assertTrue("type lt what",      TypeEvaluator.evaluate(Filter.lt("type", "what"),      candidate.type()));
      assertTrue("type le thumbnail", TypeEvaluator.evaluate(Filter.le("type", "thumbnail"), candidate.type()));
      assertTrue("type gt a",         TypeEvaluator.evaluate(Filter.gt("type", "a"),         candidate.type()));
      assertTrue("type ge b",         TypeEvaluator.evaluate(Filter.ge("type", "b"),         candidate.type()));
    }
    catch (Exception e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   tesInstantMessagingTypeAIM
  /**
   ** Tests instant messaging match the expected type <code>aim</code>.
   */
  @Test
  public void tesInstantMessagingTypeAIM() {
    try {
      final InstantMessaging candidate = Support.objectMapper().readValue("{\"type\" : \"aim\", \"value\" : \"someaimhandle\"}", InstantMessaging.class);
      assertNotNull(candidate.type());
      assertNotNull(candidate.value());
      assertEquals(candidate.type(), "aim");
    }
    catch (Exception e) {
      failed(e);
    }
  }
}