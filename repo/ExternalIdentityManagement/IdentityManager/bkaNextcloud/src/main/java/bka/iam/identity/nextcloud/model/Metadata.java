package bka.iam.identity.nextcloud.model;

public class Metadata {
    private String status;
    private long statuscode;
    private String message;
    private String totalitems;
    private String itemsperpage;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(long statuscode) {
        this.statuscode = statuscode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalitems() {
        return totalitems;
    }

    public void setTotalitems(String totalitems) {
        this.totalitems = totalitems;
    }

    public String getItemsperpage() {
        return itemsperpage;
    }

    public void setItemsperpage(String itemsperpage) {
        this.itemsperpage = itemsperpage;
    }

    @Override
    public String toString() {
        return getStatus() + ":" + getStatuscode() + " - " + getMessage();
    }
}
