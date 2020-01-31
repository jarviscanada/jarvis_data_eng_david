package ca.jrvs.apps.trading.dao;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.Quote;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import sun.nio.ch.LinuxAsynchronousChannelProvider;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class QuoteDaoIntTest {

  @Autowired
  private QuoteDao quoteDao;

  private Quote savedQuote;

  @Before
  public void insertOne() {
    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10L);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10L);
    savedQuote.setId("aapl");
    savedQuote.setLastPrice(10.1d);
  }

  @Test
  public void saveQuote() {
    Quote returnedQuote = quoteDao.save(savedQuote);
    assertEquals(returnedQuote.getTicker(), savedQuote.getTicker());
    assertEquals(returnedQuote.getBidPrice(), savedQuote.getBidPrice());
  }

  @Test
  public void existTest() {
    saveQuote();
    assertTrue(quoteDao.existsById("aapl"));
  }

  @Test
  public void updateQuote() {
    saveQuote();
    Quote updateQuote = new Quote();
    updateQuote.setAskPrice(2d);
    updateQuote.setAskSize(2L);
    updateQuote.setBidPrice(2.2d);
    updateQuote.setBidSize(2L);
    updateQuote.setId("aapl");
    updateQuote.setLastPrice(2.2d);
    Quote returnedQuote = quoteDao.save(updateQuote);
    assertEquals(returnedQuote.getTicker(), updateQuote.getTicker());
    assertEquals(returnedQuote.getBidPrice(), updateQuote.getBidPrice());
  }

  @Test
  public void testSaveAll() {
    updateQuote();
    Quote quote1 = new Quote();
    quote1.setAskPrice(30d);
    quote1.setAskSize(30L);
    quote1.setBidPrice(30.2d);
    quote1.setBidSize(30L);
    quote1.setId("t");
    quote1.setLastPrice(30.01d);
    Quote quote2 = new Quote();
    quote2.setAskPrice(1d);
    quote2.setAskSize(1L);
    quote2.setBidPrice(1.2d);
    quote2.setBidSize(1L);
    quote2.setId("ibm");
    quote2.setLastPrice(1.01d);
    List<Quote> quoteOutput = (List<Quote>) quoteDao.saveAll(Arrays.asList(quote1,quote2));
    assertEquals(quoteOutput.get(0).getTicker(),quote1.getTicker());
    assertEquals(quoteOutput.get(1).getTicker(),quote2.getTicker());
  }

  @Test
  public void countTest() {
    testSaveAll();
    //should have 3 entries after testSaveAll
    assertEquals(3,quoteDao.count());
  }

  @Test
  public void testFind() {
    testSaveAll();
    Long l = new Long(1);
    //FindById
    Quote returnedQuote = quoteDao.findById("ibm").get();
    assertEquals(new Double(1d), returnedQuote.getAskPrice());
    assertEquals(l, returnedQuote.getAskSize());
    assertEquals(new Double(1.2d), returnedQuote.getBidPrice());
    assertEquals(l, returnedQuote.getBidSize());
    assertEquals(new Double(1.01d), returnedQuote.getLastPrice());

    //FindAllById
    List<String> names = new ArrayList<String>();
    List<String> returnedNames = new ArrayList<String>();
    names.add("ibm");
    names.add("aapl");
    names.add("t");
    List<Quote> allQuotes = (List<Quote>) quoteDao.findAll();
    for (Quote q: allQuotes) {
      returnedNames.add(q.getId());
    }
    assertEquals(3, quoteDao.count()); //assert 3 returned
    assertThat(names, containsInAnyOrder(returnedNames.toArray()));
  }

  @Test
  public void deleteById() {
    testSaveAll();

    quoteDao.deleteById("ibm");

    //FindAllById
    List<String> names = new ArrayList<String>();
    List<String> returnedNames = new ArrayList<String>();
    names.add("aapl");
    names.add("t");
    List<Quote> allQuotes = (List<Quote>) quoteDao.findAll();
    for (Quote q: allQuotes) {
      returnedNames.add(q.getId());
    }
    assertEquals(2, quoteDao.count()); //assert 3 returned
    assertThat(names, containsInAnyOrder(returnedNames.toArray()));
  }


  @After
  public void deleteAll() {
    quoteDao.deleteAll();
    //nothing remains after deleteAll
    assertEquals(0,quoteDao.count());
  }

}
