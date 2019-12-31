package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "id_str",
    "name",
    "screen_name"
})
public class UserMention {
  @JsonProperty("id")
  private long id;
  @JsonProperty("id_str")
  private String id_str;
  @JsonProperty("name")
  private String name;
  @JsonProperty("screen_name")
  private String screen_name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getId_str() {
    return id_str;
  }

  public void setId_str(String id_str) {
    this.id_str = id_str;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getScreen_name() {
    return screen_name;
  }

  public void setScreen_name(String screen_name) {
    this.screen_name = screen_name;
  }

  @Override
  public String toString() {
    return "UserMention{" +
        "id=" + id +
        ", id_str='" + id_str + '\'' +
        ", name='" + name + '\'' +
        ", screen_name='" + screen_name + '\'' +
        '}';
  }
}
