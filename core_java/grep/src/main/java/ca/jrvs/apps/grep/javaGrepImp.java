package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class javaGrepImp implements javaGrep {

  private String regex;
  private String rootDir;
  private String outFile;

  @Override
  public List<File> listFiles(String rootDir) throws IOException {
    List<File> allFiles = new ArrayList<File>();
    if (new File(rootDir).isFile()) {
      allFiles.add(new File(rootDir));
    } else {
      for (File fileName : new File(rootDir).listFiles()) {
        if (fileName.isDirectory()) {
          allFiles.addAll(listFiles(fileName.getAbsolutePath()));
        } else {
          allFiles.add(fileName);
        }
      }
    }
    return allFiles;
  }

  @Override
  public List<String> readLines(File inputFile) {
    List<String> readSave = new ArrayList<String>();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(inputFile));
      String line = reader.readLine();
      while (line != null) {
        readSave.add(line);
        line = reader.readLine();
      }
    } catch (IllegalArgumentException i) {
      System.out.println("Input could not be found");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return readSave;
  }

  @Override
  public boolean containsPattern(String line) {
    Pattern pattern = Pattern.compile(this.regex);
    Matcher matcher = pattern.matcher(line);
    return matcher.matches();
  }

  @Override
  public void writeToFile(List<String> lines) throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(new File(this.outFile)));
    try {
      for (String l : lines) {
        bw.write(l);
        bw.write("\n");
      }
      bw.close();
    } catch (IOException i) {
      i.printStackTrace();
    } finally {
      try {
        if (bw != null) {
          bw.close();
        }
      } catch (Exception ex) {
        System.out.println("Error in closing the BufferedWriter" + ex);
      }
    }
  }

  @Override
  public String getRootPath() {
    return this.rootDir;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootDir = rootPath;
  }

  @Override
  public String getRegex() {
    return this.rootDir;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }

  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<String>();
    for (File f : listFiles(rootDir)) {
      for (String line : readLines(f)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: regex rootPath outFile");
    }
    javaGrepImp javaGrepImp = new javaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


}
