import TFailureException from "@/framework/exception/failure/TFailureException";

export default class NetworkFailureException extends TFailureException {
    constructor() {
        super('NetworkFailureException');
    }
}