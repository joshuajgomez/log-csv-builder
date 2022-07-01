package com.joshgm3z.data;

public class Param {
    private String mName;
    private int mSize;
    private int mType;
    private int mSuffix;

    public Param(String name, int size, int type, int suffix) {
        mName = name;
        mSize = size;
        mType = type;
        mSuffix = suffix;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSize() {
        return String.valueOf(mSize);
    }

    public void setSize(int size) {
        mSize = size;
    }

    public String getType() {
        return String.valueOf(mType);
    }

    public void setType(int type) {
        mType = type;
    }

    public String getSuffix() {
        return String.valueOf(mSuffix);
    }

    public void setSuffix(int suffix) {
        mSuffix = suffix;
    }

    @Override
    public String toString() {
        return "Param{" +
                "mName='" + mName + '\'' +
                ", mSize=" + mSize +
                ", mType=" + mType +
                ", mSuffix=" + mSuffix +
                '}';
    }
}

