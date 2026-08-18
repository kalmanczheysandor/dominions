import TException from "@/framework/exception/TException";

export default class TBackendException extends TException {
    #parameters;
    constructor(code, parameters={}) {
        //
        if (new.target === TBackendException) {
            throw new Error('The TBackendException class is not instantiable.');
        }

        //
        super(code);
        this.#parameters = parameters;
    }
    getParameters() {
        return this.#parameters;
    }
}