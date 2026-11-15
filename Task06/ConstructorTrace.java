/** Task 6: order of constructor execution for ElectricCar, and the composition alternative. */
public class ConstructorTrace {

    static boolean trace = true;

    static abstract class Vehicle {
        private final String regNo;
        Vehicle(String regNo) { this.regNo = regNo; if (trace) System.out.println("  3. Vehicle(String) body: regNo = " + regNo); }
        { if (trace) System.out.println("  2. Vehicle instance initialiser"); }
        String getRegNo() { return regNo; }
        abstract double ratePerHour();
        double fee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); }
    }

    static class Car extends Vehicle {
        Car(String regNo) { super(regNo); if (trace) System.out.println("  4. Car(String) body"); }
        double ratePerHour() { return 30; }
    }

    static class ElectricCar extends Car {
        ElectricCar(String regNo) { super(regNo); if (trace) System.out.println("  5. ElectricCar(String) body"); }
        @Override String getRegNo() { return "EV-" + super.getRegNo(); }        // super for a method
    }

    // ---- composition alternative: one Vehicle class that HAS-A VehicleType ----
    static class VehicleType {
        final String name; final double rate;
        VehicleType(String n, double r) { name = n; rate = r; }
    }

    static class CVehicle {
        private final String regNo; private final VehicleType type;
        CVehicle(String regNo, VehicleType type) { this.regNo = regNo; this.type = type; }
        double fee(int minutes) { return ((minutes + 59) / 60) * type.rate; }
    }

    public static void main(String[] args) {
        System.out.println("new ElectricCar(\"TN03C3\") - constructor trace:");
        System.out.println("  1. ElectricCar(String) is called; first statement super(regNo) hands over to Car");
        System.out.println("     (Car calls Vehicle, Vehicle calls Object - nothing is printed for Object)");
        Vehicle ev = new ElectricCar("TN03C3");
        System.out.println("  6. object ready; getRegNo() -> " + ev.getRegNo() + ", fee(130) = " + ev.fee(130));

        trace = false;                                       // silence the trace for the rest
        System.out.println("\nComposition version:");
        VehicleType car = new VehicleType("CAR", 30);
        VehicleType van = new VehicleType("VAN", 60);       // a new category = a new data object, no new class
        System.out.println("  CVehicle car fee(130) = " + new CVehicle("TN02B2", car).fee(130));
        System.out.println("  CVehicle van fee(130) = " + new CVehicle("TN09V9", van).fee(130));

        // timing of fee computation
        int n = 5_000_000; double sink = 0;
        Vehicle inh = new Car("TN02B2"); CVehicle com = new CVehicle("TN02B2", car);
        for (int w = 0; w < 2_000_000; w++) sink += inh.fee(w % 500) + com.fee(w % 500);
        long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += inh.fee(i % 500);
        double a = (System.nanoTime() - t0) / (double) n;
        t0 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += com.fee(i % 500);
        double b = (System.nanoTime() - t0) / (double) n;
        System.out.printf("%n  fee time: inheritance %.2f ns/call, composition %.2f ns/call (both O(1))%n", a, b);
        if (sink == 1) System.out.println();
    }
}
