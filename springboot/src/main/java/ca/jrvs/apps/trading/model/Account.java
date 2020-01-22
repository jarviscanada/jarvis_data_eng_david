package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.Objects;

public class Account implements Entity<Integer> {
  private Integer id;
  private Integer trader_id;
  private Double amount;

  public Account() {
    this.id = null;
    this.trader_id = null;
    this.amount = null;
  }

  public Double getAmount() { return amount; }

  public void setAmount(Double amount) { this.amount = amount; }

  public Integer getTrader_id() {
    return trader_id;
  }

  public void setTrader_id(Integer trader_id) {
    this.trader_id = trader_id;
  }

  @Override
  public Integer getId() { return id; }

  @Override
  public void setId(Integer id) { this.id = id; }

  @Override
  public String toString() {
    return "Account{" +
        "id=" + id +
        ", trader_id=" + trader_id +
        ", amount=" + amount +
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
    Account account = (Account) o;
    return Objects.equals(id, account.id) &&
        Objects.equals(trader_id, account.trader_id) &&
        Objects.equals(amount, account.amount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, trader_id, amount);
  }
}
