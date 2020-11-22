package com.example.t_commerce.models;


import java.util.Date;

public class PaymentHistory
{

   Date date;
   Long Amount;
   String Mode;

   public PaymentHistory(Date D, Long A, String Mode)
   {
       this.date = D;
       this.Amount = A;
       this.Mode = Mode;
   }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getAmount() {
        return Amount;
    }

    public void setAmount(Long amount) {
        Amount = amount;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }
}
