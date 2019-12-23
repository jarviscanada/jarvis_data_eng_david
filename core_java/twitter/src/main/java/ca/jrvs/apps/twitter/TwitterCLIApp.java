package ca.jrvs.apps.twitter;

import ca.jrvs.apps.twitter.DAO.Controller;
import ca.jrvs.apps.twitter.DAO.HttpHelper;
import ca.jrvs.apps.twitter.DAO.Service;
import ca.jrvs.apps.twitter.DAO.TwitterController;
import ca.jrvs.apps.twitter.DAO.TwitterDao;
import ca.jrvs.apps.twitter.DAO.TwitterHttpHelper;
import ca.jrvs.apps.twitter.DAO.TwitterService;

public class TwitterCLIApp {
  private Controller controller;

  public TwitterCLIApp(Controller controller) {
    this.controller = controller;
  }

  public void run(String[] args) {
    String type = args[0].toUpperCase();

    if (type.equals("POST")) {
      System.out.println(controller.postTweet(args));
    } else if (type.equals("SHOW")) {
      System.out.println(controller.showTweet(args));
    } else if (type.equals("DELETE")) {
      controller.deleteTweet(args).forEach(System.out::println);
    } else {
      throw new IllegalArgumentException("Need POST, SHOW or DELETE as first argument");
    }
  }

  public static void main(String[] args) {
    String consumerKey = System.getenv("consumerKey");
    String consumerSecret = System.getenv("consumerSecret");
    String accessToken =  System.getenv("accessToken");
    String tokenSecret = System.getenv("tokenSecret");
    HttpHelper hHelp = new TwitterHttpHelper(consumerKey,consumerSecret,accessToken,tokenSecret);
    TwitterDao td = new TwitterDao(hHelp);
    Service service = new TwitterService(td);
    Controller controller = new TwitterController(service);
    TwitterCLIApp app = new TwitterCLIApp(controller);
    app.run(args);
  }

}
