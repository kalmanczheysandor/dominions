import TErrorException from "@/framework/exception/error/TErrorException";
export default class GeneralErrorException extends TErrorException {
    #message = "";
    constructor(message) {
        super();
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }

}