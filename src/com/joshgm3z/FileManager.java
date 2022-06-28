package com.joshgm3z;

import com.joshgm3z.data.LogData;
import com.joshgm3z.data.LogHeader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManager {

    public List<LogData> readFileToString(String filePath) {
        File logIdFile = new File(filePath);
        StringBuilder data = new StringBuilder();
        try {
            Scanner myReader = new Scanner(logIdFile);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine().trim();
                if (!line.isEmpty()) {
                    data.append(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return parseData(data.toString());
    }

    public List<LogData> parseData(String data) {
        return buildLogData(data, buildLogHeader(data));
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

    public List<LogData> buildLogData(String data, List<LogHeader> headerList) {
        List<LogData> logDataList = new ArrayList<>();
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
                    logDataList.add(new LogData(logIdNumber, logIdName, headerValue));
                }
            }
        }
        return logDataList;
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

}
