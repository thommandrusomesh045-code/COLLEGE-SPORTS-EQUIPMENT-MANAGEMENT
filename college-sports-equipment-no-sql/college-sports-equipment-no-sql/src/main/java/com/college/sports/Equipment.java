package com.college.sports;
public class Equipment {
    public long id;
    public String name;
    public String sport;
    public int totalQuantity;
    public int availableQuantity;
    public int issuedQuantity;
    public int damagedQuantity;
    public Equipment() {}
    public Equipment(long id, String name, String sport, int quantity) {
        this.id=id; this.name=name; this.sport=sport;
        this.totalQuantity=quantity; this.availableQuantity=quantity;
    }
}