package com.joshgm3z;

import com.joshgm3z.data.LogData;
import com.joshgm3z.data.LogHeader;
import com.joshgm3z.data.Param;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileManager {

    private LogParser mLogParser;

    public FileManager(LogParser logParser) {
        mLogParser = logParser;
    }

    private String readData(String filePath) {
        File logIdFile = new File(filePath);
        StringBuilder data = new StringBuilder();
        try (Scanner scanner = new Scanner(logIdFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    data.append(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return data.toString();
    }


    public List<LogData> findLogs(String logIdFile, String projectRootPath) {
        String data = readData(logIdFile);

        // build initial log data list
        mLogParser.buildLogList(data);

        // get paths to all java files in project
        List<String> allFilePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(projectRootPath))) {
            allFilePaths = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // read all java files to identify log params
        for (String filePath : allFilePaths) {
            if (filePath.endsWith(".java")) {
                String fileData = readData(filePath);
                mLogParser.buildLogParams(fileData);
            }
        }
        return mLogParser.getLogDataList();
    }

    public List<String> readIntoList(String logIdFilePath) {
        List<String> fileList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(logIdFilePath));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    fileList.add(mLogParser.removeSymbol(line, "\""));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileList;
    }
}
