import java.util.HashSet;
import java.util.Set;

/** Task 7: overriding, final, Object methods, and the switch-on-type alternative. */
public class OverrideDemo {

    static abstract class Vehicle {
        private final String regNo;                       // final field
        Vehicle(String regNo) { this.regNo = regNo; }
        public final String getRegNo() { return regNo; }  // final method: cannot be overridden
        protected abstract double ratePerHour();
        public double calculateFee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); }
        @Override public boolean equals(Object o) { return o instanceof Vehicle && regNo.equals(((Vehicle) o).regNo); }
        @Override public int hashCode() { return regNo.hashCode(); }
        @Override public String toString() { return getClass().getSimpleName() + "[" + regNo + "]"; }
    }

    static class TwoWheeler extends Vehicle {
        TwoWheeler(String r) { super(r); }
        @Override protected double ratePerHour() { return 10; }
    }

    static class Car extends Vehicle {
        Car(String r) { super(r); }
        @Override protected double ratePerHour() { return 30; }
    }

    static class ElectricCar extends Car {
        ElectricCar(String r) { super(r); }
        @Override public double calculateFee(int minutes) {      // specialised: EV discount of 10 percent
            return super.calculateFee(minutes) * 0.9;
        }
    }

    // ---- alternative: one calculateFee with a switch on a type field ----
    static class TypedVehicle {
        final String regNo; final String type;
        TypedVehicle(String r, String t) { regNo = r; type = t; }
        double calculateFee(int minutes) {
            int hours = (minutes + 59) / 60;
            switch (type) {
                case "TWOWHEELER": return hours * 10;
                case "CAR":        return hours * 30;
                case "EV":         return hours * 30 * 0.9;
                default: throw new IllegalArgumentException("unknown type " + type);
            }
        }
    }

    public static void main(String[] args) {
        Vehicle[] lot = { new TwoWheeler("TN01A1"), new Car("TN02B2"), new ElectricCar("TN03C3") };
        for (Vehicle v : lot) System.out.println(v + " fee(130) = " + v.calculateFee(130));

        System.out.println("\nequals / hashCode / toString");
        Vehicle a = new Car("TN02B2"), b = new Car("TN02B2");
        System.out.println("a == b          : " + (a == b));
        System.out.println("a.equals(b)     : " + a.equals(b));
        System.out.println("same hashCode   : " + (a.hashCode() == b.hashCode()));
        Set<Vehicle> set = new HashSet<>(); set.add(a); set.add(b);
        System.out.println("HashSet size    : " + set.size() + "  (duplicate plate rejected)");
        System.out.println("toString        : " + a);

        System.out.println("\nSwitch-on-type version: " + new TypedVehicle("TN03C3", "EV").calculateFee(130));
        System.out.println("Adding a new type there means editing the switch inside TypedVehicle (an existing class).");
        System.out.println("Adding a new type with overriding means writing one new subclass; nothing else is edited.");

        int n = 10_000_000; double sink = 0;
        TypedVehicle tv = new TypedVehicle("TN02B2", "CAR");
        for (int w = 0; w < 3_000_000; w++) sink += a.calculateFee(w % 500) + tv.calculateFee(w % 500);
        long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += a.calculateFee(i % 500);
        double x = (System.nanoTime() - t0) / (double) n;
        t0 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += tv.calculateFee(i % 500);
        double y = (System.nanoTime() - t0) / (double) n;
        System.out.printf("%nTime per fee: overriding %.2f ns, switch on String %.2f ns%n", x, y);
        if (sink == 1) System.out.println();
    }
}
