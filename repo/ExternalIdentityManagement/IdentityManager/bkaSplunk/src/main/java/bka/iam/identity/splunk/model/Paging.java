package bka.iam.identity.splunk.model;

public class Paging {
    private long total;
    private long perPage;
    private long offset;

    /* ***
     * Business Logic
     * ***/
    public boolean hasNextPage() {
        return total > getTotalPulled();
    }

    public long getTotalPulled() {
        return offset + perPage;
    }

    /* ***
     * Getters / Setters
     * ***/

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPerPage() {
        return perPage;
    }

    public void setPerPage(long perPage) {
        this.perPage = perPage;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(long offset) {
        this.offset = offset;
    }
}
