package ca.jrvs.apps.jdbc;

import java.math.BigDecimal;

public class orderDetails {
  private long orderItemQuantity;
  private String productCode;
  private String productName;
  private long productSize;
  private String productVariety;
  private BigDecimal productPrice;

  @Override
  public String toString() {
    return "orderDetails{" +
        "orderItemQuantity=" + orderItemQuantity +
        ", productCode='" + productCode + '\'' +
        ", productName='" + productName + '\'' +
        ", productSize=" + productSize +
        ", productVariety='" + productVariety + '\'' +
        ", productPrice=" + productPrice +
        '}';
  }

  public long getOrderItemQuantity() {
    return orderItemQuantity;
  }

  public void setOrderItemQuantity(long orderItemQuantity) {
    this.orderItemQuantity = orderItemQuantity;
  }

  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public long getProductSize() {
    return productSize;
  }

  public void setProductSize(long productSize) {
    this.productSize = productSize;
  }

  public String getProductVariety() {
    return productVariety;
  }

  public void setProductVariety(String productVariety) {
    this.productVariety = productVariety;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }


}
