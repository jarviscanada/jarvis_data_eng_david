package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class PositionDao extends JdbcCrudDao<Position> {

  private static final Logger logger = LoggerFactory.getLogger(PositionDao.class);

  private final String TABLE_NAME = "position";
  private final String ID_COLUMN = "account_id";

  private JdbcTemplate jdbcTemplate;
  private SimpleJdbcInsert simpleInsert;

  @Autowired
  public PositionDao(DataSource dataSource) {
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
  public Class<Position> getEntityClass() { return Position.class; }


  public List<Position> findPosById(Integer id) {
    List<Position> entity = null;
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
    if (!existsById(id)) {
      return entity;
    }
    try {
      entity = (List<Position>) getJdbcTemplate()
          .query(selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()), id);
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find trader id: " + id, e);
    }
    if (entity == null) {
      logger.debug("Resource not found");
      throw new RuntimeException("query search returning null");
    }
    return entity;
  }

  @Override
  public int updateOne(Position entity) {
    throw new UnsupportedOperationException("Position table is VIEW ONLY");
  }

  @Override
  public void delete(Position position) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public void deleteAll(Iterable<? extends Position> iterable) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public Position save(Position position) {
    throw new UnsupportedOperationException("Position table is VIEW ONLY");
  }

  @Override
  public void deleteById(Integer id) {
    throw new UnsupportedOperationException("Position table is VIEW ONLY");
  }

  @Override
  public void deleteAll() {
    throw new UnsupportedOperationException("Position table is VIEW ONLY");
  }

}
