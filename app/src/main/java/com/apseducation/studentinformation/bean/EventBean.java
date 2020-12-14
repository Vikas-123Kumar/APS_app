package com.apseducation.studentinformation.bean;

public class EventBean {

    public static final String key_id_e = "id_E";
    public static final String key_id_event="event_id";
    public static final String key_name_e = "name";
    public static final String key_email_e = "email";
    public static final String key_fathername = "fatherName";
    public static final String key_address_e = "address";
    public static final String key_contact_e = "contact";
    public static final String key_event_name = "eventName";
    public static final String key_event_date = "eventDate";
    public static final String key_event_venue = "eventVenue";
    public static final String key_event_id = "eventId";
    String idE, nameE, contactE, emailE, addressE, eventName, eventDate, eventVenue, fatherName,eventId,idEvent;

    public String getNameE() {
        return nameE;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public String getIdE() {
        return idE;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public void setIdE(String idE) {
        this.idE = idE;
    }

    public void setNameE(String nameE) {
        this.nameE = nameE;
    }

    public String getContactE() {
        return contactE;
    }

    public void setContactE(String contactE) {
        this.contactE = contactE;
    }

    public String getEmailE() {
        return emailE;
    }

    public void setEmailE(String emailE) {
        this.emailE = emailE;
    }

    public String getAddressE() {
        return addressE;
    }

    public void setAddressE(String addressE) {
        this.addressE = addressE;
    }
}
