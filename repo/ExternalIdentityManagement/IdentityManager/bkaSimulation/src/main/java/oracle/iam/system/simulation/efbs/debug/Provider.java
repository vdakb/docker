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

    Copyright © 2018 All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Manager Service Simulation
    Subsystem   :   eFBS SCIM Interface

    File        :   Provider.java

    Compiler    :   JDK 1.8

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    Provider.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2018-28-06  DSteding    First release version
*/

package oracle.iam.system.simulation.efbs.debug;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.Calendar;
import java.util.ArrayList;

import oracle.hst.foundation.utility.CollectionUtility;

import oracle.hst.foundation.utility.DateUtility;

import oracle.iam.system.simulation.ProcessingException;

import oracle.iam.system.simulation.scim.domain.PatchOperation;

import oracle.iam.system.simulation.scim.object.Filter;

import oracle.iam.system.simulation.scim.schema.Email;
import oracle.iam.system.simulation.scim.schema.Name;
import oracle.iam.system.simulation.scim.schema.Metadata;
import oracle.iam.system.simulation.scim.schema.PhoneNumber;

import oracle.iam.system.simulation.scim.v2.schema.UserResource;
import oracle.iam.system.simulation.scim.v2.schema.EnterpriseUserExtension;

import oracle.iam.system.simulation.efbs.v2.schema.Account;
import oracle.iam.system.simulation.efbs.v2.schema.UserExtension;

////////////////////////////////////////////////////////////////////////////////
// class Provider
// ~~~~~ ~~~~~~~~
/**
 ** The persistence layer to acces the database schmema
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Provider {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>Provider</code>
   ** <p>
   ** Singleton Pattern
   **
   ** Yes I know it should never be public but I'm lazy
   */
  public static final Provider instance   = new Provider();

  //////////////////////////////////////////////////////////////////////////////
  // static attributes
  //////////////////////////////////////////////////////////////////////////////

  static UserResource          dsteding   = new UserResource();
  static UserResource          sstrecke   = new UserResource();
  static UserResource          wcalla     = new UserResource();

  //////////////////////////////////////////////////////////////////////////////
  // static init block
  //////////////////////////////////////////////////////////////////////////////

  static {
    dsteding.id("4711123")
      .active(true)
      .userName("sn4711123")
      .name(new Name().familyName("Steding").givenName("Dieter"))
      .email(CollectionUtility.list(new Email().value("dieter.steding@vm.oracle.com")))
      .phoneNumber(CollectionUtility.list(new PhoneNumber().value("+49 177 5948 437")))
    ;
    dsteding.extension(new EnterpriseUserExtension().organization("SN").division("SN_1").department("SN_1_1"));
    dsteding.extension(new UserExtension().validFrom(DateUtility.now()));

    sstrecke.id("4711124")
      .active(true)
      .userName("be4711124")
      .name(new Name().familyName("Strecke").givenName("Sophie"))
      .email(CollectionUtility.list(new Email().value("sophie.strecke@vm.oracle.com")))
      .phoneNumber(CollectionUtility.list(new PhoneNumber().value("+49")))
    ;      
    sstrecke.extension(new EnterpriseUserExtension().organization("BE").division("BE_1").department("BE_1_1"));
    sstrecke.extension(new UserExtension().validFrom(DateUtility.now()));

    wcalla.id("0815124")
      .active(true)
      .userName("GEDBE0815124")
      .name(new Name().familyName("Wang").givenName("Calla"))
      .email(CollectionUtility.list(new Email().value("calla.wang@vm.oracle.com")))
      .phoneNumber(CollectionUtility.list(new PhoneNumber().value("+49 30 4664 412")))
    ;
    wcalla.extension(new EnterpriseUserExtension().organization("BE").division("BE_4").department("BE_4_1"));
    wcalla.extension(new UserExtension().validFrom(DateUtility.now()));

  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Provider</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private Provider() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountExists
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>id</code>.
   **
   ** @param  identifier         the public system identifier to lookup the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the advice which identifier <code>id</code> or
   **                            <code>username</code> is ment in the request.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @return                    <code>true</code> if the user resource mapped
   **                            at either <code>id</code> or
   **                            <code>username</code> (determined by
   **                            <code>primary</code>).
   **                            <br>
   **                            Possible  object is <code>boolean</code>.
   */
  public boolean accountExists(final String identifier, final boolean primary) {
    return (identifier.equals(dsteding.id()) || identifier.equals(sstrecke.id()));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountLookup
  /**
   ** Lookup an account by the specified public system identifier
   ** <code>id</code> or unique identifier <code>username</code>.
   ** <p>
   ** If <code>primary</code> evaluate to <code>true</code> the attribute
   ** {@link Account#ID} will be used for the purpose of matching otherwise
   ** {@link Account#USERNAME} will be matched.
   **
   ** @param  identifier         the system identifier to lookup the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  primary            the advice which identifier <code>id</code> or
   **                            <code>username</code> is ment in the request.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  attribute          the {@link Set} of attributes to be returned in
   **                            the resources.
   **                            <br>
   **                            Allowed object is {@link Set} where each
   **                            element is of type {@link String}
   **
   ** @return                    the {@link UserResource} mapped at
   **                            <code>id</code>.
   **                            <br>
   **                            Possible object is {@link UserResource}.
   */
  public UserResource accountLookup(final String identifier, final boolean primary, final Set<String> attribute) {
    if (identifier.equals(dsteding.id()))
      return dsteding;
    else if (identifier.equals(sstrecke.id()))
      return sstrecke;

    return null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCount
  /**
   ** Count accounts.
   **
   ** @param  expression       the filter expression to apply to count accounts.
   **                          <br>
   **                          Allowed object is {@link String}.
   **
   ** @return                  the amount of records matching the filter
   **                          expression.
   **                          <br>
   **                          Possible object is <code>int</code>.
   */
  public int accountCount(final Filter expression) {
    return 1;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountSearch
  /**
   ** Search account.
   **
   ** @return                    the result set as a {@link List} where each
   **                            element is of type {@link UserResource}.
   */
  public List<UserResource> accountSearch() {
    final List<UserResource> resource = new ArrayList<UserResource>();
    resource.add(wcalla);
    return resource;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountCreate
  /**
   ** Create an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  resource           the {@link UserResource} mapped at
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   */
  public void accountCreate(final UserResource resource) {
    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Updates an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  id                 the system identifier to delete the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  resource           the {@link UserResource} mapped at
   **                            <code>id</code> to update.
   **                            <br>
   **                            Allowed object is {@link UserResource}.
   */
  public void accountModify(final String id, final UserResource resource) {
    
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountModify
  /**
   ** Updates an account in the database.
   ** <p>
   ** After this method completed sucessfully the metadata of the given resource
   ** updated accordingly.
   **
   ** @param  id                 the system identifier to delete the
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  operation          the {@link List} of operations to update at
   **                            <code>id</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            elementis of type {@link PatchOperation}.
   */
  public void accountModify(final String id, final List<PatchOperation> operation) {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   accountDelete
  /**
   ** Deletes an account in the database.
   **
   ** @param  id                 the identifier of the resource to delete.
   **                            <br>
   **                            Allowed object is {@link String}.
   */
  public void accountDelete(final String id) {

  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   metadata
  /**
   ** Populate a {@link Metadata} from a {@link List} of database records.
   **
   ** @param  result             the data provider.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Pair} whose key is
   **                            the name of the database attribute and the
   **                            value the corresponding value.
   **
   ** @return                    the populated SCIM resource.
   **                            <br>
   **                            Allowed object is {@link UserResource}}.
   */
  private static Metadata metadata(final  Map<String, Object> result) {
    final Metadata metadata = new Metadata().resourceType(Account.SCHEMA.resource);

    // prevent bogus input
    if (CollectionUtility.empty(result))
      return metadata;

    metadata.version("4711");
    metadata.created(Calendar.getInstance());
    metadata.created().setTime(DateUtility.now());
    metadata.modified(Calendar.getInstance());
    metadata.modified().setTime(DateUtility.now());
    return metadata;
  }
}
