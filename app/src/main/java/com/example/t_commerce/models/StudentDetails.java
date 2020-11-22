package com.example.t_commerce.models;

import com.example.t_commerce.models.PaymentHistory;

import java.util.ArrayList;

public class StudentDetails {

    String id;

    String name;
    String parent;
    String mobileContact;
    String WhatsAppNumber;
    Long amountDue;
    Long fees;
    Long SClass;
    String School;
    String board;
    ArrayList<PaymentHistory> Payments;

    public StudentDetails(String id, String name, String parent, String mobileContact, String WhatsAppNumber, Long amountDue, Long fees,
                          Long Class, String School, String board,ArrayList<PaymentHistory> P)

    {
        this.id = id;
        this.name = name;
        this.amountDue = amountDue;
        this.fees = fees;
        this.SClass = Class;
        this.parent = parent;
        this.mobileContact = mobileContact;
        this.WhatsAppNumber = WhatsAppNumber;
        this.School = School;
        this.board = board;
        this.Payments = P;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getMobileContact() {
        return mobileContact;
    }

    public void setMobileContact(String mobileContact) {
        this.mobileContact = mobileContact;
    }

    public String getWhatsAppNumber() {
        return WhatsAppNumber;
    }

    public void setWhatsAppNumber(String whatsAppNumber) {
        WhatsAppNumber = whatsAppNumber;
    }

    public Long getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(Long amountDue) {
        this.amountDue = amountDue;
    }

    public Long getFees() {
        return fees;
    }

    public void setFees(Long fees) {
        this.fees = fees;
    }


    public Long getSClass() {
        return SClass;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public void setSClass(Long aClass) {
        SClass = aClass;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String school) {
        School = school;
    }

    public ArrayList<PaymentHistory> getPayments() {
        return Payments;
    }

    public void setPayments(ArrayList<PaymentHistory> payments) {
        Payments = payments;
    }
}
