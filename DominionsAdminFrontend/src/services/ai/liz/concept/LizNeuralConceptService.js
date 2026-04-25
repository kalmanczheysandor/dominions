import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class LizNeuralConceptService extends TService {
    #baseUrl = super.extendWithBaseUrl('/ai/liz/concept');
    #permissionResource = 'Ai.Liz.Concept';


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
        const url = this.#baseUrl + '/' + uuid + '/delete';
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////// RESULT METHODS /////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async snapshotChartData(conceptUuid, scenarioUuid, playerUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend

        const url = this.#baseUrl + '/' + conceptUuid + '/result/chart/snapshot/'+scenarioUuid+'/'+playerUuid;
        return await this.accessRecordByUuid(url);
    }

    async executionChartData(conceptUuid, scenarioUuid, playerUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend

        const url = this.#baseUrl + '/' + conceptUuid + '/result/chart/execution/'+scenarioUuid+'/'+playerUuid;
        return await this.accessRecordByUuid(url);
    }

    async atResultListHistoryPlayer(conceptUuid,scenarioUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl  + '/' + conceptUuid + '/result/options/scenario/'+scenarioUuid+'/player';
        return await this.listAllRecord(url);
    }

    async atResultListHistoryScenario(conceptUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl  + '/' + conceptUuid + '/result/options/scenario';
        return await this.listAllRecord(url);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////// EXECUTION METHODS //////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    async executionStatus(conceptUuid) {
        // Checking permission
        authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + conceptUuid + '/execution/status';
        return await this.accessRecordByUuid(url);
    }

    async executionStart(conceptUuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + conceptUuid + '/execution/action/start';
        return await this.accessRecordByUuid(url);
    }

    async executionContinue(conceptUuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + conceptUuid + '/execution/action/continue';
        return await this.accessRecordByUuid(url);
    }

    async executionPause(conceptUuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + conceptUuid + '/execution/action/pause';
        return await this.accessRecordByUuid(url);
    }

    async executionCancel(conceptUuid) {
        // Checking permission
        authService.assertEditActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + conceptUuid + '/execution/action/cancel';
        return await this.accessRecordByUuid(url);
    }

}

// Create singleton instance
const lizNeuralConceptService = new LizNeuralConceptService();

// Exports
export default lizNeuralConceptService;
export {LizNeuralConceptService, lizNeuralConceptService};
