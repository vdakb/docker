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
    Subsystem   :   Unique Identifier Backend API

    File        :   TypeFacade.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    TypeFacade.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2022-03-11  DSteding    First release version
*/

package bka.iam.identity.uid.api;

import java.io.Serializable;

import javax.ejb.Stateless;

import oracle.hst.platform.jpa.SearchFilter;
import oracle.hst.platform.jpa.SearchResult;
import oracle.hst.platform.jpa.SearchRequest;
import oracle.hst.platform.jpa.PersistenceService;
import oracle.hst.platform.jpa.PersistenceException;

import oracle.hst.platform.jpa.injection.Transactional;

import bka.iam.identity.uid.model.Type;

////////////////////////////////////////////////////////////////////////////////
// class TypeFacade
// ~~~~~ ~~~~~~~~~~
/**
 ** The session facade to manage {@link Type} entity persistent.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Stateless(name=TypeFacade.NAME)
public class TypeFacade extends    PersistenceService
                        implements Serializable {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////
  
  public static final String NAME             = "typeFacade";

  // the official serial version ID which says cryptically which version we're
  // compatible with
  @SuppressWarnings("compatibility:8654605443137034427")
  private static final long serialVersionUID = 4613923988676215263L;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>TypeFacade</code> allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public TypeFacade() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   count
  /**
   ** Applies an aggregate expression applying the count operation to evaluate
   ** the numbers of entities in the persistence layer the belonges to the
   ** entity class specified.
   **
   ** @param  filter             the {@link SearchFilter} to apply on the
   **                            search.
   **                            <br>
   **                            Allowed object is {@link SearchFilter}.
   **
   ** @return                    the amount of entities belonging to the entity
   **                            class specified.
   **                            <br>
   **                            Possible object is {@link Long}.
   **
   ** @throws PersistenceException  if the entity manager has been closed or the
   **                               criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public Integer count(final SearchFilter filter)
    throws PersistenceException {

    return total(Type.class, filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Search for entities of type {@link Type} and returns
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
   **                            each element is of type {@link Type}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional(readOnly=true)
  public SearchResult<Type> list(final SearchRequest request)
    throws PersistenceException {

    return list(Type.class, request.start(), request.count(), request.total(), request.filter(), request.order());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lookup
  /**
   ** JPA request to retrieve a certain {@link Type} by its <code>id</code>.
   **
   ** @param  id                 the identifier of the {@link Type} to lookup.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link Type} mapped at <code>id</code>.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Possible object is {@link Type}.
   **
   ** @throws PersistenceException if <code>clazz</code> does not denote an
   **                              entity type or the <code>id</code> is not a
   **                              valid type for the entity's primary key or is
   **                              <code>null</code>.
   */
  @Transactional(readOnly=true)
  public Type lookup(final String id)
    throws PersistenceException {

    return lookup(Type.class, id);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   create
  /**
   ** JPA request to persist a {@link Type} entity.
   **
   ** @param  entity             the entity to persist.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @return                    the entity mapped at their <code>id</code> at
   **                            the Service Provider.
   **                            <br>
   **                            Possible object is {@link Type}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Type create(final Type entity)
    throws PersistenceException {

    // prevent bogus state of the entity
    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Type.class, Type.Attribute.ID.id);

    // prevent bogus state of the entity
    if (entity.getName() == null || entity.getName().isEmpty())
      throw PersistenceException.notNull(Type.class, Type.Attribute.NAME.id);

    // adjust the status of the entity if its not provided
    if (entity.getActive() == null)
      entity.setActive(true);

    return super.create(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   modify
  /**
   ** JPA request to modify a {@link Type} entity.
   **
   ** @param  entity             the entity to modify.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @return                    the modified entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Type}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Type modify(final Type entity)
    throws PersistenceException {

    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Type.class, Type.Attribute.ID.id);

    if (entity.getName() == null || entity.getName().isEmpty())
      throw PersistenceException.notNull(Type.class, Type.Attribute.NAME.id);

    return super.modify(entity);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delete
  /**
   ** JPA request to remove a {@link Type} entity.
   **
   ** @param  entity             the entity to remove.
   **                            <br>
   **                            Allowed object is {@link Type}.
   **
   ** @return                    the removed entity mapped at <code>id</code>
   **                            at the Service Provider.
   **                            <br>
   **                            Possible object is {@link Type}.
   **
   ** @throws PersistenceException if the entity manager has been closed or the
   **                              criteria query is found to be invalid.
   */
  @Transactional
  public Type delete(final Type entity)
    throws PersistenceException {

    if (entity.getId() == null || entity.getId().isEmpty())
      throw PersistenceException.notNull(Type.class, Type.Attribute.ID.id);

    super.delete(entity);
    return entity;
  }
}