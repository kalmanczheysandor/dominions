import TFailure from "@/framework/exception/failure/TFailure";

export default class GeneralFailureException extends TFailure {
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