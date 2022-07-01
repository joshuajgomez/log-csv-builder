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

    List<LogData> mLogDataList = new ArrayList<>();

    private final String WORD_INT_ARRAY = "new Integer[]{";
    private final String WORD_INT_ARRAY1 = "newInteger[]{";
    private final int MAX_PARAM_NAME_LENGTH = 30;

    public void readFileToString(String filePath) {
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
        parseData(data.toString());
    }

    public void parseData(String data) {
        buildLogData(data, buildLogHeader(data));
    }

    private List<LogHeader> buildLogHeader(String data) {
        List<LogHeader> logHeaderList = new ArrayList<>();
        // Log header
        String[] splitBySemiColon = data.split(";");
        for (String logId : splitBySemiColon) {
            if (logId.contains("int")) {
                // LogId or Log header constant
                String output = logId.substring(logId.lastIndexOf("int ") + 4);
                String[] splitByEquals = output.split("=");
                if (!output.contains("+")) {
                    String logHeaderName = splitByEquals[0].trim();
                    String logHeaderValueString = splitByEquals[1].trim();
                    int logHeaderValue = Integer.parseInt(logHeaderValueString);
                    logHeaderList.add(new LogHeader(logHeaderName, logHeaderValue));
                }
            }
        }
        return logHeaderList;
    }

    public void buildLogData(String data, List<LogHeader> headerList) {
        String[] splitBySemiColon = data.split(";");
        for (String logId : splitBySemiColon) {
            if (logId.contains("int")) {
                // LogId or Log header constant
                String output = logId.substring(logId.lastIndexOf("int ") + 4);
                String[] splitByEquals = output.split("=");
                if (output.contains("+")) {
                    // LogId
                    String _removeLogName = output.substring(output.lastIndexOf("=") + 1).trim();
                    String _logIdNumberString = _removeLogName.substring(_removeLogName.lastIndexOf("+") + 1).trim();
                    String logIdName = splitByEquals[0].trim();
                    String logIdHeader = _removeLogName.substring(0, _removeLogName.lastIndexOf("+")).trim();
                    int logIdNumber = Integer.parseInt(_logIdNumberString);
                    int headerValue = getHeaderValue(logIdHeader, headerList);
                    mLogDataList.add(new LogData(logIdNumber, logIdName, headerValue));
                }
            }
        }
    }

    private int getHeaderValue(String logIdHeader, List<LogHeader> headerList) {
        int headerValue = -1;
        for (LogHeader logHeader : headerList) {
            if (logHeader.getHeaderName().equals(logIdHeader)) {
                headerValue = logHeader.getValue();
            }
        }
        return headerValue;
    }

    public List<LogData> readAllFiles(String projectRootPath) {
        List<String> allFilePaths = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(projectRootPath))) {
            allFilePaths = paths.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String filePath : allFilePaths) {
            if (filePath.endsWith(".java")) {
                readFile(filePath);
            }
        }
        return mLogDataList;
    }

    private void readFile(String javaFile) {
        StringBuilder data = new StringBuilder();
        try {
            Scanner scanner = new Scanner(new File(javaFile));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty()) {
                    data.append(line);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        String[] splitBySemicolon = data.toString().split(";");
        for (String javaLine : splitBySemicolon) {
            if (javaLine.startsWith("mLogUtility") && !javaLine.contains("=")) {
                parseLogParams(javaLine);
            } else {
                // ignore
            }
        }
    }

    private void parseLogParams(String javaLine) {
        int position = -1;
        for (int i = 0; i < mLogDataList.size(); i++) {
            if (javaLine.contains(mLogDataList.get(i).getName() + ",")) {
                position = i;
                break;
            }
        }
        if (position != -1) {
            LogData logData = mLogDataList.get(position);
            if (logData != null) {
//            System.out.println();
//            System.out.println();
//            System.out.println("logData: " + logData);
//            System.out.println("javaLine: " + javaLine);
                String[] splitByLogName = javaLine.split(logData.getName());
                if (splitByLogName.length > 1) {
                    String paramData = splitByLogName[1];
// dwddd                   System.out.println("paramData: " + paramData);
                    List<Param> paramList;
                    if (paramData.contains(WORD_INT_ARRAY) || paramData.contains(WORD_INT_ARRAY1)) {
                        // integer array
                        paramList = parseParamIntArray(paramData);
                    } else {
                        // single variable
                        paramList = parseParamOthers(paramData);
                    }
                    if (paramList != null) {
                        logData.setParamList(paramList);
                        mLogDataList.set(position, logData);
                    }
                }
            }
        }
//        System.out.println(mLogDataList);
    }

    private List<Param> parseParamOthers(String paramData) {
        List<Param> paramMap = new ArrayList<>();
        String[] splitByComma = paramData.split(",");
        if (splitByComma.length > 1) {
            String split = splitByComma[1];
            split = removeCommonSymbols(split);
            String paramName = "param";
            if (!split.contains("\"") && split.length() < MAX_PARAM_NAME_LENGTH) {
                // status
                paramName = split;
            }
            paramMap.add(new Param(paramName, LogData.ParamSize.STRING, 2, 1));
        }
        return paramMap;
    }

    private List<Param> parseParamIntArray(String paramData) {
        List<Param> paramList = new ArrayList<>();
        paramData = removeSymbol(paramData, WORD_INT_ARRAY);
        paramData = removeSymbol(paramData, WORD_INT_ARRAY1);
        paramData = removeCommonSymbols(paramData);
        String[] splitByComma = paramData.split(",");
        for (String split : splitByComma) {
            if (!split.trim().isEmpty() && !split.contains("\"")) {
                paramList.add(new Param(split.trim(), LogData.ParamSize.INTEGER, 3, 1));
            }
        }
        return paramList;
    }

    private String removeCommonSymbols(String paramData) {
        return removeSymbol(paramData,
                "{", "}",
                "(", ")");
    }

    private String removeSymbol(String paramData, String... symbol) {
        for (String _s : symbol) {
            while (paramData.contains(_s)) {
                paramData = paramData.replace(_s, "");
            }
        }
        return paramData.trim();
    }
}
