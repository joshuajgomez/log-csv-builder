package com.joshgm3z;

import com.joshgm3z.data.LogData;
import com.joshgm3z.data.LogHeader;
import com.joshgm3z.data.Param;

import java.util.ArrayList;
import java.util.List;

public class LogParser {

    private List<LogData> mLogDataList = new ArrayList<>();

    private final String WORD_INT_ARRAY = "new Integer[]{";
    private final String WORD_INT_ARRAY1 = "newInteger[]{";
    private final int MAX_PARAM_NAME_LENGTH = 30;

    private List<LogHeader> buildLogHeaderList(String data) {
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

    public void buildLogList(String data) {

        List<LogHeader> headerList = buildLogHeaderList(data);

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

    public void buildLogParams(String data) {
        String[] splitBySemicolon = data.toString().split(";");
        for (String javaLine : splitBySemicolon) {
            if (!javaLine.contains("=")) {
                parseLogParams(javaLine);
            } else {
                // ignore
            }
        }
    }

    private void parseLogParams(String javaLine) {
//        System.out.println(javaLine);
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
//                    System.out.println("paramData: " + paramData);
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
//        System.out.println(paramData);
        List<Param> paramMap = new ArrayList<>();
        String[] splitByComma = paramData.split(",");
        if (splitByComma.length > 1) {
            String split = splitByComma[1];
            split = removeCommonSymbols(split);
            String paramName = "param";
            if (!split.contains("\"") && split.length() < MAX_PARAM_NAME_LENGTH && !split.matches("\\d")) {
                // status
                paramName = cleanUp(split);
            }
            paramMap.add(new Param(paramName, getParamSize(split)));
        }
        return paramMap;
    }

    private int getParamSize(String paramName) {
        int paramSize = LogData.ParamSize.STRING;
        if (paramName.contains("size") || paramName.contains("index") || paramName.contains("count") || paramName.matches("\\d")) {
            paramSize = LogData.ParamSize.INTEGER;
        } else if (paramName.startsWith("is") || paramName.startsWith("mIs"))
            paramSize = LogData.ParamSize.BOOLEAN;
        return paramSize;
    }

    private List<Param> parseParamIntArray(String paramData) {
        List<Param> paramList = new ArrayList<>();
        paramData = removeSymbol(paramData, WORD_INT_ARRAY);
        paramData = removeSymbol(paramData, WORD_INT_ARRAY1);
        paramData = removeCommonSymbols(paramData);
        String[] splitByComma = paramData.split(",");
        for (String split : splitByComma) {
            if (!split.trim().isEmpty() && !split.contains("\"")) {
                paramList.add(new Param(cleanUp(split.trim()), LogData.ParamSize.INTEGER));
            }
        }
        return paramList;
    }

    private String cleanUp(String paramName) {
        String[] splitBySpace = paramName.split(" ");
        if (splitBySpace.length > 1)
            return splitBySpace[0];
        else return paramName;
    }

    private String removeCommonSymbols(String paramData) {
        return removeSymbol(paramData,
                "{", "}",
                ".toString",
                "(", ")");
    }

    public String removeSymbol(String text, String... symbol) {
        for (String _s : symbol) {
            while (text.contains(_s)) {
                text = text.replace(_s, "");
            }
        }
        return text.trim();
    }

    public List<LogData> getLogDataList() {
        return mLogDataList;
    }
}
