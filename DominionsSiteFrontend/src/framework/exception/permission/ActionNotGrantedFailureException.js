import TFailureException from "@/framework/exception/failure/TFailureException";

export default class ActionNotGrantedFailureException extends TFailureException {
    #action;
    constructor(action) {
        super();
        this.#action = action;
    }

    getAction() {
        return this.#action;
    }

}