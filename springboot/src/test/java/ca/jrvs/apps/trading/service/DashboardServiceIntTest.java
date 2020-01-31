package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.*;
import ca.jrvs.apps.trading.dao.*;
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
public class DashboardServiceIntTest {

  @Autowired
  private TraderDao traderDao;

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private SecurityOrderDao securityOrderDao;

  @Autowired
  private QuoteDao quoteDao;

  @Autowired
  private DashboardService dashboardService;

  private Trader trader;
  private Quote fQuote;
  private Account account;
  private SecurityOrder securityOrder;

  @Before
  public void init() {
    fQuote = new Quote();
    fQuote.setAskPrice(0.4d);
    fQuote.setAskSize(31L);
    fQuote.setBidPrice(0.23d);
    fQuote.setBidSize(93L);
    fQuote.setId("SHLDQ");
    fQuote.setLastPrice(0.38d);
    quoteDao.save(fQuote);

    trader = new Trader();
    trader.setId(1);
    trader.setFirst_name("FirstName");
    trader.setLast_name("LastName");
    trader.setCountry("Zoolandia");
    trader.setDob(Date.valueOf("2020-01-20"));
    trader.setEmail("Firstname@email.com");
    traderDao.save(trader);

    account = new Account();
    account.setId(1);
    account.setTrader_id(1);
    account.setAmount(2.1d);
    accountDao.save(account);

    securityOrder = new SecurityOrder();
    securityOrder.setId(1);
    securityOrder.setAccount_id(1);
    securityOrder.setSize(3);
    securityOrder.setTicker("SHLDQ");
    securityOrder.setPrice(fQuote.getAskPrice());
    securityOrder.setNotes("Bought 3 orders at: 1.2");
    securityOrder.setStatus("FILLED");
    securityOrderDao.save(securityOrder);
  }

  @Test
  public void getTraderAccountTest() {
    TraderAccountView traderAccountView = new TraderAccountView(trader, account);
    TraderAccountView returnedView = dashboardService.getTraderAccount(trader.getId());
    assertEquals(traderAccountView, returnedView);
  }

  @Test
  public void portfolioTest() {
    Position testPosition = new Position();
    testPosition.setId(1);
    testPosition.setTicker("SHLDQ");
    testPosition.setPosition(3);
    PortfolioView returnedView = dashboardService.getProfileViewByTraderId(trader.getId());
    for (SecurityRow securityRow : returnedView.getSecurityRows()) {
      assertEquals(securityRow.getPosition(), testPosition);
      assertEquals(securityRow.getQuote(), fQuote);
    }
  }

  @Test
  public void noSecurities() {
    List<SecurityRow> securityRowis = new ArrayList<SecurityRow>();
    PortfolioView desiredView = new PortfolioView(securityRowis);
    Trader newTrader = new Trader();
    newTrader.setId(2);
    newTrader.setFirst_name("newFname");
    newTrader.setLast_name("newLname");
    newTrader.setCountry("NewLand");
    newTrader.setDob(Date.valueOf("2020-01-20"));
    newTrader.setEmail("newName@email.com");
    traderDao.save(newTrader);

    Account newAccount = new Account();
    newAccount.setId(2);
    newAccount.setTrader_id(2);
    newAccount.setAmount(2.5d);
    accountDao.save(newAccount);

    PortfolioView portfolioView = dashboardService.getProfileViewByTraderId(newTrader.getId());
    assertEquals(desiredView, portfolioView);
  }

  @After
  public void cleanup() {
    securityOrderDao.deleteAll();
    quoteDao.deleteAll();
    accountDao.deleteAll();
    traderDao.deleteAll();
  }

}
