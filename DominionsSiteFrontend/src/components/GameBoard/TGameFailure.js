import TFailureException from "@/framework/exception/failure/TFailureException";

export default class TGameFailure extends TFailureException {
    #message = "";
    constructor(message) {
        super();
        this.#message = message;
    }

    getMessage() {
        return this.#message;
    }
}