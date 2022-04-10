package com.cakestudios.diettracking;

public final class UserDoctor {
    private String id;
    private String userName;
    private String email;
    private boolean isDoctor;
    private String hospitalDoctor;
    public UserDoctor() {

    }

    public UserDoctor(String id, String userName, String email, boolean isDoctor, String hospitalDoctor) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.isDoctor = isDoctor;
        this.hospitalDoctor = hospitalDoctor;
    }

    public String getHospitalDoctor() {
        return hospitalDoctor;
    }

    public void setHospitalDoctor(String hospitalDoctor) {
        this.hospitalDoctor = hospitalDoctor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isDoctor() {
        return isDoctor;
    }

    public void setDoctor(boolean doctor) {
        isDoctor = doctor;
    }
}
