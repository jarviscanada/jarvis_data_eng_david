package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.QuoteDao;
import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.Quote;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteServiceIntTest {

  @Autowired
  private QuoteService quoteService;

  @Autowired
  private QuoteDao quoteDao;

  private Quote q1;

  @Before
  public void setup() {
    //saveQuote
    q1 = new Quote();
    q1.setTicker("AAPL");
    q1.setAskPrice(22.3d);
    q1.setAskSize(22L);
    q1.setBidPrice(44.6d);
    q1.setBidSize(44L);
    q1.setLastPrice(99.3d);
    quoteDao.save(q1);

    //saveQuotes
    Quote q2 = new Quote();
    q2.setTicker("GM");
    q2.setAskPrice(1.3d);
    q2.setAskSize(1L);
    q2.setBidPrice(1.6d);
    q2.setBidSize(1L);
    q2.setLastPrice(1.3d);
    Quote q3 = new Quote();
    q3.setTicker("T");
    q3.setAskPrice(12.3d);
    q3.setAskSize(12L);
    q3.setBidPrice(12.6d);
    q3.setBidSize(12L);
    q3.setLastPrice(12.3d);
    List<Quote> allQuotes = new ArrayList<Quote>();
    allQuotes.add(q2);
    allQuotes.add(q3);
    quoteDao.saveAll(allQuotes);
  }

  @Test
  public void findIexQuoteByTicker() {
    IexQuote iexQuote = quoteService.findIexQuoteByTicker("GM");
    assertNotNull(iexQuote);
    assertEquals("GM", iexQuote.getSymbol());
  }

  @Test
  public void updateMarketData() {
    quoteService.updateMarketData();
    Quote newAAPL = quoteDao.findById("AAPL").get();
    assertTrue(quoteDao.existsById("T"));
    assertTrue(quoteDao.existsById("AAPL"));
    assertTrue(quoteDao.existsById("GM"));
    assertNotEquals(q1,newAAPL);
  }


  @After
  public void deleteAll() {
    quoteDao.deleteAll();
  }

}
