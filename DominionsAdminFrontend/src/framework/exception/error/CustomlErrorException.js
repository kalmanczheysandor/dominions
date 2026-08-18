import TErrorException from "@/framework/exception/error/TErrorException";

export default class CustomErrorException extends TErrorException {
    #message = "";
    constructor(message) {
        super('CustomErrorException');
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }
}