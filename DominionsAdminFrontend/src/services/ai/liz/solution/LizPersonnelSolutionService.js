import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class LizPersonnelSolutionService extends TService {
    #baseUrl = super.extendWithBaseUrl('/ai/liz/personnel');
    #permissionResource = 'Ai.Liz.Personnel.Solution';


    async accessByUuid(personnelUuid,uuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/' + uuid;
        return await this.accessRecordByUuid(url);
    }

    async save(personnelUuid,data) {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/add';
        return await this.saveRecord(url, data);
    }

    async modify(personnelUuid,uuid, data) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/'+uuid+'/edit';
        return await this.modifyRecord(url, data);
    }

    async delete(personnelUuid,uuid) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/'+uuid+'/delete';
        return await this.deleteRecord(url);
    }

    async deleteMultiple(personnelUuid,uuidList) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/delete';
        return await this.deleteMultipleRecord(url, uuidList);
    }

    async listAll(personnelUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+personnelUuid+'/solution/list';
        return await this.listAllRecord(url);
    }


}

// Create singleton instance
const lizPersonnelSolutionService = new LizPersonnelSolutionService();

// Exports
export default lizPersonnelSolutionService;
export {LizPersonnelSolutionService, lizPersonnelSolutionService};
