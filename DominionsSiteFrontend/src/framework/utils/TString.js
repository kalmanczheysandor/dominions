export default class TString {
    static removeDisallowedCharacters(input, allowedSet) {
        let pattern = new RegExp(`[^${allowedSet}]`, 'g');
        return input.replace(pattern, '');
    }
    static removeMultiplicatedSpaces(input) {
        let pattern = /\s{2,}/g;
        return input.replace(pattern, ' ');
    }
}