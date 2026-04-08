import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class AccountService extends TService {
    #baseUrl = super.extendWithBaseUrl('/account');
    #permissionResource = 'Account';


    async signUp(data) {
        // Checking permission
        //authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/signup';
        return await this.saveRecord(url, data);
    }

    async signUpVerification(data) {
        // Checking permission
        //authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/signup/verification';
        return await this.saveRecord(url, data);
    }


    async recovery(data) {
        // Checking permission
        //authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/recovery';
        return await this.saveRecord(url, data);
    }


    async recoverySubmit(data) {
        // Checking permission
        //authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/recovery/submit';
        return await this.saveRecord(url, data);
    }


}

// Create singleton instance
const accountService = new AccountService();

// Exports
export default accountService;
export {AccountService, accountService};
