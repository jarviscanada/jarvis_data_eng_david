package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class AccountDao extends JdbcCrudDao<Account> {

  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

  private final String TABLE_NAME = "account";
  private final String ID_COLUMN = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public AccountDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }

  @Override
  public JdbcTemplate getJdbcTemplate() { return jdbcTemplate; }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() { return simpleInsert; }

  @Override
  public String getTableName() { return TABLE_NAME; }

  @Override
  public String getIdColumnName() { return ID_COLUMN; }

  @Override
  public Class<Account> getEntityClass() { return Account.class; }


  /**
   * Searches Account Database by traderId, returns tuples of all accounts that match trader
   * @param id, could be accountId or traderId
   * @param isTraderId boolean to indicate whether traderId
   * @return List of Accounts that match trader
   */
  public List<Account> findById(Integer id, boolean isTraderId) {
    String IDCol = null;
    if (isTraderId) {
      IDCol = new String("trader_id");
    } else {
      IDCol = getIdColumnName();
    }

    List<Account> entity = new ArrayList<Account>();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + IDCol + " =?";
    if (!existsById(id)) {
      throw new IllegalArgumentException("Id: " + id + " does not exist in database");
    }
    try {
      entity = (List<Account>) getJdbcTemplate()
          .query(selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()), id);
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find id: " + id, e);
    }
    if (entity == null) {
      logger.debug("Resource not found");
      throw new RuntimeException("query search returning null");
    }
    return entity;
  }

  public Optional<Account> findAccountByTraderId(Integer traderId) {
    Optional<Account> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE trader_id=?";
    if (!existsById(traderId)) {
      throw new IllegalArgumentException("traderId: " + traderId + " does not exist in database");
    }
    try {
      entity = Optional.ofNullable((Account) getJdbcTemplate()
          .queryForObject(selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()), traderId));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find traderId: " + traderId, e);
    }
    if (entity == null) {
      logger.debug("Resource not found");
      throw new RuntimeException("query search returning null");
    }
    return entity;
  }


  public void deleteById(Integer id, boolean isTraderId) {
    String IDCol = null;
    if (isTraderId) {
      IDCol = new String("trader_id");
    } else {
      IDCol = getIdColumnName();
    }

    if (id == null) {
      throw new IllegalArgumentException("Cannot delete null id value");
    }
    String selectSql = "DELETE FROM " + getTableName() + " WHERE " + IDCol + "=?";
    getJdbcTemplate().update(selectSql, id);
  }

  @Override
  public int updateOne(Account account) {
    String update_sql = "UPDATE " + getTableName() + " SET trader_id=?, amount=? "
        + "WHERE " + getIdColumnName() + "=?";
    if (!existsById(account.getId())) {
      throw new IllegalArgumentException("ID not found:" + account.getId());
    }
    return jdbcTemplate.update(update_sql, makeUpdateValues(account));
  }

  /**
   * Helper method that makes sql update values objects
   *
   * @param account to be updated
   * @return UPDATE_SQL values
   */
  private Object[] makeUpdateValues(Account account) {
    int num = account.getClass().getDeclaredFields().length;
    Object[] obj = new Object[num];
    obj[0] = account.getTrader_id();
    obj[1] = account.getAmount();
    obj[2] = account.getId();
    return obj;
  }

  @Override
  public void delete(Account account) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Account> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
