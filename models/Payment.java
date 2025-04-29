package models;

import java.io.Serializable;
import java.time.LocalDate;

public class Payment implements Serializable{
    private int reservationId;
    private double amount;
    private LocalDate paymentDate;
    private String paymentStatus;

    public Payment(int reservationId, double amount, LocalDate paymentDate, String paymentStatus) {
        this.reservationId = reservationId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public int getReservationId() {
        return reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
}
