import TPermissionFailureException from '@/framework/exception/permission/TPermissionFailureException.js';

export default class AddActionNotGrantedFailureException extends TPermissionFailureException {
    constructor() {
        super('AddActionNotGrantedFailureException','ADD');
    }
}