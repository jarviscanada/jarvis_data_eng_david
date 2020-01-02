package ca.jrvs.apps.twitter.DAO;

import java.io.IOException;
import java.net.URI;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TwitterHttpHelper implements HttpHelper {
  private static String CONSUMER_KEY;
  private static String CONSUMER_SECRET;
  private static String ACCESS_TOKEN;
  private static String TOKEN_SECRET;

  private OAuthConsumer consumer;
  private HttpClient httpClient;
  private final Logger logger = LoggerFactory.getLogger(TwitterHttpHelper.class);

  /**
   * Consturctor with keys to access TwitterAPI
   */
  public TwitterHttpHelper(String consumerKey, String consumerSecret, String accessToken, String tokenSecret) {
    this.CONSUMER_KEY = consumerKey;
    this.CONSUMER_SECRET = consumerSecret;
    this.ACCESS_TOKEN = accessToken;
    this.TOKEN_SECRET = tokenSecret;
    consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
        CONSUMER_SECRET);
    consumer.setTokenWithSecret(ACCESS_TOKEN,TOKEN_SECRET);
    httpClient = HttpClientBuilder.create().build();
  }

  /*
   * Consturctor no args provided for SpringBoot
   */
  public TwitterHttpHelper() {
    this.CONSUMER_KEY = System.getenv("consumerKey");
    this.CONSUMER_SECRET = System.getenv("consumerSecret");
    this.ACCESS_TOKEN =  System.getenv("accessToken");
    this.TOKEN_SECRET = System.getenv("tokenSecret");
    consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY,
        CONSUMER_SECRET);
    consumer.setTokenWithSecret(ACCESS_TOKEN,TOKEN_SECRET);
    httpClient = HttpClientBuilder.create().build();
  }

  /**
   * Helps HTTP execute request
   * @param boolean,uri
   * @return
   */
  public HttpResponse httpIntermediate(boolean type, URI uri)
      throws OAuthCommunicationException, OAuthExpectationFailedException, OAuthMessageSignerException, IOException {
    if (type == true) {
      HttpPost request = new HttpPost(uri);
      consumer.sign(request); 
      return  httpClient.execute(request);
    } else {
      HttpGet request = new HttpGet(uri);
      consumer.sign(request); 
      return  httpClient.execute(request);
    }
  }

  /**
   * Execute a HTTP Post call
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpPost(URI uri) {
    HttpResponse response = null;
    try {
      response = httpIntermediate(true, uri);
    } catch (OAuthMessageSignerException e) {
      logger.error("POST OAuthMessageSigning exception: " + e);
    } catch (OAuthExpectationFailedException e) {
      logger.error("POST OAuthExpectation exception: " + e);
    } catch (OAuthCommunicationException e) {
      logger.error("POST OAuthCommunication exception: " + e);
    } catch (ClientProtocolException e) {
      logger.error("POST ClientProtocol exception: " + e);
    } catch (IOException e) {
      logger.error("POST IOException: " + e);
    }
    return response;
  }

  /**
   * Execute a HTTP Get call
   * @param uri
   * @return
   */
  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      return httpIntermediate(false, uri);
    } catch (OAuthMessageSignerException e) {
      logger.error("GET OAuthMessageSigning exception: " + e);
    } catch (OAuthExpectationFailedException e) {
      logger.error("GET OAuthExpectation exception: " + e);
    } catch (OAuthCommunicationException e) {
      logger.error("GET OAuthCommunication exception: " + e);
    } catch (ClientProtocolException e) {
      logger.error("GET ClientProtocol exception: " + e);
    } catch (IOException e) {
      logger.error("GET IOException: " + e);
    }
    return null;
  }

}
