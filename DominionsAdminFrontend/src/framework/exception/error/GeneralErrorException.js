import TError from "@/framework/exception/error/TError";
export default class GeneralErrorException extends TError {
    #message = "";
    constructor(message) {
        super();
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }

}