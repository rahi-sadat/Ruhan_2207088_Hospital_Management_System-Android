
package com.example.ruhan_2207088_hospital_management_system;

public class Pricing {
    public String serviceId;
    public String serviceName;
    public Object price;

    public Pricing() {}

    public Pricing(String serviceId, String serviceName, Object price) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.price = price;
    }
}