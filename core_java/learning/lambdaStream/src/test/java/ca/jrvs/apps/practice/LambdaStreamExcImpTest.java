package ca.jrvs.apps.practice;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class LambdaStreamExcImpTest {

  /**
   * Rigorous Test :-)
   */

  private LambdaStreamExc lse = new LambdaStreamExcImp();

  @Before
  public void setup() {
    /*
    In future Junit tests, set variables used in tester functions here
     */
  }

  @Test
  public void objStreamTest1() {
    // Tests first 3 functions implemented
    List<String> input = Arrays.asList("validate", "compile", "test");
    //test convert to stream correctly
    List<String> inputUpper = Arrays.asList("VALIDATE", "COMPILE", "TEST");
    //test filter
    List<String> inputFilter = Arrays.asList("compile", "test");

    assertEquals("CreateStrStream fail", input.stream().collect(Collectors.toList()),
        lse.createStrStream("validate", "compile", "test").collect(Collectors.toList()));

    //test toUpperCase
    assertEquals("toUpperCase method failed", inputUpper.stream().collect(Collectors.toList()),
        lse.toUpperCase("validate", "compile", "test").collect(Collectors.toList()));

    //test filter
    assertEquals("filter method failed", inputFilter.stream().collect(Collectors.toList()),
        lse.filter(input.stream(), "a").collect(Collectors.toList()));

    //test conversion of stream to list
    assertEquals("toList implementation error", input, lse.toList(input.stream()));
  }

  @Test
  public void intStreamTest1() {

    // in future define arguments in @Before function
    int[] myIntArray = {1, 2, 3};
    int[] oddArray = {1, 3};
    double[] doubleArray = {1.0, 4.0, 9.0};

    List<Integer> intList = new ArrayList<Integer>();
    for (int i : myIntArray) {
      intList.add(i);
    }

    // test whether creating intStream works
    assertEquals("createIntStream implementation error",
        Arrays.stream(myIntArray).boxed().collect(Collectors.toList()),
        lse.createIntStream(myIntArray).boxed().collect(Collectors.toList()));

    // test whether intStream to list works
    assertEquals("Integer Stream toList did not work", intList,
        lse.toList(intList.stream()));

    //test if createIntStream - 2 arguments work
    assertEquals("inclusive createIntStream implement error",
        Arrays.stream(myIntArray).boxed().collect(Collectors.toList()),
        lse.createIntStream(1, 3).boxed().collect(Collectors.toList()));

    //test sqrtIntStreeam function
    assertEquals("squareRootIntStream implementation error",
        Arrays.stream(doubleArray).boxed().collect(Collectors.toList()),
        lse.squareRootIntStream(Arrays.stream(myIntArray)).boxed().collect(Collectors.toList()));

    //test odd numbers
    assertEquals("getOdd implementation error",
        Arrays.stream(oddArray).boxed().collect(Collectors.toList()),
        lse.getOdd(Arrays.stream(myIntArray)).boxed().collect(Collectors.toList()));
  }

  @Test
  public void consumerTest1() {

    LambdaStreamExc lse = new LambdaStreamExcImp();

    Consumer<String> printer = lse.getLambdaPrinter("start>", "<end");
    printer.accept("Message body");
    Consumer<String> expect = x -> System.out.println("start> " + x + " <end");
    assertEquals("getLambdaPrinter implementation error", expect,
        lse.getLambdaPrinter("start", "end"));

    //test printMessages
  }


}
