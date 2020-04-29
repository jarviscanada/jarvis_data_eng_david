package ca.jrvs.apps.trading.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PortfolioView {

  private List<SecurityRow> securityRows;

  public PortfolioView(List<SecurityRow> securityRows) {
    this.securityRows = securityRows;
  }

  public PortfolioView() {
    this.securityRows = new ArrayList<SecurityRow>();
  }

  public List<SecurityRow> getSecurityRows() {
    return securityRows;
  }

  public void setSecurityRows(List<SecurityRow> securityRows) {
    this.securityRows = securityRows;
  }

  @Override
  public String toString() {
    return "PortfolioView{" +
        "securityRows=" + securityRows +
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
    PortfolioView that = (PortfolioView) o;
    return Objects.equals(securityRows, that.securityRows);
  }

  @Override
  public int hashCode() {
    return Objects.hash(securityRows);
  }
}
