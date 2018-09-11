package com.gkzxhn.legalconsulting.net;

public class BaseResponseEntity<T> {

    public String message;
    public String total;
    public int code;
    public T data;
    public String new_date;
    public String last_update_time;
    public int is_calculation;
}
