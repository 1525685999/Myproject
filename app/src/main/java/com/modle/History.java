package com.modle;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2016/5/13.
 */
public class History implements Serializable {
    private boolean style;//true为转账，false为验证
    private boolean result;//true成功false为失败
    private String time;
    private String money;


    public History(boolean style, boolean result, Date date) {
        this.style = style;
        this.result = result;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = simpleDateFormat.format(date);
    }

    public History() {
    }

    public boolean isStyle() {
        return style;
    }

    public String getTime() {
        return time;
    }

    public boolean isResult() {
        return result;
    }

    public void setStyle(boolean style) {
        this.style = style;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = simpleDateFormat.format(date);
    }
    public void setTime(String date) {
        this.time = date;
    }
    public void setMoney(String money) {
        this.money = money;
    }


    public String getMoney() {
        return money;
    }

    @Override
    public String toString() {
        return "{" +
                "money:'" + money + '\'' +
                ", time:'" + time + '\'' +
                ", result:" + result +
                ", style:" + style +
                '}';
    }
}
