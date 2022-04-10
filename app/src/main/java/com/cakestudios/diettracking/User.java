package com.cakestudios.diettracking;



public final class User {
    private String id;
    private String userName;
    private String gender;
    private String illennes;
    private String useMedicine;
    private String email;
    private String password;
    private int age;
    private int weight;
    private int lenght;
    private boolean isFirstLogin;

    public User() {
    }

    public User(String id, String email,
                String userName,
                String password, String gender,
                int age,
                int weight,
                int lenght, String illennes,
                String useMedicine,boolean isFirstLogin) {
        this.id=id;
        this.email=email;
        this.userName=userName;
        this.password=password;
        this.gender=gender;
        this.age=age;
        this.weight=weight;
        this.lenght=lenght;
        this.illennes=illennes;
        this.useMedicine=useMedicine;
        this.isFirstLogin=isFirstLogin;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        isFirstLogin = firstLogin;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIllennes() {
        return illennes;
    }

    public void setIllennes(String illennes) {
        this.illennes = illennes;
    }

    public String getUseMedicine() {
        return useMedicine;
    }

    public void setUseMedicine(String useMedicine) {
        this.useMedicine = useMedicine;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }


}

