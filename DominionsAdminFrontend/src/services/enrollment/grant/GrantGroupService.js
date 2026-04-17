import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class GrantGroupService extends TService {
    #baseUrl = super.extendWithBaseUrl('/enrollment/grant');
    #grantResource = 'Enrollment.GrantGroup';

    async accessByUuid(uuid) {
        // Checking grant
        authService.assertAccessActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid;
        return await this.accessRecordByUuid(url);
    }

    async save(data) {
        // Checking grant
        authService.assertAddActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add';
        return await this.saveRecord(url, data);
    }

    async modify(uuid, data) {
        // Checking grant
        authService.assertEditActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit';
        return await this.modifyRecord(url, data);
    }

    async delete(uuid) {
        // Checking grant
        authService.assertDeleteActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl +  '/' + uuid + '/delete';
        return await this.deleteRecord(url);
    }

    async deleteMultiple(uuidList) {
        // Checking grant
        authService.assertDeleteActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl + '/delete';
        return await this.deleteMultipleRecord(url, uuidList);
    }

    async listAll() {
        // Checking grant
        authService.assertAccessActionGrantedOn(this.#grantResource);

        // Communicate with backend
        const url = this.#baseUrl + '/list';
        return await this.listAllRecord(url);
    }


}

// Create singleton instance
const grantGroupService = new GrantGroupService();

// Exports
export default grantGroupService;
export {GrantGroupService, grantGroupService};
