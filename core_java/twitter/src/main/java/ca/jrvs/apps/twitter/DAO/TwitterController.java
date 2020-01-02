package ca.jrvs.apps.twitter.DAO;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Controller
public class TwitterController implements Controller {
  private  static final String COORD_SEP = ":";

  private Service service;

  @Autowired
  public TwitterController(Service service) {
    this.service = service;
  }

  /*
   * Consturctor no args provided for SpringBoot
   */
  public TwitterController() { this.service = new TwitterService(); }

  /**
   * Parse user argument and post a tweet by calling service classes
   *
   * @param args
   * @return a posted tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet postTweet(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("Need 3 arguments, USAGE: POST <tweet text> <latitude>:<longitude>");
    }

    String stringText = args[1];
    String[] coordinates_str = args[2].split(COORD_SEP); //args[2] is <latitude>:<longitude>
    Coordinates coordinates = null;
    Tweet tweet = new Tweet();
    float lat, lon;

    if (coordinates_str.length != 2) {
      throw new IllegalArgumentException("Need <latitude>:<longitude> format for final argument");
    }

    try {
      lat = Float.valueOf(coordinates_str[0]);
      lon = Float.valueOf(coordinates_str[1]);
    } catch (Exception ex) {
      throw new IllegalArgumentException("Could not convert latitude:longitude values to float");
    }

    coordinates = new Coordinates(lat,lon);
    tweet.setText(stringText);
    tweet.setCoordinates(coordinates);
    return service.postTweet(tweet);
  }

  /**
   * Parse user argument and search a tweet by calling service classes
   *
   * @param args
   * @return a tweet
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 3) {
      throw new IllegalArgumentException("Need 2 or more arguments USAGE: SHOW <id> <fields>...");
    }
    String id = args[1];
    String[] fields = Arrays.copyOfRange(args, 2, args.length);
    return service.showTweet(id,fields);
  }

  /**
   * Parse user argument and delete tweets by calling service classes
   *
   * @param args
   * @return a list of deleted tweets
   * @throws IllegalArgumentException if args are invalid
   */
  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length <= 1) {
      throw new IllegalArgumentException("Need to delete at least 1 tweet id USAGE: DELETE <id>...");
    }
    String[] ids = Arrays.copyOfRange(args, 1, args.length);
    return service.deleteTweets(ids);
  }
}
