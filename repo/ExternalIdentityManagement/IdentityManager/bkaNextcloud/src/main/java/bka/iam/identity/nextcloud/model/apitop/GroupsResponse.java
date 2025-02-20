package bka.iam.identity.nextcloud.model.apitop;

import bka.iam.identity.nextcloud.model.AllResponse;
import bka.iam.identity.nextcloud.model.Groups;

public class GroupsResponse {
    private AllResponse<Groups> ocs;

    public AllResponse<Groups> getOcs() {
        return ocs;
    }

    public void setOcs(AllResponse<Groups> ocs) {
        this.ocs = ocs;
    }
}
