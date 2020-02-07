package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.Objects;

public class Position implements Entity<Integer> {
  private Integer account_id;
  private String ticker;
  private Integer position;

  public Position() {
    this.account_id = null;
    this.ticker = null;
    this.position = null;
  }

  @Override
  public Integer getId() { return account_id; }

  @Override
  public void setId(Integer id) { this.account_id = id; }

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
        "account_id=" + account_id +
        ", ticker='" + ticker + '\'' +
        ", position=" + position +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Position position1 = (Position) o;
    return Objects.equals(account_id, position1.account_id) &&
        Objects.equals(ticker, position1.ticker) &&
        Objects.equals(position, position1.position);
  }

  @Override
  public int hashCode() {
    return Objects.hash(account_id, ticker, position);
  }
}
