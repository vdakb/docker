package bka.iam.identity.moodle;

import org.identityconnectors.framework.spi.AbstractConfiguration;

public class MoodleConfiguration extends AbstractConfiguration {

    private String host;

    private int port;

    private boolean sslEnabled;

    private String token;

    //TODO: Expand existing functionality to support user/password as an alternative to tokens. Not required for now.
    // Moodle supports generating a token via user/password credentials against REST API, or simply we can have it
    // pre-generated and applied to the IT resource.
    //private String authenticationType;

    // ---- Lookup.Splunk.Configuration properties below ----

    private boolean useBatchingForRecon = true;
    private String nameAttr = "username";

    @Override
    public void validate() {

    }

    /* ****
     * BUSINESS LOGIC METHODS
     *****/

    public String getBaseUrl() {
        StringBuilder url = new StringBuilder("http");
        if (this.isSslEnabled()) {
            url.append("s");
        }
        url.append("://" + this.getHost() + ":" + this.getPort());
        url.append("/webservice/rest/server.php");
        url.append("?wstoken=" + this.getToken() + "&moodlewsrestformat=json");
        return url.toString();
    }

    /* ****
     * GETTERS / SETTERS
     *****/

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isUseBatchingForRecon() {
        return useBatchingForRecon;
    }

    public void setUseBatchingForRecon(boolean useBatchingForRecon) {
        this.useBatchingForRecon = useBatchingForRecon;
    }

    public String getNameAttr() {
        return nameAttr;
    }

    public void setNameAttr(String nameAttr) {
        this.nameAttr = nameAttr;
    }
}
