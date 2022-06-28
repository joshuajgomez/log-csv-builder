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
        System.out.println("args: [" + args[0] + "], [" + args[1] + "]");
        if (args.length >= 2) {
            String logIdFilePath = null;
            String projectRootPath = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains(ARG_LOG_ID_FILE)) {
                    logIdFilePath = getValue(arg);
                } else if (arg.contains(ARG_PROJECT_ROOT)) {
                    projectRootPath = getValue(arg);
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
        List<LogData> logData = fileManager.readFileToString(mLogIdFilePath);
    }
}
