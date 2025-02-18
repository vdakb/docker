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

    File        :   GroupFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    GroupFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gds.connector;

import java.util.Map;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.connector.DirectorySearch;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class GroupFixture extends Fixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        ACTOR    = "azitterbacke";
  /**
   ** The location of the people branch
   */
  static final QualifiedUid  BASE     = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=Groups"));

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>GroupFixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public GroupFixture() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s01SearchSimple
  /**
   ** Build a request to query and retrieve resources from the
   ** <code>Directory Service</code> belonging to accounts by leveraging no
   ** serach control.
   */
  @Test
  public void s01SearchSimple() {
    final boolean simple  = ENDPOINT.simplePageControl();
    final boolean virtual = ENDPOINT.virtualListControl();
    try {
      // tweak the configuration to use no control to fetch a result set
      ENDPOINT.simplePageControl(false);
      ENDPOINT.virtualListControl(false);
      // exeute a search
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.GROUP, GROUPOPTS.build(), null).execute(new Handler());
    }
    catch (SystemException e) {
      failed(e);
    }
    finally {
      ENDPOINT.simplePageControl(simple);
      ENDPOINT.virtualListControl(virtual);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s02SearchPaginated
  /**
   ** Build a request to query and retrieve resources from the
   ** <code>Directory Service</code> belonging to accounts by leveraging the
   ** <code>Simple Page Control</code>.
   */
  @Test
  public void s02SearchPaginated() {
    final boolean simple  = ENDPOINT.simplePageControl();
    final boolean virtual = ENDPOINT.virtualListControl();
    try {
      // tweak the configuration to use no control to fetch a result set
      ENDPOINT.simplePageControl(true);
      ENDPOINT.virtualListControl(false);
      // exeute a search
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.GROUP, GROUPOPTS.build(), null).execute(new Handler());
    }
    catch (SystemException e) {
      failed(e);
    }
    finally {
      ENDPOINT.simplePageControl(simple);
      ENDPOINT.virtualListControl(virtual);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s03SearchVirtualList
  /**
   ** Build a request to query and retrieve resources from the
   ** <code>Directory Service</code> belonging to accounts by leveraging the
   ** <code>Virtual List Control</code>.
   */
  @Test
  public void s03SearchVirtualList() {
    final boolean simple  = ENDPOINT.simplePageControl();
    final boolean virtual = ENDPOINT.virtualListControl();

    // virtual list requires a sort hence extend the operational option with
    // this requirement
    final Map<String, Object> option = GROUPOPTS.getOptions();
    option.put(OperationContext.SEARCH_ORDER, new String[]{"uid"});
    try {
      // tweak the configuration to use no control to fetch a result set
      ENDPOINT.simplePageControl(false);
      ENDPOINT.virtualListControl(true);
      // exeute a search
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.GROUP, GROUPOPTS.build(), null).execute(new Handler());
    }
    catch (SystemException e) {
      failed(e);
    }
    finally {
      ENDPOINT.simplePageControl(simple);
      ENDPOINT.virtualListControl(virtual);
    }
  }
}