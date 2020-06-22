package com.example.myapplication;
import org.litepal.crud.DataSupport;

public class StudyTime extends DataSupport {
    private long totalTime;
    private int giveUpNum;

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setGiveUpNum(int num){
        this.giveUpNum=num;
    }

    public int getGiveUpNum(){
        return giveUpNum;
    }

}