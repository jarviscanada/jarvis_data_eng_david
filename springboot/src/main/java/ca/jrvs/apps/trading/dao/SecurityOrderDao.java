package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.SecurityOrder;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class SecurityOrderDao extends JdbcCrudDao<SecurityOrder> {

  private static final Logger logger = LoggerFactory.getLogger(AccountDao.class);

  private final String TABLE_NAME = "security_order";
  private final String ID_COLUMN = "id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public SecurityOrderDao(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.simpleInsert = new SimpleJdbcInsert(dataSource).withTableName(TABLE_NAME)
        .usingGeneratedKeyColumns(ID_COLUMN);
  }
  @Override
  public JdbcTemplate getJdbcTemplate() { return this.jdbcTemplate; }

  @Override
  public SimpleJdbcInsert getSimpleJdbcInsert() { return this.simpleInsert; }

  @Override
  public String getTableName() { return this.TABLE_NAME; }

  @Override
  public String getIdColumnName() { return this.ID_COLUMN; }

  @Override
  public Class<SecurityOrder> getEntityClass() { return SecurityOrder.class; }

  public void deleteById(Integer id, boolean isAccountId) {
    String IDCol = null;
    if (isAccountId) {
      IDCol = new String("account_id");
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
  public int updateOne(SecurityOrder entity) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void delete(SecurityOrder securityOrder) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends SecurityOrder> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }
}
