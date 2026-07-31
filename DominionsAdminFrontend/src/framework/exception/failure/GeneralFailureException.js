import TFailure from "@/framework/exception/failure/TFailure";

export default class GeneralFailureException extends TFailure {
    #data = null;
    #code
    constructor(code, data) {
        super();
        this.#data = data;
        this.#code = code;
    }

    getData() {
        return this.#data;
    }

    getCode() {
        return this.#code;
    }
}