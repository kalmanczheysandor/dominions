import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class AccountSettingsService extends TService {
    #baseUrl = super.extendWithBaseUrl('/account/settings');
    #permissionResource = 'Account.Settings';

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
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/profile';
        return await this.accessRecordByUuid(url);
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
const accountSettingsService = new AccountSettingsService();

// Exports
export default accountSettingsService;
export {AccountSettingsService, accountSettingsService};
