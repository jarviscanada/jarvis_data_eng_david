package ca.jrvs.apps.trading.service;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.dao.*;
import ca.jrvs.apps.trading.model.*;
import ca.jrvs.apps.trading.model.TraderAccountView;
import java.sql.Date;
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
public class TraderAccountServiceIntTest {

  private TraderAccountView savedView;
  @Autowired
  private TraderAccountService traderAccountService;
  @Autowired
  private TraderDao traderDao;
  @Autowired
  private AccountDao accountDao;

  private Trader savedTrader;
  private Account savedAccount;
  private SecurityOrder savedSecurityOrder;
  private TraderAccountView traderAccountView;

  @Before
  public void init() {
    //test create, deposit and withdraw and delete test
    savedTrader = new Trader();
    savedTrader.setFirst_name("Firstname");
    savedTrader.setLast_name("Surname");
    savedTrader.setCountry("Country");
    savedTrader.setDob(Date.valueOf("2020-01-15"));
    savedTrader.setEmail("test@email.com");

    traderAccountView = traderAccountService.createTraderAndAccount(savedTrader);
  }

  @Test
  public void testCreation() {
    assertEquals(savedTrader, traderAccountView.getTrader());
    assertEquals(new Double(0.0), traderAccountView.getAccount().getAmount());
  }

  @Test
  public void transaction() {
    Integer traderId = traderAccountView.getTrader().getId();
    Account account = traderAccountService.deposit(traderId,2.3d);
    assertEquals(account.getAmount(), new Double(2.3));

    Account withDraw1 = traderAccountService.withdraw(traderId, 0.3d);
    assertEquals(withDraw1.getAmount(), new Double(2.0d));
    Account withDraw2 = traderAccountService.withdraw(traderId, 2.0d);
    assertEquals(withDraw2.getAmount(), new Double(0.0));
  }

  @After
  public void delete() {
    //do nothing
    traderAccountService.deleteTraderById(savedTrader.getId());
  }

}
