package parking.model;

import java.time.Duration;
import java.time.LocalDateTime;

/** Delivery van: double the car rate for hours that start between 9 AM and 5 PM. */
public class DeliveryVan extends Vehicle {
    private static final double CAR_RATE = 30;
    private static final double PEAK_RATE = 2 * CAR_RATE;
    private static final int PEAK_START = 9;
    private static final int PEAK_END = 17;

    public DeliveryVan(String regNo) { super(regNo); }

    @Override protected double ratePerHour() { return CAR_RATE; }   // off-peak rate

    @Override public double calculateFee(LocalDateTime in, LocalDateTime out) {
        long minutes = Duration.between(in, out).toMinutes();
        if (minutes < 0) {
            throw new IllegalArgumentException("Exit time is before entry time");
        }
        int blocks = (int) ((minutes + 59) / 60);                  // started hours
        double sum = 0;
        LocalDateTime start = in;
        for (int i = 0; i < blocks; i++) {                         // price each started hour
            int h = start.getHour();
            sum += (h >= PEAK_START && h < PEAK_END) ? PEAK_RATE : CAR_RATE;
            start = start.plusHours(1);
        }
        long days = (minutes + Vehicle.MINUTES_PER_DAY - 1) / Vehicle.MINUTES_PER_DAY;
        double cap = days * Vehicle.DAILY_MAX_HOURS * PEAK_RATE;   // daily maximum
        return Math.min(sum, cap);
    }
}
