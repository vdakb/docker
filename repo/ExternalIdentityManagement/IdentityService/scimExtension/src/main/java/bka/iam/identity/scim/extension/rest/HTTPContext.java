package bka.iam.identity.scim.extension.rest;

public class HTTPContext {
  
  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  /** 
   ** Header name for specifying the type of the request content. .
   */
  public static final String CONTENT_TYPE_HEADER              = "Content-Type";
  /**
   ** Custom header used to identify the initiator of the request. 
   */
  public static final String X_REQUESTED_BY_HEADER            = "X-Requested-By";
  /** The SCIM media type string */
  public static final String MEDIA_TYPE                       = "application/scim+json";
  /**
   ** An HTTP GET to this endpoint will return a JSON structure that describes
   ** the SCIM specification features available on a
   ** <code>Service Provider</code>.
   */
  public static final String ENDPOINT_SERVICE_PROVIDER_CONFIG = "ServiceProviderConfig";
  /**
   ** An HTTP GET to this endpoint is used to discover the types of resources
   ** available on a SCIM <code>Service Provider</code> (for example, Users and
   ** Groups).
   */
  public static final String ENDPOINT_RESOURCE_TYPES          = "ResourceTypes";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** resource schemas supported by a SCIM <code>Service Provider</code>.
   */
  public static final String ENDPOINT_SCHEMAS                 = "Schemas";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>User</code>.
   ** <p>
   ** SCIM provides a resource type for <code>User</code>s resources. The core
   ** schema for <code>User</code> is identified using the following schema URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:User</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:User.
   ** </ul>
   */
  public static final String ENDPOINT_USERS                   = "Users";
  /**
   ** An HTTP GET to this endpoint is used to retrieve information about
   ** of the resource type <code>Group</code> provided by a SCIM
   ** <code>Service Provider</code>.
   ** <p>
   ** SCIM provides a schema for representing groups. The core schema for
   ** resource type <code>Group</code> is identified using the following schema
   ** URI:
   ** <ul>
   **   <li>Version 1: <code>urn:scim:schemas:core:1.0</code>.
   **   <li>Version 2: <code>urn:ietf:params:scim:schemas:core:2.0:Group</code>,
   **                  urn:ietf:params:scim:schemas:extension:enterprise:2.0:Group.
   ** </ul>
   */
  public static final String ENDPOINT_GROUPS                   = "Groups";
  /**
   ** The "<code>/Me</code>" authenticated subject URI alias for the User or
   ** other resource associated with the currently authenticated subject for any
   ** SCIM operation.
   */
  public static final String ENDPOINT_ME                      = "Me";
  /**
   ** The HTTP query parameter used in a URI to include specific SCIM
   ** attributes.
   */
  public static final String QUERY_PARAMETER_ATTRIBUTES       = "attributes";
  /**
   ** The HTTP query parameter used in a URI to exclude specific SCIM
   ** attributes.
   */
  public static final String QUERY_PARAMETER_EXCLUDED         = "excludedAttributes";
  /**
   ** The HTTP query parameter used in a URI to provide a filter expression.
   */
  public static final String QUERY_PARAMETER_FILTER           = "filter";
  /**
   ** The HTTP query parameter used in a URI to sort by a SCIM attribute.
   */
  public static final String QUERY_PARAMETER_SORT_BY          = "sortBy";
  /**
   ** The HTTP query parameter used in a URI to specify the sort order.
   */
  public static final String QUERY_PARAMETER_SORT_ORDER       = "sortOrder";
  /**
   ** The HTTP query parameter used in a URI to specify the starting index for
   ** page results.
   */
  public static final String QUERY_PARAMETER_PAGE_START_INDEX = "startIndex";
  /**
   ** The HTTP query parameter used in a URI to specify the maximum size of a
   ** page of results.
   */
  public static final String QUERY_PARAMETER_PAGE_SIZE        = "count";


  public enum RequestType {
    GET("GET"),
    PUT("PUT"),
    POST("POST"),
    PATCH("PATCH"),
    DELETE("DELETE");
    
    private final String display;
    
    RequestType(String display) {
      this.display = display;
    }
    
    public String toString() {
      return this.display;
    }
  }
  
  public enum StatusCode {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    RESET_CONTENT(205, "Reset Content"),
    PARTIAL_CONTENT(206, "Partial Content"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),
    NOT_MODIFIED(304, "Not Modified"),
    USE_PROXY(305, "Use Proxy"),
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    PAYMENT_REQUIRED(402, "Payment Required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    CONFLICT(409, "Conflict"),
    GONE(410, "Gone"),
    LENGTH_REQUIRED(411, "Length Required"),
    PRECONDITION_FAILED(412, "Precondition Failed"),
    REQUEST_ENTITY_TOO_LARGE(413, "Request Entity Too Large"),
    REQUEST_URI_TOO_LONG(414, "Request-URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested Range Not Satisfiable"),
    EXPECTATION_FAILED(417, "Expectation Failed"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version Not Supported");
    
    private final int code;
    
    private final String message;
    
    StatusCode(int statusCode, String message) {
      this.code = statusCode;
      this.message = message;
    }
    
    public int getStatusCode() {
      return this.code;
    }
    
    public String getMessage() {
      return toString();
    }
    
    public String toString() {
      return this.message;
    }
    
    public static StatusCode getStatus(int code) {
      for (StatusCode statusCode : values()) {
        if (statusCode.code == code)
          return statusCode; 
      } 
      return null;
    }
  }
  
}
