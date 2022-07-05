package com.joshgm3z;

import com.joshgm3z.data.LogData;

import java.util.List;

public class Main {

    private final static String ARG_LOG_ID_FILE = "log-file";
    private final static String ARG_PROJECT_ROOT = "project-root";
    private final static String ARG_OUTPUT_DIR = "output-dir";

    private String mLogIdFilePath;
    private String mProjectRootPath;
    private String mOutputPath;
    private FileManager mFileManager;

    public Main(String logIdFilePath, String outputPath) {
        System.out.println("Main() called with: logIdFilePath = [" + logIdFilePath + "], outputPath = [" + outputPath + "]");
        this.mLogIdFilePath = logIdFilePath;
        this.mOutputPath = outputPath;
        mFileManager = new FileManager();
    }

    public static void main(String[] args) {
        // write your code here
        if (args.length >= 1) {
            String logIdFilePath = null;
            String outputPath = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains(ARG_LOG_ID_FILE)) {
                    logIdFilePath = getValue(arg);
                } else if (arg.contains(ARG_OUTPUT_DIR)) {
                    outputPath = getValue(arg);
                }
            }
            new Main(logIdFilePath, outputPath).init();
        } else {
            // invalid parameters
        }
    }

    private static String getValue(String keyValue) {
        String[] splitByEquals = keyValue.split("=");
        return splitByEquals[1];
    }

    private void init() {

        List<String> logIdFiles = mFileManager.readIntoList(mLogIdFilePath);
        CSVBuilder csvBuilder = new CSVBuilder(mOutputPath);

        for (String logIdFile : logIdFiles) {
            List<LogData> logDataList = mFileManager.findLogs(logIdFile, getProjectRootPath(logIdFile));
            getFileName(logIdFile);
            csvBuilder.build(logDataList, getFileName(logIdFile));
        }
    }

    private String getFileName(String logIdFile) {
        String[] splitBySlash = logIdFile.split("\\\\");
        List<String> splitBySlashList = List.of(splitBySlash);
        return splitBySlashList.get(splitBySlashList.indexOf("app") - 1);
    }

    private String getProjectRootPath(String logIdFilePath) {
        String[] splitByJava = logIdFilePath.split("java");
        return splitByJava[0] + "java\\";
    }

}
