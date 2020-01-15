package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.Quote;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class QuoteDao implements CrudRepository<Quote, String> {

  private static final String TABLE_NAME = "quote";
  private static final String ID_COLUMN_NAME = "ticker";

  private static final Logger logger = LoggerFactory.getLogger(QuoteDao.class);
  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleJdbcInsert;

  @Autowired
  public QuoteDao(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME);
  }

  @Override
  public <S extends Quote> S save(S quote) {
    if (quote == null) {
      throw new IllegalArgumentException("Cannot save null quote");
    }
    if (existsById(quote.getTicker())) {
      int updatedRowNo = updateOne(quote);
      //should only update 1 entry, as ticker should be unique in query
      if (updatedRowNo != 1) {
        throw new IncorrectResultSizeDataAccessException("Failed to Update", 1, updatedRowNo);
      }
    } else {
      addOne(quote);
    }
    return quote;
  }

  /**
   * Helper method that saves one quote
   */
  private void addOne(Quote quote) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(quote);
    int row = simpleJdbcInsert.execute(parameterSource);
    if (row != 1) {
      throw new IncorrectResultSizeDataAccessException("Failed to insert", 1, row);
    }
  }

  /**
   * Helper method that updates one quote
   */
  private int updateOne(Quote quote) {
    String update_sql = "UPDATE quote SET last_price=?, bid_price=?, "
        + "bid_size=?, ask_price=?, ask_size=? WHERE ticker=?";
    if (!existsById(quote.getTicker())) {
      throw new IllegalArgumentException("Ticker not found:" + quote.getTicker());
    }
    return jdbcTemplate.update(update_sql, makeUpdateValues(quote));
  }

  /**
   * Helper metehod that makes sql update values objects
   *
   * @param quote to be updated
   * @return UPDATE_SQL values
   */
  private Object[] makeUpdateValues(Quote quote) {
    int num = quote.getClass().getDeclaredFields().length;
    Object[] obj = new Object[num];
    obj[0] = quote.getLastPrice();
    obj[1] = quote.getBidPrice();
    obj[2] = quote.getBidSize();
    obj[3] = quote.getAskPrice();
    obj[4] = quote.getAskSize();
    obj[5] = quote.getTicker();
    return obj;
  }

  /**
   * Helper function to run all queries
   *
   * @param allQuotes list of Quotes to update/insert
   * @return outputQuotes list of Quotes run in table
   */
  @Override
  public <S extends Quote> Iterable<S> saveAll(Iterable<S> allQuotes) {
    List<Quote> outputQuotes = new ArrayList<Quote>();
    //run save for all quotes in list and append to array
    for (Quote q : allQuotes) {
      outputQuotes.add(save(q));
    }
    return (Iterable<S>) outputQuotes;
  }

  /**
   * Find a quote by ticker
   *
   * @param ticker name
   * @return quote or Option.empty if not found
   */
  @Override
  public Optional<Quote> findById(String ticker) {
    if (ticker == null) {
      throw new IllegalArgumentException("Cannot find null ticker");
    }
    Optional<Quote> findID = Optional.empty();
    String selectSql = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    //Advanced: handle read + update race condition
    Quote quote = null;
    try {
      quote = jdbcTemplate.queryForObject(
          selectSql,
          new Object[]{ticker},
          new RowMapper<Quote>() {
            public Quote mapRow(ResultSet rs, int rowNum) throws SQLException {
              Quote innerQuote = new Quote();
              innerQuote.setTicker(rs.getString("ticker"));
              innerQuote.setLastPrice(rs.getDouble("last_price"));
              innerQuote.setBidPrice(rs.getDouble("bid_price"));
              innerQuote.setBidSize(rs.getLong("bid_size"));
              innerQuote.setAskPrice(rs.getDouble("ask_price"));
              innerQuote.setAskSize(rs.getLong("ask_size"));
              return innerQuote;
            }
          });
    } catch (Exception e) {
      logger.debug("Can't find ticker: " + ticker, e);
    }

    if (quote == null) {
      logger.debug("Resource not found");
      throw new RuntimeException("query search returning null");
    }
    return Optional.of(quote);
  }

  /**
   * Check if ticker exists
   *
   * @param ticker id String
   * @return boolean True if exists, False if not
   */
  @Override
  public boolean existsById(String ticker) {
    if (ticker == null) {
      throw new IllegalArgumentException("null ticker cannot exist");
    }
    String selectSql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    long count = jdbcTemplate.queryForObject(selectSql, new Object[]{ticker}, Long.class);
    return count > 0;
  }

  /**
   * return all quotes
   */
  @Override
  public Iterable<Quote> findAll() {
    String selectSql = "SELECT * FROM " + TABLE_NAME;
    return this.jdbcTemplate.query(selectSql, new QuoteMapper());
  }

  private static final class QuoteMapper implements RowMapper<Quote> {

    public Quote mapRow(ResultSet rs, int rowNum) throws SQLException {
      Quote innerQuote = new Quote();
      innerQuote.setTicker(rs.getString("ticker"));
      innerQuote.setLastPrice(rs.getDouble("last_price"));
      innerQuote.setBidPrice(rs.getDouble("bid_price"));
      innerQuote.setBidSize(rs.getLong("bid_size"));
      innerQuote.setAskPrice(rs.getDouble("ask_price"));
      innerQuote.setAskSize(rs.getLong("ask_size"));
      return innerQuote;
    }
  }

  /**
   * Return count of all entries in table
   * @return count of type long of all entries in table
   */
  @Override
  public long count() {
    String selectSql =
        "SELECT COUNT(*) FROM " + TABLE_NAME ;
    return this.jdbcTemplate.queryForObject(selectSql, Long.class);
  }

  /**
   * Delete ticker entry
   * @param ticker id to delete
   */
  @Override
  public void deleteById(String ticker) {
    if (ticker == null) {
      throw new IllegalArgumentException("Cannot delete null ticker value");
    }
    String selectSql = "DELETE FROM " + TABLE_NAME + " WHERE " + ID_COLUMN_NAME + "=?";
    this.jdbcTemplate.update(selectSql, ticker);
  }

  /**
   * Delete all entries in TABLE_NAME
   */
  @Override
  public void deleteAll() {
    String selectSql = "DELETE FROM " + TABLE_NAME;
    this.jdbcTemplate.update(selectSql);
  }

  @Override
  public void delete(Quote quote) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Quote> iterable) {
    throw new UnsupportedOperationException("Not Implemented");
  }

  @Override
  public Iterable<Quote> findAllById(Iterable<String> iterable) {
    throw new UnsupportedOperationException("Not Implemented");
  }

}
