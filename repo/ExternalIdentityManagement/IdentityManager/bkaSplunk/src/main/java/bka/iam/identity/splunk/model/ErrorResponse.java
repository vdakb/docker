package bka.iam.identity.splunk.model;

import java.util.List;

public class ErrorResponse {
    private List<Message> messages;

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        for (Message message : messages) {
            builder.append(message);
        }
        return builder.toString();
    }
}
