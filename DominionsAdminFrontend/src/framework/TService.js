import GeneralBackendException from "@/framework/exception/backend/GeneralBackendException";
import AuthenticationBackendException from "@/framework/exception/backend/AuthenticationBackendException";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import SecurityBackendException from "@/framework/exception/backend/SecurityBackendException";
import backendConfiguration from "@/configurations/backendConfiguration"
import HttpBackendException from "@/framework/exception/backend/HttpBackendException";
import CustomErrorException from "@/framework/exception/error/CustomlErrorException";

class TService {
    constructor() {
    }

    static convertBackendResponsesToExceptionIfNeeded(context, data) {

        if (typeof data === "object" && data !== null && Object.hasOwn(data, "type")) {

            //
            let type = data.type;
            let code = data.code;
            let parameters = {};
            if (Object.hasOwn(data, "parameters")) {
                parameters = data.parameters;
            }

            //
            if (type === 'GeneralErrorResponse') {
                throw new GeneralBackendException(code,parameters);
            } else if (type === 'GeneralFailureResponse') {
                throw new GeneralBackendException(code,parameters);
            } else if (type === 'SecurityFailureResponse') {
                throw new SecurityBackendException(code,parameters);
            } else if (type === 'AuthenticationFailureResponse') {
                throw new AuthenticationBackendException(code,parameters);
            } else {
                throw new CustomErrorException('Unsupported response type is found:' + type + '!');
            }
        } else if (typeof context === "object" && context !== null && Object.hasOwn(context, "status")) {
            if (context.status >= 400 && context.status <= 599) {
                throw new HttpBackendException(context.status, context);
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