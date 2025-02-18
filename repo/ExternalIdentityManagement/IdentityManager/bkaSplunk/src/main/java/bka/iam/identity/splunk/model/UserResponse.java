package bka.iam.identity.splunk.model;

import java.util.List;
import java.util.Map;

public class UserResponse {

    private Map<String, String> links;
    private String origin;
    private String updated;
    private Map<String, String> generator;
    private List<User> entry;
    private Paging paging;
    private List<Object> messages;

    public Map<String, String> getLinks() {
        return links;
    }

    public void setLinks(Map<String, String> links) {
        this.links = links;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Map<String, String> getGenerator() {
        return generator;
    }

    public void setGenerator(Map<String, String> generator) {
        this.generator = generator;
    }

    public List<User> getEntry() {
        return entry;
    }

    public void setEntry(List<User> entry) {
        this.entry = entry;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Object> getMessages() {
        return messages;
    }

    public void setMessages(List<Object> messages) {
        this.messages = messages;
    }
}