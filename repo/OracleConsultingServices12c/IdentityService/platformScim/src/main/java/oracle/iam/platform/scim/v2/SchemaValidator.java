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

    File        :   SchemaValidator.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   dieter.steding@oracle.com

    Purpose     :   This file implements the class
                    SchemaValidator.


    Revisions    Date        Editor      Comment
    ------------+-----------+-----------+-----------------------------------
    1.0.0.0      2015-28-06  DSteding    First release version
*/

package oracle.iam.platform.scim.v2;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.LinkedHashSet;

import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import oracle.hst.platform.core.utility.StringUtility;
import oracle.hst.platform.core.utility.CollectionUtility;

import oracle.hst.platform.rest.ServiceException;

import oracle.iam.platform.scim.AttributeDefinition;
import oracle.iam.platform.scim.BadRequestException;
import oracle.iam.platform.scim.ProcessingException;
import oracle.iam.platform.scim.ResourceTypeDefinition;

import oracle.iam.platform.scim.schema.Support;

import oracle.iam.platform.scim.request.PatchOperation;

import oracle.iam.platform.scim.entity.Or;
import oracle.iam.platform.scim.entity.And;
import oracle.iam.platform.scim.entity.Not;
import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Equals;
import oracle.iam.platform.scim.entity.Filter;
import oracle.iam.platform.scim.entity.LessThan;
import oracle.iam.platform.scim.entity.Contains;
import oracle.iam.platform.scim.entity.EndsWith;
import oracle.iam.platform.scim.entity.Presence;
import oracle.iam.platform.scim.entity.StartsWith;
import oracle.iam.platform.scim.entity.GreaterThan;
import oracle.iam.platform.scim.entity.ComplexFilter;
import oracle.iam.platform.scim.entity.AttributeFilter;
import oracle.iam.platform.scim.entity.FilterEvaluator;
import oracle.iam.platform.scim.entity.LessThanOrEqual;
import oracle.iam.platform.scim.entity.GreaterThanOrEqual;

import static oracle.iam.platform.scim.annotation.Attribute.Mutability;

////////////////////////////////////////////////////////////////////////////////
// class SchemaValidator
// ~~~~~ ~~~~~~~~~~~~~~~
/**
 ** Utility class used to validate and enforce the schema constraints of a
 ** Resource Type on JSON objects representing SCIM resources.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class SchemaValidator {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private final Set<Option>                     enabled;
  private final ResourceTypeDefinition          resource;
  private final Collection<AttributeDefinition> definition;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // enum Option
  // ~~~~~ ~~~~~~
  /**
   ** Constraints that defines options affecting the way schema verifying is
   ** performed.
   ** <br>
   ** These options may be enabled and disabled before using the schema
   ** validator.
   */
  public enum Option {
      /**
       ** Relax SCIM 2 standard schema requirements by allowing core or extended
       ** attributes in the resource that are not defined by any schema in the
       ** resource type definition.
       */
      RELAX
      /**
       ** Weak SCIM 2 standard schema requirements by allowing sub-attributes
       ** that are not defined by the definition of the parent attribute.
       */
    , WEAK
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Result
  // ~~~~~ ~~~~~~
  /**
   ** Schema validation results.
   */
  public static class Result {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final List<String> path       = new LinkedList<String>();
    private final List<String> syntax     = new LinkedList<String>();
    private final List<String> filter     = new LinkedList<String>();
    private final List<String> mutability = new LinkedList<String>();

    ////////////////////////////////////////////////////////////////////////////
    // Accessor methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: path
    /**
     ** Returns the collection of path violations found during schema
     ** validation.
     **
     ** @return                  the collection of path violations found during
     **                          schema validation.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public List<String> path() {
      return CollectionUtility.unmodifiableList(this.path);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: syntax
    /**
     ** Returns the collection of syntax violations found during schema
     ** validation.
     **
     ** @return                  the collection of syntax violations found
     **                          during schema validation.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public List<String> syntax() {
      return CollectionUtility.unmodifiableList(this.syntax);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Returns the collection of filter violations found during schema
     ** validation.
     **
     ** @return                  the collection of filter violations found
     **                          during schema validation.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public List<String> filter() {
      return CollectionUtility.unmodifiableList(this.filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mutability
    /**
     ** Returns the collection of mutability violations found during schema
     ** validation.
     **
     ** @return                  the collection of mutability violations found
     **                          during schema validation.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     */
    public List<String> mutability() {
      return CollectionUtility.unmodifiableList(this.mutability);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: path
    /**
     ** Adds the specified <code>issue</code> to the collection of path
     ** violations.
     **
     ** @param  issue            the issue to add to the collection of path
     **                          violations.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    void path(final String issue) {
      this.path.add(issue);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: syntax
    /**
     ** Adds the specified <code>issue</code> to the collection of syntax
     ** violations.
     **
     ** @param  issue            the issue to add to the collection of syntax
     **                          violations.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    void syntax(final String issue) {
      this.syntax.add(issue);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: filter
    /**
     ** Adds the specified <code>issue</code> to the collection of filter
     ** violations.
     **
     ** @param  issue            the issue to add to the collection of filter
     **                          violations.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    void filter(final String issue) {
      this.filter.add(issue);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: mutability
    /**
     ** Adds the specified <code>issue</code> to the collection of mutability
     ** violations.
     **
     ** @param  issue            the issue to add to the collection of
     **                          mutability violations.
     **                          <br>
     **                          Allowed object is {@link String}.
     */
    void mutability(final String issue) {
      this.mutability.add(issue);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: throwViolation
    /**
     ** Throws an exception if there are schema validation errors.
     ** <br>
     ** The exception will contain all of the syntax errors, mutability errors
     ** or path issues (in that order of precedence).
     ** <br>
     ** The exception message will be the content of baseExceptionMessage
     ** followed by a space delimited list of all of the issues of the type
     ** (syntax, mutability, or path) being reported.
     **
     ** @throws BadRequestException if issues are found during schema verifying.
     */
    public void throwViolation()
      throws BadRequestException {

      if (this.syntax.size() > 0)
        throw BadRequestException.invalidSyntax(join(this.syntax));

      if (this.mutability.size() > 0)
        throw BadRequestException.mutability(join(this.mutability));

      if (this.path.size() > 0)
        throw BadRequestException.invalidPath(join(this.path));

      if (this.filter.size() > 0)
        throw BadRequestException.invalidFilter(join(this.filter));
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: join
    /**
     ** Converts a {@link List} of strings to single string by inserting a comma
     ** between each element of the list.
     **
     ** @param  list             the {@link List} of Strings to convert.
     **                          <br>
     **                          Allowed object is {@link List} where each
     **                          element is of type {@link String}.
     **
     ** @return                  a String made up of the words in the list.
     **                          <br>
     **                          Possible object is {@link String}.
     */
    private String join(final List<String> collection) {
      return StringUtility.join(collection, ", ");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Visitor
  // ~~~~~ ~~~~~~
  /**
   ** Filter visitor to verify attribute paths against the schema.
   */
  private class Visitor implements Filter.Visitor<Filter, Object> {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////

    private final Path                   path;
    private final ResourceTypeDefinition resource;
    private final SchemaValidator        validator;
    private final SchemaValidator.Result collector;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Create a new filter vistor.
     **
     ** @param  path             the path targeted by the filter operation.
     **                          <br>
     **                          Allowed object is {@link Path}.
     ** @param  resource         the {@link ResourceTypeDefinition} whose schema
     **                          to enforce.
     **                          <br>
     **                          Allowed object is
     **                          {@link ResourceTypeDefinition}.
     ** @param  collector        the collector of generated messages are to be
     **                          added.
     **                          <br>
     **                          Allowed object is
     **                          {@link SchemaValidator.Resul}.
     */
    private Visitor(final Path path, final ResourceTypeDefinition resource, final SchemaValidator validator, final SchemaValidator.Result collector) {
    	// ensure inheritance
      super();

      // initialize instance attributes
      this.path      = path;
      this.resource  = resource;
      this.validator = validator;
      this.collector = collector;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method of implemented interfaces
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>and</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>true</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link And}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final And filter, final Object parameter)
      throws ProcessingException {

      for (Filter f : filter.filter()) {
        f.accept(this, parameter);
      }
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>or</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** For the purposes of matching, an empty sub-filters should always
     ** evaluate to <code>false</code>.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Or}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final Or filter, final Object parameter)
      throws ProcessingException {

      for (Filter f : filter.filter()) {
        f.accept(this, parameter);
      }
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>not</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Not}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final Not filter, final Object parameter)
      throws ProcessingException {

      filter.filter().accept(this, parameter);
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>present</code> filter.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Presence}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final Presence filter, final Object parameter)
      throws ProcessingException {

      verifyPath(filter.path());
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits an <code>equality</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Equals}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final Equals filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>greater than</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter            the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThan}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final GreaterThan filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>greater than or equal to</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link GreaterThanOrEqual}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final GreaterThanOrEqual filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>less than</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThan}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final LessThan filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>less than or equal to</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link LessThanOrEqual}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final LessThanOrEqual filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>starts with</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link StartsWith}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final StartsWith filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a {@code ends with} filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link EndsWith}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link JsonNode}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final EndsWith filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>contains</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link Contains}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Filter}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override
    public final Filter apply(final Contains filter, final Object parameter)
      throws ProcessingException {

      return verify(filter);
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: apply (Filter.Visitor)
    /**
     ** Visits a <code>complex</code> filter.
     ** <p>
     ** <b>Note</b>:
     ** <br>
     ** Only the syntax of the filter attribute is considered.
     ** Any parameter is ignored.
     **
     ** @param  filter           the visited filter.
     **                          <br>
     **                          Allowed object is {@link ComplexFilter}.
     ** @param  parameter        a visitor specified parameter.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     ** @throws ProcessingException if an exception occurs during the operation.
     */
    @Override public final Filter apply(final ComplexFilter filter, final Object parameter)
      throws ProcessingException {

      verifyPath(filter.path());
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: verify
    /**
     ** Verifies an attribute filter.
     **
     ** @param  filter           the attribute filter to verify filter.
     **                          <br>
     **                          Allowed object is {@link AttributeFilter}.
     **
     ** @return                  a visitor specified result.
     **                          <br>
     **                          Possible object is {@link Boolean}.
     **
     */
    private Filter verify(final AttributeFilter filter) {
      verifyPath(filter.path());
      return filter;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: verifyPath
    /**
     ** Verifies a path.
     **
     ** @param  path             the path expression to verifiy.
     **                          <br>
     **                          Allowed object is {@link Path}.
     */
    private void verifyPath(final Path path) {
      if (this.path != null) {
        // simple, multi-valued attributes implicitly use "value" as the name to
        // access sub-attributes
        // don't print the sub-attribute undefined error in this case
        if (path.element(0).attribute().equalsIgnoreCase("value")) {
          final AttributeDefinition parent = this.resource.attributeDefinition(this.path);
          if (parent.multiValued() && (parent.attributes() == null)) {
            return;
          }
        }
        final AttributeDefinition definition = this.resource.attributeDefinition(this.path.attribute(path));
        if (definition == null) {
          // can't find the definition for the sub-attribute in a value filter
          this.collector.filter.add("sub-attribute " + path.element(0) + " in value filter for path " + this.path.toString() + " is undefined");
        }
      }
      else {
        final AttributeDefinition definition = this.resource.attributeDefinition(path);
        if (definition == null) {
          // can't find the attribute definition for attribute in path
          final List<String> messages = new ArrayList<String>();
          this.validator.undefinedAttribute(path, "", messages);
          if (!messages.isEmpty()) {
            for (String m : messages) {
              this.collector.filter.add(m);
            }
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a new instance that may be used to validate and enforce schema
   ** constraints for a resource type.
   **
   ** @param  resource           the {@link ResourceTypeDefinition} whose schema
   **                            to enforce.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   */
  private SchemaValidator(final ResourceTypeDefinition resource) {
  	// ensure inheritance
    super();

    // initialize instance attributes
    this.enabled    = new HashSet<Option>();
    this.resource   = resource;
    this.definition = new LinkedHashSet<AttributeDefinition>(resource.core().attribute().size() + 4);
    this.definition.addAll(SchemaFactory.COMMON);
    this.definition.addAll(resource.core().attribute());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   with
  /**
   ** Enable an option.
   **
   ** @param  value              the option to enable.
   **                            <br>
   **                            Allowed object is {@link Option}.
   **
   ** @return                    the <code>SchemaValidator</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SchemaValidator</code>.
   */
  public final SchemaValidator with(final Option value) {
    this.enabled.add(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   without
  /**
   ** Disnable an option.
   **
   ** @param  value              the option to disable.
   **                            <br>
   **                            Allowed object is {@link Option}.
   **
   ** @return                    the <code>SchemaValidator</code> to allow
   **                            method chaining.
   **                            <br>
   **                            Possible object is
   **                            <code>SchemaValidator</code>.
   */
  public final SchemaValidator without(final Option value) {
    this.enabled.remove(value);
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   of
  /**
   ** Factory method to create a SCIM <code>SchemaValidator</code>.
   **
   ** @param  resource           the {@link ResourceTypeDefinition} whose schema
   **                            to enforce.
   **                            <br>
   **                            Allowed object is
   **                            {@link ResourceTypeDefinition}.
   **
   ** @return                    an instance of a SCIM
   **                            <code>SchemaValidator</code>.
   **                            <br>
   **                            Possible object is <code>SchemaValidator</code>.
   **
   ** @throws ServiceException   if <code>resource</code> is <code>null</code>.
   */
  public static SchemaValidator of(final ResourceTypeDefinition resource)
    throws ServiceException {

    // prevent bogus input
    if (resource == null)
      throw ServiceException.argumentIsNull("resource");

    return new SchemaValidator(resource);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onSearch
  /**
   ** Validates the provided filter against the schema.
   **
   ** @param  filter             the filter to verify.
   **
   ** @return                    the validation results.
   **                            <br>
   **                            Possible object is {@link Result}
   **
   ** @throws ProcessingException if an error occurred while verifying the
   **                             schema.
   */
  public Result onSearch(final Filter filter)
      throws ProcessingException {

    final Result     collector = new Result();
    verifyFilter(filter, collector);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onCreate
  /**
   ** Validates a new SCIM resource against the schema.
   ** <br>
   ** The following checks will be performed:
   ** <ul>
   **   <li>All schema URIs in the schemas attribute are defined.
   **   <li>All required schema extensions are present.
   **   <li>All required attributes are present.
   **   <li>All attributes are defined in schema.
   **   <li>All attribute values match the types defined in schema.
   **   <li>All canonical type values match one of the values defined in the
   **       schema.
   **   <li>No attributes with values are read-only.
   ** </ul>
   **
   ** @param  node               the SCIM resource that will be created.
   **                            <br>
   **                            Any read-only attributes should be removed
   **                            first using
   **                            {@link #removeReadOnly(ObjectNode)}.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the validation results.
   **                            <br>
   **                            Possible object is {@link Result}
   **
   ** @throws ProcessingException if an error occurred while verifying the
   **                             schema.
   */
  public Result onCreate(final ObjectNode node)
    throws ProcessingException {

    final Result collector = new Result();
    verifyResource("", node.deepCopy(), collector, null, false);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onReplace
  /**
   ** Validates a replacement SCIM resource against the schema.
   ** <br>
   ** The current state of the SCIM resource may be provided to enable
   ** additional checks or attributes that are immutable.
   ** <br>
   ** The following checks will be performed:
   ** <ul>
   **   <li>All schema URIs in the schemas attribute are defined.
   **   <li>All required schema extensions are present.
   **   <li>All attributes are defined in schema.
   **   <li>All attribute values match the types defined in schema.
   **   <li>All canonical type values match one of the values defined in the
   **       schema.
   **   <li>No attributes with values are read-only.
   ** </ul>
   ** <p>
   ** Additional checks if the current state of the SCIM resource is provided:
   ** <ul>
   **   <li>Immutable attribute values are not replaced if they already have a
   **       value.
   ** </ul>
   **
   ** @param  replace            the replacement SCIM resource that will be
   **                            replaced.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  current            the current state of the SCIM resource or
   **                            <code>null</code> if not available.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the validation results.
   **                            <br>
   **                            Possible object is {@link Result}
   **
   ** @throws ProcessingException if an error occurred while verifying the
   **                             schema.
   */
  public Result onReplace(final ObjectNode replace, final ObjectNode current)
    throws ProcessingException {

    final Result collector = new Result();
    verifyResource("", replace.deepCopy(), collector, current == null ? null : current.deepCopy(), true);
    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   onModify
  /**
   ** Validates a set of modify patch operations against the schema.
   ** <br>
   ** The current state of the SCIM resource may be provided to enable
   ** additional checks for attributes that are immutable or required.
   ** <br>
   ** The following checks will be performed:
   ** <ul>
   **   <li>Undefined schema URIs are not added to the schemas attribute.
   **   <li>Required schema extensions are not removed.
   **   <li>Required attributes are not removed.
   **   <li>Undefined attributes are not added.
   **   <li>New attribute values match the types defined in the schema.
   **   <li>New canonical values match one of the values defined in the schema.
   **   <li>Read-only attribute are not modified.
   **   </li>
   ** </ul>
   ** <p>
   ** Additional checks if the current state of the SCIM resource is provided:
   ** <ul>
   **   <li>The last value from a required multi-valued attribute is not
   **       removed.
   **   <li>Immutable attribute values are not modified if they already have a
   **       value.
   **   </li>
   ** </ul>
   **
   ** @param  operation          the set of modify patch operations to verify.
   **                            <br>
   **                            Allowed object is {@link Iterable} where each
   **                            element is of type {@link PatchOperation}.
   ** @param  current            the current state of the SCIM resource or
   **                            <code>null</code> if not available.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    the validation results.
   **                            <br>
   **                            Possible object is {@link Result}
   **
   ** @throws ProcessingException if an error occurred while verifying the
   **                             schema.
   */
  public Result onModify(final Iterable<PatchOperation> operation, final ObjectNode current)
    throws ProcessingException {

    final Result     collector = new Result();
    final ObjectNode snapshot  = current == null ? null : current.deepCopy();
    final ObjectNode applied   = current == null ? null : removeReadOnly(current.deepCopy());

    int    i = 0;
    String prefix;
    for (PatchOperation op : operation) {
      final Path                path      = op.path();
      final JsonNode            value     = op.node();
      final Filter              filter    = path == null ? null : path.element(path.size() - 1).filter();
      final AttributeDefinition attribute = path == null ? null : this.resource.attributeDefinition(path);

      prefix = "Patch op[" + i + "]: ";
      if (path != null && attribute == null) {
        // can't find the attribute definition for attribute in path
        undefinedAttribute(path, prefix, collector.path);
        continue;
      }
      if (filter != null && attribute != null && !attribute.multiValued())
        collector.path.add(prefix + "Attribute " + path.element(0) + " in path " + path.toString() + " must not have a value selection filter because it is not multi-valued");

      if (filter != null && attribute != null)
        verifyFilter(path.withoutFilters(), filter, collector);

      switch (op.type()) {
        case ADD      : if (attribute == null) {
                          verifyPartial(prefix, (ObjectNode)value, collector, snapshot, false, true);
                        }
                        else {
                          verifyMutability(prefix, value, path, attribute, collector, current, false, true, false);
                          if (filter != null) {
                            verifyValues(prefix, value, path, attribute, collector, current, false, true);
                          }
                          else {
                            verifyValues(prefix, value, path, attribute, collector, snapshot, false, true);
                          }
                        }
                        break;
        case REMOVE   : if (attribute == null) {
                          continue;
                        }
                        verifyMutability(prefix, null, path, attribute, collector, current, false, false, false);
                        if (filter == null) {
                          verifyRequired(prefix, path, attribute, collector);
                        }
                        break;
        case REPLACE  : if (attribute == null) {
                          verifyPartial(prefix, (ObjectNode)value, collector, snapshot, true, false);
                        }
                        else {
                          verifyMutability(prefix, value, path, attribute, collector, current, true, false, false);
                          if (filter != null) {
                            verifyValues(prefix, value, path, attribute, collector, current, true, false);
                          }
                          else {
                            verifyValues(prefix, value, path, attribute, collector, snapshot, true, false);
                          }
                        }
                        break;
      }

      if (applied != null) {
        // apply the patch so we can later ensure these set of operations wont
        // be removing the all the values from a required multi-valued attribute
        try {
          op.apply(applied);
        }
        catch (BadRequestException e) {
          // no target exceptions are operational errors and not related to the
          // schema
          // just ignore
          if (!e.error().type().equals(BadRequestException.INVALID_TARGET)) {
            throw e;
          }
        }
      }
      i++;
    }
    if (applied != null)
      verifyResource("Applying patch op results in an invalid resource: ", applied, collector, snapshot, false);

    return collector;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeReadOnly
  /**
   ** Remove any read-only attributes and/or sub-attributes that are present in
   ** the provided SCIM resource.
   ** <br>
   ** This should be performed on new and replacement SCIM resources before
   ** schema verifying since read-only attributes should be ignored by the
   ** service provider on create with POST and modify with PUT operations.
   **
   ** @param  node               the SCIM resource to remove read-only
   **                            attributes from.
   **                            <br>
   **                            This method will not alter the provided
   **                            resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **
   ** @return                    a copy of the SCIM resource with the read-only
   **                            attributes (if any) removed.
   **                            <br>
   **                            Possible object is {@link ObjectNode}.
   */
  public ObjectNode removeReadOnly(final ObjectNode node) {
    final ObjectNode copy = node.deepCopy();
    for (SchemaResource extension : this.resource.extension().keySet()) {
      JsonNode schema = copy.get(extension.id());
      if (schema != null && schema.isObject()) {
        removeReadOnly(extension.attribute(), (ObjectNode)schema);
      }
    }
    removeReadOnly(this.definition, copy);
    return copy;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   undefinedAttribute
  /**
   ** Generate an appropriate error message(s) for an undefined attribute, or no
   ** message if the enabled options allow for the undefined attribute.
   **
   ** @param  path               the path referencing an undefined attribute.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  prefix             a prefix for the generated message, or empty
   **                            string if no prefix is needed.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  collector          the generated messages are to be added to this
   **                            list.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link String}.
   */
  void undefinedAttribute(final Path path, final String prefix, final List<String> collector) {
    if (path.size() > 1) {
      // this is a path to a sub-attribute
      // see if the parent attribute is defined
      if (this.resource.attributeDefinition(path.sub(1)) == null) {
        // the parent attribute is also undefined
        if (!this.enabled.contains(Option.RELAX)) {
          collector.add(prefix + "Attribute " + path.element(0) + " in path " + path.toString() + " is undefined");
        }
      }
      else {
        // the parent attribute is defined but the sub-attribute is undefined
        if (!this.enabled.contains(Option.WEAK)) {
          collector.add(prefix + "Sub-attribute " + path.element(1) + " in path " + path.toString() + " is undefined");
        }
      }
    }
    else if (!this.enabled.contains(Option.RELAX)) {
      collector.add(prefix + "Attribute " + path.element(0) + " in path " + path.toString() + " is undefined");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   removeReadOnly
  /**
   ** Internal method to remove read-only attributes.
   **
   ** @param  attributes         the collection of attribute definitions.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link AttributeDefinition}.
   ** @param  node               the {@link ObjectNode} to remove from.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   */
  private void removeReadOnly(final Collection<AttributeDefinition> attributes, final ObjectNode node) {
    for (AttributeDefinition attribute : attributes) {
      if (attribute.mutability() == Mutability.READ_ONLY) {
        node.remove(attribute.name());
        continue;
      }
      if (attribute.attributes() != null) {
        JsonNode path = node.path(attribute.name());
        if (path.isObject()) {
          removeReadOnly(attribute.attributes(), (ObjectNode)path);
        }
        else if (path.isArray()) {
          for (JsonNode value : path) {
            if (value.isObject()) {
              removeReadOnly(attribute.attributes(), (ObjectNode)value);
            }
          }
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyResource
  /**
   ** Internal method to verify a SCIM resource.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  node               the partial SCIM resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  replace            whether this is a replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyResource(final String prefix, final ObjectNode node, final Result collector, final ObjectNode current, final boolean replace)
      throws ProcessingException {

    // iterate through the schemas
    final JsonNode schemas = node.get(SchemaFactory.SCHEMAS.name());
    if (schemas != null && schemas.isArray()) {
      boolean coreFound = false;
      for (JsonNode schema : schemas) {
        if (!schema.isTextual()) {
          // go to the next one if the schema URI is not valid. We will report
          // this issue later when we verify the values for the schemas
          // attribute
          continue;
        }

        // get the extension namespace object node
        JsonNode extensionNode = node.remove(schema.textValue());
        if (extensionNode == null) {
          // extension listed in schemas but no namespace in resource
          // treat it as an empty namesapce to verify for required attributes
          extensionNode = Support.nodeFactory().objectNode();
        }
        if (!extensionNode.isObject()) {
          // go to the next one if the extension namespace is not valid
          collector.syntax.add(prefix + "Extended attributes namespace " + schema.textValue() + " must be a JSON object");
          continue;
        }

        // find the schema definition
        Map.Entry<SchemaResource, Boolean> extensionDefinition = null;
        if (schema.textValue().equals(this.resource.core().id())) {
          // skip the core schema
          coreFound = true;
          continue;
        }
        else {
          for (Map.Entry<SchemaResource, Boolean> extension : this.resource.extension().entrySet()) {
            if (schema.textValue().equals(extension.getKey().id())) {
              extensionDefinition = extension;
              break;
            }
          }
        }
        if (extensionDefinition == null) {
          // bail if we can't find the schema definition. We will report this
          // issue later when we verify the values for the schemas attribute
          continue;
        }
        verifyNode(prefix, Path.build(schema.textValue()), extensionDefinition.getKey().attribute(), (ObjectNode)extensionNode, collector, current, replace, false, replace);
      }

      if (!coreFound) {
        // make sure core schemas was included
        collector.syntax.add(prefix + "Value for attribute schemas must contain schema URI " + this.resource.core().id() + " because it is the core schema for this resource type");
      }

      // make sure all required extension schemas were included
      for (Map.Entry<SchemaResource, Boolean> extension : this.resource.extension().entrySet()) {
        if (extension.getValue()) {
          boolean found = false;
          for (JsonNode schema : schemas) {
            if (schema.textValue().equals(extension.getKey().id())) {
              found = true;
              break;
            }
          }
          if (!found) {
            collector.syntax.add(prefix + "Value for attribute schemas must contain schema URI " + extension.getKey().id() + " because it is a required schema extension for this resource type");
          }
        }
      }
    }

    // all defined schema extensions should be removed
    // remove any additional extended attribute namespaces not included in the
    // schemas attribute
    final Iterator<Map.Entry<String, JsonNode>> i = node.fields();
    while(i.hasNext()) {
      final String fieldName = i.next().getKey();
      if (Support.namespace(fieldName)) {
        collector.syntax.add(prefix + "Extended attributes namespace " + fieldName + " must be included in the schemas attribute");
        i.remove();
      }
    }
    // verify common and core schema
    verifyNode(prefix, Path.build(), this.definition, node, collector, current, replace, false, replace);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyPartial
  /**
   ** Verify a partial resource that is part of the patch operation with no
   ** path.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @param  node               the partial SCIM resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   **                            Allowed object is {@link ObjectNode}.
   ** @param  partialReplace     whether this is a partial replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  partialAdd         whether this is a partial add.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyPartial(final String prefix, final ObjectNode node, final Result collector, final ObjectNode current, final boolean partialReplace, final boolean partialAdd)
    throws ProcessingException {

    Iterator<Map.Entry<String, JsonNode>> i = node.fields();
    while (i.hasNext()) {
      Map.Entry<String, JsonNode> field = i.next();
      if (Support.namespace(field.getKey())) {
        if (!field.getValue().isObject()) {
          // bail if the extension namespace is not valid
          collector.syntax.add(prefix + "Extended attributes namespace " + field.getKey() + " must be a JSON object");
        }
        else {
          boolean found = false;
          for (SchemaResource extension : this.resource.extension().keySet()) {
            if (extension.id().equals(field.getKey())) {
              verifyNode(prefix, Path.build(field.getKey()), extension.attribute(), (ObjectNode)field.getValue(), collector, current, partialReplace, partialAdd, false);
              found = true;
              break;
            }
          }
          if (!found && !this.enabled.contains(Option.RELAX)) {
            collector.syntax.add(prefix + "Undefined extended attributes namespace " + field);
          }
        }
        i.remove();
      }
    }
    // verify common and core schema
    verifyNode(prefix, Path.build(), this.definition, node, collector, current, partialReplace, partialAdd, false);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyNode
  /**
   ** Verify an {@link ObjectNode} containing the core attributes or extended
   ** attributes.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parent             the path of the parent node.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  definition         the attribute definitions.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link AttributeDefinition}.
   ** @param  objectNode         the {@link ObjectNode} to verify.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  partialReplace     whether this is a partial replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  partialAdd         whether this is a partial add.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  replace            whether this is a replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyNode(final String prefix, final Path parent, final Collection<AttributeDefinition> definition, final ObjectNode objectNode, final Result collector, final ObjectNode current, final boolean partialReplace, final boolean partialAdd, final boolean replace)
    throws ProcessingException {

    // prevent bogus input
    if (definition == null)
      return;

    for (AttributeDefinition attribute : definition) {
      final Path     path   = parent.attribute((attribute.name()));
      final JsonNode cursor = objectNode.remove(attribute.name());
      if (cursor == null || cursor.isNull() || (cursor.isArray() && cursor.size() == 0)) {
        // from SCIM's perspective, these are the same thing
        if (!partialAdd && !partialReplace) {
          verifyRequired(prefix, path, attribute, collector);
        }
      }
      if (cursor != null) {
        // additional checks for when the field is present
        verifyMutability(prefix, cursor, path, attribute, collector, current, partialReplace, partialAdd, replace);
        verifyValues(prefix, cursor, path, attribute, collector, current, partialReplace, partialAdd);
      }
    }

    // all defined attributes should be removed
    // remove any additional undefined attributes
    final Iterator<Map.Entry<String, JsonNode>> i = objectNode.fields();
    while (i.hasNext()) {
      final String undefined = i.next().getKey();
      if (parent.size() == 0) {
        if (!this.enabled.contains(Option.RELAX)) {
          collector.syntax.add(prefix + "Core attribute " + undefined + " is undefined for schema " + this.resource.core().id());
        }
      }
      else if (parent.empty() && parent.namespace() != null) {
        if (!this.enabled.contains(Option.RELAX)) {
          collector.syntax.add(prefix + "Extended attribute " + undefined + " is undefined for schema " + parent.namespace());
        }
      }
      else {
        if (!this.enabled.contains(Option.WEAK)) {
          collector.syntax.add(prefix + "Sub-attribute " + undefined + " is undefined for attribute " + parent);
        }
      }
      i.remove();
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyMutability
  /**
   ** Verify the attribute to see if it violated any mutability constraints.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  node               the attribute value.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  path               the attribute path.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  attribute          the attribute definition.
   **                            <br>
   **                            Allowed object is {@link AttributeDefinition}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  partialReplace     whether this is a partial replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  partialAdd         whether this is a partial add.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  replace            whether this is a replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyMutability(final String prefix, final JsonNode node, final Path path, final AttributeDefinition attribute, final Result collector, final ObjectNode current, final boolean partialReplace, final boolean partialAdd, final boolean replace)
    throws ProcessingException {
    if (attribute.mutability() == Mutability.READ_ONLY) {
      collector.mutability.add(prefix + "Attribute " + path + " is read-only");
    }
    if (attribute.mutability() == Mutability.IMMUTABLE) {
      if (node == null) {
        collector.mutability.add(prefix + "Attribute " + path + " is immutable and value(s) may not be removed");
      }
      if (partialReplace && !replace) {
        collector.mutability.add(prefix + "Attribute " + path + " is immutable and value(s) may not be replaced");
      }
      else if (partialAdd && current != null && Support.existsPath(path, current)) {
        collector.mutability.add(prefix + "Attribute " + path + " is immutable and value(s) may not be added");
      }
      else if (current != null) {
        List<JsonNode> currentValues = Support.matchPath(path, current);
        if (currentValues.size() > 1 || (currentValues.size() == 1 && !currentValues.get(0).equals(node))) {
          collector.mutability.add(prefix + "Attribute " + path + " is immutable and it already has a value");
        }
      }
    }

    Filter valueFilter = path.element(path.size() - 1).filter();
    if (attribute.equals(SchemaFactory.SCHEMAS) && valueFilter != null) {
      // make sure the core schema and/or required schemas extensions are not
      // removed
      if (FilterEvaluator.evaluate(valueFilter, TextNode.valueOf(this.resource.core().id()))) {
        collector.syntax.add(prefix + "Attribute value(s) " + path + " may not be removed or replaced because the core schema " + this.resource.core().id() + " is required for this resource type");
      }
      for (Map.Entry<SchemaResource, Boolean> extension : this.resource.extension().entrySet()) {
        if (extension.getValue() && FilterEvaluator.evaluate(valueFilter, TextNode.valueOf(extension.getKey().id()))) {
          collector.syntax.add(prefix + "Attribute value(s) " + path + " may not be removed or replaced because the schema extension " + extension.getKey().id() + " is required for this resource type");
        }
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyNode
  /**
   ** Verify the attribute to see if it violated any requirement constraints.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  path               the attribute path.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  attribute          the attribute definition.
   **                            <br>
   **                            Allowed object is {@link AttributeDefinition}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   */
  private void verifyRequired(final String prefix, final Path path, final AttributeDefinition attribute, final Result collector) {
    // verify required attributes are all present
    if (attribute.required()) {
      collector.syntax.add(prefix + "Attribute " + path + " is required and must have a value");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyValues
  /**
   ** Verify the attribute values to see if it has the right type.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  node               the attribute value.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  path               the attribute path.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  attribute          the attribute definition.
   **                            <br>
   **                            Allowed object is {@link AttributeDefinition}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  partialReplace     whether this is a partial replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  partialAdd         whether this is a partial add.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyValues(final String prefix, final JsonNode node, final Path path, final AttributeDefinition attribute, final Result collector, final ObjectNode current, final boolean partialReplace, final boolean partialAdd)
    throws ProcessingException {

    if (attribute.multiValued() && !node.isArray()) {
      collector.syntax.add(prefix + "Value for multi-valued attribute " + path + " must be a JSON array");
      return;
    }
    if (!attribute.multiValued() && node.isArray()) {
      collector.syntax.add(prefix + "Value for single-valued attribute " + path + " must not be a JSON array");
      return;
    }

    if (node.isArray()) {
      int i = 0;
      for (JsonNode value : node) {
        // use a special notation attr[index] to refer to a value of an JSON
        // array
        if (path.empty())
          throw BadRequestException.noTarget("Path should always point to an attribute");

        final Path valuePath = path.sub(path.size() - 1).attribute(path.element(path.size() - 1).attribute() + "[" + i + "]");
        verifyValue(prefix, value, valuePath, attribute, collector, current, partialReplace, partialAdd);
        i++;
      }
    }
    else {
      verifyValue(prefix, node, path, attribute, collector, current, partialReplace, partialAdd);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyValue
  /**
   ** Verify an attribute value to see if it has the expected type.
   **
   ** @param  prefix             the issue prefix.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  node               the attribute value.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   ** @param  path               the attribute path.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  attribute          the attribute definition.
   **                            <br>
   **                            Allowed object is {@link AttributeDefinition}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   ** @param  current            the current resource.
   **                            <br>
   **                            Allowed object is {@link ObjectNode}.
   ** @param  partialReplace     whether this is a partial replace.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   ** @param  partialAdd         whether this is a partial add.
   **                            <br>
   **                            Allowed object is <code>boolean</code>.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyValue(final String prefix, final JsonNode node, final Path path, final AttributeDefinition attribute, final Result collector, final ObjectNode current, final boolean partialReplace, final boolean partialAdd)
      throws ProcessingException {

    // prevent bogus input
    if(node.isNull())
      return;

    // verify the node type
    switch (attribute.type()) {
      case STRING:
      case DATETIME:
      case REFERENCE : if (!node.isTextual()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " must be a JSON string");
                         return;
                       }
                       break;
      case BOOLEAN   : if (!node.isBoolean()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " must be a JSON boolean");
                         return;
                       }
                       break;
      case DECIMAL   :
      case INTEGER   : if (!node.isNumber()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " must be a JSON number");
                         return;
                       }
                       break;
      case COMPLEX   : if (!node.isObject()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " must be a JSON object");
                         return;
                       }
                       break;
      case BINARY    : if (!node.isTextual() && !node.isBinary()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " must be a JSON string");
                         return;
                       }
                       break;
      default        : throw BadRequestException.invalidSyntax("Unexpected attribute type " + attribute.type());
    }

    // if the node type checks out, verify the actual value
    switch (attribute.type()) {
      case DATETIME  : try {
                         Support.nodeToDateValue(node);
                       }
                       catch (Exception e) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " is not a valid xsd:dateTime formatted string");
                       }
                       break;
      case BINARY    : try {
                         node.binaryValue();
                       }
                       catch (Exception e) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " is not a valid base64 encoded string");
                       }
                       break;
      case REFERENCE : try {
                         new URI(node.textValue());
                       }
                       catch (Exception e) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " is not a valid URI string");
                       }
                       break;
      case INTEGER   : if (!node.isIntegralNumber()) {
                         collector.syntax.add(prefix + "Value for attribute " + path + " is not an integral number");
                       }
                       break;
      case COMPLEX   : verifyNode(prefix, path, attribute.attributes(), (ObjectNode)node, collector, current, partialReplace, partialAdd, false);
                       break;
      case STRING    : // verify for canonical values
                       if (attribute.canonical() != null) {
                         boolean found = false;
                         for (String canonicalValue : attribute.canonical()) {
                           if (attribute.caseExact() ? canonicalValue.equals(node.textValue()) : StringUtility.equalIgnoreCase(canonicalValue, node.textValue())) {
                             found = true;
                             break;
                           }
                         }
                         if (!found) {
                           collector.syntax.add(prefix + "Value " + node.textValue() + " is not valid for attribute " + path + " because it is not one of the canonical types: " + StringUtility.join(attribute.canonical(), ", "));
                         }
                       }
    }
    // special verifying of the schemas attribute to ensure that no undefined
    // schemas are listed
    if (attribute.equals(SchemaFactory.SCHEMAS) && path.size() == 1) {
      boolean found = false;
      for (SchemaResource extension : this.resource.extension().keySet()) {
        if (node.textValue().equals(extension.id())) {
          found = true;
          break;
        }
      }
      if (!found) {
        found = node.textValue().equals(this.resource.core().id());
      }
      if (!found && !this.enabled.contains(Option.RELAX)) {
        collector.syntax.add(prefix + "Schema URI " + node.textValue() + " is not a valid value for attribute " + path + " because it is undefined as a core or schema extension for this resource type");
      }
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyFilter
  /**
   ** Verify the provided filter against the schema.
   **
   ** @param  filter             the filter to verify.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyFilter(final Filter filter, final Result collector)
    throws ProcessingException {

    verifyFilter(null, filter, collector);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   verifyFilter
  /**
   ** Verify the provided value filter in a patch path against the schema.
   **
   ** @param  ppath              the attribute path associated with the value
   **                            filter.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  filter             the filter to verify.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  collector          the schema validation result collector.
   **                            <br>
   **                            Allowed object is {@link Result}.
   **
   ** @throws ProcessingException if an error occurs.
   */
  private void verifyFilter(final Path path, final Filter filter, final Result collector)
    throws ProcessingException {

    if (this.enabled.contains(Option.RELAX) && this.enabled.contains(Option.WEAK)) {
      // nothing to verify because all undefined attributes are allowed
      return;
    }

    final Visitor visitor = new Visitor(path, this.resource, this, collector);
    filter.accept(visitor, null);
  }
}