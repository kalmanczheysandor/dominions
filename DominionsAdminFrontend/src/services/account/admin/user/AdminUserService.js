import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class AdminUserService extends TService {
    #baseUrl = super.extendWithBaseUrl('/account/admin/user');
    #permissionResource = 'Account.Admin.User';




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

    async atAddListPermissionGroup() {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/add/options/permission';
        return await this.listAllRecord(url);
    }

    async atEditListPermissionGroup(uuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid + '/edit/options/permission';
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
const adminUserService = new AdminUserService();

// Exports
export default adminUserService;
export {AdminUserService, adminUserService};
