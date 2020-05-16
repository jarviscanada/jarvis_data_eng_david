package ca.jrvs.apps.practice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class RegexExcTest {

  /**
   * Rigorous Test :-)
   */
  @Test
  public void jpegTest() {
    RegexExc rxe = new RegexExcImp();
    assertTrue("WARNING: abc.jpg should match", rxe.matchJpeg("abc.jpg"));
    assertTrue("WARNING: abc.jpeg should match", rxe.matchJpeg("abc.jpeg"));

    assertFalse("WARNING: .jpg should NOT match", rxe.matchJpeg(".jpg"));
    assertFalse("WARNING: .jpeg should NOT match", rxe.matchJpeg(".jpeg"));
    assertFalse("WARNING: abc.jpggx should NOT match", rxe.matchJpeg("abc.jpggx"));
    assertFalse("WARNING: jpg should NOT match", rxe.matchJpeg("jpg"));
    assertFalse("WARNING: jpeg should NOT match", rxe.matchJpeg("jpeg"));
  }

  @Test
  public void matchIpTest() {
    RegexExc rxe = new RegexExcImp();
    assertTrue("WARNING: 192.16.0.1 should match", rxe.matchIp("192.16.0.1"));
    assertTrue("WARNING: 182.168.100.100 should match!",rxe.matchIp("182.168.100.100"));

    assertFalse("WARNING: 192.168 should NOT match", rxe.matchIp("192.168"));
    assertFalse("WARNING: 192#168#0#1 should NOT match", rxe.matchIp("192#168#0#1"));
  }

  @Test
  public void testEmpty() {
    RegexExc rxe = new RegexExcImp();
    assertTrue("WARNING: space, \\t & \\n in string should match", rxe.isEmptyLine("  \t\n"));
    assertTrue("WARNING: empty string should match", rxe.isEmptyLine(""));
    assertFalse("WARNING: '-' should NOT match", rxe.isEmptyLine("     -"));
  }
}
