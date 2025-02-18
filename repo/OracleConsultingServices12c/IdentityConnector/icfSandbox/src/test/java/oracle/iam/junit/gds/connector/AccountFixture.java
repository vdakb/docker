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

    File        :   AccountFixture.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   Dieter.Steding@oracle.com

    Purpose     :   This file implements the interface
                    AccountFixture.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.junit.gds.connector;

import java.util.Set;
import java.util.Map;

import org.junit.Test;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.QualifiedUid;

import oracle.iam.identity.connector.service.Descriptor;
import oracle.iam.identity.connector.service.DescriptorFactory;
import oracle.iam.identity.connector.service.DescriptorTransformer;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.icf.foundation.SystemException;
import oracle.iam.identity.icf.foundation.OperationContext;

import oracle.iam.identity.icf.foundation.utility.CollectionUtility;

import oracle.iam.identity.icf.connector.DirectorySearch;
import oracle.iam.identity.icf.connector.DirectoryCreate;
import oracle.iam.identity.icf.connector.DirectoryModify;
import oracle.iam.identity.icf.connector.DirectoryDelete;
import oracle.iam.identity.icf.connector.DirectoryEndpoint;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountFixture extends Fixture {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  static final String        ACTOR    = "azitterbacke";
  /**
   ** The location of the people branch
   */
  static final QualifiedUid  BASE     = new QualifiedUid(new ObjectClass("organizationalUnit"), new Uid("ou=People"));

  /** The provier mapping for testing create purpose */
  static Map<String, Object> CREATE   = CollectionUtility.<String, Object>map(
    new String[] {
      "UD_GDS_USR_DIT"
    , "UD_GDS_USR_UID"
    , "UD_GDS_USR_PWD"
    , "UD_GDS_USR_FIRST_NAME"
    , "UD_GDS_USR_LAST_NAME"
    , "UD_GDS_USR_COMMON_NAME"
    , "UD_GDS_USR_LANGUAGE"
    , "UD_GDS_USR_EMAIL"
  }
  , new Object[] {
      BASE.getUid().getUidValue() + "," + ENDPOINT.rootContext()
    , ACTOR
    , "Welcome1"
    , "Alfons"
    , "Zitterbacke"
    , "Zitterbacke, Alfons"
    , "en"
    , "x"
    }
  );

  /** The provier mapping for testing modify purpose */
  static Map<String, Object> MODIFY   = CollectionUtility.<String, Object>map(
    new String[] {
      "UD_GDS_USR_DIT"
    , "UD_GDS_USR_UID"
    , "UD_GDS_USR_EMAIL"
  }
  , new Object[] {
      BASE.getUid().getUidValue() + "," + ENDPOINT.rootContext()
    , ACTOR
    , "y"
    }
  );

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>AccountFixture</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public AccountFixture() {
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
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.ACCOUNT, PEOPLEOPTS.build(), null).execute(new Handler());
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
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.ACCOUNT, PEOPLEOPTS.build(), null).execute(new Handler());
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
    final Map<String, Object> option = PEOPLEOPTS.getOptions();
    option.put(OperationContext.SEARCH_ORDER, new String[]{"uid"});
    try {
      // tweak the configuration to use no control to fetch a result set
      ENDPOINT.simplePageControl(false);
      ENDPOINT.virtualListControl(true);
      // exeute a search
      DirectorySearch.build(ENDPOINT.simplePageControl(true), ObjectClass.ACCOUNT, PEOPLEOPTS.build(), null).execute(new Handler());
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
  // Method:   s04Create
  /**
   ** Build and executes the request to create the provided new LDAP account
   ** resource at the <code>Directory Service</code>.
   */
  @Test
  public void s04Create() {
    try {
      // simulating what the integration framework at OIM side does
      final Descriptor     descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      final Set<Attribute> attribute  =  DescriptorTransformer.build(descriptor, CREATE);
      // simulating what the implementation framework at ICF side does
      final Uid            uid        = DirectoryCreate.build(ENDPOINT, ObjectClass.ACCOUNT).execute(attribute);
      CONSOLE.info(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s05Modify
  /**
   ** Build and executes the request to modify a LDAP account resource at the
   ** <code>Directory Service</code>.
   */
  @Test
  public void s05Modify() {
    try {
      // simulating what the integration framework at OIM side does
      final Descriptor     descriptor = DescriptorFactory.configure(Descriptor.buildProvisioning(CONSOLE), PROVISIONING);
      final Set<Attribute> attribute  = DescriptorTransformer.build(descriptor, MODIFY);
      // simulating what the implementation framework at ICF side does
      final Uid uid = DirectoryModify.build(ENDPOINT, ObjectClass.ACCOUNT).execute(peopleUUID(ACTOR), attribute);
      CONSOLE.info(uid.toString());
    }
    catch (TaskException e) {
      failed(e);
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   s06Delete
  /**
   ** Build and executes the request to delete a LDAP account resource at the
   ** <code>Directory Service</code>.
   */
  @Test
  public void s06Delete() {
    try {
      final DirectoryDelete delete = DirectoryDelete.build(ENDPOINT, ObjectClass.ACCOUNT, false);
      delete.execute(peopleUUID(ACTOR));
    }
    catch (SystemException e) {
      failed(e);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   peopleUUID
  /**
   ** Returns te entry <code>UUID</code> the belongs to the specified entry
   ** name.
   **
   ** @param  entryName          the name of the entry used in the
   **                            <code>RDN</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    if entry <code>UUID</code> wrapped in an ICF
   **                           {@link UID}.
   */
  public static Uid peopleUUID(final String entryName) {
    return entryUUID(ObjectClass.ACCOUNT, entryName, PEOPLEOPTS.build());
  }
}