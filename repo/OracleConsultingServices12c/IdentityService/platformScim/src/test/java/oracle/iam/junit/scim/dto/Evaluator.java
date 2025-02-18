package oracle.iam.junit.scim.dto;

import oracle.iam.platform.scim.ProcessingException;

import oracle.iam.platform.scim.entity.Or;
import oracle.iam.platform.scim.entity.And;
import oracle.iam.platform.scim.entity.Not;
import oracle.iam.platform.scim.entity.Path;
import oracle.iam.platform.scim.entity.Filter;
import oracle.iam.platform.scim.entity.Equals;
import oracle.iam.platform.scim.entity.Presence;
import oracle.iam.platform.scim.entity.Contains;
import oracle.iam.platform.scim.entity.EndsWith;
import oracle.iam.platform.scim.entity.LessThan;
import oracle.iam.platform.scim.entity.StartsWith;
import oracle.iam.platform.scim.entity.GreaterThan;
import oracle.iam.platform.scim.entity.ComplexFilter;
import oracle.iam.platform.scim.entity.LessThanOrEqual;
import oracle.iam.platform.scim.entity.GreaterThanOrEqual;

public class Evaluator implements Filter.Visitor<Boolean, Entity> {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The one and only instance of the <code>Evaluator</code>
   ** <p>
   ** Singleton Pattern
   */
  private static final Evaluator instance   = new Evaluator();

  private static final Path      VALUE_PATH = Path.build().attribute("value");

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Evaluator</code> handler that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  protected Evaluator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>and</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>true</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link And}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final And filter, final Entity node)
    throws ProcessingException {

    for (Filter cursor : filter.filter()) {
      if (!cursor.accept(this, node)) {
        return false;
      }
    }
    return true;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>or</code> filter.
   ** <p>
   ** <b>Note</b>:
   ** <br>
   ** For the purposes of matching, an empty sub-filters should always
   ** evaluate to <code>false</code>.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Or}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Or filter, final Entity node)
    throws ProcessingException {

    for (Filter cursor : filter.filter()) {
      if (cursor.accept(this, node)) {
        return true;
      }
    }
    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>not</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Not}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Not filter, final Entity node)
    throws ProcessingException {

    return !filter.filter().accept(this, node);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>present</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Presence}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Presence filter, final Entity node)
    throws ProcessingException {

    return node.id != null;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits an <code>equality</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Equals}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Equals filter, final Entity node)
    throws ProcessingException {

    return node.id.longValue() == filter.value().asLong();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThan}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThan filter, final Entity node)
    throws ProcessingException {

    return node.id.longValue() > filter.value().asLong();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>greater than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link GreaterThanOrEqual}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final GreaterThanOrEqual filter, final Entity node)
    throws ProcessingException {

    return node.id.longValue() >= filter.value().asLong();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThan}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThan filter, final Entity node)
    throws ProcessingException {

    return node.id.longValue() < filter.value().asLong();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>less than or equal to</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link LessThanOrEqual}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final LessThanOrEqual filter, final Entity node)
    throws ProcessingException {

    return node.id.longValue() <= filter.value().asLong();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>starts with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link StartsWith}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final StartsWith filter, final Entity node)
    throws ProcessingException {

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>ends with</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link EndsWith}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final EndsWith filter, final Entity node)
    throws ProcessingException {

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>contains</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link Contains}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final Contains filter, final Entity node)
    throws ProcessingException {

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   apply (Filter.Visitor)
  /**
   ** Visits a <code>complex</code> filter.
   **
   ** @param  filter             the visited filter.
   **                            <br>
   **                            Allowed object is {@link ComplexFilter}.
   ** @param  node               a visitor specified parameter.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    a visitor specified result.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   **
   ** @throws ProcessingException if an exception occurs during the operation.
   */
  @Override
  public final Boolean apply(final ComplexFilter filter, final Entity node)
    throws ProcessingException {

    return false;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method group by functionallity
  //////////////////////////////////////////////////////////////////////////////

  ////////////////////////////////////////////////////////////////////////////
  // Method:   evaluate
  /**
   ** Evaluate the provided filter against the provided {@link Entity}.
   **
   ** @param  filter             the filter to evaluate.
   **                            <br>
   **                            Allowed object is {@link Filter}.
   ** @param  node               the {@link Entity} to evaluate the filter
   **                            against.
   **                            <br>
   **                            Allowed object is {@link Entity}.
   **
   ** @return                    <code>true</code> if the {@link Entity}
   **                            matches the filter or <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   **
   ** @throws ProcessingException if the filter is not valid for matching.
   */
  public static boolean evaluate(final Filter filter, final Entity node)
    throws ProcessingException {

    return filter.accept(instance, node);
  }
}