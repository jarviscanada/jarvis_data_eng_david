package ca.jrvs.apps.twitter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import ca.jrvs.apps.twitter.DAO.Controller;
import ca.jrvs.apps.twitter.DAO.CrdDao;
import ca.jrvs.apps.twitter.DAO.HttpHelper;
import ca.jrvs.apps.twitter.DAO.Service;
import ca.jrvs.apps.twitter.DAO.TwitterController;
import ca.jrvs.apps.twitter.DAO.TwitterDao;
import ca.jrvs.apps.twitter.DAO.TwitterHttpHelper;
import ca.jrvs.apps.twitter.DAO.TwitterService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class TwitterControllerIntTest {
  private Service service;
  private CrdDao dao;
  private HttpHelper httpHelper;
  private Controller controller;
  private String text;
  private Tweet createdTweet;
  private Long id = 1210736890166435842L;
  private Long workId;
  private Coordinates coordinates;

  @Before
  public void init() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    String hashtag = "#abc";
    this.text = "@somone sometext " + hashtag + " " + System.currentTimeMillis();
    float lat = 11.2f;
    float lon = -23.4f;
    float badlat = 190f;
    coordinates = new Coordinates(lat, lon);
    Coordinates badCoord = new Coordinates(badlat,lon);
    httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
    this.dao = new TwitterDao(httpHelper);
    this.service = new TwitterService(dao);
    this.controller = new TwitterController(service);
  }

  @Test(expected = IllegalArgumentException.class)
  public void badArg() {
    String[] args = {"arg1", "arg2","arg3", "arg4"};
    controller.postTweet(args);
  }

  @Test(expected = IllegalArgumentException.class)
  public void badCoord() {
    String[] args = {"arg1", "arg2","11.2:-23.4:21.5"};
    controller.postTweet(args);
  }

  @Test
  public void postTest() {
    String[] args = {"POST", this.text,"11.2:-23.4"};
    Tweet output = controller.postTweet(args);
    assertNotNull(output);
    assertNotNull(output.getId());
    assertEquals(output.getText(),text);
    assertNotNull(output.getCoordinates());
    this.workId = new Long(output.getId());
    this.createdTweet = output;
  }

  @Test
  public void showTest() {
    postTest();
    String[] args = {"SHOW", Long.toString(this.workId), "created_at", "id", "text", "coordinates"};
    Tweet output = controller.showTweet(args);
    assertNotNull(output);
    assertEquals(createdTweet.getId(),output.getId());
    assertNotNull(output.getCoordinates());
    assertEquals(createdTweet.getCreated_at(),output.getCreated_at());
    assertEquals(createdTweet.getText(),output.getText());
  }

  @Test
  public void deleteTest() {
    /*
    postTest();
    String str_id = Long.toString(this.workId);
    Tweet tweetA = this.createdTweet;
    this.text = this.text + " NEWLINE";
    postTest();
    String[] args = {"DELETE",str_id ,Long.toString(this.workId)};
    List<Tweet> output = controller.deleteTweet(args);
    assertNotNull(output.get(0));
    assertNotNull(output.get(1));

    assertEquals(tweetA.getText(),output.get(0).getText());
    assertEquals(this.createdTweet.getText(),output.get(1).getText());

    assertEquals(tweetA.getId(),output.get(0).getId());
    assertEquals(this.createdTweet.getId(),output.get(1).getId());

    assertNotNull(output.get(0).getCoordinates());
    assertNotNull(output.get(1).getCoordinates());
     */
  }

}
