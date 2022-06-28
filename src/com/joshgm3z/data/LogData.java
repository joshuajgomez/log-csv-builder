package com.joshgm3z.data;

import java.util.HashMap;

public class LogData {

    public @interface ParamType {
        int BOOLEAN = 0;
        int INTEGER = 1;
        int STRING = 2;
    }

    public LogData(int id, String name, int headerValue) {
        mId = id;
        mName = name;
        mHeaderValue = headerValue;
    }

    private String mName;
    private int mId;
    private int mHeaderValue;
    /**
     * mParamList< Name , Type >
     */
    private HashMap<String, Integer> mParamList = new HashMap<>();

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getId() {
        return mId;
    }

    public int getHeaderValue() {
        return mHeaderValue;
    }

    public void setHeaderValue(int headerValue) {
        mHeaderValue = headerValue;
    }

    public void setId(int id) {
        mId = id;
    }

    public HashMap<String, Integer> getParamList() {
        return mParamList;
    }

    public void addParam(String name, int type) {
        mParamList.put(name, type);
    }

    @Override
    public String toString() {
        return "LogData{" +
                "mName='" + mName + '\'' +
                ", mId=" + mId +
                ", mHeaderValue=" + mHeaderValue +
                ", mParamList=" + mParamList +
                '}';
    }
}