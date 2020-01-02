package ca.jrvs.apps.twitter.DAO;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@org.springframework.stereotype.Service
public class TwitterService implements Service {

  private CrdDao dao;
  private final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

  @Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  /*
   * Consturctor no args provided for SpringBoot
   */
  public TwitterService() {
    this.dao = new TwitterDao();
  }

  /**
   * Validate and post a user input Tweet
   *
   * @param tweet tweet to be created
   * @return created tweet
   *
   * @throws IllegalArgumentException if text exceed max number of allowed characters or lat/long out of range
   */
  @Override
  public Tweet postTweet(Tweet tweet) {
    if (tweet == null) {
      throw new IllegalArgumentException("Value of Tweet is null");
    }
    Coordinates coordinates = tweet.getCoordinates();
    int textLength = tweet.getText().length();
    if (textLength > 140) {
      throw new IllegalArgumentException("Text over 140 characters");
    }

    if (coordinates != null) {
      float lon = coordinates.getCoordinates()[0];
      float lat = coordinates.getCoordinates()[1];
      if (!coordinateBound(lon, lat)) {
        throw new IllegalArgumentException("Coordinates are invalid");
      }
    }
    return (Tweet) dao.create(tweet);
  }

  public boolean coordinateBound(float lon, float lat) {
    lon = Math.abs(lon);
    lat = Math.abs(lat);
    return (lon <= 180 && lat <= 90);
  }


  /**
   * Search a tweet by ID
   *
   * @param id tweet id
   * @param fields set fields not in the list to null
   * @return Tweet object which is returned by the Twitter API
   *
   * @throws IllegalArgumentException if id or fields param is invalid
   */
  @Override
  public Tweet showTweet(String id, String[] fields) {
    long longId;
    try {
      longId = Long.parseLong(id);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Id: " + id + " param is invalid");
    }
    Tweet tweet = (Tweet) dao.findById(longId); 
    if (fields.length == 0 ) {
      return tweet;
    }
    Tweet workTweet = new Tweet();
    Method[] methods = Tweet.class.getDeclaredMethods(); 
    Map<String,Method> methodNames = new HashMap<String,Method>();

    for (Method m : methods) {
      int lastBracket = m.toString().lastIndexOf('(');
      String s = m.toString().substring(0,lastBracket);
      int lastIndxDot = s.lastIndexOf('.');
      String key = s.substring(lastIndxDot+1,lastBracket);
      methodNames.put(key,m);
    }

    for (String field: fields) {
      String template = field.substring(0, 1).toUpperCase() + field.substring(1);
      String getter = "get" + template;
      String setter = "set" + template;
      if (methodNames.containsKey(getter) && methodNames.containsKey(setter)) {
        Method setMethod = methodNames.get(setter);
        Method getMeth = methodNames.get(getter);
        try {
          setMethod.invoke(workTweet, getMeth.invoke(tweet));
        } catch (IllegalAccessException ex) {
          logger.error("IllegalAccess Invocation: " + ex);
        } catch (InvocationTargetException ex) {
          logger.error("Invocation Target Exception: " + ex);
        }
      } else {
        throw new IllegalArgumentException("field: " + field + " does not exist");
      }
    }
    return workTweet;
  }

  /**
   * Delete Tweet(s) by id(s).
   *
   * @param ids tweet IDs which will be deleted
   * @return A list of Tweets
   *
   * @throws IllegalArgumentException if one of the IDs is invalid.
   */
  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> delTweet = new ArrayList<Tweet>();

    for (String id : ids) {
      long longId;
      try {
        longId = Long.parseLong(id);
      } catch (Exception ex) {
        throw new IllegalArgumentException("Id: " + id + " param is invalid");
      }

      try {
        delTweet.add((Tweet) dao.deleteById(longId));
      } catch (Exception ex) {
        throw new IllegalArgumentException("Id: " + longId + " could not be deleted");
      }
    }
    return delTweet;
  }
}
