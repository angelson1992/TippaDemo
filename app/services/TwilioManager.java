package services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import javax.inject.Singleton;

/**
 * Created by John on 7/7/2017.
 */
@Singleton
public class TwilioManager {

  public static final String ACCOUNT_SID = "AC9d7da8caa1ddf4cd62cdcd9aa58850b4";
  public static final String AUTH_TOKEN = "72fbacd0d9d96241572527a31927750c";

  public void sendMessage(String reciever, String message){

    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

    Message mess = Message.creator(new PhoneNumber("+1" + reciever), new PhoneNumber("+" + "16786079094"), message).create();

    System.out.println(mess.getSid());
  }

}
