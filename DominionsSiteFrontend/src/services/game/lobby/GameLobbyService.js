import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class BreedService extends TService {
    #baseUrl = super.extendWithBaseUrl('/game');
    #permissionResource = 'Game.Lobby';

    //
    // async accessByUuid(uuid) {
    //     // Checking permission
    //     authService.assertAccessActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl + '/' + uuid;
    //     return await this.accessRecordByUuid(url);
    // }
    //
    // async save(data) {
    //     // Checking permission
    //     authService.assertAddActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl + '/add';
    //     return await this.saveRecord(url, data);
    // }
    //
    // async modify(uuid, data) {
    //     // Checking permission
    //     authService.assertEditActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl + '/' + uuid + '/edit';
    //     return await this.modifyRecord(url, data);
    // }
    //
    // async delete(uuid) {
    //     // Checking permission
    //     authService.assertDeleteActionGrantedOn(this.#permissionResource);
    //
    //     // Communicate with backend
    //     const url = this.#baseUrl +  '/' + uuid + '/delete';
    //     return await this.deleteRecord(url);
    // }
    //
   //  async deleteMultiple(uuidList) {
   //      // Checking permission
   //      authService.assertDeleteActionGrantedOn(this.#permissionResource);
   //
   //      // Communicate with backend
   //      const url = this.#baseUrl + '/delete';
   //      return await this.deleteMultipleRecord(url, uuidList);
   //  }
   //
   //      // Checking permission
   // authService.assertAccessActionGrantedOn(this.#permissionResource);
   //
   //      // Communicate with backend
   //      const url = this.#baseUrl + '/list';
   //      return await this.listAllRecord(url);
   //  }    // async listAll() {



    async listAll() {
        // Checking permission
        //authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/list';
        return await this.listAllRecord(url);
    }





    async listAll2() {
        // Checking permission
        //authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        // const url = this.#baseUrl + '/list';
        // return await this.listAllRecord(url);

        return {
            data:[
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-1.jpg',
                    title:'World map',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:2,
                    playerCurrentCount:1,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-2.jpg',
                    title:'World map',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:2,
                    playerCurrentCount:1,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-2.jpg',
                    title:'World map',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:2,
                    playerCurrentCount:1,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-3.jpg',
                    title:'World map',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:2,
                    playerCurrentCount:1,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd',
                    boardImage:'board-3.jpg',
                    title:'World map',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:2,
                    playerCurrentCount:1,
                },

            ]
        }
    }


}

// Create singleton instance
const breedService = new BreedService();

// Exports
export default breedService;
export {BreedService, breedService};
