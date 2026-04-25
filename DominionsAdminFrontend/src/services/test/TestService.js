import TService from "@/framework/TService";
import {authService} from "@/services/auth/AuthService";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";

class TestService extends TService {
    #baseUrl = super.extendWithHost('/auth/data/test');
    #baseUrl2 = super.extendWithHost('/test/data/test');
    #permissionResource = 'Test';


    async accessByUuid(uuid) {
        // Communicate with backend
        const url = this.#baseUrl + '/' + uuid;

        try {

            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });
            console.log("XXXXXXXXXXX")
            console.log(response);
            //const xx = await this.handleResponse(response);

            return {};
        } catch(exp) {
            if(exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }

    }

    async accessByUuid2(uuid) {
        // Communicate with backend
        const url = this.#baseUrl2 + '/' + uuid;

        try {

            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });
            console.log("XXXXXXXXXXX")
            console.log(response);
            //const xx = await this.handleResponse(response);

            return {};
        } catch(exp) {
            if(exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }

    }



}

// Create singleton instance
const testService = new TestService();

// Exports
export default testService;
export {TestService, testService};
