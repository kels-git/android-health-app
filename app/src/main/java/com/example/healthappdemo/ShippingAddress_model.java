package com.example.healthappdemo;

public class ShippingAddress_model {

    String userShip_Name;
    String userShip_phone;
    String userShip_Address;
    String userShip_City ;
    String userShip_TotalAmount;
    String OrderDate, OrderTime, OrderStatus;

  public  ShippingAddress_model(){

  }

    public  ShippingAddress_model( String mUserShip_Name, String mUserShip_phone,   String mUserShip_Address,
                                   String mUserShip_City, String mUserShip_TotalAmount,String mOrderDate,
                                   String mOrderTime, String mOrderStatus ){

      this.userShip_Name = mUserShip_Name;
      this.userShip_phone = mUserShip_phone;
      this.userShip_Address = mUserShip_Address;
      this.userShip_City = mUserShip_City;
      this.userShip_TotalAmount = mUserShip_TotalAmount;
      this.OrderDate = mOrderDate;
      this.OrderTime = mOrderTime;
      this.OrderStatus = mOrderStatus;

    }

    public String getUserShip_Name() {
        return userShip_Name;
    }

    public void setUserShip_Name(String userShip_Name) {
        this.userShip_Name = userShip_Name;
    }

    public String getUserShip_phone() {
        return userShip_phone;
    }

    public void setUserShip_phone(String userShip_phone) {
        this.userShip_phone = userShip_phone;
    }

    public String getUserShip_Address() {
        return userShip_Address;
    }

    public void setUserShip_Address(String userShip_Address) {
        this.userShip_Address = userShip_Address;
    }

    public String getUserShip_City() {
        return userShip_City;
    }

    public void setUserShip_City(String userShip_City) {
        this.userShip_City = userShip_City;
    }

    public String getUserShip_TotalAmount() {
        return userShip_TotalAmount;
    }

    public void setUserShip_TotalAmount(String userShip_TotalAmount) {
        this.userShip_TotalAmount = userShip_TotalAmount;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }
}
