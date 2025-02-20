package bka.iam.identity.nextcloud.model;

import java.util.List;
import java.util.Map;

public class UserDetails {
    private boolean enabled;
    private String storageLocation;
    private String id;
    private long lastLogin;
    private String backend;
    private Object subadmin; // Not tested or seen what this data type could be when populated
    private Map<String, Object> quota;
    private String email;
    private List<String> additional_mail;
    private String displayname;
    private String phone;
    private String address;
    private String website;
    private String twitter;
    private List<String> groups;
    private String language;
    private String locale;
    private Object notify_email;
    private Map<String, Object> backendCapabilities;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getBackend() {
        return backend;
    }

    public void setBackend(String backend) {
        this.backend = backend;
    }

    public Object getSubadmin() {
        return subadmin;
    }

    public void setSubadmin(Object subadmin) {
        this.subadmin = subadmin;
    }

    public Map<String, Object> getQuota() {
        return quota;
    }

    public void setQuota(Map<String, Object> quota) {
        this.quota = quota;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getAdditional_mail() {
        return additional_mail;
    }

    public void setAdditional_mail(List<String> additional_mail) {
        this.additional_mail = additional_mail;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Object getNotify_email() {
        return notify_email;
    }

    public void setNotify_email(Object notify_email) {
        this.notify_email = notify_email;
    }

    public Map<String, Object> getBackendCapabilities() {
        return backendCapabilities;
    }

    public void setBackendCapabilities(Map<String, Object> backendCapabilities) {
        this.backendCapabilities = backendCapabilities;
    }
}
