package ca.jrvs.apps.twitter.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterServiceUnitTest {

  @Mock
  CrdDao dao;

  @InjectMocks
  TwitterService service;

  private Tweet tweet;
  private Long id;
  private String[] fields = {"id", "text"};

  @Before
  public void init() {
    tweet = new Tweet();
    tweet.setText("test");
    float lat = 50.0f;
    float lon = 1.0f;
    id = 1097607853932564480L;
    Coordinates coordinates = new Coordinates(lat, lon);
    tweet.setId(id);
    tweet.setCoordinates(coordinates);
    when(dao.create(ArgumentMatchers.any(Tweet.class))).thenReturn(tweet);
    when(dao.findById(ArgumentMatchers.any(Long.class))).thenReturn(tweet);
    when(dao.deleteById(ArgumentMatchers.any(Long.class))).thenReturn(tweet);
  }

  @Test
  public void postTweet() {
    Tweet output = service.postTweet(tweet);
    assertNotNull(output);
    assertEquals(output.getText(),tweet.getText());
    assertEquals(output.getCoordinates(),tweet.getCoordinates());
    assertEquals(output.getId(),tweet.getId());
  }

  @Test
  public void showTweet() {
    Tweet output = service.showTweet(Long.toString(id),fields);
    assertNotNull(output);
    assertNull(output.getCoordinates());
    assertEquals(output.getId(),tweet.getId());
    assertEquals(output.getText(),tweet.getText());
  }

  @Test
  public void deleteTweet() {
    String[] list = {Long.toString(id)};
    List<Tweet> lsTweet = service.deleteTweets(list);
    assertNotNull(lsTweet.get(0));
    assertEquals(lsTweet.get(0).getId(),tweet.getId());
    assertEquals(lsTweet.get(0).getCoordinates(),tweet.getCoordinates());
    assertEquals(lsTweet.get(0).getText(),tweet.getText());
  }

}
