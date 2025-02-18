package oracle.iam.identity.icf.connector.keycloak.schema;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import oracle.iam.identity.icf.schema.Attribute;
import oracle.iam.identity.icf.schema.Schema;

import java.util.List;
import java.util.Objects;

////////////////////////////////////////////////////////////////////////////////
// final class Client
// ~~~~~ ~~~~~ ~~~~~~
/**
 ** The REST client role entity representation.
 ** <br>
 ** Client roles are basically a namespace dedicated to a client. Each client
 ** gets its own namespace.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
@Schema("__CLIENTROLE__")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Client extends Entity<Client> {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  /**
   ** The unqiue name of the resource defined by a Service Provider
   ** administrator.
   */
  @JsonProperty("clientId")
  @JsonAlias("client")
  @Attribute(required=true)
  private String     name;
  /**
   ** The collection of roles the client provides.
   */
  @JsonProperty("mappings")
  @Attribute(multiValueClass=Role.class)
  private List<Role> role;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>Total</code> REST representation that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Client() {
    super();
  }

  public String name() {
    return name;
  }

  public void name(String name) {
    this.name = name;
  }

  public List<Role> role() {
    return role;
  }

  public void role(List<Role> role) {
    this.role = role;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   hashCode (Resource)
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
    return Objects.hash(this.id, this.name, this.role);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   equals (overridden)
  /**
   ** Indicates whether some other object is "equal to" this one according to
   ** the contract specified in {@link Object#equals(Object)}.
   ** <p>
   ** Two <code>Client</code>s are considered equal if and only if they represent
   ** the same properties. As a consequence, two given <code>Client</code>s may
   ** be different even though they contain the same set of names with the same
   ** values, but in a different order.
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

    final Client that = (Client)other;
    return Objects.equals(this.id,    that.id)
        && Objects.equals(this.name,  that.name)
        && Objects.equals(this.role,  that.role)
    ;
  }
}