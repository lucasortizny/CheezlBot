package nyc.pikaboy.data;


public class CheezlQuote {
    public String quoteKeyName;
    public String quote;

    public CheezlQuote(){}

    public CheezlQuote (String quoteKeyName, String quote){
        this.quote = quote;
        this.quoteKeyName = quoteKeyName;
    }

    public String getQuoteKeyName() {
        return quoteKeyName;
    }

    public void setQuoteKeyName(String quoteKeyName) {
        this.quoteKeyName = quoteKeyName;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}
