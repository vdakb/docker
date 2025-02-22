package bka.iam.identity.scim.extension.oim;

import java.util.HashSet;
import java.util.Set;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;
////////////////////////////////////////////////////////////////////////////////
// class Option
// ~~~~~ ~~~~~~
/**
 ** Represents the options for SCIM query parameters.
 ** <p>
 ** This class encapsulates search options such as pagination, sorting,
 ** and attribute selection (included or excluded attributes).
 ** </p>
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class Option {
  
  //////////////////////////////////////////////////////////////////////////////
  // Instance Attributes
  //////////////////////////////////////////////////////////////////////////////

  /** The maximum number of results to return */
  private final Integer     count;
  
  /** The starting index for pagination */
  private final Integer     startIndex;
  
  /** The set of attributes to be included in the response */
  private final Set<String> requiredAttributeName;
  
  /** The set of attributes to be excluded from the response */
  private final Set<String> excludedAttributeName;
  
  /** The sort by for the query results */
  private final String     sortBy;
  
  /** The sort order for the query results */
  private final SortOrder   sortOrder;
  
  /** The searchCriteria for the specific reseac */
  private SearchCriteria   searchCriteria;
  
  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs an <code>Option</code> object with the specified parameters.
   ** 
   ** @param count                  The maximum number of results to return.
   **                               Allowed object is {@link Integer}.
   ** @param startIndex             The starting index for pagination.
   **                               Allowed object is {@link Integer}.
   ** @param sortBy                 The sorting attribute.
   **                               Allowed object is {@link String}.
   ** @param sortOrder              The sorting order (ascending or descending).
   **                               Allowed object is {@link SortOrder}.
   ** @param requiredAttributeName  The set of attributes to be included.
   **                               Allowed object is {@link Set}.
   ** @param excludedAttributeName  The set of attributes to be excluded.
   **                               Allowed object is {@link Set}.
   */
  public Option(final Integer count, final Integer startIndex, final String sortBy, final SortOrder sortOrder, final Set<String> requiredAttributeName, final Set<String> excludedAttributeName, final SearchCriteria searchCriteria) {
    super();
    
    this.count      = count;
    this.sortOrder  = sortOrder != null ? sortOrder : sortOrder.ASCENDING;
    this.sortBy     = sortBy    != null ? sortBy : "";
    this.startIndex = startIndex;
    this.requiredAttributeName = requiredAttributeName != null ? requiredAttributeName : new HashSet<>();
    this.excludedAttributeName = excludedAttributeName != null ? excludedAttributeName : new HashSet<>();
    this.searchCriteria        = searchCriteria != null ? searchCriteria : new SearchCriteria("1", "1", SearchCriteria.Operator.EQUAL);
  }
  
  public Option(final Option option) {
    super();
    
    this.count      = option.count;
    this.sortBy     = option.sortBy;
    this.sortOrder  = option.sortOrder;
    this.startIndex = option.startIndex;
    this.requiredAttributeName = new HashSet<>(option.requiredAttributeName);
    this.excludedAttributeName = new HashSet<>(option.excludedAttributeName);
    this.searchCriteria        = new SearchCriteria(searchCriteria.getFirstArgument(), searchCriteria.getSecondArgument(), searchCriteria.getOperator());
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Accessor Methods
  //////////////////////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getRequiredAttributeNames
  /**
   ** Retrieves the set of required attribute names to be included in the response.
   ** 
   ** @return                      The set of required attribute names.
   **                              Allowed object is {@link Set}.
   */
  public Set<String> getRequiredAttributeNames() {
    return this.requiredAttributeName;
  }
  
  //////////////////////////////////////////////////////////////////////////////
  // Method:   getExcludedAttributeNames
  /**
   ** Retrieves the set of excluded attribute names to be omitted from the response.
   ** 
   ** @return                       The set of excluded attribute names.
   **                               Allowed object is {@link Set}.
   */
  public Set<String> getExcludedAttributeNames() {
    return this.excludedAttributeName;
  }
  
  public SearchCriteria getSearchCriteria() {
    return this.searchCriteria;
  }
  
  public void setSearchCriteria(final SearchCriteria searchCriteria) {
    this.searchCriteria = searchCriteria;
  }
  
  public String getSortBy() {
    return this.sortBy;
  }
  
  public SortOrder getSortOrder() {
    return this.sortOrder;
  }
  
  public Integer getStarIndex() {
    return this.startIndex;
  }
  
  public Integer getCount() {
    return this.count;
  }
  
  
  
  
  //////////////////////////////////////////////////////////////////////////////
  // Member enum
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Enum:   SortOrder
  /**
   ** Defines the sorting order for query results.
   ** <p>
   ** This enum provides sorting options such as ascending and descending.
   ** </p>
   */
  public enum SortOrder {
    /** Ascending order */
    ASCENDING("ascending"),

    /** Descending order */
    DESCENDING("descending");
    
    /** The string value of the sorting order */
    private final String value;
    
    //////////////////////////////////////////////////////////////////////////////
    // Constructors
    //////////////////////////////////////////////////////////////////////////////
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   Ctor
    /**
     ** Constructs a <code>SortOrder</code> with the specified value.
     ** 
     ** @param value The string representation of the sorting order.
     **              Allowed object is {@link String}.
     */
    SortOrder(String value) {
      this.value = value;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   getValue
    /**
     ** Retrieves the string representation of the sorting order.
     ** 
     ** @return The string value of the sorting order.
     */
    public String getValue() {
      return this.value;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   toString
    /**
     ** Returns the string representation of the sorting order.
     ** 
     ** @return The sorting order as a string.
     */
    public String toString() {
      return this.value;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   fromString
    /**
     ** Converts a string to the corresponding <code>SortOrder</code> enum value.
     ** 
     ** @param text The string value to convert.
     **             Allowed object is {@link String}.
     ** 
     ** @return The corresponding <code>SortOrder</code> value, or null if not found.
     */
    public static SortOrder fromString(String text) {
      if (text != null)
        for (SortOrder so : values()) {
          if (text.equalsIgnoreCase(so.value))
            return so; 
        }  
      return null;
    }
    
    //////////////////////////////////////////////////////////////////////////////
    // Method:   getDefault
    /**
     ** Retrieves the default sorting order.
     ** 
     ** @return The default sorting order, which is <code>ASCENDING</code>.
     */
    public static SortOrder getDefault() {
      return ASCENDING;
    }
  }
  
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    builder.append(String.format("count: %d | startIndex: %d | sortBy: %s | sortOrder: %s | requiredAttributeName: %s | excludedAttributeName: %s | searchCriteria: %s | ", count, startIndex, sortBy,sortOrder.getValue(), requiredAttributeName.toString(), excludedAttributeName.toString(), searchCriteria.toString()));
    return builder.toString();
  }
}
