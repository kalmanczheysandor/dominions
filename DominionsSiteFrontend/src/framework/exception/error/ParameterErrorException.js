import TException from "@/framework/exception/TException";

export default class ParameterErrorException extends TException {
    #message = "";
    constructor(message) {
        super('ParameterErrorException');
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }
}