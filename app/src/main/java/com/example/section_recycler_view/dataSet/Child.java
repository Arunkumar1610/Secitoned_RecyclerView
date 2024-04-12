package com.example.section_recycler_view.dataSet;

public class Child {


    String name;
    String date;
    String amount;
    String txn_id;

    public String getDate() {
        return date;
    }

    public String getAmount() {
        return amount;
    }

    public String getTxn_id() {
        return txn_id;
    }

    public Child(String date, String amount, String txn_id) {
        this.date = date;
        this.amount = amount;
        this.txn_id = txn_id;
    }

    public Child() {

    }


    public Child(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
