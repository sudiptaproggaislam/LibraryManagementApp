package com.progga.librarymanagement;

public class ret {
    private  String book,category,issuedate,returndate,key,roll,status;

    public ret() {
    }

    public ret(String book, String category, String issuedate, String returndate, String key, String roll, String status) {
        this.book = book;
        this.category = category;
        this.issuedate = issuedate;
        this.returndate = returndate;
        this.key = key;
        this.roll = roll;
        this.status = status;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIssuedate() {
        return issuedate;
    }

    public void setIssuedate(String issuedate) {
        this.issuedate = issuedate;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
