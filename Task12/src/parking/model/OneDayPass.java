package parking.model;

import java.time.LocalDate;

/** One-day visitor pass: sold at the gate, valid only on the day it is bought. */
public final class OneDayPass implements Pass {
    public static final double PRICE = 100;
    private final LocalDate day;

    public OneDayPass(LocalDate day) {
        if (day == null) throw new IllegalArgumentException("Pass date is required");
        this.day = day;
    }

    @Override public boolean isValidOn(LocalDate date) { return day.equals(date); }

    @Override public String describe() { return "One-day pass for " + day; }

    @Override public double priceAtGate() { return PRICE; }
}
