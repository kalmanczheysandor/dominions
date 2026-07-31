import TError from "@/framework/exception/error/TError";
export default class GeneralErrorException extends TError {
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