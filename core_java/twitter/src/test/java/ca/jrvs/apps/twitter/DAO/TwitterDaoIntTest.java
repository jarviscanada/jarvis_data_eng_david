package ca.jrvs.apps.twitter.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TwitterDaoIntTest {

  TwitterDao dao;
  long findId;
  Tweet createdTweet;

  @Before
  public void init() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    this.createdTweet = null;
    HttpHelper httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    this.dao = new TwitterDao(httpHelper);

  }

  @Test
  public void testDaoPost() {
    String hashtag = "#abc";
    String text = "@somone sometext " + hashtag + " " + System.currentTimeMillis();
    float lat = 1f;
    float lon = -1f;
    Coordinates coordinates = new Coordinates(lat, lon);
    Tweet testTweet = new Tweet();
    testTweet.setText(text);
    testTweet.setCoordinates(coordinates);
    Tweet tweet = dao.create(testTweet);
    this.findId = tweet.getId();
    this.createdTweet = tweet;

    //assert commands here
    assertEquals("ERROR: text does not match tweet's text",text,tweet.getText());
    assertNotNull("ERROR: Coordinates value is null", tweet.getCoordinates());
    assertEquals("ERROR: Need longitude and latitude for coordinates",2,tweet.getCoordinates().getCoordinates().length);
    assertEquals("ERROR: latitude entered not same as tweet latitude",lat, tweet.getCoordinates().getCoordinates()[0]);
    assertEquals("ERROR: longitude entered not same as tweet longitude",lon,tweet.getCoordinates().getCoordinates()[1]);
    assertTrue("ERROR: hashtag value different from tweet hashtag", hashtag.contains(tweet.getEntities().getHashTag()[0].getText()));

  }

  @Test
  public void testDaoShow() {
    //need ids;
    Tweet tweet = dao.findById(this.findId);
    assertEquals("Searched tweet has mismatching fields compared to created" ,this.createdTweet, tweet);
  }

  @After
  public void testDaoDelete() {
    Tweet tweet = dao.deleteById(this.findId);
    assertEquals("Deleted tweet has mismatching fields compared to created" ,this.createdTweet, tweet);
  }

}
