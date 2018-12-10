package com.skrumble.picketeditor.public_interface;

public interface OnCompletion<T, K> {
    void onCompleted(T t, K k);
}
