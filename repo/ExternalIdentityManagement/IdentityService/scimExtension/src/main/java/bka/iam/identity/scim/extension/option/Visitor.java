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

    System      :   Oracle Identity Manager
    Subsystem   :   Custom SCIM Service

    File        :   Visitor.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   sylvert.bernet@silverid.fr

    Purpose     :   This file implements the interface
                    Visitor.

    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-12-11  SBernet     First release version
*/
package bka.iam.identity.scim.extension.option;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.model.Attribute;
import bka.iam.identity.scim.extension.model.ListResponse;
import bka.iam.identity.scim.extension.model.Resource;
import bka.iam.identity.scim.extension.model.ScimResource;

import java.util.Collection;
////////////////////////////////////////////////////////////////////////////////
// interface Visitor
// ~~~~~~~~ ~~~~~~~~
/**
 ** The Visitor interface allows visiting SCIM resources and their attributes.
 ** <p>
 ** This interface defines the Visitor pattern, enabling processing or
 ** transformation of {@link Resource}, {@link Attribute}, and
 ** {@link ListResponse} objects. Implementations can define custom behaviors
 ** based on the type of object being visited.
 **
 ** @param <C> The type of collection returned when visiting a {@link Resource}.
 **            Allowed object is {@link Collection}.
 ** @param <T> The type of object returned when visiting an {@link Attribute}.
 **            Allowed object is {@link Object}.
 ** 
 ** @author sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since 1.0.0.0
 */
public interface Visitor<C extends Collection<T>, T> {

  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   ** Visits a {@link Resource} object and performs the necessary processing.
   ** <p>
   ** Implementations should define the specific logic for processing SCIM resources.
   **
   ** @param resource        The resource to be visited.
   **                        Allowed object is {@link Resource}.
   ** 
   ** @return                A collection of processed objects derived from the
   **                        resource.
   **                        Possible object is {@link Collection}.
   */
  C visit(ScimResource resource);

  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   ** Visits an {@link Attribute} object and performs the necessary processing.
   ** <p>
   ** This method is typically used for processing individual attributes within
   ** a SCIM resource.
   **
   ** @param attribute       The attribute to be visited.
   **                        Allowed object is {@link Attribute}.
   ** 
   ** @return                A processed object derived from the attribute.
   **                        Possible object is {@link Object}.
   */
  T visit(Attribute attribute);
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   visit
  /**
   ** Visits a {@link ListResponse} object and performs the necessary processing.
   ** <p>
   ** Implementations should define how to handle list responses, such as extracting or
   ** transforming the contained resources.
   **
   ** @param listResource     The list response to be visited.
   **                         Allowed object is {@link ListResponse}.
   ** 
   ** @param <R>              The type of resource contained in the list
   **                         response.
   ** 
   ** @return                 The processed list response, potentially modified
   **                         or analyzed.
   **                         Possible object is {@link ListResponse}.
   ** 
   ** @throws ScimException   If an error occurs during processing.
   */
  <R extends ScimResource>  ListResponse<R> visit(ListResponse<R> listResource)
   throws ScimException;
}
