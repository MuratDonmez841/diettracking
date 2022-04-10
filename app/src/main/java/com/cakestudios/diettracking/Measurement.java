package com.cakestudios.diettracking;



public final class Measurement {
    String id;
    private String date;
    private String varMesurement;
    private String note;
    private String isHungry;
    private int totalCalori;
    public Measurement() {
    }

    public Measurement(String id,String isHungry,String date, String varMesurement,String note, int totalCalori) {
        this.id=id;
        this.isHungry=isHungry;
        this.date=date;
        this.varMesurement=varMesurement;
        this.note=note;
        this.totalCalori=totalCalori;
    }

    public String getIsHungry() {
        return isHungry;
    }

    public void setIsHungry(String isHungry) {
        this.isHungry = isHungry;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVarMesurement() {
        return varMesurement;
    }

    public void setVarMesurement(String varMesurement) {
        this.varMesurement = varMesurement;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getTotalCalori() {
        return totalCalori;
    }

    public void setTotalCalori(int totalCalori) {
        this.totalCalori = totalCalori;
    }


}

