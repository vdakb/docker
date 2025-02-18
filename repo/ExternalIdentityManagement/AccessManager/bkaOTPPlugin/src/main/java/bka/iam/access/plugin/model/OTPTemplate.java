package bka.iam.access.plugin.model;


/**
 * POJO object to store OTP Template data
 */
public class OTPTemplate {
    
    public static final String LOCALE  = "locale";
    public static final String SUBJECT = "subject";
    public static final String BODY    = "body";
    
    private String locale;
    private String subject;
    private String body;
    
    
    public OTPTemplate() {
        super();
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getLocale() {
        return locale;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("Locale=%s, Subject=%s, Body=%s", this.locale, this.subject, this.body);
    }

}
