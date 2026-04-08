export const RequiredFieldRule = (value) => {
    if(!value) return 'Field is required!';
    return true;
};

export const EmailFieldRule = (value) => {
    if(value==null){
        value=""
    }

    const emailRegex = /.+@.+\..+/;
    if(!emailRegex.test(value)) {
        return 'Field must be in e-mail format';
    }
    return true;
};

export const MinLengthFieldRule = (min) => (value) => {
    if(value==null){
        value=""
    }

    if(value.length < min) {
        return 'Field must be not shorter than ' + min + '!';
    }
    return true;
};

export const MaxLengthFieldRule = (max) => (value) => {
    if(value==null){
        value=""
    }
    if(value.length > max) {
        return 'Field must not be longer than ' + max + '!';
    }
    return true;
};

export const NumberFieldRule = (value) => {
    if(isNaN(value)) {
        return 'Field must be in number format!';
    }
    return true;
};

export const UrlFieldRule = (value) => {
    if(value==null){
        value=""
    }
    const urlRegex = /^(https?:\/\/[^\s]+)$/;
    if(!urlRegex.test(value)) {
        return 'Value is not a valid url!';
    }
    return true;
};

export const PasswordFieldRule = (value) => {
    const max = 20;
    const min = 8;


    if(value==null){
        value=""
    }

    if(value.length > max) {
        return 'Field must not be longer than ' + max + '!';
    }

    if(value.length < min) {
        return 'Field must be not shorter than ' + min + '!';
    }

    return true;
};

export const OptionalPasswordFieldRule = (value) => {
    const max = 20;
    const min = 8;

    if(value === null) {
        value = '';
    }

    if(value.length > 0) {
        if(value.length > max) {
            return 'Field must not be longer than ' + max + '!';
        }

        if(value.length < min) {
            return 'Field must be not shorter than ' + min + '!';
        }
    }
    return true;
};


export const TextFieldRule = (value) => {
    const max = 150;
    const min = 0;

    if(value.length > max) {
        return 'Field must not be longer than ' + max + '!';
    }

    if(value.length < min) {
        return 'Field must not shorter than ' + min + '!';
    }

    return true;
};

export const globalRules = {
    RequiredFieldRule,
    EmailFieldRule,
    NumberFieldRule,
    TextFieldRule,
    PasswordFieldRule,
    MinLengthFieldRule,
    MaxLengthFieldRule
};