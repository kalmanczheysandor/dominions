import GeneralSocketException from "@/framework/exception/socket/GeneralSocketException";

export default class ResponseWaitingTimedOutSocketException extends GeneralSocketException {
    #url;

    constructor(url) {
        super('ResponseWaitingTimedOutSocketException', {
            parameters: {
                url: url
            }
        });
        this.#url = url;
    }

    getUrl() {
        return this._url;
    }
}