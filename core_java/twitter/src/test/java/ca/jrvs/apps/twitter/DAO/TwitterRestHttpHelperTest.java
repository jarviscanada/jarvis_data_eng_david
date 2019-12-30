package ca.jrvs.apps.twitter.DAO;

import static org.junit.Assert.assertEquals;

import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;

public class TwitterRestHttpHelperTest {
  private HttpHelper httpHelper;
  private String s = null;
  @Before
  public void init() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken = System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    this.httpHelper = new TwitterHttpHelper(consumerKey, consumerSecret, accessToken,
        tokenSecret);
  }

  @Test
  public void httpPostTest() {
    String hashtag = "#abc";
    String text = "@somone sometext " + hashtag + " " + System.currentTimeMillis();
    PercentEscaper percentEscaper = new PercentEscaper("",false);
    URI uriPost = URI.create("https://api.twitter.com/1.1/statuses/update.json?status="  + percentEscaper.escape(text));
    try {
      s = EntityUtils.toString(httpHelper.httpPost(uriPost).getEntity());
      System.out.println(s);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void httpGetTest() {
    URI uriGet = URI.create("https://api.twitter.com/1.1/statuses/show.json?id=1209275746847514625"); //use Test tweet
    String sGet = null;
    try {
      sGet = EntityUtils.toString(httpHelper.httpGet(uriGet).getEntity());
      System.out.println(sGet);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }



}
