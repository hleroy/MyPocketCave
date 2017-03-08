package com.myadridev.mypocketcave.models;

public interface IStorableModel {
    int getId();

    boolean isValid();

    void trimAll();
}
