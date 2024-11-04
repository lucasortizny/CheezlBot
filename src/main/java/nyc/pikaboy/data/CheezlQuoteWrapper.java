package nyc.pikaboy.data;

import lombok.Data;

import java.util.List;

@Data
public class CheezlQuoteWrapper {
    public int count;
    public List<CheezlQuote> quoteList;
}
