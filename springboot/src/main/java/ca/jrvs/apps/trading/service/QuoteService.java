package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class QuoteService {

  public static final Logger logger = LoggerFactory.getLogger(QuoteService.class);

  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
  }

  /**
   * Update quote table against IEX source
   *  - get all quotes from the db
   *  - foreach ticker get iexQuote
   *  - convert iexQuote to quote entity
   *  - persist quote to db
   *
   * @return saved quotes
   */
  public List<Quote> updateMarketData() {
    List<Quote> allQuotes = (List<Quote>) quoteDao.findAll();
    List<Quote> savedQuotes = new ArrayList<Quote>();
    for (Quote quote : allQuotes) {
      IexQuote iexQuote = findIexQuoteByTicker(quote.getTicker());
      Quote updatedQuote = buildQuoteFromIexQuote(iexQuote);
      savedQuotes.add(saveQuote(updatedQuote));
    }
    return savedQuotes;
  }

  /**
   * Validate (against IEX) and save given tickers to quote table.
   *
   *  - Get iexQuote(s)
   *  - convert each iexQuote to Quote entity
   *  - persist the quote to db
   * @param tickers a list of tickers/symbols
   * @return IllegalArgumentException if ticker is not found from IEX
   */
  public List<Quote> saveQuotes(List<String> tickers) {
    List<Quote> allQuotes = new ArrayList<Quote>();
    for (String ticker: tickers) {
      allQuotes.add(saveQuote(ticker));
    }
    return allQuotes;
  }

  /**
   * Helper method
   */
  public Quote saveQuote(String ticker) {
    IexQuote iexQuote = findIexQuoteByTicker(ticker);
    Quote quote = buildQuoteFromIexQuote(iexQuote);
    return saveQuote(quote);
  }

  /**
   * Update a given quote to quote table without validation
   * @param quote entity
   */
  public Quote saveQuote(Quote quote) { return quoteDao.save(quote); }

  /**
   * Find all quotes from the quote table
   * @return a list of quotes
   */
  public List<Quote> findAllQuotes() { return (List<Quote>) quoteDao.findAll(); }

  /**
   * Helper method. Map an IexQuote to a Quote entity.
   * Note: `iexQuote.getLatestPrice() == null` if the stock market is closed
   * Make sure set a default value for number field(s).
   * @param iexQuote from REST
   * @return Quote to persist in database
   */
  protected static Quote buildQuoteFromIexQuote(IexQuote iexQuote) {
    Quote newQuote = new Quote();
    newQuote.setTicker(iexQuote.getSymbol());
    newQuote.setAskPrice(iexQuote.getIexAskPrice());
    newQuote.setAskSize(iexQuote.getIexAskSize());
    newQuote.setBidPrice(iexQuote.getIexBidPrice());
    newQuote.setBidSize(iexQuote.getIexBidSize());
    newQuote.setLastPrice(iexQuote.getLatestPrice());
    return newQuote;
  }

  /**
   * Returns IexQuote corresponding to ticker
   *
   * @param ticker String
   * @return IexQuote
   * @throws IllegalArgumentException if ticker is invalid
   */
  public IexQuote findIexQuoteByTicker(String ticker) {
    return marketDataDao.findById(ticker)
        .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
  }

  /**
   * Returns IexQuotes List corresponding to tickers List
   *
   * @param tickers List
   * @return IexQuote List
   * @throws IllegalArgumentException to corresponding invalid ticker
   */
  public List<IexQuote> findAllIexQuoteByTickers(List<String> tickers) {
    List<IexQuote> allIex = new ArrayList<IexQuote>();
    for (String ticker : tickers) {
      IexQuote temp = marketDataDao.findById(ticker)
          .orElseThrow(() -> new IllegalArgumentException(ticker + " is invalid"));
      allIex.add(temp);
    }
    return allIex;
  }

}
