package ca.jrvs.apps.twitter.DAO;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.Arrays;
import java.util.List;

public class TwitterController implements Controller {
  private  static final String COORD_SEP = ":";

  private Service service;

  public TwitterController(Service service) {
    this.service = service;
  }

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
    } catch (Exception e) {
      throw new IllegalArgumentException("Could not convert latitude:longitude values to float");
    }

    coordinates = new Coordinates(lat,lon);
    tweet.setText(stringText);
    tweet.setCoordinates(coordinates);
    return service.postTweet(tweet);
  }

  @Override
  public Tweet showTweet(String[] args) {
    if (args.length < 3) {
      throw new IllegalArgumentException("Need 2 or more arguments USAGE: SHOW <id> <fields>...");
    }
    String id = args[1];
    String[] fields = Arrays.copyOfRange(args, 2, args.length);
    return service.showTweet(id,fields);
  }

  @Override
  public List<Tweet> deleteTweet(String[] args) {
    if (args.length <= 1) {
      throw new IllegalArgumentException("Need to delete at least 1 tweet id USAGE: DELETE <id>...");
    }
    String[] ids = Arrays.copyOfRange(args, 1, args.length);
    return service.deleteTweets(ids);
  }
}
