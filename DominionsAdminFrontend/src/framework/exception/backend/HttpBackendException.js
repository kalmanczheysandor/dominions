import TBackendException from "@/framework/exception/backend/TBackendException";

export default class HttpBackendException extends TBackendException {
    #statusCode;

    constructor(statusCode,parameters={}){
        super('Http'+statusCode+'BackendException',parameters);
        this.#statusCode = statusCode;
    }

    getStatusCode() {
        return this.#statusCode;
    }
}