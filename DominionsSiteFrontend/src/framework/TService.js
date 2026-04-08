import GeneralFailureException from "@/framework/exception/failure/GeneralFailureException";
import GeneralErrorException from "@/framework/exception/error/GeneralErrorException";
import AuthenticationFailureException from "@/framework/exception/failure/AuthenticationFailureException";
import NetworkFailureException from "@/framework/exception/failure/NetworkFailureException";
import SecurityFailureException from "@/framework/exception/failure/SecurityFailureException";
import backendConfiguration from "@/configurations/backendConfiguration"

class TService {
    constructor() {
    }


    static convertToExceptionIfNeeded(data) {


        if (data.type) {
            if (data.type == 'GeneralFailureResponse') {

                // SiteAccountSignUp
                if (data.code == 'SiteAccountSignUpVerificationTokenNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Sign up verification token is not found!');
                } else if (data.code == 'SiteAccountSignUpUserDetailsNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Sign up user details is not found!');
                } else if (data.code == 'SiteAccountSignUpUserUuidDuplicationException') {
                    throw new GeneralFailureException(data.code, 'User UUID already exists!');
                }else if (data.code == 'SiteAccountSignUpReservedUserIdentifierException') {
                    throw new GeneralFailureException(data.code, 'User identifier already exists!');
                }

                // SiteAccountRecovery
                if (data.code == 'SiteAccountRecoveryVerificationTokenNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Recovery verification token is not found!');
                } else if (data.code == 'SiteAccountRecoveryUserDetailsNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Recovery user details not found!');
                } else if (data.code == 'SiteAccountRecoveryUserUuidDuplicationException') {
                    throw new GeneralFailureException(data.code, 'User UUID already exists!');
                } else if (data.code == 'SiteAccountRecoveryIdentifierNotFoundException') {
                    throw new GeneralFailureException(data.code, 'User identifier not found!');
                } else if (data.code == 'SiteAccountRecoveryUserNotFinalisedException') {
                    throw new GeneralFailureException(data.code, 'User is not finalised!');
                } else if (data.code == 'SiteAccountRecoveryUserNotEnabledException') {
                    throw new GeneralFailureException(data.code, 'User is not enabled!');
                } else if (data.code == 'SiteAccountRecoveryConfirmPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'Confirm password mismatch!');
                }



                // AccountSettings
                if (data.code == 'SiteAccountSettingsUserNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record!');
                } else if (data.code == 'SiteAccountSettingsUserNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'SiteAccountSettingsIdentifierIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The identifier "' + data.parameters.identifier + '" is already in use!');
                } else if (data.code == 'SiteAccountSettingsCredentialCurrentPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The given password is not matching with the current one!');
                } else if (data.code == 'SiteAccountSettingsCredentialConfirmPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The confirm password is not matching!');
                }


                // GamePlaySession
                if (data.code == 'GameSessionNotRecruitingException') {
                    throw new GeneralFailureException(data.code, 'No more free slot!');
                } else if (data.code == 'PlayerEndpointKeyAlreadyRegisteredSessionException') {
                    throw new GeneralFailureException(data.code, 'This endpoint is already attached to this gameplay!');
                } else if (data.code == 'UserUuidAlreadyRegisteredSessionException') {
                    throw new GeneralFailureException(data.code, 'You are already in!');
                }


                // Etc


                throw new GeneralErrorException('??[GeneralFailureResponse]:' + data.code + '??');

            }
            else if (data.type == 'SecurityFailureResponse') {

                if (data.code == 'AccessActionNotGrantedException') {
                    throw new SecurityFailureException(data.code, 'Access action is not granted!');
                } else if (data.code == 'AddActionNotGrantedException') {
                    throw new SecurityFailureException(data.code, 'Add action is not granted!');
                } else if (data.code == 'EditActionNotGrantedException') {
                    throw new SecurityFailureException(data.code, 'Eddit action is not granted!');
                } else if (data.code == 'DeleteActionNotGrantedException') {
                    throw new SecurityFailureException(data.code, 'Delete action is not granted!');
                }

                if (data.code == 'AccessDeniedException') {
                    throw new SecurityFailureException(data.code, 'Access denied!');
                }
                else if (data.code == 'NoResourceFoundException') {
                    throw new SecurityFailureException(data.code, 'The requested endpoint does not exist!');
                }
                else if (data.code == 'SessionExpiredException') {
                    throw new SecurityFailureException(data.code, 'The Session is expired!');
                }
                else if (data.code == 'UserIsNotAuthenticatedException') {
                    throw new SecurityFailureException(data.code, 'The user is not authenticated!');
                }


                throw new SecurityFailureException('??[SecurityFailureResponse]:' + data.code + '??');

            } else if (data.type && data.type == 'AuthenticationFailureResponse') {
                if (data.code == 'BadCredentialsException') {
                    throw new AuthenticationFailureException(data.code, 'Invalid username or password');
                } else if (data.code == 'LockedException') {
                    throw new AuthenticationFailureException(data.code, 'Your account is locked');
                } else if (data.code == 'DisabledException') {
                    throw new AuthenticationFailureException(data.code, 'Your account is disabled');
                } else if (data.code == 'AccountExpiredException') {
                    throw new AuthenticationFailureException(data.code, 'Your account has expired');
                } else if (data.code == 'CredentialsExpiredException') {
                    throw new AuthenticationFailureException(data.code, 'Your password has expired');
                }
                throw new AuthenticationFailureException('??[AuthenticationFailureException]:' + data.code + '??');
            } else if (data.type == 'GeneralErrorResponse') {

                if (data.code == 'InternalServerError') {
                    throw new GeneralErrorException(data.code, 'An error?');
                }

                throw new GeneralErrorException('??[GeneralErrorResponse]:' + data.code + '??');

            } else {
                throw new GeneralErrorException('Unsupported response type is found:' + data.type + '!');
            }
        } else {
            throw new GeneralErrorException('Unsupported response received!');
        }
    }

    static async handleHttpResponse(response) {
        let data = null;

        const contentType = response.headers.get('content-type');
        if (contentType && contentType.includes('application/json')) {
            data = await response.json();
        }

        if (response.ok) {
            // return {data: data};
            return data;
        } else {
            TService.convertToExceptionIfNeeded(data);
        }
    }

    async saveRecord(url, data) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async modifyRecord(url, data) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async deleteRecord(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });
            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async deleteMultipleRecord(url, uuidList) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify({items: uuidList})
            });
            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async listAllRecord(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    async accessRecordByUuid(url) {
        try {

            // Communicate with server
            const response = await fetch(url, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }

    extendWithBaseUrl(path) {
        return backendConfiguration.BACKEND_BASE_URL + path;
    }

    extendWithHost(path) {
        return backendConfiguration.BACKEND_HOST + path;
    }

    async isImageExist(url) {

        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'HEAD',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include'
            });

            if (response.ok) {
                return true;
            }
            return false;
        } catch (exp) {
            return false;
        }
    }


    async sendPostRequest(url, data) {
        try {
            // Communicate with server
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'include',
                body: JSON.stringify(data)
            });

            return await TService.handleHttpResponse(response);
        } catch (exp) {
            if (exp instanceof TypeError) {
                throw new NetworkFailureException();
            }
            throw exp;
        }
    }


    getSessionIdFromCookies() {
        console.log('COOKIES');
        const cookies = document.cookie.split('; ');
        console.log(document.cookie);
        for (const cookie of cookies) {
            const [name, value] = cookie.split('=');

            console.log(name + ' == ' + value);

            // if (name === 'SESSION') {
            //     return value;
            // }
        }
        return null;
    }


}

// Exports
export default TService;
export {TService};