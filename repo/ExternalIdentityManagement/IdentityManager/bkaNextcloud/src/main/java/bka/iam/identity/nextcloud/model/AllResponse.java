package bka.iam.identity.nextcloud.model;

public class AllResponse<T> {
    private Metadata meta;
    private T data;

    public Metadata getMeta() {
        return meta;
    }

    public void setMeta(Metadata meta) {
        this.meta = meta;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}