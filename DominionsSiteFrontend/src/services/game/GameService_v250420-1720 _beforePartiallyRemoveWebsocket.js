import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";
import backendConfiguration from "@/configurations/backendConfiguration";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import TSocketService from "@/framework/TSocketService";
import async from "async";
import TFailureException from "@/framework/exception/failure/TFailureException";
import TParameterValidation from "@/framework/utils/TParameterValidation";

class GameService extends TSocketService {
    #baseUrl = super.extendWithBaseUrl('/game');
    stompClient = null;
    isConnected = false;
    socketEndpointUrl = 'http://localhost:15000/site/spring-boot-chat';
    responseTimeout = 5000;


    async connectToSocketEndpoint() {
        this.stompClient = await TSocketService.connectToSocketEndpoint(this.stompClient, this.socketEndpointUrl);
    }

    async waitForResponse(endpointUrl, chanelUrl, requestObj) {
        return await TSocketService.waitForResponse(this.stompClient, endpointUrl, chanelUrl, requestObj, this.responseTimeout);
    }

    /////////////////////////////////////
    /////////////////////////////////////
    /////////////////////////////////////

    async listenOnStateChanel(gamePlayUuid,callback) {
        // Input checking
        TParameterValidation.assertParameterIsACallbackFunction(callback, 'callback');
        TParameterValidation.assertParameterIsNotABlankString(gamePlayUuid,'gamePlayUuid');

        // Execution
        await this.connectToSocketEndpoint();
        await TSocketService.listenOnAChanel(this.stompClient, '/user/queue/state/'+gamePlayUuid, callback);
    }

    async create(requestBody) {
        await this.connectToSocketEndpoint();
        return await this.waitForResponse('/app/create', '/user/queue/create', requestBody);
    }

    async join(requestBody) {
        await this.connectToSocketEndpoint();
        return await this.waitForResponse('/app/join', '/user/queue/join', requestBody);
    }

    async listAllRecruiting() {
        await this.connectToSocketEndpoint();
        return await this.waitForResponse('/app/list', '/user/queue/list', {});
    }

    async sendAttackAction(requestBody) {
        await this.connectToSocketEndpoint();
        return await this.waitForResponse('/app/action/attack', '/user/queue/action', requestBody);
    }



    async init(requestBody) {
        await this.connectToSocketEndpoint();
        return await this.waitForResponse('/app/init', '/user/queue/init', requestBody);
    }



    async listAllBoards() {
        // Checking permission
        //authService.assertAccessActionGrantedOn(this.#permissionResource);

        // Communicate with backend
        // const url = this.#baseUrl + '/list';
        // return await this.listAllRecord(url);

        return {
            data: [
                {
                    uuid: 'jakdhajsdhasjkdhasjkd',
                    boardImage: 'board-1.jpg',
                    title: 'World map',
                    subtitle: 'some text',
                    description: 'Here are some description',
                    difficulty: 'Beginner',
                    playerAiCount: 3,
                    playerNormalCount: 1,
                    isOpen: true,
                },
                {
                    uuid: 'jakdhajsdhasjkdhasjkd2',
                    boardImage: 'board-2.jpg',
                    title: 'Europe map',
                    subtitle: 'some text',
                    description: 'Here are some description',
                    difficulty: 'Moderate',
                    playerAiCount: 3,
                    playerNormalCount: 1,
                    isOpen: false,
                },
                {
                    uuid: 'jakdhajsdhasjkdhasjkd3',
                    boardImage: 'board-3.jpg',
                    title: 'Africa map',
                    subtitle: 'some text3',
                    description: 'Here are some description',
                    difficulty: 'Hard',
                    playerAiCount: 3,
                    playerNormalCount: 1,
                    isOpen: false,
                },
                {
                    uuid: 'jakdhajsdhasjkdhasjkd4',
                    title: 'test13',
                    subtitle: 'some text3',
                    description: 'Here are some description',
                    difficulty: 'Beginner',
                    playerAiCount: 3,
                    playerNormalCount: 1,
                    isOpen: false,
                },
                {
                    uuid: 'jakdhajsdhasjkdhasjkd5',
                    title: 'test13',
                    subtitle: 'some text3',
                    description: 'Here are some description',
                    difficulty: 'Beginner',
                    playerAiCount: 3,
                    playerNormalCount: 1,
                    isOpen: false,
                },

            ]
        }
    }


}

// Create singleton instance
const gameService = new GameService();

// Exports
export default gameService;
export {GameService, gameService};
