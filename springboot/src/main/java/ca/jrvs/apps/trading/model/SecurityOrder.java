package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.util.Objects;

public class SecurityOrder implements Entity<Integer> {

  private Integer id;
  private Integer account_id;
  private String status;
  private String ticker;
  private Integer size;
  private Double price;
  private String notes;

  public SecurityOrder() {
    this.id = null;
    this.account_id = null;
    this.status = null;
    this.ticker = null;
    this.size = null;
    this.price = null;
    this.notes = null;
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getAccount_id() {
    return account_id;
  }

  public void setAccount_id(Integer account_id) {
    this.account_id = account_id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Double getPrice() {
    return price;
  }

  public void setPrice(Double price) {
    this.price = price;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    return "SecurityOrder{" +
        "id=" + id +
        ", account_id=" + account_id +
        ", status='" + status + '\'' +
        ", ticker='" + ticker + '\'' +
        ", size=" + size +
        ", price=" + price +
        ", notes='" + notes + '\'' +
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
    SecurityOrder that = (SecurityOrder) o;
    return Objects.equals(id, that.id) &&
        Objects.equals(account_id, that.account_id) &&
        Objects.equals(status, that.status) &&
        Objects.equals(ticker, that.ticker) &&
        Objects.equals(size, that.size) &&
        Objects.equals(price, that.price) &&
        Objects.equals(notes, that.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, account_id, status, ticker, size, price, notes);
  }

}
