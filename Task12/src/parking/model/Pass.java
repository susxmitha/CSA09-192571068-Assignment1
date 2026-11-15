package parking.model;

import java.time.LocalDate;

/** A parking pass. Any class that can say "valid on this date" can be used as a pass. */
public interface Pass {
    boolean isValidOn(LocalDate date);

    String describe();

    /** Amount collected at the gate when the pass is issued (monthly passes are bought earlier). */
    default double priceAtGate() {
        return 0;
    }
}
