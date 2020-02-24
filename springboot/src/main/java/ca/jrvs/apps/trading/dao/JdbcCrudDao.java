package ca.jrvs.apps.trading.dao;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

public abstract class JdbcCrudDao <T extends Entity<Integer>> implements CrudRepository<T, Integer> {

  private static final Logger logger = LoggerFactory.getLogger(JdbcCrudDao.class);

  abstract  public JdbcTemplate getJdbcTemplate();

  abstract public SimpleJdbcInsert getSimpleJdbcInsert();

  abstract public String getTableName();

  abstract public String getIdColumnName();

  abstract public Class<T> getEntityClass();

  /**
   * Helper method that updates one quote
   */
  abstract public int updateOne(T entity);

  /**
   * Helper method that saves one quote
   */
  private <S extends T> void addOne(S entity) {
    SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);

    Number newId = getSimpleJdbcInsert().executeAndReturnKey(parameterSource);
    entity.setId(newId.intValue());
  }

  /**
   * Save an entity and update auto-generated integer ID
   * @param entity to be saved
   * @return save entity
   */
  @Override
  public <S extends T> S save(S entity) {
    if (existsById(entity.getId())) {
      if (updateOne(entity) != 1) {
        throw new DataRetrievalFailureException("Unable to update quote");
      }
    } else {
      addOne(entity);
    }
    return entity;
  }

  @Override
  public <S extends T> List<S> saveAll(Iterable<S> iterable) {
    List<S> allSave = new ArrayList<S>();
    for (S iter: iterable) {
      allSave.add(save(iter));
    }
    return allSave;
  }

  @Override
  public Optional<T> findById(Integer id) {
    Optional<T> entity = Optional.empty();
    String selectSql = "SELECT * FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
    if (!existsById(id)) {
      return entity;
    }
    try {
      entity = Optional.ofNullable((T) getJdbcTemplate()
          .queryForObject(selectSql,
              BeanPropertyRowMapper.newInstance(getEntityClass()), id));
    } catch (IncorrectResultSizeDataAccessException e) {
      logger.debug("Can't find trader id: " + id, e);
    }
    if (entity.get() == null) {
      logger.debug("Resource not found");
      throw new RuntimeException("query search returning null");
    }
    return entity;
  }

  @Override
  public boolean existsById(Integer id) {
    String selectSql = "SELECT COUNT(*) FROM " + getTableName() + " WHERE " + getIdColumnName() + " =?";
    long count = getJdbcTemplate().queryForObject(selectSql, new Object[]{id}, Long.class);
    return count > 0;
  }

  @Override
  public List<T> findAll() {
    String selectSql = "SELECT * FROM " + getTableName();
    return this.getJdbcTemplate().query(selectSql, BeanPropertyRowMapper.newInstance(getEntityClass()));
  }

  @Override
  public List<T> findAllById(Iterable<Integer> ids) {
    List<T> all = new ArrayList<T>();
    for (Integer id : ids) {
      all.add(findById(id).get());
    }
    return all;
  }

  @Override
  public long count() {
    String selectSql =
        "SELECT COUNT(*) FROM " + getTableName() ;
    return getJdbcTemplate().queryForObject(selectSql, Long.class);
  }

  @Override
  public void deleteById(Integer id) {
    if (id == null) {
      throw new IllegalArgumentException("Cannot delete null id value");
    }
    String selectSql = "DELETE FROM " + getTableName() + " WHERE " + getIdColumnName() + "=?";
    getJdbcTemplate().update(selectSql, id);
  }

  @Override
  public void deleteAll() {
    String selectSql = "DELETE FROM " + getTableName();
    getJdbcTemplate().update(selectSql);
  }

}
