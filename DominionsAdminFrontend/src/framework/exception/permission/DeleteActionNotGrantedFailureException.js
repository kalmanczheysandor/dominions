import ActionNotGrantedFailureException from '@/framework/exception/permission/ActionNotGrantedFailureException.js';

export default class DeleteActionNotGrantedFailureException extends ActionNotGrantedFailureException {
    constructor() {
        super('DELETE');
    }
}