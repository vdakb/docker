package bka.iam.identity.scim.extension.spi.requestfilter;

import bka.iam.identity.scim.extension.exception.ScimException;
import bka.iam.identity.scim.extension.exception.ScimExceptionMapper;
import bka.iam.identity.scim.extension.rest.HTTPContext;
import bka.iam.identity.scim.extension.rest.OIMSchema;
import bka.iam.identity.scim.extension.spi.AbstractEndpoint;

import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

@Provider
public class SchemaFilter extends AbstractEndpoint implements ContainerRequestFilter {

  @Context
  private HttpServletRequest httpRequest;
  
  
  public SchemaFilter() {
    super();
  }

  @Override
  public void filter(final ContainerRequestContext requestContext) {
    final String method = "filter";
    this.entering(method);
    // Initialize singleton with the current request
    try {
      final String contentType    = httpRequest.getHeader(HTTPContext.CONTENT_TYPE_HEADER);
      
      String xRequestedBy   = httpRequest.getHeader(HTTPContext.X_REQUESTED_BY_HEADER);
      if (contentType == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, HTTPContext.StatusCode.BAD_REQUEST.getMessage());
      }
      if (xRequestedBy == null) {
        throw new ScimException(HTTPContext.StatusCode.BAD_REQUEST, HTTPContext.StatusCode.BAD_REQUEST.getMessage());
      }
      if (!contentType.equals(HTTPContext.MEDIA_TYPE)) {
        throw new ScimException(HTTPContext.StatusCode.UNSUPPORTED_MEDIA_TYPE, HTTPContext.StatusCode.UNSUPPORTED_MEDIA_TYPE.getMessage());
      }
      OIMSchema.getInstance().initialize(this, httpRequest);
    }
    catch (ScimException e) {
      this.error(method, e.getMessage());
      requestContext.abortWith(new ScimExceptionMapper().toResponse(e));
    }
    finally {
      this.exiting(method);
    }
  }
}
