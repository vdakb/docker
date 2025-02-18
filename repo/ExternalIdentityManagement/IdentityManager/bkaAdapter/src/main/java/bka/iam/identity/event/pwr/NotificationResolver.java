package bka.iam.identity.event.pwr;

import bka.iam.identity.event.OrchestrationBundle;
import bka.iam.identity.event.OrchestrationHandler;
import bka.iam.identity.event.OrchestrationMessage;
import bka.iam.identity.event.OrchestrationResolver;
import oracle.hst.foundation.SystemMessage;
import oracle.hst.foundation.utility.CollectionUtility;
import oracle.iam.identity.usermgmt.vo.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.FIRSTNAME;
import static oracle.iam.identity.usermgmt.api.UserManagerConstants.AttributeName.LASTNAME;

/**
 * The NotificationResolver provide the basic implementation to resolve notification events
 * that belongs to password reset operations.
 */
public class NotificationResolver extends OrchestrationResolver {

  private static final String DEFAULT_TIMESTAMP_FORMAT = "yyyy.MM.dd hh:mm:ss";

  @Override
  public HashMap<String, Object> getReplacedData(final String eventType, final Map<String, Object> subject)
    throws Exception {

    final String method = "getReplacedData";
    trace(method, SystemMessage.METHOD_ENTRY);

    // write in the log list of the passed in attributes
    if (this.logger() != null && this.logger().debugLevel())
      debug(method, formatData(OrchestrationBundle.string(OrchestrationMessage.NOTIFICATION_RESOLVE_INCOME), subject));

    final HashMap<String, Object> replace = new HashMap<>();
    LocalDateTime expire = (LocalDateTime) subject.remove("expire");
    if (expire != null) {
      replace.put("expire", expire.format(DateTimeFormatter.ofPattern(DEFAULT_TIMESTAMP_FORMAT)));
    }

    replace.put("url", subject.remove("url"));

    final String userLogin = (String) subject.remove("userLogin");
    User user = OrchestrationHandler.identity(userLogin, CollectionUtility.set(FIRSTNAME.getId(), LASTNAME.getId()), true);
    if (user != null) {
      replace.put("firstName", user.getFirstName());
      replace.put("lastName" , user.getLastName());
    } else {
      replace.put("firstName", "");
      replace.put("lastName" , userLogin);
    }

    // write in the log list of the returned attributes
    if (this.logger() != null && this.logger().debugLevel())
      debug(method, formatData(OrchestrationBundle.string(OrchestrationMessage.NOTIFICATION_RESOLVE_OUTCOME), replace));

    replace.put("css", mailHead());
    trace(method, SystemMessage.METHOD_EXIT);
    return replace;
  }
}