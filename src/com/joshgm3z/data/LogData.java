package com.joshgm3z.data;

import java.util.ArrayList;
import java.util.List;

public class LogData {

    public void setParamList(List<Param> paramList) {
        mParamList = paramList;
    }

    public @interface ParamSize {
        int UNKNOWN = 0;
        int BOOLEAN = 1;
        int INTEGER = 4;
        int STRING = 32;
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
    private List<Param> mParamList;

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

    public List<Param> getParamList() {
        return mParamList != null ? mParamList : new ArrayList<>();
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
