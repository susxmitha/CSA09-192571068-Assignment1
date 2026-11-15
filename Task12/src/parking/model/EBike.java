package parking.model;

/** E-bike: a flat fee for any stay, however long. */
public class EBike extends Vehicle {
    public static final double FLAT_FEE = 20;

    public EBike(String regNo) { super(regNo); }

    @Override protected double ratePerHour() { return 0; }   // not used: the fee is flat

    @Override public double calculateFee(int minutes) {
        if (minutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative: " + minutes);
        }
        return minutes == 0 ? 0 : FLAT_FEE;
    }
}
