package com.example.firebase;

public class User {
    private String userName,phoneNumber,profileImageUrl,age;

    public User() {
    }

    public User(String userName, String phoneNumber, String profileImageUrl,String age) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", age='" + age + '\'' +
                '}';
    }
}
