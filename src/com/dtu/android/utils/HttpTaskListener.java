package com.dtu.android.utils;

public interface HttpTaskListener {

    public void onResponse(String response);

    public void onCancelled();
}
