package oracle.hst.platform.rest.entity;

import java.util.Date;
import java.util.List;
import java.util.Collection;

import java.text.ParseException;

import com.fasterxml.jackson.databind.JsonNode;

import oracle.hst.platform.core.entity.Path;
import oracle.hst.platform.core.entity.Filter;

import oracle.hst.platform.core.utility.DateUtility;

import oracle.hst.platform.rest.BadRequestException;

import oracle.hst.platform.rest.schema.Support;

////////////////////////////////////////////////////////////////////////////////
// class FilterFactory
// ~~~~~ ~~~~~~~~~~~~~
/**
 ** Basic interface to match a {@link Path} with {@link JsonNode}s.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class FilterFactory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** the one and only instance of the <code>FilterFactory</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final FilterFactory instance   = new FilterFactory();

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>FilterFactory</code> handler that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private FilterFactory() {
    // ensure inhritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to <code>logically and</code>'s together the two specified
   ** instances of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** The resulting <i>conjunct</i> {@link Filter} is <code>true</code> if
   ** and only if at both of the specified filters are <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            {@link JsonNode}.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @return                    the result of <code>(lhs &amp;&amp; rhs)</code>
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */ 
  public static Filter<JsonNode> and(final Filter<JsonNode> lhs, final Filter<JsonNode> rhs) {
    return Filter.and(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code> {@link Filter} using
   ** the provided list of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** Creating a new <code>logically and</code> {@link Filter} with a
   ** <code>null</code> or empty list of {@link Filter}s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link Filter} for
   **                            type {@link JsonNode}.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  @SuppressWarnings("unchecked")
  public static Filter<JsonNode> and(final Filter<JsonNode>... filters) {
    return Filter.and(filters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   and
  /**
   ** Factory method to create <code>logically and</code> {@link Filter} using
   ** the provided list of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** Creating a new <code>logically and</code> {@link Filter} with a
   ** <code>null</code> or empty list of {@link Filter}s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link List} where each
   **                            element is of type {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @return                    the newly created "AND" filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> and(final List<Filter<JsonNode>> filters) {
    return Filter.and(filters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to <code>logically or</code>'s together the two specified
   ** instances of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** The resulting <i>disjunct</i> {@link Filter} is <code>true</code> if and
   ** only if at least one of the specified filters is <code>true</code>.
   **
   ** @param  lhs                the left-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            {@link JsonNode}.
   ** @param  rhs                the right-hand-side filter.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @return                    the result of <code>(lhs || rhs)</code>
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> or(final Filter<JsonNode> lhs, final Filter<JsonNode> rhs) {
    return Filter.or(lhs, rhs);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code> {@link Filter} using
   ** the provided array of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** Creating a new <code>logically or</code> {@link Filter} with a
   ** <code>null</code> or empty list of {@link Filter}s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is array of {@link Filter} for
   **                            type {@link JsonNode}.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  @SuppressWarnings("unchecked")
  public static Filter<JsonNode> or(final Filter<JsonNode>... filters) {
    return Filter.or(filters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   or
  /**
   ** Factory method to create <code>logically or</code> {@link Filter} using
   ** the provided list of {@link Filter}s of {@link JsonNode}.
   ** <p>
   ** Creating a new <code>logically or</code> {@link Filter} with a
   ** <code>null</code> or empty list of {@link Filter}s is equivalent to
   ** <em>alwaysTrue</em>.
   **
   ** @param  filters            the list of filters, may be empty or
   **                            <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link Collection} where each
   **                            element is of type {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @return                    the newly created "OR" filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> or(final List<Filter<JsonNode>> filters) {
    return Filter.or(filters);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Factory method to create <code>logically negate</code> {@link Filter}
   ** from the specified filter expression.
   ** <br>
   ** The resulting {@link Filter} is <code>true</code> if and only if the
   ** specified filter expression is <code>false</code>.
   **
   ** @param  filter             the filter expression to negate.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> not(final String filter)
    throws BadRequestException {

    return Filter.not(from(filter));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   not
  /**
   ** Factory method to create <code>logically negate</code> {@link Filter}.
   ** <br>
   ** The resulting {@link Filter} is <code>true</code> if and only if the
   ** specified filter is <code>false</code>.
   **
   ** @param  filter             the {@link Filter} to negate.
   **                            <br>
   **                            Allowed object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @return                    the result of <code>(!filter)</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> not(final Filter<JsonNode> filter) {
    return Filter.not(filter);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path {@link Filter}
   ** that select only a path if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> pr(final String path)
    throws BadRequestException {

    try {
      return pr(Path.from(path));
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   pr
  /**
   ** Factory method to create <code>presence</code> input path {@link Filter}
   ** that select only a {@link Path} if its exists.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   **
   ** @return                    the newly created <code>presence</code> filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  @SuppressWarnings("unchecked")
  public static Filter<JsonNode> pr(final Path path) {
    return Filter.<JsonNode>pr(path);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em> of
   ** the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Boolean} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Boolean}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Boolean value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that is equal to the specified
   ** {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Integer value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that is equal to of the specified
   ** {@link Long} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Long value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that is equal to the specified
   ** {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double containing }<em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Double value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that is equal to the specified
   ** {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Float value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that is equal to the specified
   ** {@link Date} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Date} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Date}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Date}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final Date value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().textNode(DateUtility.formatDate(value, DateUtility.RFC4517_ZULU)));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em> the
   ** specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final String value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em> the
   ** specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link byte[]}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final byte[] value)
    throws BadRequestException {

    return eq(path, Support.nodeFactory().binaryNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em> the
   ** specified <code>byte[] value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> eq(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return eq(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   eq
  /**
   ** Factory method to create <code>equal to</code> input path {@link Filter}
   ** that select only a path with a value that <em>lexically equal to</em> the
   ** specified {@link JsonNode}.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link byte[]}<em> containing exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    an instance of {@link Filter} whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link byte[]}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> eq(final Path path, final JsonNode value) {
    return Filter.eq(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> gt(final String path, final Boolean value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is greater
   ** than the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link Boolean}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> gt(final Path path, final Boolean value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().booleanNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> gt(final String path, final Integer value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Integer} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Integer}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Integer}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final Integer value) {
    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Long} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsedå.
   */
  public static Filter<JsonNode> gt(final String path, final Long value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Long} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Long} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Long}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Long}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final Long value) {
    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsedå.
   */
  public static Filter<JsonNode> gt(final String path, final Double value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Double} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Double} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Double}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Double}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final Double value) {
    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsedå.
   */
  public static Filter<JsonNode> gt(final String path, final Float value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link Float} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link Float} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link Float}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link Float}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final Float value) {
    return gt(path, Support.nodeFactory().numberNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsedå.
   */
  public static Filter<JsonNode> gt(final String path, final String value)
    throws BadRequestException {

    return gt(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than</em> the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link String} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified {@link String}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final String value) {
    return gt(path, Support.nodeFactory().textNode(value));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a {@link Path} with a value that is
   ** <em>lexically greater than</em> the specified {@link JsonNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link JsonNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code>  or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric.
   ** <br>
   ** When compared lexically, <code>"99"</code> is greater than
   ** <code>"123"</code>.
   ** <br>
   ** When compared arithmetically, <code>99</code> is less than
   ** <code>123</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
    **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> gt(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return gt(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   gt
  /**
   ** Factory method to create <code>greater than</code> input path
   ** <code>Filter</code> that select only a {@link Path} with a value that is
   ** <em>lexically greater than</em> the specified {@link JsonNode}.
   ** <p>
   ** <b>NOTE: Is comparison case-sensitive?</b>
   ** <p>
   ** For example, if the specified {@link JsonNode} were
   ** <code>{"hairColor": "brown"}</code>, this would match any {@link Path}
   ** with a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brownish-gray"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "black"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "blond"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code>  or that lacks the attribute
   ** <code>"hairColor"</code>.
   ** <p>
   ** <b>NOTE:</b> <i>Lexical</i> comparison of two string values compares the
   ** characters of each value, even if the string values could be interpreted
   ** as numeric.
   ** <br>
   ** When compared lexically, <code>"99"</code> is greater than
   ** <code>"123"</code>.
   ** <br>
   ** When compared arithmetically, <code>99</code> is less than
   ** <code>123</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> gt(final Path path, final JsonNode value) {
    return Filter.gt(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified {@link Boolean}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> ge(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return ge(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ge
  /**
   ** Factory method to create <code>greater than or equal to</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>greater than or equal to</em> the specified {@link Integer}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> ge(final Path path, final JsonNode value) {
    return Filter.ge(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than</em> the specified {@link Boolean} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> lt(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return lt(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   lt
  /**
   ** Factory method to create <code>less than</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than</em> the specified {@link Integer} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> lt(final Path path, final JsonNode value) {
    return Filter.lt(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified {@link Boolean}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> le(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return le(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   le
  /**
   ** Factory method to create <code>less or equal</code> input path
   ** <code>Filter</code> that select only a path with a value that is
   ** <em>less than or equal to</em> the specified {@link Integer}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is <code>T</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> le(final Path path, final JsonNode value) {
    return Filter.le(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that <em>initial
   ** substring</em> the specified {@link String} <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> sw(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return sw(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   sw
  /**
   ** Factory method to create <code>starts with</code> input path
   ** <code>Filter</code> that select only a path with a value that <em>initial
   ** substring</em> the specified {@link JsonNode}.
   ** <p>
   ** For example, if the specified {@link JsonNode} were
   ** <code>{"hairColor": "b"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "brown"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> sw(final Path path, final JsonNode value) {
    return Filter.sw(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as a final substring</em> the specified {@link String}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> ew(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return ew(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ew
  /**
   ** Factory method to create <code>ends with</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as a final substring</em> the specified {@link JsonNode}.
   ** <p>
   ** For example, if the specified {@link JsonNode} were
   ** <code>{"hairColor": "d"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blond"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only values
   ** such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "blonde"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp; <code>{"hairColor": "auburn"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code>
   ** <br>
   ** or that lacks the attribute <code>"hairColor"</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> ew(final Path path, final JsonNode value) {
    return Filter.ew(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as any substring</em> the specified {@link String}
   ** <code>value</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the object value representation to compare
   **                            <em>containing exactly one value</em> to test
   **                            against each value of a corresponding
   **                            <code>Entity</code> attribute.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> co(final String path, final JsonNode value)
    throws BadRequestException {

    try {
      return co(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   co
  /**
   ** Factory method to create <code>contains</code> input path
   ** <code>Filter</code> that select only a path with a value that
   ** <em>contains as any substring</em> the specified {@link JsonNode}.
   ** <p>
   ** For example, if the specified {@link JsonNode} were
   ** <code>{"hairColor": "a"}</code>, this would match any {@link Path} with
   ** a value such as
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "auburn"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "gray"}</code>
   ** <br>
   ** but would <em>not</em> match any {@link Path} that contains only
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "red"}</code> or
   ** <br>
   ** &nbsp;&nbsp;&nbsp;&nbsp;<code>{"hairColor": "grey"}</code>.
   ** <br>
   ** This also would <em>not</em> match any {@link Path} that contains only
   ** <code>{"hairColor": null}</code> or that lacks the attribute
   ** <code>"hairColor"</code>.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the {@link JsonNode} containing <em>exactly
   **                            one value</em> to test against each value of
   **                            the corresponding {@link Path} attribute.
   **                            <br>
   **                            Allowed object is {@link JsonNode}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>matches lexically</em> the value of the
   **                            specified <code>value</code>; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> co(final Path path, final JsonNode value) {
    return Filter.co(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** Factory method to create <code>complex</code> input path
   ** <code>Filter</code> with multi-valued attribute.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  expression         the filter expression to test against each
   **                            value of the corresponding path.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the path
   **                            <em>matches lexically</em> the value of the
   **                            specified expression; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is <code>Filter</code>.
   **
   ** @throws BadRequestException if the path or filter expression could not be
   **                             parsed.
   */
  public static Filter<JsonNode> complex(final String path, final JsonNode expression)
    throws BadRequestException {
    
    return null;
/*
    try {
      return complex(Path.from(path), expression);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
*/
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** a new complex multi-valued attribute value filter.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the <code>Filter</code> value to apply.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link JsonNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the path expression could not be parsed.
   */
  public static Filter<JsonNode> complex(final String path, final Filter<JsonNode> value)
    throws BadRequestException {

    try {
      return complex(Path.from(path), value);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidPath(e.getLocalizedMessage());
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   complex
  /**
   ** a new complex multi-valued attribute value filter.
   **
   ** @param  path               the path to the attribute to filter by.
   **                            <br>
   **                            Allowed object is {@link Path}.
   ** @param  value              the <code>Filter</code> value to apply.
   **                            <br>
   **                            Allowed object is <code>Filter</code>.
   **
   ** @return                    an instance of <code>Filter</code> whose
   **                            <code>accept()</code> method will return
   **                            <code>true</code> if at least one value of the
   **                            corresponding attribute of the {@link Path}
   **                            <em>contains anywhere within it</em> the value
   **                            of the specified {@link JsonNode}; otherwise
   **                            <code>false</code>.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   */
  public static Filter<JsonNode> complex(final Path path, final Filter<JsonNode> value) {
    return Filter.complex(path, value);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   from
  /**
   ** Parse a filter from its string representation.
   **
   ** @param  expression         the string representation of the filter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the parsed filter.
   **                            <br>
   **                            Possible object is {@link Filter} for type
   **                            {@link JsonNode}.
   **
   ** @throws BadRequestException if the filter expression could not be parsed.
   */
  public static Filter<JsonNode> from(final String expression)
    throws BadRequestException {

    try {
      return Filter.from(expression);
    }
    catch (ParseException e) {
      throw BadRequestException.invalidFilter(e.getLocalizedMessage());
    }
  }
}