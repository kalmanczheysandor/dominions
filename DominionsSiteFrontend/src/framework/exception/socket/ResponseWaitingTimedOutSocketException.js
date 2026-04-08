import TSocketException from "@/framework/exception/socket/TSocketException";

export default class ResponseWaitingTimedOutSocketException extends TSocketException {
    #url;

    constructor(url) {
        super();
        this.#url = url;
        this._url = url;
    }

    get url() {
        return this._url;
    }
}