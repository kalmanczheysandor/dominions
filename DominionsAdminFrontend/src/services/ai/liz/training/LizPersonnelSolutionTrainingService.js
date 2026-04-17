import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class LizPersonnelSolutionTrainingService extends TService {
    #baseUrl = super.extendWithBaseUrl('/ai/liz/personnel/0/solution');
    #permissionResource = 'Ai.Liz.Personnel.Solution.Training';


    async accessByUuid(solutionUuid,uuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/' + uuid;
        return await this.accessRecordByUuid(url);
    }

    async save(solutionUuid,data) {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/add';
        return await this.saveRecord(url, data);
    }

    async modify(solutionUuid,uuid, data) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/'+uuid+'/edit';
        return await this.modifyRecord(url, data);
    }

    async delete(solutionUuid,uuid) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/'+uuid+'/delete';
        return await this.deleteRecord(url);
    }

    async deleteMultiple(solutionUuid,uuidList) {
        // Checking permission
        authService.assertDeleteActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/delete';
        return await this.deleteMultipleRecord(url, uuidList);
    }

    async listAll(solutionUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/'+solutionUuid+'/training/list';
        return await this.listAllRecord(url);
    }


}

// Create singleton instance
const lizPersonnelSolutionTrainingService = new LizPersonnelSolutionTrainingService();

// Exports
export default lizPersonnelSolutionTrainingService;
export {LizPersonnelSolutionTrainingService, lizPersonnelSolutionTrainingService};
