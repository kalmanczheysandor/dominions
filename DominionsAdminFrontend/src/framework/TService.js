import GeneralFailureException from "@/framework/exception/failure/GeneralFailureException";
import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";
import AuthenticationFailureException from "@/framework/exception/failure/AuthenticationFailureException";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import SecurityFailureException from "@/framework/exception/failure/SecurityFailureException";
import backendConfiguration from "@/configurations/backendConfiguration"
import HttpFailureException from "@/framework/exception/failure/HttpFailureException";
import CustomErrorException from "@/framework/exception/error/CustomlErrorException";

class TService {
    constructor() {
    }

    static convertBackendResponsesToExceptionIfNeeded(context, data) {
        console.log("-----TService-----");
        console.log("Data:", data);
        console.log("Context:", context);

        console.log("???:", "Alma");

        if (typeof data === "object" && data !== null && Object.hasOwn(data, "type")) {
            console.log("Type:", data.type);
            if (data.type == 'GeneralFailureResponse') {
                throw new GeneralFailureException(data.code, data);
            } else if (data.type == 'SecurityFailureResponse') {
                throw new SecurityFailureException(data.code, data);
            } else if (data.type == 'AuthenticationFailureResponse') {
                throw new AuthenticationFailureException(data.code, data);
            } else if (data.type == 'GeneralErrorResponse') {
                throw new GeneralErrorException(data.code, data);
            } else {
                throw new CustomErrorException('Unsupported response type is found:' + data.type + '!');
            }
        } else if (typeof context === "object" && context !== null && Object.hasOwn(context, "status")) {
            if (context.status >= 400 && context.status <= 599) {
                throw new HttpFailureException(context.status, context);
            } else {
                throw new CustomErrorException('Unsupported http status code is found:' + context.status + '!');
            }
        } else {
            throw new CustomErrorException('Unsupported response received!');
        }


    }

    static async handleHttpResponse(response) {
        let data = null;

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        }
        if (response.ok) {
            return {data: data};
            //return data;
        } else {
            console.log("handleHttpResponse:");
            console.log(response);
            TService.convertBackendResponsesToExceptionIfNeeded({status: response.status}, data);
        }

    }

    async saveRecord(url, data) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async modifyRecord(url, data) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async deleteRecord(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });
            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async deleteMultipleRecord(url, uuidList) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({items: uuidList})
            });
            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async listAllRecord(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async accessRecordByUuid(url) {
        try {

            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });
            return await TService.handleHttpResponse(response);
            // return await TService.handleHttpResponse(response);
        } catch (exp) {
            console.log("ACCESS. exp: " + exp);
            if (exp instanceof TypeError) {
                console.log("ACCESS. YES TypeError: " + exp);
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    extendWithBaseUrl(path) {
        return backendConfiguration.BACKEND_BASE_URL + path;
    }

    extendWithHost(path) {
        return backendConfiguration.BACKEND_HOST + path;
    }

    async isImageExist(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'HEAD',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            if (response.ok) {
                return true;
            }
            return false;
        } catch (exp) {
            return false;
        }
    }

}

// Exports
export default TService;
export {
    TService
};