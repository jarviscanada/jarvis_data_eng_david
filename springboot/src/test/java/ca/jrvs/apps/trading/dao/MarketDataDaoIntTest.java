package ca.jrvs.apps.trading.dao;

import static org.junit.jupiter.api.Assertions.*;

import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.data.repository.CrudRepository;
import org.yaml.snakeyaml.error.Mark;

public class MarketDataDaoIntTest {

  MarketDataDao dao;

  @Before
  public void setUp() {
    PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    cm.setMaxTotal(50);
    cm.setDefaultMaxPerRoute(50);
    MarketDataConfig marketDataConfig = new MarketDataConfig();
    marketDataConfig.setHost("http://cloud.iexapis.com/v1");
    marketDataConfig.setToken(System.getenv("IEX_PUB_TOKEN"));

    dao = new MarketDataDao(cm, marketDataConfig);
  }

  @Test
  public void findAllById() {
    //happy path
    List<IexQuote> quoteList = dao.findAllById(Arrays.asList("AAPL", "FB"));
    assertEquals(2, quoteList.size());
    assertEquals("AAPL", quoteList.get(0).getSymbol());

    //sad path
    try {
      dao.findAllById(Arrays.asList("AAPL", "FB2"));
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }
  }

  @Test
  public void findId() {
    //happy path
    Optional<IexQuote> quoteList = dao.findById("FB");
    assertEquals("FB",quoteList.get().getSymbol());

    //sad path
    try {
      dao.findById("FB2");
      fail();
    } catch (IllegalArgumentException e) {
      assertTrue(true);
    } catch (Exception e) {
      fail();
    }

  }
}