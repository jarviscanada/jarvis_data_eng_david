package ca.jrvs.apps.twitter.springboot;

import ca.jrvs.apps.twitter.DAO.Controller;
import ca.jrvs.apps.twitter.DAO.CrdDao;
import ca.jrvs.apps.twitter.DAO.HttpHelper;
import ca.jrvs.apps.twitter.DAO.Service;
import ca.jrvs.apps.twitter.DAO.TwitterController;
import ca.jrvs.apps.twitter.DAO.TwitterDao;
import ca.jrvs.apps.twitter.DAO.TwitterHttpHelper;
import ca.jrvs.apps.twitter.DAO.TwitterService;
import ca.jrvs.apps.twitter.TwitterCLIApp;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TwitterCLIBean {
  public static void main(String[] args) {
    ApplicationContext context = new AnnotationConfigApplicationContext(TwitterCLIBean.class);
    TwitterCLIApp app = context.getBean(TwitterCLIApp.class);
    app.run(args);
  }

  @Bean
  public TwitterCLIApp twitterCLIapp(Controller controller) {
    return new TwitterCLIApp(controller);
  }

  @Bean
  public Controller controller(Service service) {
    return new TwitterController(service);
  }

  @Bean
  public Service service(CrdDao dao) { return new TwitterService(dao); }

  @Bean
  public CrdDao crdDao(HttpHelper httpHelper) {
    return new TwitterDao(httpHelper);
  }

  @Bean
  HttpHelper helper() {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken =  System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    return new TwitterHttpHelper(consumerKey,consumerSecret,accessToken,tokenSecret);
  }
}
