import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class HugoCharacterService extends TService {
    #baseUrl = super.extendWithBaseUrl('/ai/hugo/character');
    #permissionResource = 'Ai.Hugo.Character';


    async accessByUuid(uuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid;
        return await this.accessRecordByUuid(url);
    }

    async save(data) {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add';
        return await this.saveRecord(url, data);
    }

    async modify(uuid, data) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit';
        return await this.modifyRecord(url, data);
    }

    async delete(uuid) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl +  '/' + uuid + '/delete';
        return await this.deleteRecord(url);
    }

    async deleteMultiple(uuidList) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/delete';
        return await this.deleteMultipleRecord(url, uuidList);
    }

    async listAll() {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/list';
        return await this.listAllRecord(url);
    }


    async atAddListVariant() {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add/options/variant';
        return await this.listAllRecord(url);
    }

    async atEditListVariant(uuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit/options/variant';
        return await this.listAllRecord(url);
    }



}

// Create singleton instance
const hugoCharacterService = new HugoCharacterService();

// Exports
export default hugoCharacterService;
export {HugoCharacterService, hugoCharacterService};
