package oracle.iam.identity.icf.connector.plp;

import java.util.List;

import javax.naming.directory.SearchResult;

import oracle.iam.identity.icf.foundation.SystemException;

import oracle.iam.identity.icf.connector.DirectoryFilter;
import oracle.iam.identity.icf.connector.DirectorySchema;

import oracle.iam.identity.icf.connector.DirectorySearch;

import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.OperationOptionsBuilder;
////////////////////////////////////////////////////////////////////////////////
// class TestUserSearch
// ~~~~~ ~~~~~~~~~~~~~~
/**
 ** The test case to fetch users from the target system.
 **
 ** @author  sylvert.bernet@oracle.com
 ** @version 1.0.0.0
 ** @since   1.0.0.0
 */
public class TestUserSearch {
  
  //////////////////////////////////////////////////////////////////////////////
  // Methods grouped by functionality
  //////////////////////////////////////////////////////////////////////////////

  //////////////////////////////////////////////////////////////////////////////
  // Method:   main
  /**
   ** Simple command line interface to execute the test case.
   **
   ** @param  args               the command line arguments.
   */
  @SuppressWarnings("unused")
  public static void main(String[] args) {
    try {
      DirectorySchema schema = Client.intranet();
      DirectoryFilter filter = DirectoryFilter.entry("ou=P20-Benutzer");
      OperationOptionsBuilder builder = new OperationOptionsBuilder();
      builder.setAttributesToGet("tenant", "cn", "genericOU");
      DirectorySearch   search = DirectorySearch.build(schema.endpoint(), ObjectClass.ACCOUNT, builder.build(), null);
      List<SearchResult> result = search.list();
      for (SearchResult cursor : result)
        System.out.println(cursor);
    }
    catch (SystemException e) {
      System.out.println(e.getClass().getSimpleName().concat("::").concat(e.code()).concat("::").concat(e.getLocalizedMessage()));
    }
  }
}