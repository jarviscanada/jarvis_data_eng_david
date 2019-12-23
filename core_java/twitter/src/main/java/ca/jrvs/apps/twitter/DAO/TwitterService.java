package ca.jrvs.apps.twitter.DAO;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterService implements Service {

  private CrdDao dao;

  //@Autowired
  public TwitterService(CrdDao dao) {
    this.dao = dao;
  }

  @Override
  public Tweet postTweet(Tweet tweet) {
    //get Text
    //get long/lat
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

  @Override
  public Tweet showTweet(String id, String[] fields) {
    long longId;
    try {
      longId = Long.parseLong(id);
    } catch (Exception i) {
      throw new IllegalArgumentException("id param is invalid");
    }
    Tweet tweet = (Tweet) dao.findById(longId); //takes a while to run
    if (fields.length == 0 ) {
      return tweet;
    }
    Tweet workTweet = new Tweet();
    Method[] methods = Tweet.class.getDeclaredMethods(); //all Tweet methods
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
        //run setter
        Method setMethod = methodNames.get(setter);
        Method getMeth = methodNames.get(getter);
        try {
          setMethod.invoke(workTweet, getMeth.invoke(tweet));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        }
      } else {
        throw new IllegalArgumentException("field: " + field + " does not exist");
      }
    }
    return workTweet;
  }

  @Override
  public List<Tweet> deleteTweets(String[] ids) {
    List<Tweet> delTweet = new ArrayList<Tweet>();

    for (String id : ids) {
      long longId;
      try {
        longId = Long.parseLong(id);
      } catch (Exception i) {
        throw new IllegalArgumentException("Id: " + id + " param is invalid");
      }

      try {
        delTweet.add((Tweet) dao.deleteById(longId));
      } catch (Exception i) {
        throw new IllegalArgumentException("Id: " + longId + " could not be deleted");
      }
    }
    return delTweet;
  }

  public static void main(String[] s) {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper hHelp = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken, tokenSecret);
    TwitterDao td = new TwitterDao(hHelp);
    Service service = new TwitterService(td);
    Long id = 1207176914974195712L;
    String str_id = Long.toString(id);
    String[] fields = {"created_at", "id", "id_str", "text", "entities", "coordinates",
        "retweet_count", "favorite_count", "favorited", "retweeted"};

    System.out.println(service.showTweet(str_id, fields));
  }
}
