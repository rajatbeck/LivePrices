package com.capitalvia.getliveprices;

/**
 * Created by Rajat on 10/5/2016.
 */
public class LivePrice {
    private String live_price;
    private String prev_day_close;
    private String last_updated_time;

    public void setLast_updated_time(String last_updated_time) {
        this.last_updated_time = last_updated_time;
    }

    public void setLive_price(String live_price) {
        this.live_price = live_price;
    }

    public void setPrev_day_close(String prev_day_close) {
        this.prev_day_close = prev_day_close;
    }

    public String getLast_updated_time() {
        return last_updated_time;
    }

    public String getLive_price() {
        return live_price;
    }

    public String getPrev_day_close() {
        return prev_day_close;
    }
}
