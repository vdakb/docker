package bka.iam.access.plugin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;

import oracle.sdp.messaging.util.StringDataSource;

import oracle.security.am.common.utilities.constant.Component;
import oracle.security.am.common.utilities.log.OAMLogger;

import oracle.ucs.messaging.ws.MessagingClient;
import oracle.ucs.messaging.ws.MessagingException;
import oracle.ucs.messaging.ws.MessagingFactory;
import oracle.ucs.messaging.ws.types.Address;
import oracle.ucs.messaging.ws.types.Message;
import oracle.ucs.messaging.ws.types.Status;
import oracle.ucs.messaging.ws.types.StatusType;

/**
 * Utility class to send email message to end user
 */
public class UMSUtil {

  protected Logger LOGGER = OAMLogger.getLogger(Component.PLGN);
  private static String CLASS_NAME = UMSUtil.class.getName();

  private static final String APPLICATION_NAME       = "OTPAuthenticationPlugin";
  private static final String UMS_MESSAGE_TYPE       = "EMAIL";
  private static final String OWSM_WSS11_POLICY_NAME = "oracle/wss11_username_token_with_message_protection_client_policy";
  
  private String url;
  private String csfKey;
  private boolean wss11_username_token;
  

  /**
   * Create UMS Utility which support sending of emails
   * @param url End URL to UMS service
   * @param csfKey CSF key created under oracle.wsm.security. CDF key must be created under the oracle.wsm.security
   * @param wss11_username_token if this value is set to true then OWSM policy oracle/wss11_username_token_with_message_protection_client_policy 
   *                             is used. Otherwise basic authentication s used.
   */
  public UMSUtil(String url, String csfKey, boolean wss11_username_token) {
    super();
    this.url = url;
    this.csfKey = csfKey;
    this.wss11_username_token=wss11_username_token;
  }

  protected MessagingClient getMessagingClient() {
    String METHOD = "getMessagingClient";
    LOGGER.entering(CLASS_NAME, METHOD);

    HashMap<String, Object> config = new HashMap<String, Object>();
    config.put(oracle.ucs.messaging.ws.MessagingConstants.APPLICATION_NAME           , APPLICATION_NAME);   
    config.put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY                , this.url);
    config.put(oracle.wsm.security.util.SecurityConstants.ClientConstants.WSS_CSF_KEY, this.csfKey);
    if(wss11_username_token){
      config.put(oracle.ucs.messaging.ws.ClientConstants.POLICIES      , new String[] {OWSM_WSS11_POLICY_NAME});
    }

    MessagingClient client = new MessagingClient(config);
    
    LOGGER.exiting(CLASS_NAME, METHOD, ("returning new UMS MessageClient " + client == null) ? null : client);
    return client;
  }

  /**
   * Send email address via UMS
   * @param from
   * @param fromName
   * @param to
   * @param subject
   * @param body
   * @return
   */
  public String sendEmail(String from, String fromName, String to, String subject, String body) {
    String METHOD = "getMessagingClient";
    LOGGER.entering(CLASS_NAME, METHOD);
    
    List<String> toList = new ArrayList<String>();
    if (to != null) {
      toList.add(to);
      if (to.contains(",")) {
	String[] split = to.split(",");
	for (String recepient : split) {
	  recepient = recepient.replace("\\", "");
	  toList.add(recepient.trim());
	}
      } else {
	toList.add(to);
      }
    }
    
    LOGGER.exiting(CLASS_NAME, METHOD);
    return sendEmail(from, fromName, toList, subject, body);
  }

  /**
   * Send email via UMS service
   * @param from From email addresss
   * @param fromName From user frienly name
   * @param to To who will be email delivered
   * @param subject email subject
   * @param body email body
   * @return Email message ID. THis ID can be later on used to check status of the delivery
   */
  public String sendEmail(String from, String fromName, List<String> to, String subject, String body) {
    String METHOD = "sendEmail";
    LOGGER.entering(CLASS_NAME, METHOD);

    String msgId = null;
    String msgType = "text/html; charset=UTF-8";
    MessagingClient client = getMessagingClient();
    try {
      Message message = new Message();
      message.getRecipients().addAll(getAddress(to));
      message.setContent(new DataHandler((DataSource) new StringDataSource(body, msgType)));
      if (from != null) {
	message.getSenders().add(getAddress(from, fromName));
      }
      message.setSubject(subject);
      msgId = client.send(message, null, null);
      LOGGER.logp(Level.FINEST,  CLASS_NAME, METHOD,"Email is succesfully send to "+ to);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.log(Level.SEVERE, "Error sending OTP", e);
    }
    LOGGER.exiting(CLASS_NAME, METHOD,"MessageID is "+msgId);
    return msgId;
  }

  /**
   * Check status of send email
   * @param msgId massage ID
   * @return
   */
  public String checkDeliveryStatus(String msgId) {
    String METHOD = "checkDeliveryStatus";
    LOGGER.entering(CLASS_NAME, METHOD);
    String sendStatus = "pending";
    MessagingClient messagingClient = getMessagingClient();
    List<Status> statusList = null;
    try {
      this.LOGGER.log(Level.FINER, "Getting message status list from Webservice for message: " + msgId);
      statusList = messagingClient.getStatus(msgId, getRecipients());
      this.LOGGER.log(Level.FINER,
		      "Got message status list from Webservice for message: " + msgId + " status list: " +
		      statusList.toString());
    } catch (MessagingException mEx) {
      this.LOGGER.log(Level.SEVERE, "Error getting OTP message status.", (Throwable) mEx);
      return "error";
    }
    if (statusList != null) {
      Iterator<Status> statusItr = statusList.iterator();
      while (statusItr.hasNext()) {
	Status status = statusItr.next();
	StatusType statusType = null;
	if (status == null) {
	  statusType = StatusType.OVERALL_PENDING;
	} else {
	  statusType = status.getType();
	}
	if (statusType == StatusType.DELIVERY_TO_DEVICE_SUCCESS || statusType == StatusType.DELIVERY_TO_GATEWAY_SUCCESS ||
	    statusType == StatusType.OVERALL_SUCCESS) {
	  sendStatus = "success";
	  continue;
	}
	if (statusType == StatusType.DELIVERY_TO_GATEWAY_FAILURE || statusType == StatusType.ENGINE_PROCESSING_FAILURE ||
	    statusType == StatusType.DELIVERY_TO_DEVICE_FAILURE || statusType == StatusType.OVERALL_FAILURE)
	  return "fail";
      }
    } else {
      this.LOGGER.log(Level.FINER, "No status found for OTP message.");
    }
    this.LOGGER.log(Level.FINER, "handleWebservicesStatus: Returning: " + sendStatus);
    LOGGER.exiting(CLASS_NAME, METHOD);
    return sendStatus;
  }

  /**
   * 
   * @param stringAddress
   * @return
   */
  protected List<Address> getAddress(List<String> stringAddress) {
    String METHOD = "getAddress";
    if (LOGGER.isLoggable(Level.FINER)) {
      LOGGER.entering(CLASS_NAME, METHOD);
    }
    
    List<Address> addresses = new ArrayList<Address>();
    for (String addr : stringAddress) {
      addresses.add(getAddress(addr));
    }
    
    if (LOGGER.isLoggable(Level.FINER)) {
      LOGGER.exiting(CLASS_NAME, METHOD);
    }
    return addresses;
  }

  /**
   * Get address expected by UMS server
   * @param address email address in format name@domain
   * @return
   */
  protected Address getAddress(String address) {
    String METHOD = "getAddress";
    if (this.LOGGER.isLoggable(Level.FINER))
      this.LOGGER.entering(CLASS_NAME, METHOD);
    if (address == null || address.length() == 0)
      return null;
    if (this.LOGGER.isLoggable(Level.FINER))
      this.LOGGER.exiting(CLASS_NAME, METHOD, "returning address " + UMS_MESSAGE_TYPE.toString() + ":" + address);
    return MessagingFactory.createAddress(UMS_MESSAGE_TYPE.toString() + ":" + address);
  }
  
  /**
   * Get address with user frienly name
   * @param address email address in format name@domain
   * @param fromName user friendly name like Administrator
   * @return
   */
  protected Address getAddress(String address, String fromName) {
    String METHOD = "getAddress";
    if (LOGGER.isLoggable(Level.FINER))
      LOGGER.entering(CLASS_NAME, METHOD,"address=" + address + ", type=" + this.UMS_MESSAGE_TYPE + ", fromName=" + fromName);
    if (UMS_MESSAGE_TYPE == null || address == null || address.length() == 0 || fromName == null || fromName.length() == 0)
      return null;
    if (LOGGER.isLoggable(Level.FINER))
      LOGGER.exiting(CLASS_NAME, METHOD, "returning address " + String.format("%s: \"%s\" <%s>", new Object[] { this.UMS_MESSAGE_TYPE, fromName, address }));
    return MessagingFactory.createAddress(String.format("%s: \"%s\" <%s>", new Object[] { this.UMS_MESSAGE_TYPE, fromName, address }));
  }
  
  /**
   ** Get empty Recipients
   ** @return
   **/
  protected List<Address> getRecipients() {
    return Collections.emptyList();
  }
  
  
  public static void main(String[] args){
    UMSUtil ums = new UMSUtil("http://oig-instance.sub03231021150.bamfdevvcn.oraclevcn.com:8000/ucs/messaging/webservice","umsKey",false);
    String emailID = ums.sendEmail("TestSender@ocipoc.de", "TestSender", "tomas.sebo@imguru.eu", "UMS test email", "This is UMS test email");
    System.out.println(emailID);
  }
  
  
}
