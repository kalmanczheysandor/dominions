import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class GameScenarioService extends TService {
    #baseUrl = super.extendWithBaseUrl('/game/scenario');
    #permissionResource = 'Game.Scenario';
    #difficultyLevels = {EASY:'Easy',MODERATE:'Moderate',HARD:'Hard'};

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
        const result = await this.listAllRecord(url);

        // Replace enum value to its caption
        const rows = result.data;
        let items = [];
        for (const row of rows) {
            let item = row;
            let difficultyEnum = this.#difficultyLevels[row.difficulty]
            if (difficultyEnum) {
                item.difficulty = this.#difficultyLevels[row.difficulty];
            }
            items.push(item)
        }
        result.data = items;
        return result;
    }

    async atAddListDifficulty() {
        // Checking permission
        authService.assertAddActionGrantedOn(this.#permissionResource);

        return {
            data:[
                {key:'EASY',title:'Easy'},
                {key:'MODERATE',title:'Moderate'},
                {key:'HARD',title:'Hard'}
            ]
        }
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
const gameScenarioService = new GameScenarioService();

// Exports
export default gameScenarioService;
export {GameScenarioService, gameScenarioService};
