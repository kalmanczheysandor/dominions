export default class TException {
    #exceptionCode
    constructor(code) {
        //
        if (new.target === TException) {
            throw new Error('The TException class is not instantiable.');
        }

        //
        this.#exceptionCode = code;
    }
    getExceptionCode() {
        return this.#exceptionCode;
    }
}