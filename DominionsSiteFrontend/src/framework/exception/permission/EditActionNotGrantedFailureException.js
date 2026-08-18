import TPermissionFailureException from '@/framework/exception/permission/TPermissionFailureException.js';

export default  class EditActionNotGrantedFailureException extends TPermissionFailureException {
    constructor() {
        super('EditActionNotGrantedFailureException','EDIT');
    }
}