package oracle.iam.access.http;

import java.io.IOException;

import javax.security.auth.message.AuthStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.soteria.servlet.RequestData;
import org.glassfish.soteria.servlet.HttpServletRequestDelegator;

import org.glassfish.soteria.mechanisms.jaspic.Jaspic;

import oracle.iam.platform.http.AbstractFilter;

public class ServerAuthenticationFilter extends AbstractFilter {
  private static final String ORIGIN = "org.glassfish.soteria.security.jaspic.request.origin";

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a <code>ServerAuthenticationFilter</code> that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public ServerAuthenticationFilter() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   chain (overridden)
  /**
   ** Actually executes the specified filter chain by calling
   ** <code>chain.doFilter(request,response);</code>.
   ** <p>
   ** Can be overridden by subclasses for custom logic.
   **
   ** @param  request            the incoming {@link HttpServletRequest} object
   **                            contains the client's request.
   **                            <br>
   **                            Allowed object is {@link HttpServletRequest}.
   ** @param  response           the outgoing {@link HttpServletResponse} object
   **                            contains the filter's response.
   **                            <br>
   **                            Allowed object is {@link HttpServletResponse}.
   ** @param  chain              the {@link FilterChain} for invoking the next
   **                            filter or the resource.
   **                            <br>
   **                            Allowed object is {@link FilterChain}.
   **
   ** @throws Exception          if an exception occurs that interferes with the
   **                            filter's normal operation.
   */
  @Override
  protected void chain(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
    throws Exception {

    final HttpSession session = request.getSession(false);
    // trigger an explicit call to the Security Application Modules, so they
    // will execute within a context where CDI, EJB etc is available.
		authenticateFromFilter(request, response);
    if (oneOf(lastStatus(request), AuthStatus.SUCCESS, null)) {
      HttpServletRequest newRequest = request;
      RequestData requestData = (RequestData)session.getAttribute(ORIGIN);
      if (requestData != null) {
        boolean matchesRequest = requestData.matchesRequest(request);
				// Note that we check for "authenticationSucceeded()" below to see if
        // authentication actually happened during the call above to
        // "authenticateFromFilter()". The return status "SUCCESS" is too
        // general, and could mean we have a remembered authentication or an
        // unauthenticated identity. Here we need to react on the initial
        // authentication only.
        if (!matchesRequest && AuthStatus.SUCCESS.equals(lastStatus(request)) && authenticationSucceeded(request)) {
					// if we just authenticated but are on another URL than where we
          // should take the user, we redirect the user to this original URL.
					// It's less ideal for the SAM to do this, since there's no status
          // code for doing authentication AND redirecting (after which JASPIC
          // should not invoke the resource).
					redirect(response, requestData.getFullRequestURL());
					// don't continue the chain
          // this is what a SAM cannot instruct JASPIC to do.
					return;
        }
				// if there was a saved request and it matches with the current request,
        // it means the user was originally redirected from a protected resource
        // to authenticate and has now been redirected back to it
        // since we reach this filter, it means the user is now authenticated to
        // access this protected resource.
				if (matchesRequest) {
					if (requestData.isRestoreRequest()) {
						// restore the original request here by providing a wrapped request
            // this will ensure the original request parameters (GET + POST) as
            // well as the original cookies etc are available again.
						newRequest = new HttpServletRequestDelegator(request, requestData);
					}
					request.getSession().removeAttribute(ORIGIN);
				}
      }
      chain.doFilter(request, response);
    }
  }

  public static boolean authenticateFromFilter(final HttpServletRequest request, final HttpServletResponse response) {
    try {
      request.setAttribute(Jaspic.IS_AUTHENTICATION_FROM_FILTER, true);
      return request.authenticate(response);
    }
    catch (ServletException | IOException e) {
      throw new IllegalArgumentException(e);
    }
    finally {
      request.removeAttribute(Jaspic.IS_AUTHENTICATION_FROM_FILTER);
    }
  }

  public static void redirect(final HttpServletResponse response, final String location) {
    try {
      response.sendRedirect(location);
    }
    catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  /**
   ** Returns <code>true</code> if a SAM has indicated that it intended
   ** authentication to be happening during the current request.
   ** <br>
   ** Does not ne the actual user/caller principal should be checked as well.
   */
  public static boolean authenticationDone(final HttpServletRequest request) {
    return Boolean.TRUE.equals(request.getAttribute(Jaspic.DID_AUTHENTICATION));
  }

  public static boolean authenticationSucceeded(final HttpServletRequest request) {
    return Boolean.TRUE.equals(request.getAttribute(Jaspic.DID_AUTHENTICATION)) && request.getUserPrincipal() != null;
  }

	public static void lastStatus(final HttpServletRequest request, AuthStatus status) {
		request.setAttribute(Jaspic.LAST_AUTH_STATUS, status);
	}
	
	public static AuthStatus lastStatus(final HttpServletRequest request) {
		return (AuthStatus) request.getAttribute(Jaspic.LAST_AUTH_STATUS);
	}

  /**
   * Returns <code>true</code> if the given object equals one of the given
   * objects.
   * @param <T> The generic object type.
   * @param object The object to be checked if it equals one of the given objects.
   * @param objects The argument list of objects to be tested for equality.
   * @return <code>true</code> if the given object equals one of the given objects.
   */
  @SafeVarargs
  public static <T> boolean oneOf(final T object, final T... objects) {
    for (Object other : objects) {
      if (object == null ? other == null : object.equals(other)) {
        return true;
      }
    }
    return false;
  }
}
