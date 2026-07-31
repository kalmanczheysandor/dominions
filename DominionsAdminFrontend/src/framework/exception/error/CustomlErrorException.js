import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";
import TError from "@/framework/exception/error/TError";

export default class CustomErrorException extends TError {
    #message = "";
    constructor(message) {
        super();
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }
}