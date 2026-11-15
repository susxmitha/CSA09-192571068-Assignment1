package parking.model;

public class ElectricCar extends Car implements Chargeable {
    public ElectricCar(String regNo) { super(regNo); }

    @Override public double chargingFee(int kWh) {
        if (!isValidUnits(kWh)) {
            throw new IllegalArgumentException("Units cannot be negative: " + kWh);
        }
        return Chargeable.roundRupees(kWh * RATE_PER_KWH);
    }
}
