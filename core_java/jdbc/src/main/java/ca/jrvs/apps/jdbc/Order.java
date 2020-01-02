package ca.jrvs.apps.jdbc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import ca.jrvs.apps.jdbc.util.DataTransferObject;

public class Order implements DataTransferObject {

  private String customerFirstName;
  private String customerLastLane;
  private String customerEmail;
  private long id; 
  private Date creationDate;
  private BigDecimal totalDue;
  private String status;
  private String salespersonFirstName;
  private String salespersonLastName;
  private String salespersonEmail;
  private List<orderDetails> orderList;
  /*
  // For order, can have multiple items and multiple products in an order
   */

  @Override
  public String toString() {
    return "Order{" +
        "customerFirstName='" + customerFirstName + '\'' +
        ", customerLastLane='" + customerLastLane + '\'' +
        ", customerEmail='" + customerEmail + '\'' +
        ", id=" + id +
        ", creationDate=" + creationDate +
        ", totalDue=" + totalDue +
        ", status='" + status + '\'' +
        ", salespersonFirstName='" + salespersonFirstName + '\'' +
        ", salespersonLastName='" + salespersonLastName + '\'' +
        ", salespersonEmail='" + salespersonEmail + '\'' +
        ", orderList=" + orderList +
        '}';
  }

  public Order() {
      this.orderList = new ArrayList<orderDetails>();
  }

  public List<orderDetails> getOrderList() {
    return orderList;
  }

  public void setOrderList(List<orderDetails> orderList) {
    this.orderList = orderList;
  }

  @Override
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getCustomerFirstName() {
    return customerFirstName;
  }

  public void setCustomerFirstName(String customerFirstName) {
    this.customerFirstName = customerFirstName;
  }

  public String getCustomerLastLane() {
    return customerLastLane;
  }

  public void setCustomerLastLane(String customerLastLane) {
    this.customerLastLane = customerLastLane;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public BigDecimal getTotalDue() {
    return totalDue;
  }

  public void setTotalDue(BigDecimal totalDue) {
    this.totalDue = totalDue;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getSalespersonFirstName() {
    return salespersonFirstName;
  }

  public void setSalespersonFirstName(String salespersonFirstName) {
    this.salespersonFirstName = salespersonFirstName;
  }

  public String getSalespersonLastName() {
    return salespersonLastName;
  }

  public void setSalespersonLastName(String salespersonLastName) {
    this.salespersonLastName = salespersonLastName;
  }

  public String getSalespersonEmail() {
    return salespersonEmail;
  }

  public void setSalespersonEmail(String salespersonEmail) {
    this.salespersonEmail = salespersonEmail;
  }

}
