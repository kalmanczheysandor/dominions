import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class AdminAccountSettingsService extends TService {
    #baseUrl = super.extendWithBaseUrl('/account/admin/settings');
    #permissionResource = 'Account.Admin.Settings';

    async getProfilePhotoUrlIfExists() {

        // Communicate with backend
        const url = this.#baseUrl + '/profile/photo';
        if(await super.isImageExist(url)) {
            return url;
        }
        return null;
    }

    async accessProfileDetails() {
        // Checking permission
        console.log("ProfileDetails:W0");
        authService.assertAccessActionGrantedOn(this.#permissionResource);
        console.log("ProfileDetails:W1");

        // Communicate with backend
        const url = this.#baseUrl + '/profile';
        let x = await this.accessRecordByUuid(url);
        console.log("ProfileDetails:W2");
        return x;
    }

    async modifyProfileDetails(data) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/profile/edit';
        return await this.modifyRecord(url, data);
    }

    async accessCredentialPassword() {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/credential/password';
        return await this.accessRecordByUuid(url);
    }

    async modifyCredentialPassword(data) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/credential/password/edit';
        return await this.modifyRecord(url, data);
    }
}

// Create singleton instance
const adminAccountSettingsService = new AdminAccountSettingsService();

// Exports
export default adminAccountSettingsService;
export {AdminAccountSettingsService, adminAccountSettingsService};
