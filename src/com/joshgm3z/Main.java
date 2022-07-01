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

    public Main(String logIdFilePath, String projectRootPath, String outputPath) {
        this.mLogIdFilePath = logIdFilePath;
        mProjectRootPath = projectRootPath;
        this.mOutputPath = outputPath;
    }

    public static void main(String[] args) {
        // write your code here
        if (args.length >= 1) {
            String logIdFilePath = null;
            String projectRootPath = null;
            String outputPath = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains(ARG_LOG_ID_FILE)) {
                    logIdFilePath = getValue(arg);
                    String[] splitByJava = logIdFilePath.split("java");
                    projectRootPath = splitByJava[0] + "java\\";
                } else if (arg.contains(ARG_OUTPUT_DIR)) {
                    outputPath = getValue(arg);
                }
            }
            new Main(logIdFilePath, projectRootPath, outputPath).init();
        } else {
            // invalid parameters
        }
    }

    private static String getValue(String keyValue) {
        String[] splitByEquals = keyValue.split("=");
        return splitByEquals[1];
    }

    private void init() {

        FileManager fileManager = new FileManager();
        fileManager.readFileToString(mLogIdFilePath);
        List<LogData> logDataList = fileManager.readAllFiles(mProjectRootPath);

        CSVBuilder csvBuilder = new CSVBuilder(mOutputPath);
        csvBuilder.build(logDataList);
    }
}
