package ca.jrvs.apps.trading.model;

import ca.jrvs.apps.trading.model.domain.Entity;
import java.sql.Date;
import java.util.Objects;

public class Trader implements Entity<Integer> {

  private Integer id;
  private String first_name;
  private String last_name;
  private Date dob;
  private String country;
  private String email;

  public Trader() {
    this.id = null;
    this.first_name = null;
    this.last_name = null;
    this.dob = null;
    this.country = null;
    this.email = null;
  }

  public String getFirst_name() {
    return first_name;
  }

  public void setFirst_name(String first_name) {
    this.first_name = first_name;
  }

  public String getLast_name() {
    return last_name;
  }

  public void setLast_name(String last_name) {
    this.last_name = last_name;
  }

  public Date getDob() {
    return dob;
  }

  public void setDob(Date dob) {
    this.dob = dob;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Trader{" +
        "id=" + id +
        ", first_name='" + first_name + '\'' +
        ", last_name='" + last_name + '\'' +
        ", dob=" + dob +
        ", country='" + country + '\'' +
        ", email='" + email + '\'' +
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
    Trader trader = (Trader) o;
    return Objects.equals(id, trader.id) &&
        Objects.equals(first_name, trader.first_name) &&
        Objects.equals(last_name, trader.last_name) &&
        Objects.equals(dob, trader.dob) &&
        Objects.equals(country, trader.country) &&
        Objects.equals(email, trader.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, first_name, last_name, dob, country, email);
  }
}
