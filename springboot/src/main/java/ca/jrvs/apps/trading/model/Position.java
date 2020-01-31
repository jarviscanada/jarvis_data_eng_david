package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;

public class Position implements Entity<Integer> {
  private Integer id;
  private String ticker;
  private Integer position;

  public Position() {
    this.id = null;
    this.ticker = null;
    this.position = null;
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "Position{" +
        "id=" + id +
        ", ticker='" + ticker + '\'' +
        ", position=" + position +
        '}';
  }
}
