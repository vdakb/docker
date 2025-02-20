package bka.iam.identity.nextcloud.model.apitop;

import bka.iam.identity.nextcloud.model.AllResponse;

public class MetaResponse {
    private AllResponse<Object> ocs;

    public AllResponse<Object> getOcs() {
        return ocs;
    }

    public void setOcs(AllResponse<Object> ocs) {
        this.ocs = ocs;
    }
}
