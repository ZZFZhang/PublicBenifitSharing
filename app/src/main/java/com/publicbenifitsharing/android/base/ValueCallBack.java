package com.publicbenifitsharing.android.base;

public interface ValueCallBack<T> {
    void onSuccess(T t);
    void onFail();
}
