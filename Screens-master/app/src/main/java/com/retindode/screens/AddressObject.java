package com.retindode.screens;

public class AddressObject {

    private String postalCode = "";
    private String city = "";
    private String state = "";
    private String area;
    private String landmark = "";
    private String houseNo = "";
    private String street = "";

    public AddressObject() {
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getSingleLineAddress() {
        return getHouseNo() + ", " + getStreet() + ", " + getLandmark() + ", " + getArea() + ", " +
                getCity() + ", " + getState() + ", " + getPostalCode();
    }
}
