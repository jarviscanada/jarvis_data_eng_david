# Grep Project #
## 1) Introduction ##
The purpose of this application is to allow users to search all files in a root directory recursively to locate all lines within each files
that have a match with a user inputted regex, and write this information to a file as directed by the user. This project is split into 2 implementations -
first uses a standard loop to travserse through the directories, while the other makes use of lambda streams. This project has also familiarized me
with using Intellij as a IDE, especially with regards to reading Source code.

## 2) Usage ##
**Format of arguments:** <br />
```<regex_pattern> <location of root directory> <file directory for grep output>```<br />
**Example of Usage:** <br />
```.*IllegalArgumentException.* ./grep/src ~/javaGrep.out```

## 3) Pseudocode ##
matchedLines = []<br />
for file in listFilesRecursively(rootDir)<br />
  for line in readLines(file)<br />
      if containsPattern(line)<br />
        matchedLines.add(line)<br />
writeToFile(matchedLines)<br />

## 4) Performance Issues ##
Advantages of using streams over the loops have been that it condenses the code, making it easier to read and streams, not being stored physically as a 
data structure, allow us to work with much larger files.<br />
Sequentially iterates throw each line within each file that is written on disk which can impede performance.

## 5) Improvements ##
Things I think can be added to the project:
1) Multithread the program to read multiple files at once
2) Can make use of shell grep command in java - is faster because iterates through every byte
3) Add the file location of the grepped text to the left in the output file
