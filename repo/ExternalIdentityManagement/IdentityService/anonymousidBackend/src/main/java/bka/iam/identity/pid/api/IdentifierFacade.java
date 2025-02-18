/*
    Oracle Deutschland BV & Co. KG

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

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Identity Governance Extension
    Subsystem   :   Anonymous Identifier Assembler

    File        :   IdentifierFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    IdentifierFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.pid.api;

import java.io.Serializable;

import javax.ejb.Stateless;

import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceService;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.jpa.injection.Transactional;

import bka.iam.identity.pid.model.Identifier;

////////////////////////////////////////////////////////////////////////////////
// class IdentifierFacade
// ~~~~~ ~~~~~~~~~~~~~~~~
/**
 ** The session facade to manage {@link Identifier} entity persistent.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Stateless(name="identifierFacade")
public class IdentifierFacade extends    PersistenceService
                              implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:-8841952167905562919")
  private static final long serialVersionUID = 3535739342252084302L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>IdentifierFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public IdentifierFacade() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link Identifier} and returns
   ** a partial result based on given {@link SearchRequest} with the option
   ** whether to cache the results or not.
   ** <p>
   ** To ensure that the pagination approach works as expected, it have to
   ** ensured that the query always returns the result in the same order. This
   ** is only the case if the query contains an ORDER BY clause. Otherwise, the
   ** order of the result set is undefined and might change.
   **
   ** @param  request            the {@link SearchRequest} to apply on the
   **                            search operation.
   **                            <br>
   **                            Allowed object is {@link SearchRequest}.
   **
   ** @return                    the collection of entities.
   **                            <br>
   **                            Possible object is {@link SearchResult} where
   **                            each element is of type {@link Identifier}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public SearchResult<Identifier> list(final SearchRequest request)
    throws PersistenceException {

    return list(Identifier.class, request.start(), request.count(), request.total(), request.filter(), request.order());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link Identifier} by its
   ** <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Identifier} to
   **                            lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Identifier} mapped at
   **                            <code>id</code>.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Transactional(readOnly=true)
  public Identifier lookup(final String id)
    throws PersistenceException {

    return lookup(Identifier.class, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** JPA request to persist a {@link Identifier} entity.
   **
   ** @param  entity             the entity to persist.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   **
   ** @return                    the entity mapped at their <code>id</code> at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Identifier create(final Identifier entity)
    throws PersistenceException {

    final String id = entity.getId();
    if (id == null || id.isEmpty())
      throw PersistenceException.notNull(Identifier.class, Identifier.Attribute.ID.id);

    if (entity.getUsedBy() == null || entity.getUsedBy().isEmpty())
      throw PersistenceException.notNull(Identifier.class, Identifier.Attribute.USEDBY.id);

    return super.create(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** JPA request to modify a {@link Identifier} entity.
   **
   ** @param  entity             the entity to modify.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   **
   ** @return                    the modified entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Identifier}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Identifier modify(final Identifier entity)
    throws PersistenceException {

    final String id = entity.getId();
    if (id == null || id.isEmpty())
      throw PersistenceException.notNull(Identifier.class, Identifier.Attribute.ID.id);

    if (entity.getUsedBy() == null || entity.getUsedBy().isEmpty())
      throw PersistenceException.notNull(Identifier.class, Identifier.Attribute.USEDBY.id);

    final Identifier subject = this.em.find(entity.getClass(), id);
    subject.setActive(entity.getActive());
    subject.setUsedBy(entity.getUsedBy());
    return super.modify(subject);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** JPA request to remove a {@link Identifier} entity.
   **
   ** @param  entity             the entity to remove.
   **                            <br>
   **                            Allowed object is {@link Identifier}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public void delete(final Identifier entity)
    throws PersistenceException {

    final String id = entity.getId();
    if (id == null || id.isEmpty())
      throw PersistenceException.notNull(Identifier.class, Identifier.Attribute.ID.id);

    super.delete(entity);
  }
}