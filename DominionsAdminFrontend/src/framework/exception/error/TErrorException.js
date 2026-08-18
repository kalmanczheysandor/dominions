import TException from "@/framework/exception/TException";

export default class TErrorException extends TException {
    constructor(code) {
        //
        if (new.target === TErrorException) {
            throw new Error('The TErrorException class is not instantiable.');
        }

        //
        super(code);
    }
}