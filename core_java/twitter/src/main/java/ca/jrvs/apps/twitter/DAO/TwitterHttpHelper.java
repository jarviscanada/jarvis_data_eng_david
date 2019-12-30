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

public class TwitterHttpHelper implements HttpHelper {
  private static String CONSUMER_KEY;
  private static String CONSUMER_SECRET;
  private static String ACCESS_TOKEN;
  private static String TOKEN_SECRET;

  private OAuthConsumer consumer;
  private HttpClient httpClient;
  private static Logger logger = LoggerFactory.getLogger(TwitterHttpHelper.class);

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
    HttpResponse response = null;
    try {
      response = httpIntermediate(true, uri);
    } catch (OAuthMessageSignerException e) {
      TwitterHttpHelper.logger.error("POST OAuthMessageSigning exception: " + e);
    } catch (OAuthExpectationFailedException e) {
      TwitterHttpHelper.logger.error("POST OAuthExpectation exception: " + e);
    } catch (OAuthCommunicationException e) {
      TwitterHttpHelper.logger.error("POST OAuthCommunication exception: " + e);
    } catch (ClientProtocolException e) {
      TwitterHttpHelper.logger.error("POST ClientProtocol exception: " + e);
    } catch (IOException e) {
      TwitterHttpHelper.logger.error("POST IOException: " + e);
    }
    return response;
  }

  @Override
  public HttpResponse httpGet(URI uri) {
    try {
      return httpIntermediate(false, uri);
    } catch (OAuthMessageSignerException e) {
      TwitterHttpHelper.logger.error("GET OAuthMessageSigning exception: " + e);
    } catch (OAuthExpectationFailedException e) {
      TwitterHttpHelper.logger.error("GET OAuthExpectation exception: " + e);
    } catch (OAuthCommunicationException e) {
      TwitterHttpHelper.logger.error("GET OAuthCommunication exception: " + e);
    } catch (ClientProtocolException e) {
      TwitterHttpHelper.logger.error("GET ClientProtocol exception: " + e);
    } catch (IOException e) {
      TwitterHttpHelper.logger.error("GET IOException: " + e);
    }
    return null;
  }

}
