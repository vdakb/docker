package bka.iam.identity.nextcloud;

import org.identityconnectors.framework.spi.AbstractConfiguration;

import java.util.Base64;

public class NextcloudConfiguration extends AbstractConfiguration {

    private String host;

    private int port;

    private boolean sslEnabled;

    private String username;

    private String password;

    // Comma separated list of fields (no spaces) e.g. "phone,displayname"
    private String postUpdateValidateFields;

    private int reconPageSize;

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
        url.append("://" + this.getHost() + ":" + this.getPort() + "/ocs/v1.php/");
        return url.toString();
    }

    public String getBasicAuthValue() {
        return Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
    }

    public boolean isPostUpdateValidateField(String field) {
        if (postUpdateValidateFields == null) {
            return false;
        }

        String[] fields = postUpdateValidateFields.split(",");
        for (String f : fields) {
            if (field.equals(f)) {
                return true;
            }
        }

        return false;
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

    public String getPostUpdateValidateFields() {
        return postUpdateValidateFields;
    }

    public void setPostUpdateValidateFields(String postUpdateValidateFields) {
        this.postUpdateValidateFields = postUpdateValidateFields;
    }

    public int getReconPageSize() {
        return reconPageSize;
    }

    public void setReconPageSize(int reconPageSize) {
        this.reconPageSize = reconPageSize;
    }
}
