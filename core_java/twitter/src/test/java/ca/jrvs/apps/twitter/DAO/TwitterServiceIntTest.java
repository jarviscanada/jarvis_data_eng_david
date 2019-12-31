package ca.jrvs.apps.twitter.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterServiceIntTest {
  private Service service;
  private CrdDao dao;
  private HttpHelper httpHelper;
  private Tweet createdTweet;
  private Tweet badTweet;
  private Long id = 1210691223322841089L;
  private Long newId;
  private String text;

  @Before
  public void init() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    String hashtag = "#abc";
    this.text = "@somone sometext " + hashtag + " " + System.currentTimeMillis();
    float lat = 1f;
    float lon = -1f;
    float badlat = 190f;
    Coordinates coordinates = new Coordinates(lat, lon);
    Coordinates badCoord = new Coordinates(badlat,lon);
    this.createdTweet = new Tweet();
    this.createdTweet.setId(this.id);
    this.createdTweet.setText(text);
    this.createdTweet.setCoordinates(coordinates);

    this.badTweet = new Tweet();
    this.badTweet.setId(id);
    this.badTweet.setText(text + " Bad");
    this.badTweet.setCoordinates(badCoord);
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    this.dao = new TwitterDao(httpHelper);
    this.service = new TwitterService(dao);
  }

  @Test
  public void testPost() {
    Tweet outputTweet = service.postTweet(this.createdTweet);
    assertEquals(outputTweet.getCoordinates(),createdTweet.getCoordinates());
    assertNotNull(createdTweet.getId());
    this.newId = new Long(outputTweet.getId());
  }

  @Test(expected = IllegalArgumentException.class)
  public void outBoundTest() {
    Tweet failTweet = service.postTweet(this.badTweet);
  }

  @Test
  public void testShow() {
    testPost();
    String str_id = Long.toString(newId);
    String[] fields = {"created_at", "id", "id_str", "text", "entities",
        "retweet_count", "favorite_count", "favorited", "retweeted"};
    Tweet showTweet = service.showTweet(str_id,fields);
    assertEquals(null,showTweet.getCoordinates());
    assertEquals(newId,showTweet.getId());
    assertNotNull(createdTweet.getText());
  }

  @Test
  public void testDelete() {
    testPost();
    String[] list = {Long.toString(newId)};
    List<Tweet> lsTweet = service.deleteTweets(list);
    assertNotNull(lsTweet.get(0));
    assertNotNull(lsTweet.get(0).getText());
    assertNotNull(lsTweet.get(0).getId());
  }

}
