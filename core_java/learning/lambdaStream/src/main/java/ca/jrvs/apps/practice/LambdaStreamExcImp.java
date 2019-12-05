package ca.jrvs.apps.practice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * LambdaStream interface
 */

public class LambdaStreamExcImp implements LambdaStreamExc {

  //convert vararg strings to Stream
  public Stream<String> createStrStream(String... strings) {
    return Stream.of(strings);
  }

  //convert string in stream to uppercase
  public Stream<String> toUpperCase(String... strings) {
    LambdaStreamExcImp inst = new LambdaStreamExcImp();
    return inst.createStrStream(strings).map(String::toUpperCase);
  }

  // returns stream that does not contain pattern
  public Stream<String> filter(Stream<String> stringStream, String pattern) {
    return stringStream.filter(x -> !x.contains(pattern));
  }

  // create intStream from array
  public IntStream createIntStream(int[] arr) {
    return IntStream.of(arr);
  }

  // Convert a stream to a  list
  public <E> List<E> toList(Stream<E> stream) {
    return stream.collect(Collectors.toList());
  }

  //Convert IntStream to list
  public List<Integer> toList(IntStream intStream) {
    return intStream
        .boxed()
        .collect(Collectors.toList());
  }

  //return IntStream in rangeInclusive(start,end)
  public IntStream createIntStream(int start, int end) {
    return IntStream.rangeClosed(start, end);
  }

  //convert intStream to doubleStream & compute sqrt
  public DoubleStream squareRootIntStream(IntStream intStream) {
    return intStream
        .mapToDouble(x -> x * x);
  }

  //filter all even numbers & return only odd
  public IntStream getOdd(IntStream intStream) {
    return intStream
        .filter(x -> x % 2 == 1);
  }

  //ret lambda func that contains prefix + suffix
  public Consumer<String> getLambdaPrinter(String prefix, String suffix) {
    return x -> System.out.println(prefix + "> " + x + " <" + suffix);
  }

  // print each message with a given printer
  public void printMessages(String[] messages, Consumer<String> printer) {
    Stream.of(messages)
        .forEach(x -> printer.accept(x));
  }

  //print all odd numbers from IntStream
  public void printOdd(IntStream intStream, Consumer<String> printer) {
    intStream
        .filter(x -> x % 2 == 1)
        .mapToObj(x -> ((Integer) x).toString())
        .forEach(x -> printer.accept(x));
  }

  //use flatMap
  public Stream<Integer> flatMapInt(Stream<List<Integer>> ints) {
    return ints
        .flatMap(x -> x.stream())
        .map(x -> x * x);
  }

  public static void main(String[] args) {
    LambdaStreamExc lse = new LambdaStreamExcImp();
    List<Integer> l1 = new ArrayList<Integer>();
    l1.add(0, 1); // adds 1 at 0 index
    l1.add(1, 2); // adds 2 at 1 index

    List<Integer> l2 = new ArrayList<Integer>();
    l2.add(1);
    l2.add(2);
    l2.add(3);

    Stream<List<Integer>> ss = Stream.of(l1, l2);
    lse.flatMapInt(ss).forEach(x -> System.out.println(x));


    /*
    //Testing getLambdaPrinter
    LambdaStreamExc lse = new LambdaStreamExcImp();
    Consumer<String> printer =
    lse.getLambdaPrinter("start>", "<end");
    printer.accept("Message body");
     */
    /*
    //Testing of printMessages/printOdd
    LambdaStreamExc lse = new LambdaStreamExcImp();
    int[] messages = {1,2,5};
    lse.printOdd(IntStream.of(messages), lse.getLambdaPrinter("msg:", "!") );
    */
  }

}
