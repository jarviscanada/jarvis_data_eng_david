package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Arrays;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "coordinates",
    "point"
})
public class Coordinates {
  @JsonProperty("coordinates")
  private float[] coordinates;
  @JsonProperty("point")
  private String point;

  public Coordinates(float latitude, float longitude) {
    this.point = new String("Point");
    this.coordinates = new float[2];
    this.coordinates[0] = latitude;
    this.coordinates[1] = longitude;
  }
  public Coordinates () { }

  public float[] getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(float[] coordinates) {
    this.coordinates = coordinates;
  }

  public String getPoint() {
    return point;
  }

  public void setPoint(String point) {
    this.point = point;
  }

  @Override
  public String toString() {
    return "Coordinates{" +
        "coordinates=" + Arrays.toString(coordinates) +
        ", point='" + point + '\'' +
        '}';
  }
}
