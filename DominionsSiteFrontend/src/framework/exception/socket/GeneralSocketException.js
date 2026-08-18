import TSocketException from "@/framework/exception/socket/TSocketException";

export default class GeneralSocketException extends TSocketException {

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