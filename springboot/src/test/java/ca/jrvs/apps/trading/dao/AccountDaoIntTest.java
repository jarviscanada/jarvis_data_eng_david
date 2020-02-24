package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.Account;
import ca.jrvs.apps.trading.model.Trader;
import java.sql.Date;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestConfig.class})
@Sql({"classpath:schema.sql"})
public class AccountDaoIntTest {

  @Autowired
  private AccountDao accountDao;

  @Autowired
  private TraderDao traderDao;

  private Account savedAccount;
  private Trader savedTrader;
  private int trader_id;

  @Before
  public void init() {
    trader_id = 1;

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
  }

  @Test
  public void findAllbyId() {
    List<Account> accounts = Lists
        .newArrayList(accountDao.findAllById(Arrays.asList(savedAccount.getId())));
    List<Account> accounts2 = accountDao.findAll();
    assertEquals(accounts.get(0), accounts2.get(0));
    assertEquals(accountDao.count(), accounts.size());
  }

  @Test
  public void updateOne() {
    Account updateAccount = new Account();
    updateAccount.setId(1);
    updateAccount.setTrader_id(trader_id);
    updateAccount.setAmount(23.2);
    Account update = accountDao.save(updateAccount);

    assertEquals(update, updateAccount);
  }


  @After
  public void deleteAll() {
    accountDao.deleteAll();
    traderDao.deleteAll();
  }

}
