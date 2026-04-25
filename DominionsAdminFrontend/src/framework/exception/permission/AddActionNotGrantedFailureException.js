import ActionNotGrantedFailureException from '@/framework/exception/permission/ActionNotGrantedFailureException.js';

export default class AddActionNotGrantedFailureException extends ActionNotGrantedFailureException {
    constructor() {
        super('ADD');
    }
}