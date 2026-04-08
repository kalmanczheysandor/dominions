import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";

class GameBoardSelectorService extends TService {
    #baseUrl = super.extendWithBaseUrl('/data/board');
    #permissionResource = 'Board';

    async accessByUuid(uuid) {
        // Checking permission
        //authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid;
        return await this.accessRecordByUuid(url);
    }

    async listAll() {
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
                    subtitle:'some text',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:true,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd2',
                    boardImage:'board-2.jpg',
                    title:'Europe map',
                    subtitle:'some text',
                    description:'Here are some description',
                    difficulty:'Moderate',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd3',
                    boardImage:'board-3.jpg',
                    title:'Africa map',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Hard',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd4',
                    title:'test13',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },
                {
                    uuid:'jakdhajsdhasjkdhasjkd5',
                    title:'test13',
                    subtitle:'some text3',
                    description:'Here are some description',
                    difficulty:'Beginner',
                    playerAiCount:3,
                    playerNormalCount:1,
                    isOpen:false,
                },

            ]
        }
    }


}

// Create singleton instance
const boardService = new GameBoardSelectorService();

// Exports
export default boardService;
export {GameBoardSelectorService, boardService};
