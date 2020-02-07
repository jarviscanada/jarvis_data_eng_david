package ca.jrvs.apps.trading.model;

public class MarketOrderDto {
  private Integer size;
  private String ticker;
  private Integer accountId;

  public MarketOrderDto() {
    this.size = null;
    this.ticker = null;
    this.accountId = null;
  }

  public Integer getSize() { return size; }

  public void setSize(Integer size) {
    this.size = size;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }
}
