package ca.jrvs.apps.twitter.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

import ca.jrvs.apps.twitter.DAO.Service;
import ca.jrvs.apps.twitter.DAO.TwitterController;
import ca.jrvs.apps.twitter.DAO.TwitterService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterControllerUnitTest {
  @Mock
  Service service;

  @InjectMocks
  TwitterController controller;

  public Tweet tweet;
  public Tweet tweet2;

  @Before
  public void init() {
    Coordinates coordinates = new Coordinates(11.2f, -23.4f);
    Coordinates coordinates2 = new Coordinates(-22.1f, -49.1f);
    tweet = new Tweet();
    List<Tweet> tweetList = new ArrayList<Tweet>();
    tweet.setId(123);
    tweet.setCoordinates(coordinates);
    tweet.setText("test");
    tweetList.add(tweet);

    tweet2 = new Tweet();
    tweet2.setId(456);
    tweet2.setCoordinates(coordinates2);
    tweet2.setText("NewText");
    tweetList.add(tweet2);
    when(service.postTweet(any(Tweet.class))).thenReturn(tweet);
    when(service.showTweet(any(String.class),any(String[].class))).thenReturn(tweet);
    when(service.deleteTweets(any(String[].class))).thenReturn(tweetList);
  }

  @Test
  public void testPost() {
    String[] args = {"POST", tweet.getText(),"11.2:-23.4"};
    Tweet output = controller.postTweet(args);
    assertNotNull(output);
    assertEquals(output.getText(),tweet.getText());
    assertEquals(output.getId(),tweet.getId());
    assertEquals(output.getCoordinates(),tweet.getCoordinates());
  }

  @Test
  public void testShow() {
    String[] args = {"SHOW", Long.toString(123), "created_at", "id", "text", "coordinates"};
    Tweet output = controller.showTweet(args);
    assertNotNull(output);
    assertEquals(output.getText(),tweet.getText());
    assertEquals(output.getId(),tweet.getId());
    assertEquals(output.getCoordinates(),tweet.getCoordinates());
  }

  @Test
  public void testDelete() {
    String[] args = {"DELETE", Long.toString(123) , Long.toString(456)};
    List<Tweet> output = controller.deleteTweet(args);
    assertNotNull(output.get(0));
    assertNotNull(output.get(1));
    assertEquals(output.get(0).getText(),tweet.getText());
    assertEquals(output.get(0).getId(),tweet.getId());
    assertEquals(output.get(0).getCoordinates(),tweet.getCoordinates());
    assertEquals(output.get(1).getText(),tweet2.getText());
    assertEquals(output.get(1).getId(),tweet2.getId());
    assertEquals(output.get(1).getCoordinates(),tweet2.getCoordinates());
  }

}
