package parking.model;

/** Capability: a vehicle that can be charged and billed separately per kWh. */
public interface Chargeable {
    double RATE_PER_KWH = 12;                       // constant

    double chargingFee(int kWh);                    // abstract

    default boolean isValidUnits(int kWh) {         // default method shared by all implementors
        return kWh >= 0;
    }

    static double roundRupees(double amount) {      // static utility method
        return Math.round(amount * 100) / 100.0;
    }
}
