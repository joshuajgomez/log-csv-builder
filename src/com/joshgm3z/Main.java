package com.joshgm3z;

import com.joshgm3z.data.LogData;

import java.util.List;

public class Main {

    private final static String ARG_LOG_ID_FILE = "log-file";
    private final static String ARG_PROJECT_ROOT = "project-root";

    private String mLogIdFilePath;
    private String mProjectRootPath;

    public Main(String logIdFilePath, String projectRootPath) {
        this.mLogIdFilePath = logIdFilePath;
        this.mProjectRootPath = projectRootPath;
    }

    public static void main(String[] args) {
        // write your code here
        if (args.length >= 1) {
            String logIdFilePath = null;
            String projectRootPath = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains(ARG_LOG_ID_FILE)) {
                    logIdFilePath = getValue(arg);
                    String[] splitByJava = logIdFilePath.split("java");
                    projectRootPath = splitByJava[0] + "java\\";
                }
            }
            new Main(logIdFilePath, projectRootPath).init();
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
        fileManager.readAllFiles(mProjectRootPath);
    }
}
