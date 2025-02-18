package oracle.iam.human.capital;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.DefaultValue;

import javax.ws.rs.core.MediaType;

@Path("/region")
public class Region {

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Region</code> service endpoint that allows use
   ** as a JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Region() {
    // ensure inheritance
    super();
  }

  //////////////////////////////////////////////////////////////////////////////
  // Methods group by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   parts
  /**
   ** Inventory request to retrieve a collection of parts.
   **
   ** @param  identityAssertion  the identity assertion injected in the request
   **                            flow.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   ** @param  uriInfo            the UriInfo of the request.
   **                            <br>
   **                            Allowed object is {@link UriInfo}.
   **
   ** @return                    the retrieved schema.
   **                            <br>
   **                            Possible object is {@link Resource}.
   **
   ** @throws ServiceException   if the request fails.
   */
  @GET
  @Path("{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String search(final @PathParam("id") Long id, final @QueryParam("startIndex") @DefaultValue("1") int start, final @QueryParam("count") @DefaultValue("500") int count) {
    if (id != null)
      return "{\"result\":{\"id\":\"" + id + "\",\"startIndex\":\"" + start + "\",\"count\":\"" + count + "\"}}";
    else
      return "{\"result\":{\"startIndex\":\"" + start + "\",\"count\":\"" + count + "\"}}";
  }
}