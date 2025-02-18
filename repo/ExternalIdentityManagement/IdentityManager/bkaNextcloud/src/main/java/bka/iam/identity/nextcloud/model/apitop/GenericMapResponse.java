package bka.iam.identity.nextcloud.model.apitop;

import bka.iam.identity.nextcloud.model.AllResponse;

import java.util.Map;

public class GenericMapResponse {
    private AllResponse<Map<String, Object>> ocs;

    public AllResponse<Map<String, Object>> getOcs() {
        return ocs;
    }

    public void setOcs(AllResponse<Map<String, Object>> ocs) {
        this.ocs = ocs;
    }
}
