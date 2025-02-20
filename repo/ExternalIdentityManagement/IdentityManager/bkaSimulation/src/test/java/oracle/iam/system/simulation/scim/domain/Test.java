package oracle.iam.system.simulation.scim.domain;

import java.util.List;

import java.util.Set;

import oracle.hst.foundation.object.Pair;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.hst.foundation.utility.StringUtility;

public class Test {
  
  
  private static class Account {
    final String name;
    final String status;
    final List<String> data;
    
    Account(final String name, final String status, List<String> data) {
      this.name   = name;
      this.status = status;
      this.data   = data;
    }
  }
  static final Account       requested = new Account("AAAAA", "",            CollectionUtility.list("SN4711123", "SN"));
  static final List<Account> account   = CollectionUtility.list(
    new Account("AAAAA", "Revoke",      CollectionUtility.list("SN4711123se", "SN"))
  , new Account("AAAAA", "Provisioned", CollectionUtility.list("SN4711123gfa", "SN"))
  );
  
  static Set<String> exclude = CollectionUtility.set("Revoke");

  public static void main(String[] args) {
    boolean match = true;
    // digging into the account to coompare each account value id its equal to
    // the account data which are may requested
    for (Account cursor : account) {
      // thats gone or one that still arise, both of them are not the right
      // ones
      if (exclude.contains(cursor.status)) {
        continue;
      }
      for (int i = 0; i < requested.data.size(); i++) {
        // combine the match with the previuos on so that we aggregate all
        // comparesions in one value
        match = match && (StringUtility.isCaseInsensitiveEqual(requested.data.get(i), cursor.data.get(i)));
      }
      if (match)
        break;
    }
    System.out.println(match ? "match" : "nomatch");
 }
}
