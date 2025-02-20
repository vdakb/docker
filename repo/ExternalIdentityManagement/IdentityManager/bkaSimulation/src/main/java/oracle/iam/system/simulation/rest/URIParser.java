package oracle.iam.system.simulation.rest;

import java.util.List;
import java.util.Arrays;

import oracle.hst.foundation.utility.CollectionUtility;

/**
 ** The <code>URIParser</code> is responsible for parsing URI's, and returning
 ** their parameters.
 ** <br>
 ** It aims to abstract the parsing of parameters in a RESTful API and instead
 ** allow the developer to focus on the back-end of the RESTful API. Its usage
 ** is as follows:
 ** <br>
 ** Setup:
 ** <pre>
 **   URIParser parser = URIParser.build();
 **   // or any other delimiter
 **   parser.delimiter("[/\\\\]");
 **   // path to resource
 **   parser.template("/path/to/resource/{STRING:StringParam}");
 ** </pre>
 ** Parsing:
 ** <pre>
 **   // or request path
 **   URIParameter parameter = parser.parse(request.getURI());
 **
 **   Boolean hasStringParam = parameter.exists("StringParam");
 **   final String string    = parameter.string("StringParam");
 ** </pre>
 ** See the respective methods for more info on formatting codes.
 **
 ** @author  dieter.steding@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class URIParser {

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

	private String       delimiter;
	private String       template;
	private List<String> path;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>URIParser</code> with default delimiter of
   ** <code>[/\\\\]</code> and no default template.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  private URIParser() {
    // ensure inheritance
    super();
    
    // initialize instance attributes
    delimiter("[/\\\\]");
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>URIParser</code> with the given template.
   ** <br>
   ** Identical to:
   ** <pre>
   **   URIParser parser = URIParser.build();
   **   // or any other delimiter
   **   parser.delimiter("[/\\\\]");
   **   // path to resource
   **   parser.template("/path/to/resource/{string:stringParam}");
   ** </pre>
   **
   ** @param  template           the template passed to
   **                            {@link #delimiter(String)}.
   */
  public URIParser(final String template) {
    // ensure inheritance
    this();
    
    // initialize instance attributes
    template(template);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Accessor and Mutator methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   path
  /**
   ** Returns the path form of the template used.
   **
   ** @return                    the path form of the template used.
   **                            <br>
   **                            Possible object is {@link List} where each
   **                            element is of type {@link String}.
	 */
	public List<String> path() {
		return this.path;
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delimiter
  /**
   ** Sets the delimiter used.
   ** <br>
   ** <b>Note</b>:
   ** Must be in standard regex form (e.g. <code>"[/\\\\]"</code>).
   **
   ** @param  value              the delimiter used.
   **                            <br>
   **                            Allowed object is  {@link String}.
   **
   ** @return                    the <code>URIParser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>URIParser</code>.
   */
  public final URIParser delimiter(final String value) {
    this.delimiter = value;
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delimiter
  /**
   ** Returns the delimiter used.
   **
   ** @return                    the delimiter used.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String delimiter() {
    return this.delimiter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   delimiter
  /**
   ** Sets the template used.
   ** <br>
	 ** The template is the base URI with placeholders in the parameters.
   ** <br>
   ** For example:
   ** <pre>
   **   /path/to/resource/{String:StringParam}/{Integer:IntParam}/more/paths/{Boolean:BooleanParam}
   ** </pre>
   ** <p>
   ** Please do not close the path with a / (or other delimiter). The parser
   ** strips out these characters during parsing.
   ** <bR>
   ** Placeholder values are as follows: <code>{Type:name}</code>.
   ** <br>
   ** The type can be any of the types listed in {@link URIParameter.Type},
   ** namely
   ** <ul>
   **   <li>String
   **   <li>Boolean
   **   <li>Integer
   ** </ul>
   ** The name can be any alphanumeric value.
   **
   ** @param  value              the template used.
   **                            <br>
   **                            Allowed object is  {@link String}.
   **
   ** @return                    the <code>URIParser</code> to allow method
   **                            chaining.
   **                            <br>
   **                            Possible object is <code>URIParser</code>.
   */
  public final URIParser template(final String value) {
    this.template = value;
		this.path     = CollectionUtility.list(this.template.split(this.delimiter));
    return this;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   template
  /**
   ** Returns the template used.
   **
   ** @return                    the template used.
   **                            <br>
   **                            Possible object is {@link String}.
   */
  public final String template() {
    return this.template;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   build
  /**
   ** Factory method to create an <code>URIParser</code> with default delimiter
   ** of <code>[/\\\\]</code> and no default template.
   **
   ** @return                    the <code>URIParser</code> with default
   **                            delimiter.
   **                            <br>
   **                            Possible object is <code>URIParser</code>.
   */
  public static URIParser build() {
    return new URIParser();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses the provided URI and returns a {@link URIParameter} mapping.
   ** <br>
   ** This mapping contains all the provided parameters that are found in the
   ** URI.
   ** <br>
   ** If the template is longer than the URI, the parameters are marked as not
   ** being provided (excluded from the {@link URIParameter})
   ** <br>
   ** If the URI is longer than the template, any extra values are ignored.
   **
   ** @param  uri                the uri to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the {@link URIParameter} that contains the
   **                            provided parameters.
   **                            <br>
   **                            Possible object is {@link URIParameter}.
   */
  public URIParameter parse(final String uri) {
    return parse(uri, URIParameter.build());
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Allows you to add parameters to a custom {@link URIParameter} mapping or a
   ** subclass.
   **
   ** @see #parse(String)
   **
   ** @param  uri                the uri to parse.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the {@link URIParameter} to add parameters to.
   **                            <br>
   **                            Allowed object is {@link URIParameter}.
   **
   ** @return                    the {@link URIParameter} that contains the
   **                            provided parameters.
   **                            <br>
   **                            Possible object is {@link URIParameter}.
   */
  public URIParameter parse(final String uri, URIParameter parameter) {
    final String[] path = trim(uri).split(this.delimiter);
    for (int i = 0; i < this.path.size() && i < path.length; i++) {
      if (this.path.get(i).matches("\\{.*\\}")) {
        parse(this.path.get(i), path[i], parameter);
      }
    }
    return parameter;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parse
  /**
   ** Parses individual parameters, sorting them according to their type
   **
   ** @param  template           the template to be used for parsing.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the parameter.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  parameter          the {@link URIParameter} to add the parameters
   **                            <br>
   **                            Allowed object is {@link URIParameter}.
   **                            to.
   */
  void parse(final String template, final String value, final URIParameter parameter) {
    final String[] decl = template.split(":");
    // crop out beginning "{" from "{type:name}"
    final String   type = decl[0].substring(1); 
    // crop out end "}" from "{type:name}"
    final String   name = decl[1].substring(0, decl[1].length() - 1);
    switch (type) {
      case "string"  : addString(name, value, parameter);
                       break;
      case "integer" : addInteger(name, value, parameter);
                       break;
      case "boolean" : addBoolean(name, value, parameter);
                       break;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addString
  /**
   ** Parses a string, adding it to the provided {@link URIParameter}.
   **
	 ** @param  name               the name of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  value              the value of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  collector          the {@link URIParameter} to add the parameter
   **                            to.
   **                            <br>
   **                            Allowed object is {@link URIParameter}.
	 */
	void addString(final String name, final String value, final URIParameter collector) {
		collector.add(name, URIParameter.Type.String, value);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addBoolean
  /**
   ** Parses a boolean, adding it to the provided {@link URIParameter}.
   **
   ** @param  name               the name of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  value              the value of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  collector          the {@link URIParameter} to add the parameter
   **                            to.
   **                            <br>
   **                            Allowed object is {@link URIParameter}.
   */
  void addBoolean(final String name, final String value, final URIParameter parameter) {
    try {
      boolean intValue = parseBoolean(value);
      parameter.add(name, URIParameter.Type.Boolean, intValue);
    }
    catch (NumberFormatException e) {
      // intentionally left blank
      ;
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   addInteger
  /**
   ** Parses an integer, adding it to the provided {@link URIParameter}.
   **
	 ** @param  name               the name of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  value              the value of the parameter
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  collector          the {@link URIParameter} to add the parameter
   **                            to.
   **                            <br>
   **                            Allowed object is {@link URIParameter}.
	 */
	void addInteger(final String name, final String value, final URIParameter collector) {
    try {
      int intValue = Integer.parseInt(value);
      collector.add(name, URIParameter.Type.Integer, intValue);
    }
    catch (NumberFormatException e) {
      // intentionally left blank
      ;
    }
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Converts multiple formats of booleans from their string representation to
   ** a boolean format
   ** <table summary="">
   ** <tr>
   **   <th>String</th>
   **   <th>Boolean</th>
   ** </tr>
   ** <tr>
   **   <td>true</td>
   **   <td><code>true</code></td>
   ** </tr>
   ** <tr>
   **   <td>false</td>
   **   <td><code>false</code></td>
   ** </tr>
   ** <tr>
   **   <td>1</td>
   **   <td><code>true</code></td>
   ** </tr>
   ** <tr>
   **   <td>0</td>
   **   <td><code>false</code></td>
   ** </tr>
   ** <tr>
   **   <td>yes</td>
   **   <td>{<code>true</code></td>
   ** </tr>
   ** <tr>
   **   <td>no</td>
   **   <td><code>false</code></td>
   ** </tr>
   ** </table>
   * @author Arjun Vikram
   * @since 1.0.0
   * @param value the value of the parameter
   * @return the boolean value of the passed value
   * @throws NumberFormatException if value is not one of the possible values listed above
   */
  static boolean parseBoolean(final String value)
    throws NumberFormatException {

    switch (value.toLowerCase()) {
      case "true"  : return true;
      case "false" : return false;
      case "1"     : return true;
      case "0"     : return false;
      case "yes"   : return true;
      case "no"    : return false;
      default      : throw new NumberFormatException("Error converting \"" + value + "\" to boolean");
    }
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Removes the last delimiter (slash by default) in the URI passed to it.
   ** <br>
   ** If the passed string ends with the delimiter, it is removed. Otherwise, it
   ** is returned as is.
   **
   ** @param  s                  the URI to trim.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the URI without its final delimiter.
   **                            <br>
   **                            Possible object is {@link String}.
	 */
	String trim(final String s){
		return trim(s, this.delimiter);
	}

  //////////////////////////////////////////////////////////////////////////////
  // Method:   trim
  /**
   ** Removes the last delimiter (slash by default) in the URI passed to it.
   ** <br>
   ** If the passed string ends with the delimiter, it is removed. Otherwise, it
   ** is returned as is.
   **
   ** @param  s                  the URI to trim.
   **                            <br>
   **                            Allowed object is {@link String}.
	 ** @param  delimiter          the delimiter to remove from the end of the
   **                            string.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the URI without its final delimiter.
   **                            <br>
   **                            Possible object is {@link String}.
	 */
	static String trim(final String s, final String delimiter){
		return Character.valueOf(s.charAt(s.length() - 1)).toString().matches(delimiter) ? s.substring(0, s.length() - 1) : s;
	}

  public static void main(String[] args) {
		final String    template = "/v1/organizations/{string:org_name}/userroles/{string:role_name}/users";
		final URIParser parser   = URIParser.build().template(template);
    // normally its the request URI we have to pass
    final URIParameter parameter = parser.parse(template);
 }
}
