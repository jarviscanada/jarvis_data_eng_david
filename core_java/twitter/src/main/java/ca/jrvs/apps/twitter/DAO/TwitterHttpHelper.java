package ca.jrvs.apps.twitter.DAO;

import com.google.gdata.util.common.base.PercentEscaper;
import java.io.IOException;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TwitterHttpHelper implements HttpHelper {
  private static String CONSUMER_KEY;
  private static String CONSUMER_SECRET;
  private static String ACCESS_TOKEN;
  private static String TOKEN_SECRET;

  private OAuthConsumer consumer;
  private HttpClient httpClient;

  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
    this.CONSUMER_KEY = consumerKey;
    this.CONSUMER_SECRET = consumerSecret;
    this.ACCESS_TOKEN = accessToken;
    this.TOKEN_SECRET = tokenSecret;
  }

  public HttpResponse httpIntermediate(boolean type, URI uri)
      throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException, IOException {
    consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
        CONSUMER_SECRET);
    consumer.setTokenWithSecret(ACCESS_TOKEN,TOKEN_SECRET);
    httpClient = HttpClientBuilder.create().build();
    if (type == true) {
      // httpPost
      HttpPost request = new HttpPost(uri);
      consumer.sign(request); //add headers
      return  httpClient.execute(request);
    } else {
      //httpGet
      HttpGet request = new HttpGet(uri);
      consumer.sign(request); //add headers
      return  httpClient.execute(request);
    }
  }

  @Override
  public HttpResponse httpPost(URI uri) {
    try {
      /*
      HttpPost request = new HttpPost(uri);
      httpClient = HttpClientBuilder.create().build();
      consumer.sign(request); //add headers
      return  httpClient.execute(request);
       */
      return httpIntermediate(true, uri);
    } catch (OAuthMessageSignerException e) {
      e.printStackTrace();
    } catch (OAuthExpectationFailedException e) {
      e.printStackTrace();
    } catch (OAuthCommunicationException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      /*
      httpClient = HttpClientBuilder.create().build();
      consumer.sign(request); //add headers
      return  httpClient.execute(request);
      */
      return httpIntermediate(false, uri);
    } catch (OAuthMessageSignerException e) {
      e.printStackTrace();
    } catch (OAuthExpectationFailedException e) {
      e.printStackTrace();
    } catch (OAuthCommunicationException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void main(String[] args) throws IOException {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken =  System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");

    //testing HttpGet
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    TwitterHttpHelper thh = new TwitterHttpHelper(consumerKey,consumerSecret,accessToken,tokenSecret);
    String status = "Testing Post: " + timestamp;
    PercentEscaper percentEscaper = new PercentEscaper("",false);
    URI uriGet = URI.create("https://api.twitter.com/1.1/statuses/show.json?id=1206530744622948353");
    URI uriPost = URI.create("https://api.twitter.com/1.1/statuses/update.json?status="  + percentEscaper.escape(status));
    System.out.println(EntityUtils.toString(thh.httpGet(uriGet).getEntity()));
//    System.out.println(EntityUtils.toString(thh.httpPost(uriPost).getEntity()));

  }
}
