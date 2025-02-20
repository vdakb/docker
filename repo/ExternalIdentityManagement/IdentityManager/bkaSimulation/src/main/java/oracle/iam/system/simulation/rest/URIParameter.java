package oracle.iam.system.simulation.rest;

import java.util.HashMap;
import java.util.Map;

/**
 ** The <code>URIParameter</code> is responsible for returning the parameters
 ** passed to it. Its usage is as follows:
 ** <pre>
 **   URLParameter parameter = parser.parse("/some/uri");
 **   Boolean hasStringName = parameter.parameterExists("stringParamName");
 **   String stringParameterName = params.getString("stringParamName");
 ** </pre>
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class URIParameter {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  private Map<String, Value> map;

  //////////////////////////////////////////////////////////////////////////////
  // Member classes
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // class Type
  // ~~~~~ ~~~~
  /**
   ** A certain value is on of this type.
   */
  public enum Type {
  	  String
    , Boolean
    , Integer
    ;
  }

  //////////////////////////////////////////////////////////////////////////////
  // class Value
  // ~~~~~ ~~~~~
  /**
   ** A certain value is on of this type.
   */
  public class Value {

    ////////////////////////////////////////////////////////////////////////////
    // instance attributes
    ////////////////////////////////////////////////////////////////////////////
		Type   type;
		Object value;

    ////////////////////////////////////////////////////////////////////////////
    // Constructors
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: Ctor
    /**
     ** Constructs a <code>Value</code> REST Resource with the specified
     ** <code>type</code> and <code>value</code>.
     **
     ** @param  type             the type can be any of the types enumerated in
     **                          {@link Type}, namely
     **                          <ul>
     **                            <li>String
     **                            <li>Boolean
     **                            <li>Integer
     **                          </ul>
     **                          <br>
     **                          Allowed object is {@link Type}.
     ** @param  value            the value of the <code>Value</code>.
     **                          <br>
     **                          Allowed object is {@link Object}.
     */
    public Value(final Type type, Object value) {
      // ensure inheritance
      super();

      // initialize instance attributes
			this.type  = type;
			this.value = value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Accessor and Mutator methods
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Sets the type of the <code>Value</code>.
     **
     ** @param  value            the type of the <code>Value</code>.
     **                          <br>
     **                          Allowed object is {@link Type}.
     **
     ** @return                  the <code>Value</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Value</code>.
     */
    public final Value type(final Type value) {
      this.type = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: type
    /**
     ** Returns the type of the <code>Value</code>.
     **
     ** @return                  the type of the <code>Value</code>.
     **                          <br>
     **                          Possible object is {@link Type}.
     */
    public final Type type() {
      return this.type;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Sets the value of the <code>Value</code>.
     **
     ** @param  value            the value of the <code>Value</code>.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  the <code>Value</code> to allow method
     **                          chaining.
     **                          <br>
     **                          Possible object is <code>Value</code>.
     */
    public final Value value(final Object value) {
      this.value = value;
      return this;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: value
    /**
     ** Returns the value of the <code>Value</code>.
     **
     ** @return                  the value of the <code>Value</code>.
     **                          <br>
     **                          Possible object is {@link Object}.
     */
    public final Object value() {
      return this.value;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Methods group by functionality
    ////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////
    // Method: hashCode (overridden)
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
     **       method, then calling the <code>hashCode</code> method on each of
     **       the two objects must produce distinct integer results. However,
     **       the programmer should be aware that producing distinct integer
     **       results for unequal objects may improve the performance of hash
     **       tables.
     ** </ul>
     **
     ** @return                  a hash code value for this object.
     **                          <br>
     **                          Possible object is <code>int</code>.
     */
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = prime * (this.type  != null ? this.type.hashCode() : 0);
      result = prime * result + (this.value != null ? this.value.hashCode() : 0);
      return result;
    }

    ////////////////////////////////////////////////////////////////////////////
    // Method: equals (overridden)
    /**
     ** Indicates whether some other object is "equal to" this one according to
     ** the contract specified in {@link Object#equals(Object)}.
     ** <p>
     ** Two <code>Value</code>s are considered equal if and only if they
     ** represent the same properties. As a consequence, two given
     ** <code>Value</code>s may be different even though they contain the same
     ** set of names with the same values, but in a different
     ** order.
     **
     ** @param  other            the reference object with which to compare.
     **                          <br>
     **                          Allowed object is {@link Object}.
     **
     ** @return                  <code>true</code> if this object is the same as
     **                          the object argument; <code>false</code>
     **                          otherwise.
     **                          <br>
     **                          Possible object is <code>boolean</code>.
     */
    @Override
    public boolean equals(final Object other) {
      if (this == other)
        return true;

      if (other == null || getClass() != other.getClass())
        return false;

      final Value that = (Value)other;
      if (this.type != null ? !this.type.equals(that.type) : that.type != null)
       return false;

      return !(this.value != null ? !this.value.equals(that.value) : that.value != null);
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>URIParameter</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private URIParameter() {
    // ensure inheritance
    this(new HashMap<>());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an empty <code>URIParameter</code> with the specified map as
   ** its backend.
   ** <br>
   ** Used only for dependency injection purposes.
   **
   ** @param  map                a map to use as the backend for the
   **                            URIParametersMap.
   **                            <br>
   **                            Allowed object is {@link Map} where each
   **                            element is of type {@link String} for the key
   **                            and {@link Value} for the value.
   */
  public URIParameter(final Map<String, Value> map) {
    this.map = map;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   exists
  /**
   ** Checks whether a parameter of the given name is present in the
   ** <code>URIParameter</code>.
   ** <br>
   ** This is <code>true</code> if the parameter was provided. It can be
   ** <code>false</code> in either of two ways:
   ** <ul>
   **   <li>The parameter of the given name was not provided in the template.
   **   <li>The parameter of the given name was not provided in the request URI.
   ** </ul>
   **
   ** @param  name               the name of the parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    <code>true</code> if parameter is present in
   **                            this <code>URIParameter</code>.
   **                            <br>
   **                            Possible object is <code>boolean</code>.
   */
  public boolean exists(final String name) {
    return map.containsKey(name);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   bool
  /**
   ** Returns the boolean value of the parameter of given name.
   **
   ** @param  name               the name of the parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the parameter.
   **                            <br>
   **                            Possible object is {@link Boolean}.
   */
  public Boolean bool(final String name) {
    if (!this.map.containsKey(name))
      return null;

    final Value value = this.map.get(name);
    return value.type() == Type.Boolean ? (Boolean)value.value() : Boolean.valueOf(string(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   integer
  /**
   ** Returns the integer value of the parameter of given name.
   **
   ** @param  name               the name of the parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the parameter.
   **                            <br>
   **                            Possible object is {@link Integer}.
   */
  public Integer integer(final String name) {
    if (!this.map.containsKey(name))
      return null;

    final Value value = this.map.get(name);
    return value.type() == Type.Integer ? (Integer)value.value() : Integer.valueOf(string(name));
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   string
  /**
   ** Returns the string value of the parameter of given name.
   **
   ** @param  name               the name of the parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the value of the parameter.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public String string(final String name) {
    if (!this.map.containsKey(name))
      return null;

    final Value value = this.map.get(name);
    return value.type() == Type.String ? (String)value.value() : value.value().toString();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an empty <code>URIParameter</code> thats backed
   ** by a {@link HashMap}.
   **
   ** @return                    the <code>URIParameter</code> with default
   **                            delimiter.
   **                            <br>
   **                            Possible object is <code>URIParameter</code>.
   */
  public static URIParameter build() {
    return new URIParameter();
  }


  //////////////////////////////////////////////////////////////////////////////
  // Method:   add
  /**
	 ** Adds a parameter to the <code>URIParameter</code>.
   ** This is a convenience method, and should not be used.
   **
   ** @param  name               the name of the parameter to be added.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  type               the type of the parameter to be added, must be
   **                            one of {@link Type}.
   **                            <br>
   **                            Allowed object is {@link Type}.
   ** @param  value              the value of the parameter to be added.
   **                            <br>
   **                            Allowed object is {@link Object}.
	 */
  public void add(final String name, final Type type, final Object value){
		map.put(name, new Value(type,value));
	}
}
