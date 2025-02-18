package oracle.iam.backend.service;

import java.io.IOException;
import java.io.ByteArrayInputStream;

import java.security.cert.X509Certificate;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Application;

import oracle.iam.access.AssertionProcessor;
import oracle.iam.access.AssertionException;

import oracle.iam.access.security.DER;

import oracle.iam.access.saml2.schema.Assertion;

@Path("/inventory")
public class Inventory {

  //////////////////////////////////////////////////////////////////////////////
  // static final attributes
  //////////////////////////////////////////////////////////////////////////////

  private static final String HEADER = "OAM_IDENTITY_ASSERTION";

  //////////////////////////////////////////////////////////////////////////////
  // instance attributes
  //////////////////////////////////////////////////////////////////////////////

  @Context
  private Application application;

  //////////////////////////////////////////////////////////////////////////////
  // Constructors
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   Ctor
  /**
   ** Constructs a default <code>Inventory</code> service that allows use as a
   ** JavaBean.
   ** <br>
   ** Zero argument constructor required by the framework.
   ** <br>
   ** Default Constructor
   */
  public Inventory() {
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
  @Path("parts")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String parts(final @HeaderParam(HEADER) String identityAssertion) {
    System.out.println("****** DEBUG: Inventory Service: Request header OAM_IDENTITY_ASSERTION: [[" + identityAssertion + "]]");
    String result = "{\"result\":";
    result += "[";
    result += "{\"uniqueid\" : \"123\", \"name\" : \"ABC\", \"desc\" : \"This is part ABC\", \"price\" : \"100.00\"},";
    result += "{\"uniqueid\" : \"456\", \"name\" : \"DEF\", \"desc\" : \"This is part DEF\", \"price\" : \"200.00\"},";
    result += "{\"uniqueid\" : \"789\", \"name\" : \"GHI\", \"desc\" : \"This is part GHI\", \"price\" : \"300.00\"}";
    result += "]";
    result += "}";
    return result;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   ticket
  /**
   ** Inventory request to retrieve an assertion.
   **
   ** @param  identityAssertion  the identity assertion injected in the request
   **                            flow.
   **                            <br>
   **                            May be <code>null</code>.
   **                            <br>
   **                            Allowed object is {@link String}.
   **
   ** @return                    the retrieved schema.
   **                            <br>
   **                            Possible object is {@link Resource}.
   **
   ** @throws ServiceException   if the request fails.
   */
  @GET
  @Path("assertion")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_XML)
  public String assertion(final @HeaderParam(HEADER) String identityAssertion)
    throws IOException {
    return identityAssertion;
  }

  //////////////////////////////////////////////////////////////////////////////
  // Method:   subject
  /**
   ** Inventory request to retrieve the identity assertion.
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
  @Path("subject")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public String subject(final @HeaderParam(HEADER) String identityAssertion)
    throws IOException {

    final X509Certificate certificate = DER.certificate(this.getClass().getClassLoader().getResourceAsStream("META-INF/buster-access-manager.cer"));
    String result = null;
    try {
      final Assertion assertion = AssertionProcessor.parse(certificate.getPublicKey(), new ByteArrayInputStream(identityAssertion.getBytes()));
      result = "{\"assertion\":";
      result += "{ \"id\" : \"" + assertion.id() + "\"}";
      result += ", \"subject\" : \"" + assertion.subject().nameID().value() + "\"";
      result += ", \"nameQualifier\" : \"" + assertion.subject().nameID().nameQualifier() + "\"";
      result += ", \"spProvidedID\" : \"" + assertion.subject().nameID().spProvidedID() + "\"";
      result += "}";

//      result += "{\"nameID\": {\"format\":" + assertion.subject().nameID().format() + "\"";assertion.subject().nameID().value()
    }
    catch (AssertionException e) {
      result = "{\"exception\":";;
      result += e.getLocalizedMessage();
      result += "}";
    }
    catch (Throwable t) {
      result = "{\"thrown\":";
      result += t.getLocalizedMessage();
      result += "}";
    }

    return result;
  }
}