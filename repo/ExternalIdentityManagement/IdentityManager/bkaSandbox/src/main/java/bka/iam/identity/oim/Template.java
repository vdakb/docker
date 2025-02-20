package bka.iam.identity.oim;

import java.util.List;

import oracle.iam.requestprofile.vo.RequestProfile;
import oracle.iam.requestprofile.vo.RequestProfileEntity;

import oracle.iam.requestprofile.api.RequestProfileService;

import oracle.iam.identity.foundation.rmi.IdentityServer;
import oracle.iam.requestprofile.vo.RequestProfileEntityAttribute;

public class Template {

  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   **
   ** @throws Exception          if the test case fails.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args)
    throws Exception {
    
    final IdentityServer service = Network.extranet();
    try {
      service.connect();
      final RequestProfileService      facade   = service.service(RequestProfileService.class);
      final RequestProfile             profile  = facade.getRequestProfile("e-FBS Schulungszugriff");
      final List<RequestProfileEntity> entities = profile.getEntities();
      for (RequestProfileEntity entity : profile.getEntities()) {
        Network.CONSOLE.debug(String.format("%s::%s:%s", entity.getEntityID() ,entity.getEntityName() , entity.getEntityType()));
        final List<RequestProfileEntityAttribute> attributes = entity.getAttributes();
        for (RequestProfileEntityAttribute cursor : attributes) {
          Network.CONSOLE.debug(cursor.toString());
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      service.disconnect();
    }
  }
}
