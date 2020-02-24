package ca.jrvs.apps.trading.model;

import java.util.Objects;

public class SecurityRow {

  private Position position;
  private Quote quote;
  private String ticker;

  public SecurityRow(Position position, Quote quote, String ticker) {
    this.position = position;
    this.quote = quote;
    this.ticker = ticker;
  }

  public SecurityRow() {
    this.position = null;
    this.quote = null;
    this.ticker = null;
  }

  public Position getPosition() {
    return position;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public Quote getQuote() {
    return quote;
  }

  public void setQuote(Quote quote) {
    this.quote = quote;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public String toString() {
    return "SecurityRow{" +
        "position=" + position +
        ", quote=" + quote +
        ", ticker='" + ticker + '\'' +
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
    SecurityRow that = (SecurityRow) o;
    return Objects.equals(position, that.position) &&
        Objects.equals(quote, that.quote) &&
        Objects.equals(ticker, that.ticker);
  }

  @Override
  public int hashCode() {
    return Objects.hash(position, quote, ticker);
  }
}
