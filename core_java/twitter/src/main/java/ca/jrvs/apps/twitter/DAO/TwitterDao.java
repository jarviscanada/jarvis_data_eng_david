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
  private final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  private static final String API_BASE_URI = "https://api.twitter.com";
  private static final String POST_PATH = "/1.1/statuses/update.json";
  private static final String SHOW_PATH = "/1.1/statuses/show.json";
  private static final String DELETE_PATH = "/1.1/statuses/destroy/";
  private static final String QUERY_SYM = "?";
  private static final int HTTP_OK = 200;

  private HttpHelper httpHelper;

  @Autowired
  public TwitterDao(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }

  /*
   * Consturctor no args provided for SpringBoot
   */
  public TwitterDao() {
    this.httpHelper = new TwitterHttpHelper();
  }

  public HttpHelper getHttpHelper() {
    return httpHelper;
  }

  public void setHttpHelper(HttpHelper httpHelper) {
    this.httpHelper = httpHelper;
  }


  /**
   * Convert String to object
   * @param String, object Class
   * @return Object of given class
   */
  public static <T> T toObjectFromJson(String json, Class clazz) throws IOException {
    ObjectMapper m = new ObjectMapper();
    m.configure(
    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return (T) m.readValue(json,clazz);
  }

  /**
   * Check HttpResponse for errors
   * @param response returned from HttpGet/HttpPost
   * @return Tweet object of response
   */
  public Tweet httpResponseCheck(HttpResponse response) {
    Tweet tweet = null;
    if (response == null) {
      throw new RuntimeException("Response is NULL");
    }
    if (response.getStatusLine().getStatusCode() != HTTP_OK) {
      try {
        logger.info(EntityUtils.toString(response.getEntity()));
      } catch (IOException e) {
        logger.error("IOException converting Entity to String: " + e);
      }
      throw new RuntimeException("HttpResponse not return HTTP_OK");
    }

    if (response.getEntity() == null) {
      throw new RuntimeException("Response entity is null");
    }
    try {
      tweet = toObjectFromJson(EntityUtils.toString(response.getEntity()),Tweet.class);
    } catch (IOException e) {
      logger.error("IOException converting Entity to JSON: " + e);
    }
    return tweet;
  }

  /**
   * Create an entity(Tweet) to the underlying storage
   * @param entity entity that to be created
   * @return created entity
   */
  @Override
  public Tweet create(Tweet entity) {
    PercentEscaper percentEscaper = new PercentEscaper("", false);
    Coordinates coordinates = entity.getCoordinates();
    float lat, lon;
    URI uri = null;
    if (entity.getCoordinates() != null) {
      lat = coordinates.getCoordinates()[0];
      lon = coordinates.getCoordinates()[1];
      uri = URI.create(API_BASE_URI + POST_PATH + QUERY_SYM + "status=" + percentEscaper
          .escape(entity.getText()) + "&lat=" + lat + "&long=" + lon);
    } else {
      uri = URI.create(API_BASE_URI + POST_PATH + QUERY_SYM + "status=" + percentEscaper
          .escape(entity.getText()));
    }
    return httpResponseCheck(httpHelper.httpPost(uri));
  }

  /**
   * Find an entity(Tweet) by its id
   * @param id entity id
   * @return Tweet entity
   */
  @Override
  public Tweet findById(Long aLong) {
    URI uri = URI.create(API_BASE_URI + SHOW_PATH + QUERY_SYM + "id=" + aLong);
    return httpResponseCheck(httpHelper.httpGet(uri));
  }

  /**
   * Delete an entity(Tweet) by its ID
   * @param id of the entity to be deleted
   * @return deleted entity
   */
  @Override
  public Tweet deleteById(Long aLong) {
    URI uri = URI.create(API_BASE_URI + DELETE_PATH + aLong + ".json");
    return httpResponseCheck(httpHelper.httpPost(uri));
  }

}
