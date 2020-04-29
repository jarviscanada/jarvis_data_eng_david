package ca.jrvs.apps.trading.dao;

import static org.junit.Assert.assertEquals;

import ca.jrvs.apps.trading.TestConfig;
import ca.jrvs.apps.trading.model.Trader;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.util.Lists;
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
public class TraderDaoIntTest {

  @Autowired
  private TraderDao traderDao;

  private Trader savedTrader;

  @Before
  public void init() {
    savedTrader = new Trader();
    savedTrader.setId(1);
    savedTrader.setFirst_name("Firstname");
    savedTrader.setLast_name("Surname");
    savedTrader.setCountry("Country");
    savedTrader.setDob(Date.valueOf("2020-01-15"));
    savedTrader.setEmail("test@email.com");
    List<Trader> insert = new ArrayList<Trader>();
    insert.add(savedTrader);
    traderDao.saveAll(insert);
  }

  @Test
  public void findAllbyId() {
    List<Trader> traders = Lists
        .newArrayList(traderDao.findAllById(Arrays.asList(savedTrader.getId())));
    List<Trader> traders2 = traderDao.findAll();
    assertEquals(traders.get(0), traders2.get(0));
    assertEquals(traderDao.count(), traders.size());
    assertEquals(savedTrader.getCountry(), traders.get(0).getCountry());
  }

  @After
  public void deleteAll() {
    traderDao.deleteById(savedTrader.getId());
  }

}
