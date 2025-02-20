package bka.iam.identity.oim;

import java.util.List;

import oracle.iam.platform.entitymgr.vo.SearchCriteria;

import oracle.iam.provisioning.vo.Account;
import oracle.iam.provisioning.vo.ApplicationInstance;

import oracle.iam.provisioning.api.ProvisioningService;
import oracle.iam.provisioning.api.ProvisioningConstants;
import oracle.iam.provisioning.api.ApplicationInstanceService;

import oracle.iam.identity.foundation.rmi.IdentityServer;

public class ValidateAccount {

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

    final IdentityServer             server   = Network.extranet();
    try {
      server.connect();
      final ApplicationInstanceService service  = server.service(ApplicationInstanceService.class);
      final ApplicationInstance        instance = service.findApplicationInstanceByName("eFBSAccountProduction");

      final SearchCriteria             criteria = new SearchCriteria(ProvisioningConstants.AccountSearchAttribute.APPINST_ID.getId(), Long.valueOf(instance.getApplicationInstanceKey()), SearchCriteria.Operator.EQUAL);            

      final ProvisioningService        facade   = server.service(ProvisioningService.class);
      // the API returns a list of all the accounts provisioned to the user.
      // the additionall passed criteria used to filter the accounts being
      // returned.
      // we are not interested in any account details for the purpose of the use
      // case 
      final List<Account>              account  = facade.getAccountsProvisionedToUser("oooo", criteria, null);
      System.out.println(account.toString());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    finally {
      server.disconnect();
    }
  }
}
