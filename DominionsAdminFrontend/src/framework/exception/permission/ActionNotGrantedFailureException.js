import TFailure from "@/framework/exception/failure/TFailure";

export default class ActionNotGrantedFailureException extends TFailure {
    #action;
    constructor(action) {
        super();
        this.#action = action;
    }

    getAction() {
        return this.#action;
    }

}