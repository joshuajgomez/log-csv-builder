package com.joshgm3z;

import java.util.HashMap;

public class LogData {

    public @interface ParamType {
        int BOOLEAN = 0;
        int INTEGER = 1;
        int STRING = 2;
    }

    private String mName;
    private String mId;
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

    public String getId() {
        return mId;
    }

    public void setId(String id) {
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
                ", mId='" + mId + '\'' +
                ", mParamList=" + mParamList +
                '}';
    }
}
