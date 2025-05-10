<<<<<<< HEAD:src/utils/PriceCalculator.java
// Utility class responsible for price calculation
package utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PriceCalculator {

    // This nested class stores the pricing details as a breakdown
    public static class PriceBreakdown {
        private double basePrice;
        private double longStayDiscount;
        private double holidayDiscount;
        private double promoCodeDiscount;
        private double tax;
        private double totalAmount;
        private int numberOfNights;

        public PriceBreakdown(double basePrice, double longStayDiscount, double holidayDiscount, double promoCodeDiscount,
                              double tax, double totalAmount, int numberOfNights) {
            this.basePrice = basePrice;
            this.longStayDiscount = longStayDiscount;
            this.holidayDiscount = holidayDiscount;
            this.promoCodeDiscount = promoCodeDiscount;
            this.tax = tax;
            this.totalAmount = totalAmount;
            this.numberOfNights = numberOfNights;
        }

        // All getters go here
        public double getBasePrice() { return basePrice; }
        public double getLongStayDiscount() { return longStayDiscount; }
        public double getHolidayDiscount() { return holidayDiscount; }
        public double getPromoCodeDiscount() { return promoCodeDiscount; }
        public double getTax() { return tax; }
        public double getTotalAmount() { return totalAmount; }
        public int getNumberOfNights() { return numberOfNights; }
    }

    // This method performs the price computation
    public static PriceBreakdown calculatePrice(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate, String promoCode) {
        int numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = pricePerNight * numberOfNights;

        // Compute discounts, tax, etc.
        double longStayDiscount = (numberOfNights > 7) ? basePrice * 0.10 : 0.0;
        double holidayDiscount = 0.0;
        // ... Add holiday logic ...

        double tax = (basePrice - longStayDiscount - holidayDiscount) * 0.12;
        double totalAmount = basePrice - longStayDiscount - holidayDiscount + tax;

        return new PriceBreakdown(basePrice, longStayDiscount, holidayDiscount, 0.0, tax, totalAmount, numberOfNights);
    }
=======
// Utility class responsible for price calculation
package utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PriceCalculator {

    // This nested class stores the pricing details as a breakdown
    public static class PriceBreakdown {
        private double basePrice;
        private double longStayDiscount;
        private double holidayDiscount;
        private double promoCodeDiscount;
        private double tax;
        private double totalAmount;
        private int numberOfNights;

        public PriceBreakdown(double basePrice, double longStayDiscount, double holidayDiscount, double promoCodeDiscount,
                              double tax, double totalAmount, int numberOfNights) {
            this.basePrice = basePrice;
            this.longStayDiscount = longStayDiscount;
            this.holidayDiscount = holidayDiscount;
            this.promoCodeDiscount = promoCodeDiscount;
            this.tax = tax;
            this.totalAmount = totalAmount;
            this.numberOfNights = numberOfNights;
        }

        // All getters go here
        public double getBasePrice() { return basePrice; }
        public double getLongStayDiscount() { return longStayDiscount; }
        public double getHolidayDiscount() { return holidayDiscount; }
        public double getPromoCodeDiscount() { return promoCodeDiscount; }
        public double getTax() { return tax; }
        public double getTotalAmount() { return totalAmount; }
        public int getNumberOfNights() { return numberOfNights; }
    }

    // This method performs the price computation
    public static PriceBreakdown calculatePrice(double pricePerNight, LocalDate checkInDate, LocalDate checkOutDate, String promoCode) {
        int numberOfNights = (int) ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = pricePerNight * numberOfNights;

        // Compute discounts, tax, etc.
        double longStayDiscount = (numberOfNights > 7) ? basePrice * 0.10 : 0.0;
        double holidayDiscount = 0.0;
        // ... Add holiday logic ...

        double tax = (basePrice - longStayDiscount - holidayDiscount) * 0.12;
        double totalAmount = basePrice - longStayDiscount - holidayDiscount + tax;

        return new PriceBreakdown(basePrice, longStayDiscount, holidayDiscount, 0.0, tax, totalAmount, numberOfNights);
    }
>>>>>>> 7e3406ce4df201df35e6d3556d8654a04774ca96:utils/PriceCalculator.java
}