package com.example.demo.entity;

import java.util.List;

public class Bookes {
    private List<Books>   list;

    public List<Books> getList() {
        return list;
    }

    public void setList(List<Books> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Bookes{" +
                "list=" + list +
                '}';
    }
}
