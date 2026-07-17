package edu.uph.m24si2.petcareapp.model;

public class BookingItem {
    private int id;
    private int petId;
    private String serviceName;
    private String date;
    private String time;
    private int price;
    private String status;
    private Object originalObject;
    private String type; // "Grooming", "PetHotel", "HomeService"
    private float rating;

    public BookingItem(int id, int petId, String serviceName, String date, String time, int price, String status, Object originalObject, String type, float rating) {
        this.id = id;
        this.petId = petId;
        this.serviceName = serviceName;
        this.date = date;
        this.time = time;
        this.price = price;
        this.status = status;
        this.originalObject = originalObject;
        this.type = type;
        this.rating = rating;
    }

    public int getId() { return id; }
    public int getPetId() { return petId; }
    public String getServiceName() { return serviceName; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public int getPrice() { return price; }
    public String getStatus() { return status; }
    public Object getOriginalObject() { return originalObject; }
    public String getType() { return type; }
    public float getRating() { return rating; }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
