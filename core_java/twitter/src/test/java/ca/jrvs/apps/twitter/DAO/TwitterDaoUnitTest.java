package ca.jrvs.apps.twitter.DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import ca.jrvs.apps.twitter.model.Coordinates;
import ca.jrvs.apps.twitter.model.Tweet;
import java.io.IOException;
import oauth.signpost.http.HttpResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TwitterDaoUnitTest {

  @Mock
  HttpHelper mockHelper;
  @InjectMocks
  TwitterDao dao;

  public String tweetJsonStr = "{\n   \"created_at\":\"Mon Feb 18 21:24:39 +0000 2019\",\n   "
      + "\"id\":1097607853932564480,\n   \"id_str\":\"1097607853932564480\",\n   "
      + "\"text\":\"test with loc223\",\n   \"entities\":{\n      \"hashtags\":[],\n      "
      + "\"user_mentions\":[]\n   },\n   \"coordinates\":null,\n   \"retweet_count\":0,\n   "
      + "\"favorite_count\":0,\n   \"favorited\":false,\n   \"retweeted\":false\n}";
  public Tweet tweet;
  public Long id = 1097607853932564480L;

  @Mock
  public HttpResponse httpResponse;

  @Before
  public void init() {
    String hashtag = "#abc";
    String text = "@somone sometext " + hashtag + " " + System.currentTimeMillis();
    float lat = 1f;
    float lon = -1f;
    Coordinates coordinates = new Coordinates(lat, lon);
    when(mockHelper.httpPost(isNotNull())).thenThrow(new RuntimeException("mock POST exception"));
    when(mockHelper.httpGet(isNotNull())).thenThrow(new RuntimeException("mock GET exception"));
    tweet = new Tweet();
    tweet.setText(text);
    tweet.setCoordinates(coordinates);
    try {
      dao.create(tweet);
      fail();
    } catch (RuntimeException e) {
      assertTrue(true);
    }
  }

  @Test
  public void createTweet() {
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = null;
    try {
      expectedTweet = TwitterDao.toObjectFromJson(tweetJsonStr,Tweet.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    doReturn(expectedTweet).when(spyDao).create(any(Tweet.class));
    Tweet testTweet = spyDao.create(tweet);
    assertNotNull(testTweet);
    assertNotNull(testTweet.getText());
  }

  @Test
  public void showTweet() {
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = null;
    try {
      expectedTweet = TwitterDao.toObjectFromJson(tweetJsonStr,Tweet.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    doReturn(expectedTweet).when(spyDao).findById(any(Long.class));
    Tweet testTweet = spyDao.findById(id);
    assertNotNull(testTweet);
    assertNotNull(testTweet.getText());
    assertEquals(id,testTweet.getId());
  }


  @Test
  public void deleteTweet() {
    TwitterDao spyDao = Mockito.spy(dao);
    Tweet expectedTweet = null;
    try {
      expectedTweet = TwitterDao.toObjectFromJson(tweetJsonStr,Tweet.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    doReturn(expectedTweet).when(spyDao).deleteById(any(Long.class));
    Tweet testTweet = spyDao.deleteById(id);
    assertNotNull(testTweet);
    assertNotNull(testTweet.getText());
    assertEquals(id,testTweet.getId());
  }
}
