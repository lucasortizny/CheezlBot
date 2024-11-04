package nyc.pikaboy.service;

public class CheezlQuoteUtils {
    /**
     * Validate pair of Key and Quote Key.
     * @param key Key of the Quote
     * @param quote The quote itself
     * @return True if key and quote-key successfully passes validation.
     */
    public static boolean validateQuoteKeyPair(String key, String quote) {
        return (!((key.isBlank() || quote.isBlank()) && !(key.matches("[A-z]*"))));
    }
}
