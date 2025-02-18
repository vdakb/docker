package oracle.iam.identity.icf;

import java.util.Map;
import java.util.HashMap;

import Thor.API.tcResultSet;

import Thor.API.Operations.tcGroupOperationsIntf;

import oracle.hst.foundation.SystemConsole;

import oracle.iam.identity.foundation.TaskException;

import oracle.iam.identity.foundation.rmi.IdentityServer;

public class TestGroup {
  /** The console used for logging purpose */
  static final SystemConsole CONSOLE    = new SystemConsole("icf");
  public static void main(String[] args) {
    IdentityServer server = null;
    try {
      server = IdentityManager.server(IdentityManager.intranet());
      server.connect();
      final tcGroupOperationsIntf facade = server.service(tcGroupOperationsIntf.class);
      try {
        final Map<String, Object> filter = new HashMap<String, Object>();
        filter.put("Groups.Name", "SYSTEM*");
        final tcResultSet resultSet = facade.findGroups(filter);
        System.out.println(resultSet);
      }
      catch (Exception e) {
        throw TaskException.unhandled(e);
      }
    }
    catch (TaskException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
    finally {
      if (server != null)
        server.disconnect();
    }
  }
}