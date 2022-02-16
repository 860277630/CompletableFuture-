package com.example.demo.entity;

public class Books {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Books(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Books{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
