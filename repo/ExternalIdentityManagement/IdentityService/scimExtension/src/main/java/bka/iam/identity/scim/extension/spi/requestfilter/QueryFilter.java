package bka.iam.identity.scim.extension.spi.requestfilter;

import bka.iam.identity.scim.extension.spi.AbstractEndpoint;

import java.io.IOException;

import java.net.URI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

@PreMatching
@Provider
public class QueryFilter extends AbstractEndpoint implements ContainerRequestFilter {
  
  public QueryFilter() {
    super();
  }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
    // Get the original URI and query parameters
    UriInfo uriInfo = requestContext.getUriInfo();
    MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<String, String>(uriInfo.getQueryParameters());

    // Process the "attributes" parameter
    if (queryParameters.containsKey("attributes")) {
        List<String> values = queryParameters.get("attributes");

        // Split comma-separated values and trim whitespace
        List<String> splitValues = values.stream()
            .flatMap(value -> Stream.of(value.split(",")))
            .map(String::trim)
            .collect(Collectors.toList());

        // Replace the original "attributes" parameter with split values
        queryParameters.put("attributes", splitValues);
    }
    
    if (queryParameters.containsKey("excludedAttributes")) {
        List<String> values = queryParameters.get("excludedAttributes");

        // Split comma-separated values and trim whitespace
        List<String> splitValues = values.stream()
            .flatMap(value -> Stream.of(value.split(",")))
            .map(String::trim)
            .collect(Collectors.toList());

        // Replace the original "attributes" parameter with split values
        queryParameters.put("excludedAttributes", splitValues);
    }
    
    if (queryParameters.containsKey("count") && !queryParameters.containsKey("startIndex")) {
      List<String> values = new ArrayList<String>();
      values.add("1");
      queryParameters.put("startIndex", values);
    }
    
    if (!queryParameters.containsKey("count") && queryParameters.containsKey("startIndex")) {
      List<String> values = new ArrayList<String>();
      values.add("10");
      queryParameters.put("count", values);
    }

    // Build a new URI with modified query parameters
    URI originalUri = uriInfo.getRequestUri();
    URI modifiedUri = UriBuilder.fromUri(originalUri)
        .replaceQuery(queryParameters.entrySet().stream()
             .map(entry -> entry.getKey() + "=" + String.join("&" + entry.getKey() + "=", entry.getValue()))
            .collect(Collectors.joining("&")))
        .build();
    
    // Replace the request URI
    requestContext.setRequestUri(modifiedUri);
  }
}
