package com.joshgm3z.data;

public class LogHeader {

    private String mHeaderName;
    private int mValue;

    public LogHeader(String mHeaderName, int mValue) {
        this.mHeaderName = mHeaderName;
        this.mValue = mValue;
    }

    public String getHeaderName() {
        return mHeaderName;
    }

    public int getValue() {
        return mValue;
    }

    @Override
    public String toString() {
        return "{" +
                "'" + mHeaderName + '\'' +
                ", " + mValue +
                '}';
    }
}
