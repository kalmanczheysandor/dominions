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

                // Admin-User
                if (data.code == 'AdminUserNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record!');
                } else if (data.code == 'AdminUserNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'AdminUserAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record association is restricted!');
                } else if (data.code == 'AdminUserReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record is still referenced elsewhere!');
                } else if (data.code == 'AdminUserSelfDeleteException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record not deletable in its account!');
                } else if (data.code == 'AdminUserConfirmPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The confirm password is not matching!');
                } else if (data.code == 'AdminUserEnabledButNotFinalisedException') {
                    throw new GeneralFailureException(data.code, 'The user is enabled but not finalised!');
                } else if (data.code == 'AdminUserIdentifierIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The identifier "' + data.parameters.identifier + '" is already in use!');
                }


                // Admin-Profile
                if (data.code == 'AdminProfileNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the settings record!');
                } else if (data.code == 'AdminProfileNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the settings record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'AdminProfileAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" settings record association is restricted!');
                } else if (data.code == 'AdminProfileReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" settings record is still referenced elsewhere!');
                } else if (data.code == 'AdminProfileSelfDeleteException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" settings record not deletable in its account!');
                } else if (data.code == 'AdminProfileConfirmPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The confirm password is not matching!');
                } else if (data.code == 'AdminProfileCurrentPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The given password is not matching with the current one!');
                } else if (data.code == 'AdminProfileIdentifierIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The identifier "' + data.parameters.identifier + '" is already in use!');
                }


                // Admin-PermissionGroup
                if (data.code == 'AdminPermissionGroupNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the permission-group record!');
                } else if (data.code == 'AdminPermissionGroupNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the permission-group record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'AdminPermissionGroupAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" permission-group record association is restricted!');
                } else if (data.code == 'AdminPermissionGroupReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" permission-group record is still referenced elsewhere!');
                } else if (data.code == 'AdminPermissionGroupNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                }


                // Site-User
                if (data.code == 'SiteUserNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record!');
                } else if (data.code == 'SiteUserNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the user record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'SiteUserAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record association is restricted!');
                } else if (data.code == 'SiteUserReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record is still referenced elsewhere!');
                } else if (data.code == 'SiteUserSelfDeleteException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.identifier + '" user record not deletable in its account!');
                } else if (data.code == 'SiteUserConfirmPasswordMismatchException') {
                    throw new GeneralFailureException(data.code, 'The confirm password is not matching!');
                } else if (data.code == 'SiteUserEnabledButNotFinalisedException') {
                    throw new GeneralFailureException(data.code, 'The user is enabled but not finalised!');
                } else if (data.code == 'SiteUserIdentifierIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The identifier "' + data.parameters.identifier + '" is already in use!');
                }

                // Site-PermissionGroup
                if (data.code == 'SitePermissionGroupNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the permission-group record!');
                } else if (data.code == 'SitePermissionGroupNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the permission-group record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'SitePermissionGroupAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" permission-group record association is restricted!');
                } else if (data.code == 'SitePermissionGroupReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" permission-group record is still referenced elsewhere!');
                } else if (data.code == 'SitePermissionGroupNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                }


                // GameScenario
                if (data.code == 'GameScenarioNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the game-scenario record!');
                } else if (data.code == 'GameScenarioNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the game-scenario record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'GameScenarioAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.title + '" game-scenario record association is restricted!');
                } else if (data.code == 'GameScenarioReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.title + '" game-scenario record is still referenced elsewhere!');
                } else if (data.code == 'GameScenarioTitleIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The title "' + data.parameters.title + '" is already in use!');
                }


                // HugoCharacter
                if (data.code == 'HugoCharacterNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the character record!');
                } else if (data.code == 'HugoCharacterNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the character record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'HugoCharacterAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" character record association is restricted!');
                } else if (data.code == 'HugoCharacterReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" character record is still referenced elsewhere!');
                } else if (data.code == 'HugoCharacterNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                } else if (data.code == 'HugoCharacterCodeIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The code "' + data.parameters.code + '" is already in use!');
                }

                // HugoVariant
                if (data.code == 'HugoVariantNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the variant record!');
                } else if (data.code == 'HugoVariantNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the variant record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'HugoVariantAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" variant record association is restricted!');
                } else if (data.code == 'HugoVariantReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" variant record is still referenced elsewhere!');
                } else if (data.code == 'HugoVariantIsNotAChildOfThisParentException') {
                    throw new GeneralFailureException(data.code, 'The variant record is not a child record of referenced parent!');
                } else if (data.code == 'HugoVariantNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                }


                // HugoHeuristic
                if (data.code == 'HugoHeuristicNotFoundByCodeException.java') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the heuristic record!');
                } else if (data.code == 'HugoHeuristicNotFoundByUuidException.java') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the heuristic record by uuid:' + data.parameters.uuid);
                }

                // LizCharacter
                if (data.code == 'LizCharacterNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the character record!');
                } else if (data.code == 'LizCharacterNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the character record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'LizCharacterAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" character record association is restricted!');
                } else if (data.code == 'LizCharacterReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" character record is still referenced elsewhere!');
                } else if (data.code == 'LizCharacterNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                } else if (data.code == 'LizCharacterCodeIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The code "' + data.parameters.code + '" is already in use!');
                }

                // LizVariant
                if (data.code == 'LizVariantNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the variant record!');
                } else if (data.code == 'LizVariantNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the variant record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'LizVariantAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" variant record association is restricted!');
                } else if (data.code == 'LizVariantReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" variant record is still referenced elsewhere!');
                } else if (data.code == 'LizVariantIsNotAChildOfThisParentException') {
                    throw new GeneralFailureException(data.code, 'The variant record is not a child record of referenced parent!');
                } else if (data.code == 'LizVariantNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                }


                // LizNeuralConcept
                if (data.code == 'LizNeuralConceptNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the concept record!');
                } else if (data.code == 'LizNeuralConceptNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the concept record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'LizNeuralConceptAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" concept record association is restricted!');
                } else if (data.code == 'LizNeuralConceptReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.name + '" concept record is still referenced elsewhere!');
                } else if (data.code == 'LizNeuralConceptIsNotAChildOfThisParentException') {
                    throw new GeneralFailureException(data.code, 'The concept record is not a child record of referenced parent!');
                } else if (data.code == 'LizNeuralConceptNameIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The name "' + data.parameters.name + '" is already in use!');
                } else if (data.code == 'LizNeuralConceptHistoryPlayerNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the history-player for concept record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'LizNeuralConceptHistoryScenarioNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the history-scenario for concept record by uuid:' + data.parameters.uuid);
                }else if (data.code == 'LizNeuralConceptDirectoryDeletionFailedException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t delete file structure of concept');
                }else if (data.code == 'LizNeuralConceptDeletionBlockedByActiveExecutionException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t delete concept while there is any unfinished execution!');
                }


                // LizVariantTraining
                if (data.code == 'LizVariantTrainingNotFoundException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the liz-variant-training record!');
                } else if (data.code == 'LizVariantTrainingNotFoundByUuidException') {
                    throw new GeneralFailureException(data.code, 'Couldn\'t find the liz-variant-training record by uuid:' + data.parameters.uuid);
                } else if (data.code == 'LizVariantTrainingAssociationRestrictedException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.title + '" liz-variant-training record association is restricted!');
                } else if (data.code == 'LizVariantTrainingReferencedElsewhereException') {
                    throw new GeneralFailureException(data.code, 'The "' + data.parameters.title + '" liz-variant-training record is still referenced elsewhere!');
                } else if (data.code == 'LizVariantTrainingIsNotAChildOfThisParentException') {
                    throw new GeneralFailureException(data.code, 'The  liz-variant-training record is not a child record of referenced parent!');
                } else if (data.code == 'LizVariantTrainingTitleIsReservedException') {
                    throw new GeneralFailureException(data.code, 'The title "' + data.parameters.title + '" is already in use!');
                }

                throw new GeneralErrorException('??[GeneralFailureResponse]:' + data.code + '??');

            } else if (data.type == 'SecurityFailureResponse') {

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
                } else if (data.code == 'NoResourceFoundException') {
                    throw new SecurityFailureException(data.code, 'The requested endpoint does not exist!');
                } else if (data.code == 'SessionExpiredException') {
                    throw new SecurityFailureException(data.code, 'The Session is expired!');
                } else if (data.code == 'UserIsNotAuthenticatedException') {
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
            return {data: data};
            //return data;
        } else {
            console.log(response);
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
            // return await TService.handleHttpResponse(response);
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

}

// Exports
export default TService;
export {
    TService
};