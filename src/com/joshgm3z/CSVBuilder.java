package com.joshgm3z;

import com.joshgm3z.data.LogData;
import com.joshgm3z.data.Param;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVBuilder {

    private String mOutputPath;

    public CSVBuilder(String outputPath) {
        mOutputPath = outputPath + (outputPath.endsWith("\\") ? "" : "\\");
    }

    public void build(List<LogData> logDataList, String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(mOutputPath + fileName + ".csv");
            CSVWriter csvWriter = new CSVWriter(fileWriter);

            for (LogData logData : logDataList) {

                List<Param> paramList = logData.getParamList();

                // add log title
                String logTitleId = String.valueOf(logData.getId() + logData.getHeaderValue());
                String paramCount = String.valueOf(paramList.size());

                String[] logTitle = {logTitleId, logData.getName(), paramCount, "0"};
                csvWriter.writeNext(logTitle);

                if (!paramList.isEmpty()) {
                    for (Param param : paramList) {
                        String[] logParam = {param.getSize(), param.getName(), param.getType(), param.getSuffix()};
                        csvWriter.writeNext(logParam);
                    }
                }
            }


            // closing writer connection
            csvWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getParamSize(int type) {
        String size = "0";
        if (type == LogData.ParamSize.INTEGER)
            size = "3";
        else if (type == LogData.ParamSize.STRING)
            size = "2";
        else if (type == LogData.ParamSize.BOOLEAN)
            size = "0";
        return size;
    }

}
