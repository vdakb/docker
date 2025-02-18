package oracle.iam.platform.http;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;

////////////////////////////////////////////////////////////////////////////////
// class Authenticator
// ~~~~~ ~~~~~~~~~~~~~
public abstract class Authenticator implements Filter {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>Authenticator</code> that allows use as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Authenticator() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods of implemented interfaces
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   init (Filter)
  /**
   ** Called by the web container to indicate that it is being placed into
   ** service. The servlet container calls the init method exactly once after
   ** instantiating the filter. The init method must complete successfully
   ** before the filter is asked to do any filtering work.
   ** <p>
   ** The web container cannot place the filter into service if the init method
   ** either
   ** <ol>
   **   <li>throws a ServletException
   **   <li>Does not return within a time period defined by the web container.
   ** </ol>
   **
   ** @param  config             a {@link FilterConfig} object containing the
   **                            filter's configuration and initialization
   **                            parameters.
   **                            <br>
   **                            Allowed object is {@link FilterConfig}.
   **
   ** @throws ServletException  if an exception occurs that interferes with the
   **                           filter's normal operation.
   */
  @Override
  public final void init(final FilterConfig config)
    throws ServletException {

    // intentionally left blank
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   destroy (Filter)
  /**
   ** Called by the web container to indicate to a filter that it is being taken
   ** out of service. This method is only called once all threads within the
   ** filter's {@link #doFilter(ServletRequest, ServletResponse, FilterChain)}
   ** method have exited or after a timeout period has passed. After the web
   ** container calls this method, it will not call the
   ** {@link #doFilter(ServletRequest, ServletResponse, FilterChain)} method
   ** again on this instance of the filter.
   ** <p>
   ** This method gives the filter an opportunity to clean up any resources that
   ** are being held (for example, memory, file handles, threads) and make sure
   ** that any persistent state is synchronized with the filter's current state
   ** in memory.
   */
  @Override
  public final void destroy() {
    // intentionally left blank
  }
}