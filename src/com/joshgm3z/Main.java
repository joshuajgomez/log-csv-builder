package com.joshgm3z;

import com.joshgm3z.data.LogData;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Main {

    private final static String ARG_LOG_ID_FILE = "log-file";
    private final static String ARG_PROJECT_ROOT = "project-root";
    private final static String ARG_OUTPUT_DIR = "output-dir";
    private final static String ARG_FILE_NAMING = "file-naming";

    private String mLogIdFilePath;
    private String mProjectRootPath;
    private String mOutputPath;
    private String mFileNamingText;
    private FileManager mFileManager;
    private HashMap<String, String> mFileNameMap;

    public Main(String logIdFilePath, String outputPath, String fileNamingText) {
//        System.out.println("Main() called with: logIdFilePath = [" + logIdFilePath + "], outputPath = [" + outputPath + "]");
        this.mLogIdFilePath = logIdFilePath;
        this.mOutputPath = outputPath;
        this.mFileNamingText = fileNamingText;
        mFileManager = new FileManager(new LogParser());
    }

    public static void main(String[] args) {
        // write your code here
        if (args.length >= 1) {
            String logIdFilePath = null;
            String outputPath = null;
            String fileNamingText = null;
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg.contains(ARG_LOG_ID_FILE)) {
                    logIdFilePath = getValue(arg);
                } else if (arg.contains(ARG_OUTPUT_DIR)) {
                    outputPath = getValue(arg);
                }else if (arg.contains(ARG_FILE_NAMING)) {
                    fileNamingText = getValue(arg);
                }
            }
            new Main(logIdFilePath, outputPath, fileNamingText).init();
        } else {
            // invalid parameters
        }
    }

    private static String getValue(String keyValue) {
        String[] splitByEquals = keyValue.split("=");
        return splitByEquals[1];
    }

    private void init() {

        ProgressTimer progressTimer = new ProgressTimer();

        List<String> logIdFiles = mFileManager.readIntoList(mLogIdFilePath);
        CSVBuilder csvBuilder = new CSVBuilder(mOutputPath);
        System.out.println();

        mFileNameMap = mFileManager.readFileNames(mFileNamingText);

        for (String logIdFile : logIdFiles) {
            String projectName = getFileName(logIdFile);
            System.out.print("Checking " + projectName + " for logs...");
            progressTimer.start();
            List<LogData> logDataList = mFileManager.findLogs(logIdFile, getProjectRootPath(logIdFile));
            csvBuilder.build(logDataList, getCsvName(projectName));
            progressTimer.stop();
            System.out.print(" done.");
            System.out.println();
        }

    }

    private String getCsvName(String projectName) {
        Set<String> projectNameSet = mFileNameMap.keySet();
        for (String _projectName : projectNameSet) {
            if (projectName.equalsIgnoreCase(_projectName)) {
                return mFileNameMap.get(_projectName);
            }
        }
        return projectName;
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
