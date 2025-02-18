package oracle.iam.system.integration;

import java.util.Map;

import oracle.iam.reconciliation.config.vo.Profile;
import oracle.iam.reconciliation.config.vo.TargetAttribute;
import oracle.iam.reconciliation.config.vo.Type;

public class Test {

  public Test() {
    super();
  }
  
  public static void main(String[] args) {
//    Profile profile = new Profile();
 }

  protected String getScopeQuery(Profile profile, Map scope) {
    if (scope == null)
      return "";
    StringBuilder     query = new StringBuilder(0);
    TargetAttribute[] reconAttr = profile.getForm().getTargetAttributes();
    for (TargetAttribute attr : reconAttr) {
      String key = attr.getOIMDescName();
      if (scope.keySet().contains(key)) {
        String columnName = attr.getOIMAttribute().getFieldName();
        Type   type = attr.getOIMAttribute().getFieldType();
        if (query.length() > 0)
          query.append(" or ");
        Object value = scope.get(key);
        if (type == Type.String)
          value = "'" + value + "'";
        query.append(columnName).append("=").append(value);
      }
    }
    if (query.length() > 0)
      query.insert(0, " and (").append(")");
    return query.toString();
  }
}
