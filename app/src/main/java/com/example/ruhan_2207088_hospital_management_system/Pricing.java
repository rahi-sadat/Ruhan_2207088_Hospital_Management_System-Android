package com.example.ruhan_2207088_hospital_management_system;

public class Pricing {
    public String serviceId;   // This will be the key (e.g., "Doctor's Visit Fee")
    public String serviceName; // The display name
    public String price;       // The amount

    public Pricing() {} // Required for Firebase

    public Pricing(String serviceId, String serviceName, String price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }
}