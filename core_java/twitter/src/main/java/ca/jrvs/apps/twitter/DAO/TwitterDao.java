package ca.jrvs.apps.twitter.DAO;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterDao implements CrdDao<Tweet, Long> {
  private static Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  //URI constants
  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy/";
  //URI symbol
  private static final String QUERY_SYM = "?";
  //Response Code
  private static final int HTTP_OK = 200;


  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  public HttpHelper getHttpHelper() {
    return httpHelper;
  }

  public void setHttpHelper(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /*
    Setup to catch exceptions in the higher layer
   */
  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper m = new ObjectMapper();
    m.configure(
    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) m.readValue(json,clazz);
  }

  public Tweet httpResponseCheck(HttpResponse response) {
    Tweet tweet = null;
    if (response == null) {
      throw new RuntimeException("Response is NULL");
    }
    //Check status
    if (response.getStatusLine().getStatusCode() != HTTP_OK) {
      try {
        TwitterDao.logger.info(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        TwitterDao.logger.error("IOException converting Entity to String: " + e);
      }
      throw new RuntimeException("HttpResponse not return HTTP_OK");
    }

    //check if
    if (response.getEntity() == null) {
      throw new RuntimeException("Response entity is null");
    }
    try {
      tweet = toObjectFromJson(EntityUtils.toString(response.getEntity()),Tweet.class);
    } catch (IOException e) {
      TwitterDao.logger.error("IOException converting Entity to JSON: " + e);
    }
    return tweet;
  }

  @Override
  public Tweet create(Tweet entity) {
    //Post this tweet
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    Coordinates coordinates = entity.getCoordinates();
    float lat, lon;
    URI uri = null;
    if (entity.getCoordinates() != null) {
      lat = coordinates.getCoordinates()[0];
      lon = coordinates.getCoordinates()[1];
      uri = URI.create(API_BASE_URI + POST_PATH + QUERY_SYM + "status=" + percentEscaper
          .escape(entity.getText()) + "&lat=" + lat + "&lon=" + lon);
    } else {
      uri = URI.create(API_BASE_URI + POST_PATH + QUERY_SYM + "status=" + percentEscaper
          .escape(entity.getText()));
    }
    return httpResponseCheck(httpHelper.httpPost(uri));
  }

  @Override
  public Tweet findById(Long aLong) {
    URI uri = URI.create(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id=" + aLong);
    return httpResponseCheck(httpHelper.httpGet(uri));
  }

  @Override
  public Tweet deleteById(Long aLong) {
    URI uri = URI.create(API_BASE_URI + DELETE_PATH + aLong + ".json");
    return httpResponseCheck(httpHelper.httpPost(uri));
  }

}
