package ca.jrvs.apps.grep;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class javaGrepLambda extends javaGrepImp {

  private final Logger logger = LoggerFactory.getLogger(javaGrepLambda.class);

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> returnList = new ArrayList<File>();
    try {
      returnList = Files.walk(Paths.get(rootDir))
          .map(x -> x.toFile())
          .filter(x -> x.isFile())
          .collect(Collectors.toList());
    } catch (IOException e) {
      logger.error("listFiles IOException: " + e);
    }
    return returnList;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> listLines = new ArrayList<String>();
    try {
      listLines = Files.lines(inputFile.toPath()).collect(Collectors.toList());
    } catch (IOException e) {
      logger.error("readLines IOException: " + e);
    }
    return listLines;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }
    javaGrepLambda javaGrepLambda = new javaGrepLambda();
    javaGrepLambda.setRegex(args[0]);
    javaGrepLambda.setRootPath(args[1]);
    javaGrepLambda.setOutFile(args[2]);

    try {
      javaGrepLambda.process();
    } catch (Exception ex) {
      throw new RuntimeException("javaGrepLamba failed to run");
    }
  }
}
