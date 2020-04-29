package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.*;
import java.sql.Date;
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
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class SecurityOrderIntTest {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private QuoteDao quoteDao;

  private Account savedAccount;
  private Trader savedTrader;
  private SecurityOrder securityOrder;
  private Quote savedQuote;

  private int trader_id;
  private int account_id;
  private String ticker = "ibm";

  @Before
  public void init() {
    trader_id = 1;
    account_id = 1;

    savedQuote = new Quote();
    savedQuote.setAskPrice(10d);
    savedQuote.setAskSize(10L);
    savedQuote.setBidPrice(10.2d);
    savedQuote.setBidSize(10L);
    savedQuote.setId(ticker);
    savedQuote.setLastPrice(10.1d);
    quoteDao.save(savedQuote);

    savedTrader = new Trader();
    savedTrader.setFirst_name("fname" + trader_id);
    savedTrader.setLast_name("sname" + trader_id);
    savedTrader.setCountry("Country1" + trader_id);
    savedTrader.setDob(Date.valueOf("2021-01-15"));
    savedTrader.setEmail("test2@email.com");
    List<Trader> insert = new ArrayList<Trader>();
    insert.add(savedTrader);
    traderDao.saveAll(insert);

    savedAccount = new Account();
    savedAccount.setTrader_id(trader_id);
    savedAccount.setAmount(0.0);
    List<Account> insertAcc = new ArrayList<Account>();
    insertAcc.add(savedAccount);
    accountDao.saveAll(insertAcc);

    securityOrder = new SecurityOrder();
    securityOrder.setId(1);
    securityOrder.setAccount_id(account_id);
    securityOrder.setNotes("notes");
    securityOrder.setPrice(23.2d);
    securityOrder.setSize(2);
    securityOrder.setStatus("testing");
    securityOrder.setTicker(ticker);
    securityOrderDao.save(securityOrder);
  }

  @Test
  public void testFind() {
    List<SecurityOrder> allOrders = securityOrderDao.findAll();
    assertEquals(allOrders.get(0), securityOrder);
  }

  @After
  public void deleteAll() {
    securityOrderDao.deleteAll();
    quoteDao.deleteAll();
    accountDao.deleteAll();
    traderDao.deleteAll();
  }

}
