package com.example.section_recycler_view.baseadapter;

public class DataModel implements Identifiable{
    private int id;
    private String name;

    public DataModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
