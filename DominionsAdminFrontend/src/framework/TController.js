import ActionNotGrantedFailureException from "@/framework/exception/permission/ActionNotGrantedFailureException";
import TWarningDialog from "@/framework/component/TWarningDialog/TWarningDialog";
import GeneralFailureException from "@/framework/exception/failure/GeneralFailureException";
import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";
import AuthenticationFailureException from "@/framework/exception/failure/AuthenticationFailureException";
import TFailure from "@/framework/exception/failure/TFailure";
import TAlertDialog from "@/framework/component/TAlertDialog/TAlertDialog";
import TError from "@/framework/exception/error/TError";
import TException from "@/framework/exception/TException";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import SecurityFailureException from "@/framework/exception/failure/SecurityFailureException";
import {useToast} from "vue-toastification";
import authService from "@/services/auth/AuthService";
import router from "@/router";
import backendConfiguration from "@/configurations/backendConfiguration";

export default class TController {

    static getTime() {
        let date = new Date();
        return date.getTime();

    }

    static async displayExceptionMessages(exp) {

        if (exp instanceof TFailure) {
            if (exp instanceof GeneralFailureException) {
                await TWarningDialog({
                    message: exp.getMessage()
                });
            } else if (exp instanceof SecurityFailureException) {

                if (exp.getCode() == 'AccessActionNotGrantedException') {
                    await TWarningDialog({
                        title: 'Permission warning',
                        message: 'Access permission is not granted!'
                    });
                } else if (exp.getCode() == 'AddActionNotGrantedException') {
                    await TWarningDialog({
                        title: 'Permission warning',
                        message: 'Add permission is not granted!'
                    });
                } else if (exp.getCode() == 'EditActionNotGrantedException') {
                    await TWarningDialog({
                        title: 'Permission warning',
                        message: 'Edit permission is not granted!'
                    });
                } else if (exp.getCode() == 'DeleteActionNotGrantedException') {
                    await TWarningDialog({
                        title: 'Permission warning',
                        message: 'Delete permission is not granted!'
                    });
                } else if (exp.getCode() == 'AccessDeniedException') {
                    await TWarningDialog({
                        title: 'Security warning',
                        message: 'Access is denied!'
                    });
                } else if (exp.getCode() == 'NoResourceFoundException') {
                    await TWarningDialog({
                        title: 'Security warning',
                        message: 'The requested endpoint does not exist!'
                    });
                } else if (exp.getCode() == 'SessionExpiredException') {
                    await TWarningDialog({
                        title: 'Security warning',
                        message: 'The Session is expired!'
                    });
                    authService.invalidateAuthentication();
                    router.push('/');
                } else if (exp.getCode() == 'UserIsNotAuthenticatedException') {
                    await TWarningDialog({
                        title: 'Security warning',
                        message: 'The user is not authenticated!'
                    });
                } else {
                    await TWarningDialog({
                        message: ['Uncategorised SecurityFailureException is found! with code:' + exp.getCode()]
                    });
                }
            } else if (exp instanceof AuthenticationFailureException) {

                if (exp.getCode() == 'BadCredentialsException') {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Invalid username or password'
                    });
                } else if (exp.getCode() == 'LockedException') {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Your account is locked'
                    });
                } else if (exp.getCode() == 'DisabledException') {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Your account is disabled'
                    });
                } else if (exp.getCode() == 'AccountExpiredException') {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Your account has expired'
                    });
                } else if (exp.getCode() == 'CredentialsExpiredException') {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Your password has expired'
                    });

                } else {
                    await TWarningDialog({
                        title: 'Authentication warning',
                        message: 'Authentication is rejected!'
                    });
                }
            } else if (exp instanceof ActionNotGrantedFailureException) {
                await TWarningDialog({
                    title: 'Permission warning',
                    message: 'Sorry! You do not have the necessary "' + exp.getAction() + '" permission!'
                });
            } else if (exp instanceof NetworkFailureException) {
                await TWarningDialog({
                    title: 'Network warning',
                    message: 'Couldn\'t communicate with endpoint!'
                });
            } else {
                await TWarningDialog({
                    message: ['Uncategorised TFailure is found!', exp]
                });
            }
        } else if (exp instanceof TError) {
            if (exp instanceof GeneralErrorException) {
                await TAlertDialog({
                    message: exp.getMessage()
                });
            } else {
                await TAlertDialog({
                    message: ['Uncategorised TError is found!', exp]
                });
            }
        } else if (exp instanceof TException) {
            await TAlertDialog({
                message: ['Uncategorised TException is found!', exp]
            });
        } else {
            await TAlertDialog({
                message: ['Uncategorised exception is found!', exp]
            });
        }
    }

    static displaySavedToast() {
        const toast = useToast();
        toast.success('Saved!');
    }

    static displayModifiedToast() {
        const toast = useToast();
        toast.success('Modified!');
    }

    static displayDeletedToast() {
        const toast = useToast();
        toast.success('Deleted!');
    }

    static displaySuccessToast(message) {
        const toast = useToast();
        toast.success(message);
    }


}