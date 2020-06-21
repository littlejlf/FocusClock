package com.example.myapplication;
import org.litepal.crud.DataSupport;

public class StudyTime extends DataSupport {
    private long totalTime;

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

}