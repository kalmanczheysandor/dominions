import TService from "@/framework/TService";
import authService from "@/services/auth/AuthService";


class GameService extends TService {

    #baseUrl = super.extendWithBaseUrl('/game');
    // permissionResource = 'Game.Launch';
    // stompClient = null;
    // socketEndpointUrl = 'http://localhost:15000/site/spring-boot-chat';
    //socketEndpointUrl = 'https://game.dominions.hu/api/site/spring-boot-chat';
    // responseTimeout = 5000;

    // async connectToSocketEndpoint() {
    //     this.stompClient = await TSocket.connectToSocketEndpoint(this.stompClient, this.socketEndpointUrl);
    // }
    //
    // async waitForResponse(endpointUrl, chanelUrl, requestObj) {
    //     return await TSocket.waitForResponse(this.stompClient, endpointUrl, chanelUrl, requestObj, this.responseTimeout);
    // }


    async create(data) {
        // Checking permission
        authService.assertAddActionGrantedOn("Game.Create");

        // Communicate with backend
        const url = this.#baseUrl + '/create';
        return await this.sendPostRequest(url, data);
    }

    async listAllPublishedScenarios() {
        // Checking permission
        authService.assertAccessActionGrantedOn("Game.Create");

        // Communicate with backend
        const url = this.#baseUrl + '/list/scenario';
        return await this.listAllRecord(url);
    }

    async join(data) {
        // Checking permission
        authService.assertEditActionGrantedOn("Game.Lobby");

        // Communicate with backend
        const url = this.#baseUrl + '/join';
        return await this.sendPostRequest(url, data);
    }



    async listAllRecruiting() {
        // Checking permission
        authService.assertAccessActionGrantedOn("Game.Lobby");

        // Communicate with backend
        const url = this.#baseUrl + '/list/recruiting';
        return await this.listAllRecord(url);
    }





    // async listenOnStateChanel(gamePlayUuid,callback) {
    //     // Input checking
    //     TParameterValidation.assertParameterIsACallbackFunction(callback, 'callback');
    //     TParameterValidation.assertParameterIsNotABlankString(gamePlayUuid, 'gamePlayUuid');
    //
    //     // Execution
    //     await this.connectToSocketEndpoint();
    //     await TSocket.listenOnAChanel(this.stompClient, '/user/queue/state/' + gamePlayUuid, callback);
    // }


    async sendAttackAction(data) {
        // Checking permission
        authService.assertEditActionGrantedOn("Game.Play");

        // Communicate with backend
        const url = this.#baseUrl + '/action/attack';
        return await this.sendPostRequest(url, data);
    }

    async sendReserveAction(data) {
        // Checking permission
        authService.assertEditActionGrantedOn("Game.Play");

        // Communicate with backend
        const url = this.#baseUrl + '/action/reserve';
        return await this.sendPostRequest(url, data);
    }

    async sendResignAction(data) {
        // Checking permission
        authService.assertEditActionGrantedOn("Game.Play");

        // Communicate with backend
        const url = this.#baseUrl + '/action/resign';
        return await this.sendPostRequest(url, data);
    }

    async state(data) {
        // Checking permission
        authService.assertAccessActionGrantedOn("Game.Play");

        // Communicate with backend
        const url = this.#baseUrl + '/state';
        return await this.sendPostRequest(url, data);
    }

}

// Create singleton instance
const gameService = new GameService();

// Exports
export default gameService;
export {GameService, gameService};
