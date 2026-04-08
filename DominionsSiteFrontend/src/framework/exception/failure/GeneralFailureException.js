import TFailureException from "@/framework/exception/failure/TFailureException";

export default class GeneralFailureException extends TFailureException {
    #message;
    #code
    constructor(code, message) {
        super();
        this.#message = message;
        this.#code = code;
    }

    getMessage() {
        return this.#message;
    }

    getCode() {
        return this.#code;
    }
}