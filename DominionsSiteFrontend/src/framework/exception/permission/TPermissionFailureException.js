import TFailureException from "@/framework/exception/failure/TFailureException";

export default class TPermissionFailureException extends TFailureException {
    #action;
    constructor(code,action) {
        //
        if (new.target === TPermissionFailureException) {
            throw new Error('The TPermissionFailureException class is not instantiable.');
        }

        //
        super(code);
        this.#action = action;
    }
    getAction() {
        return this.#action;
    }
}