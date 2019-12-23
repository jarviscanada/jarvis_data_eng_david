package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "created_at",
    "id",
    "id_str",
    "text",
    "entities",
    "coordinates",
    "retweet_count",
    "favorite_count",
    "favorited",
    "retweeted"
})
public class Tweet {
  @JsonProperty("created_at")
  private String created_at;
  @JsonProperty("id")
  private long id;
  @JsonProperty("id_str")
  private String id_str;
  @JsonProperty("text")
  private String text;
  @JsonProperty("entities")
  private Entities entities;
  @JsonProperty("coordinates")
  private Coordinates coordinates;
  @JsonProperty("retweet_count")
  private long retweet_count;
  @JsonProperty("favorite_count")
  private long favorite_count;
  @JsonProperty("favorited")
  private boolean favorited;
  @JsonProperty("retweeted")
  private boolean retweeted;

  public Tweet() {
    this.created_at = null;
    this.id_str = null;
    this.text = null;
    this.entities = null;
    this.coordinates = null;
  }

  public String getCreated_at() {
    return created_at;
  }

  public void setCreated_at(String created_at) {
    this.created_at = created_at;
  }

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

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Entities getEntities() {
    return entities;
  }

  public void setEntities(Entities entities) {
    this.entities = entities;
  }

  public Coordinates getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coordinates coordinates) {
    this.coordinates = coordinates;
  }

  public long getRetweet_count() {
    return retweet_count;
  }

  public void setRetweet_count(long retweet_count) {
    this.retweet_count = retweet_count;
  }

  public long getFavorite_count() {
    return favorite_count;
  }

  public void setFavorite_count(long favorite_count) {
    this.favorite_count = favorite_count;
  }

  public boolean getFavorited() { return favorited; }

  public void setFavorited(boolean favorited) {
    this.favorited = favorited;
  }

  public boolean getRetweeted() {
    return retweeted;
  }

  public void setRetweeted(boolean retweeted) {
    this.retweeted = retweeted;
  }

  @Override
  public String toString() {
    return "Tweet{" +
        "created_at='" + created_at + '\'' +
        ", id=" + id +
        ", id_str='" + id_str + '\'' +
        ", text='" + text + '\'' +
        ", entities=" + entities +
        ", coordinates=" + coordinates +
        ", retweet_count=" + retweet_count +
        ", favorite_count=" + favorite_count +
        ", favorited=" + favorited +
        ", retweeted=" + retweeted +
        '}';
  }
}
