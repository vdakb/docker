package bka.iam.identity.scim.extension.rest;

import javax.ws.rs.core.UriInfo;
////////////////////////////////////////////////////////////////////////////////
// class RequestContext
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** Provides a thread-local context for managing <code>UriInfo</code> in SCIM 
 ** requests.
 ** <p>
 ** This class ensures that each request has its own isolated instance of
 ** <code>UriInfo</code>, which allows safe retrieval and usage of URI-related
 ** information.
 ** </p>
 ** 
 ** @author  sylvert.bernet@silverid.fr
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class RequestContext {

  //////////////////////////////////////////////////////////////////////////////
  // Static Attributes
  //////////////////////////////////////////////////////////////////////////////

  /** Thread-local storage for managing UriInfo per request */
  private static final ThreadLocal<UriInfo> uriInfoThreadLocal = new ThreadLocal<>();

  //////////////////////////////////////////////////////////////////////////////
  // Static Methods
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   setUriInfo
  /**
   ** Stores the <code>UriInfo</code> instance in the thread-local storage.
   ** <p>
   ** This method ensures that the request's URI-related information is 
   ** available within the current thread for processing.
   ** </p>
   ** 
   ** @param uriInfo     The <code>UriInfo</code> instance to store.
   **                    Allowed object is {@link UriInfo}.
   */
  public static void setUriInfo(UriInfo uriInfo) {
    uriInfoThreadLocal.set(uriInfo);
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   getUriInfo
  /**
   ** Retrieves the stored <code>UriInfo</code> instance for the current thread.
   ** <p>
   ** This method provides access to the URI-related information for the
   ** current request.
   ** </p>
   ** 
   ** @return            The stored <code>UriInfo</code> instance, or 
   **                    <code>null</code> if not set.
   */
  public static UriInfo getUriInfo() {
    return uriInfoThreadLocal.get();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   clear
  /**
   ** Clears the stored <code>UriInfo</code> instance for the current thread.
   ** <p>
   ** This method ensures that after request processing, the thread-local 
   ** storage is cleaned up to prevent memory leaks.
   ** </p>
   */
  public static void clear() {
    uriInfoThreadLocal.remove();
  }
}
