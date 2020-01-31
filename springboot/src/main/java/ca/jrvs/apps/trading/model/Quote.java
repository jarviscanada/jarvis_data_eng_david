package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;

public class Quote implements Entity<String> {

  private String ticker;
  private Double lastPrice;
  private Double bidPrice;
  private Long bidSize;
  private Double askPrice;
  private Long askSize;

  public Quote() {
    this.ticker = null;
    this.lastPrice = null;
    this.bidPrice = null;
    this.bidSize = null;
    this.askPrice = null;
    this.askSize = null;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Double getLastPrice() {
    return lastPrice;
  }

  public void setLastPrice(Double lastPrice) {
    this.lastPrice = lastPrice;
  }

  public Double getBidPrice() {
    return bidPrice;
  }

  public void setBidPrice(Double bidPrice) {
    this.bidPrice = bidPrice;
  }

  public Long getBidSize() {
    return bidSize;
  }

  public void setBidSize(Long bidSize) {
    this.bidSize = bidSize;
  }

  public Double getAskPrice() {
    return askPrice;
  }

  public void setAskPrice(Double askPrice) {
    this.askPrice = askPrice;
  }

  public Long getAskSize() { return askSize; }

  public void setAskSize(Long askSize) { this.askSize = askSize; }

  @Override
  public String getId() {
    return ticker;
  }

  @Override
  public void setId(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public String toString() {
    return "Quote{" +
        "ticker='" + ticker + '\'' +
        ", lastPrice=" + lastPrice +
        ", bidPrice=" + bidPrice +
        ", bidSize=" + bidSize +
        ", askPrice=" + askPrice +
        ", askSize=" + askSize +
        '}';
  }
}
