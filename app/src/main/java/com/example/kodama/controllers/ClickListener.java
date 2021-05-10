package com.example.kodama.controllers;

public interface ClickListener<T> {
    void onItemClick(T data);
}