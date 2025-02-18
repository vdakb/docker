package bka.iam.identity.nextcloud.model.apitop;

import bka.iam.identity.nextcloud.model.AllResponse;
import bka.iam.identity.nextcloud.model.UserDetails;

public class UserResponse {
    private AllResponse<UserDetails> ocs;

    public AllResponse<UserDetails> getOcs() {
        return ocs;
    }

    public void setOcs(AllResponse<UserDetails> ocs) {
        this.ocs = ocs;
    }
}
