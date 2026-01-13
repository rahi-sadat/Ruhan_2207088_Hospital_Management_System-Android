
package com.example.ruhan_2207088_hospital_management_system;

public class Pricing {
    public String serviceId;
    public String serviceName;
    public Object price; // Changed from String to Object to handle Numbers

    public Pricing() {} // Mandatory for Firebase

    public Pricing(String serviceId, String serviceName, Object price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }
}