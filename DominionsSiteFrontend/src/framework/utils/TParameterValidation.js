import ParameterErrorException from "@/framework/exception/error/ParameterErrorException";

export default class TParameterValidation {

    static assertParameterIsNotNull(value,parameterName, isStrict=false) {

        if(isStrict) {
            if(value===null) {
                throw new ParameterErrorException('The "'+parameterName+'" must not be null!')
            }
        }
        else {
            if(value===null) {
                throw new ParameterErrorException('The "'+parameterName+'" must not be null!')
            }
            if(value===undefined) {
                throw new ParameterErrorException('The "'+parameterName+'" must not be undefined!')
            }
        }


    }

    static assertParameterIsStringType(value,parameterName) {
        if (typeof value !== "string") {
            throw new ParameterErrorException('The "'+parameterName+'" type must be string!')
        }
    }

    static assertParameterIsNotAnEmptyString(value,parameterName) {
        TParameterValidation.assertParameterIsStringType(value,parameterName);
        if (value.length==0) {
            throw new ParameterErrorException('The "'+parameterName+'" must not be an empty string!')
        }
    }

    static assertParameterIsNotABlankString(value,parameterName) {
        TParameterValidation.assertParameterIsNotNull(value,parameterName);
        TParameterValidation.assertParameterIsStringType(value,parameterName);
        if (value.trim().length==0) {
            throw new ParameterErrorException('The "'+parameterName+'" must not be a blank string!')
        }
    }

    static assertParameterIsIntegerType(value,parameterName) {
        if (!Number.isInteger(value)) {
            throw new ParameterErrorException('The "'+parameterName+'" type must be integer!')
        }
    }


    static assertParameterIsACallbackFunction(value,parameterName) {
        if (!(value && typeof value === 'function')) {
            throw new ParameterErrorException('The "'+parameterName+'" type must be a callback function!')
        }
    }


}