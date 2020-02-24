package ca.jrvs.apps.trading.model;

import java.util.Objects;

public class TraderAccountView {
  private Trader trader;
  private Account account;

  public TraderAccountView(Trader trader, Account account) {
    this.trader = trader;
    this.account = account;
  }

  public TraderAccountView() {
    this.trader = null;
    this.account = null;
  }

  public Trader getTrader() {
    return trader;
  }

  public void setTrader(Trader trader) {
    this.trader = trader;
  }

  public Account getAccount() {
    return account;
  }

  public void setAccount(Account account) {
    this.account = account;
  }

  @Override
  public String toString() {
    return "TraderAccountView{" +
        "trader=" + trader +
        ", account=" + account +
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
    TraderAccountView that = (TraderAccountView) o;
    return Objects.equals(trader, that.trader) &&
        Objects.equals(account, that.account);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trader, account);
  }
}
