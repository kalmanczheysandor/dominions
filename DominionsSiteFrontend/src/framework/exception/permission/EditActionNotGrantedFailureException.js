import ActionNotGrantedFailureException from '@/framework/exception/permission/ActionNotGrantedFailureException.js';

export default  class EditActionNotGrantedFailureException extends ActionNotGrantedFailureException {
    constructor() {
        super('EDIT');
    }
}