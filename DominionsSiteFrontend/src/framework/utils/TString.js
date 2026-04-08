export default class TString {

    static removeDisallowedCharacters(input, allowedSet) {
        // Create a regex pattern that matches any character not in the allowed set
        // The `^` inside the character class `[^...]` means "not any of these characters"
        let pattern = new RegExp(`[^${allowedSet}]`, 'g');

        // Use the replace method to remove all disallowed characters
        return input.replace(pattern, '');
    }

    static removeMultiplicatedSpaces(input) {
        // Create a regex pattern that matches two or more consecutive spaces
        let pattern = /\s{2,}/g;

        // Replace all occurrences of the pattern with a single space
        return input.replace(pattern, ' ');
    }
}