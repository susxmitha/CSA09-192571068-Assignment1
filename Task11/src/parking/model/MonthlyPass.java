package parking.model;

import java.time.LocalDate;

/** Monthly pass for staff and students. Final: nobody should change how validity works. */
public final class MonthlyPass implements Pass {
    private final LocalDate validFrom;
    private final LocalDate validTo;

    public MonthlyPass(LocalDate validFrom, LocalDate validTo) {
        if (validFrom == null || validTo == null || validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException("Invalid pass period");
        }
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    /** A standard monthly pass runs for 30 days from the start date. */
    public MonthlyPass(LocalDate validFrom) {
        this(validFrom, validFrom.plusDays(29));        // constructor chaining
    }

    @Override public boolean isValidOn(LocalDate date) {
        return !date.isBefore(validFrom) && !date.isAfter(validTo);
    }

    @Override public String describe() {
        return "Monthly pass " + validFrom + " to " + validTo;
    }
}
