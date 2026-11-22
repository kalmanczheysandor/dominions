import TService from "@/framework/TService";
import authService from "@/services/auth/AuthService";


class GameService extends TService {

    #baseUrl = super.extendWithBaseUrl('/game');

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
