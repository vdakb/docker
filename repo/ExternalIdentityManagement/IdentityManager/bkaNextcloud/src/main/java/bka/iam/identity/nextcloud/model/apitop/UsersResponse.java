package bka.iam.identity.nextcloud.model.apitop;

import bka.iam.identity.nextcloud.model.AllResponse;
import bka.iam.identity.nextcloud.model.Users;

public class UsersResponse {
    private AllResponse<Users> ocs;

    public AllResponse<Users> getOcs() {
        return ocs;
    }

    public void setOcs(AllResponse<Users> ocs) {
        this.ocs = ocs;
    }
}

