package ca.jrvs.apps.practice;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * RegexExcImp
 */

public class RegexExcImp implements RegexExc {

  /**
   * return true if filename extension is jpg or jpeg (case insensitive)
   *
   * @param filename
   * @return
   */
  public boolean matchJpeg(String filename) {
    String regex = new String("[-\\w]+\\.(jpg|jpeg)$");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(filename);
    return matcher.matches();
  }

  /**
   * return true if ip is valid to simplify the problem, IP address range is from 0.0.0.0 to
   * 999.999.999.999
   *
   * @param ip
   * @return
   */
  public boolean matchIp(String ip) {
    String regex = new String("\\d+\\.\\d+\\.\\d+\\.\\d+");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(ip);
    return matcher.matches();
  }

  /**
   * return true if line is empty (e.g. empty, white space, tabs, etc..)
   *
   * @param line
   * @return
   */
  public boolean isEmptyLine(String line) {
    String regex = new String("^\\s*$");
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(line);
    return matcher.matches();
  }

  public static void main(String[] args) {
    /*
    RegexExcImp rce = new RegexExcImp();
    String jpg = new String("abc.jpg");
    String jpeg = new String("abc.jpeg");

    String ip1 = new String("192.16.0.1");
    String ip2 = new String("182.168.100.100");

    String emptyLine = new String(" ");
    String nonEmpty = new String(" w");

    if (rce.matchJpeg(jpg)) {
      System.out.println("jpg matches");
    } else {
      System.out.println("jpg does not match");
    }

    if (rce.matchJpeg(jpeg)) {
      System.out.println("jpeg matches");
    } else {
      System.out.println("jpeg does not match");
    }

    if (rce.matchIp(ip1)) {
      System.out.println(ip1 + " matches");
    } else {
      System.out.println(ip1 + " does not match");
    }

    if (rce.matchIp(ip2)) {
      System.out.println(ip2 + " matches");
    } else {
      System.out.println(ip2 + " does not match");
    }

    if (rce.isEmptyLine(emptyLine)) {
      System.out.println("empty line is empty");
    } else {
      System.out.println("EmptyLine regex error");
    }
    if (!rce.isEmptyLine(nonEmpty)) {
      System.out.println("nonEmpty line is not empty");
    } else {
      System.out.print("nonEmpty line regex error");
    }
     */
  }


}
