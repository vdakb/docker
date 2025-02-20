package oracle.iam.system.simulation.apigee.schema;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import oracle.iam.system.simulation.rest.schema.Resource;

////////////////////////////////////////////////////////////////////////////////
// class RoleResult
// ~~~~~ ~~~~~~~~~~
/**
 ** The REST entity of a Google API Gateway seach result for
 ** <code>Userrole</code>s.
 ** <br>
 ** Apigee provides a very limited API to search for <code>Userrole</code>s.
 ** There isn't anything that allows:
 ** <ol>
 **  <li>Apply paginiation on a result set.
 **  <li>Apply filter criteria for obtaining deltas only
 ** </ol>
 ** Furthermore in searching for <code>Userrole</code>s only the list of
 ** provisioned role names is returned and a follow up request is required to
 ** get the members for each <code>Userrole</code> REST resource.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RoleResult implements Resource<TenantResult> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @JsonProperty
  private List<String> list;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>RoleResult</code> REST Resource that allows
   ** use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public RoleResult() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Sets the user result set.
   **
   ** @param  value              the userrole result set.
   **                            <br>
   **                            Allowed object is {@link List} of
   **                            {@link String}.
   **
   ** @return                    the <code>RoleResult</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>RoleResult</code>.
   */
  public final RoleResult list(final List<String> value) {
    this.list = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   list
  /**
   ** Returns the user result set.
   ** <p>
   ** This accessor method returns a reference to the live list, not a snapshot.
   ** Therefore any modification you make to the returned list will be present
   ** inside the JSON object.
   **
   ** @return                    the user result set.
   **                            <br>
   **                            Possible object is {@link List} of
   **                            {@link String}.
   */
  public final List<String> list() {
    return this.list;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (overridden)
  /**
   ** Returns a hash code value for the object.
   ** <br>
   ** This method is supported for the benefit of hash tables such as those
   ** provided by {@link java.util.HashMap}.
   ** <p>
   ** The general contract of <code>hashCode</code> is:
   ** <ul>
   **   <li>Whenever it is invoked on the same object more than once during an
   **       execution of a Java application, the <code>hashCode</code> method
   **       must consistently return the same integer, provided no information
   **       used in <code>equals</code> comparisons on the object is modified.
   **       This integer need not remain consistent from one execution of an
   **       application to another execution of the same application.
   **   <li>If two objects are equal according to the
   **       <code>equals(Object)</code> method, then calling the
   **       <code>hashCode</code> method on each of the two objects must
   **       produce the same integer result.
   **   <li>It is <em>not</em> required that if two objects are unequal
   **       according to the {@link java.lang.Object#equals(java.lang.Object)}
   **       method, then calling the <code>hashCode</code> method on each of the
   **       two objects must produce distinct integer results. However, the
   **       programmer should be aware that producing distinct integer results
   **       for unequal objects may improve the performance of hash tables.
   ** </ul>
   **
   ** @return                    a hash code value for this object.
   **                            <br>
   **                            Possible object is <code>int</code>.
   */
  @Override
  public int hashCode() {
    return 31 * (this.list != null ? this.list.hashCode() : 0);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>RoleResult</code>s are considered equal if and only if they
   ** represent the same properties. As a consequence, two given
   ** <code>RoleResult</code>s may be different even though they contain the
   ** same set of names with the same values, but in a different
   ** order.
   **
   ** @param  other              the reference object with which to compare.
   **                            <br>
   **                            Allowed object is {@link Object}.
   **
   ** @return                    <code>true</code> if this object is the same as
   **                            the object argument; <code>false</code>
   **                            otherwise.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  @Override
  public boolean equals(final Object other) {
    if (this == other)
      return true;

    if (other == null || getClass() != other.getClass())
      return false;

    final RoleResult that = (RoleResult)other;
    return !(this.list != null ? !this.list.equals(that.list) : that.list != null);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   toString (overridden)
  /**
   ** Returns the string representation for this instance in its minimal form.
   **
   ** @return                    the string representation for this instance in
   **                            its minimal form.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  @Override
  public String toString() {
    int i = 0;
    final StringBuilder builder = new StringBuilder("[");
    for(String cursor : this.list) {
      if (i++ > 0)
        builder.append(",");
      builder.append("\"").append(cursor).append("\"");
    }
    builder.append("]");
    return builder.toString();
  }
}