package ca.jrvs.apps.trading.service;

import ca.jrvs.apps.trading.dao.MarketDataDao;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
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

  //Dao below needs to be implemented
  private QuoteDao quoteDao;
  private MarketDataDao marketDataDao;

  @Autowired
  public QuoteService(QuoteDao quoteDao, MarketDataDao marketDataDao) {
    this.quoteDao = quoteDao;
    this.marketDataDao = marketDataDao;
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
