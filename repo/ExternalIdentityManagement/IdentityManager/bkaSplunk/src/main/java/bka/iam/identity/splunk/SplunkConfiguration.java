package bka.iam.identity.splunk;

import org.identityconnectors.framework.spi.AbstractConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SplunkConfiguration extends AbstractConfiguration {

    private String host;

    private int port;

    private boolean sslEnabled;

    private String token;

    private String username;

    private String password;

    private String birthrightRole;

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
        url.append("://" + this.getHost() + ":" + this.getPort() + "/");
        return url.toString();
    }

    public boolean isBasicAuth() {
        return username != null && !username.isEmpty() && password != null && !password.isEmpty();
    }

    public String getBasicAuthValue() {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8));
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthrightRole() {
        return birthrightRole;
    }

    public void setBirthrightRole(String birthrightRole) {
        this.birthrightRole = birthrightRole;
    }
}
