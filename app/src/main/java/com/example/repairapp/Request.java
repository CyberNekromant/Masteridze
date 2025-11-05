package com.example.repairapp;

public class Request {
    private int id;
    private String ownerName;
    private String phone;
    private String model;
    private String description;
    private String dateCreated;
    private String timeCreated;
    private String status; // "новая", "в работе", "выполнено"
    private String type; // "appliance", "computer", "master"

    public Request(int id, String ownerName, String phone, String model, String description,
                   String dateCreated, String timeCreated, String status, String type) {
        this.id = id;
        this.ownerName = ownerName;
        this.phone = phone;
        this.model = model;
        this.description = description;
        this.dateCreated = dateCreated;
        this.timeCreated = timeCreated;
        this.status = status;
        this.type = type;
    }

    // Геттеры
    public int getId() { return id; }
    public String getOwnerName() { return ownerName; }
    public String getPhone() { return phone; }
    public String getModel() { return model; }
    public String getDescription() { return description; }
    public String getDateCreated() { return dateCreated; }
    public String getTimeCreated() { return timeCreated; }
    public String getStatus() { return status; }
    public String getType() { return type; }
    public void setStatus(String status) { this.status = status; }
}