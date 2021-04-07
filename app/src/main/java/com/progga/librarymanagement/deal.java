package com.progga.librarymanagement;

public class deal {
    private  String name,category,avail;

    public deal() {
    }

    public deal(String name, String category, String avail) {
        this.name = name;
        this.category = category;
        this.avail = avail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAvail() {
        return avail;
    }

    public void setAvail(String avail) {
        this.avail = avail;
    }
}
