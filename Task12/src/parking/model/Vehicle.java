package parking.model;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Vehicle {
    public static final int MINUTES_PER_DAY = 1440;      // constants are static final
    public static final int DAILY_MAX_HOURS = 8;

    private final String regNo;

    protected Vehicle(String regNo) {
        if (regNo == null || !regNo.matches("[A-Z0-9]{4,12}")) {
            throw new IllegalArgumentException("Invalid registration number: " + regNo);
        }
        this.regNo = regNo;
    }

    public final String getRegNo() { return regNo; }     // final: identity must not be changed

    /** Hourly rate in rupees; each subclass supplies its own. */
    protected abstract double ratePerHour();

    /** Every started hour is charged in full, capped at DAILY_MAX_HOURS per day. */
    public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative: " + minutes);
        }
        int days = minutes / MINUTES_PER_DAY;
        int rest = minutes % MINUTES_PER_DAY;
        int hours = (rest + 59) / 60;                    // integer ceiling, no floating point
        double cap = DAILY_MAX_HOURS * ratePerHour();
        return days * cap + Math.min(hours * ratePerHour(), cap);
    }

    /** Overload: fee from entry and exit time. Subclasses may override for time-of-day rules. */
    public double calculateFee(LocalDateTime in, LocalDateTime out) {
        long minutes = Duration.between(in, out).toMinutes();
        if (minutes < 0) {
            throw new IllegalArgumentException("Exit time is before entry time");
        }
        return calculateFee(Math.toIntExact(minutes));   // long -> int, fails loudly on overflow
    }

    @Override public boolean equals(Object o) {
        return o instanceof Vehicle && regNo.equals(((Vehicle) o).regNo);
    }

    @Override public int hashCode() { return regNo.hashCode(); }

    @Override public String toString() {
        return getClass().getSimpleName() + "[" + regNo + "]";
    }
}
