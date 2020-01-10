package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.IexQuote;
import ca.jrvs.apps.trading.model.config.MarketDataConfig;
import ca.jrvs.apps.trading.util.JsonUtil;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * MarketDataDao is resposible for getting Quotes from IEX
 */
@Repository
public class MarketDataDao implements CrudRepository<IexQuote, String> {

  private static final String IEX_BATCH_PATH = "/stock/market/batch?symbols=%s&types=quote&token=";
  private String IEX_BATCH_URL;

  private Logger logger = LoggerFactory.getLogger(MarketDataDao.class);
  private HttpClientConnectionManager httpClientConnectionManager;

  @Autowired
  public MarketDataDao(HttpClientConnectionManager httpClientConnectionManager,
      MarketDataConfig marketDataConfig) {
    this.httpClientConnectionManager = httpClientConnectionManager;
    IEX_BATCH_URL = marketDataConfig.getHost() + IEX_BATCH_PATH + marketDataConfig.getToken();
  }

  /**
   * Execute a get and return http entity/body as a string
   * <p>
   * Tip: use EntityUtils.toString to process HTTP entity
   *
   * @param url resource URL
   * @return http response boy or Optional.empty for 404 response
   * @throws DataRetrievalFailureException if HTTP failed or status code is unexpected
   */
  private Optional<String> executeHttpGet(String url) {
    HttpResponse httpResponse = null;
    String response = null;
    try {
      HttpGet httpGet = new HttpGet(URI.create(url));
      httpResponse = getHttpClient().execute(httpGet);
    } catch (IOException e) {
      logger.error("GET request execution failed: " + e);
      throw new DataRetrievalFailureException("HTTP GET request failed");
    }

    //check if httpResonse is valid
    int statusCode = httpResponse.getStatusLine().getStatusCode();
    if (statusCode != 200) {
      logger.error("HTTP GET Response has issue code: " + statusCode);
      throw new DataRetrievalFailureException("HTTP GET Response has issue code: " + statusCode);
    }

    try {
      response = EntityUtils.toString(httpResponse.getEntity());
    } catch (IOException e) {
      logger.error("Error converting HttpResponse to String");
      throw new DataRetrievalFailureException(
          "Conversion from HttpResponse to String failed: " + e);
    }
    return Optional.of(response);
  }

  /**
   * Borrow a HTTP client from the httpClientConnectionManager
   *
   * @return a httpClient
   */
  private CloseableHttpClient getHttpClient() {
    return HttpClients.custom()
        .setConnectionManager(httpClientConnectionManager)
        .setConnectionManagerShared(true)
        .build();
  }

  /**
   * Get an IexQuote (helper method which class findAllById)
   *
   * @param ticker
   * @return List of IexQuotes
   * @throws IllegalArgumentException      if a given ticket is invalid
   * @throws DataRetrievalFailureException if HTTP request failed
   */
  @Override
  public Optional<IexQuote> findById(String ticker) {
    Optional<IexQuote> iexQuote;
    List<IexQuote> quotes = findAllById(Collections.singletonList(ticker));

    if (quotes.size() == 0) {
      return Optional.empty();
    } else if (quotes.size() == 1) {
      iexQuote = Optional.of(quotes.get(0));
    } else {
      throw new DataRetrievalFailureException("Unexpected number of quotes");
    }
    return iexQuote;
  }

  /**
   * Return IEX data corresponding to each of the tickers in list
   *
   * @param tickers is List of tickers
   * @return List of IexQuote corresponding to ticker
   */
  @Override
  public List<IexQuote> findAllById(Iterable<String> tickers) {
    String tickCommaSeparate = String.join(",", tickers);
    String url = String.format(IEX_BATCH_URL, tickCommaSeparate);

    String json = executeHttpGet(url).get();
    List<IexQuote> jsonList = null;
    try {
      jsonList = JsonUtil
          .parseBatchJson(JsonUtil.generateJsonObject(json), IexQuote.class, "quote");
    } catch (IOException e) {
      logger.info("Error converting Json Object to Json String: " + e);
    }
    return jsonList;
  }

  @Override
  public boolean existsById(String s) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public Iterable<IexQuote> findAll() {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public long count() {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public void deleteById(String s) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public void delete(IexQuote iexQuote) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends IexQuote> iterable) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public <S extends IexQuote> S save(S s) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public <S extends IexQuote> Iterable<S> saveAll(Iterable<S> iterable) {
    throw new UnsupportedOperationException("Not Implemented");
  }

}
