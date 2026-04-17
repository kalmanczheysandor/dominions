import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class DogService extends TService {
    #baseUrl = super.extendWithBaseUrl('/data/dog');
    #permissionResource = 'Dog';


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

    async atAddListBreed() {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add/options/breed';
        return await this.listAllRecord(url);
    }

    async atEditListBreed(uuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit/options/breed';
        return await this.listAllRecord(url);
    }


    async atAddListSite() {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add/options/site';
        return await this.listAllRecord(url);
    }

    async atEditListSite(uuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit/options/site';
        return await this.listAllRecord(url);
    }

    async listGallery(uuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/gallery/list';
        return await this.listAllRecord(url);
    }

    async deleteGalleryItem(uuid, filename) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/gallery/' + filename + '/delete';
        return await this.deleteRecord(url);
    }


}

// Create singleton instance
const dogService = new DogService();

// Exports
export default dogService;
export {DogService, dogService};
