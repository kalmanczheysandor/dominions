import TException from "@/framework/exception/TException";

export default class TFailureException extends TException {
    constructor(code) {
        //
        if (new.target === TFailureException) {
            throw new Error('The TFailureException class is not instantiable.');
        }
        //
        super(code);
    }
}