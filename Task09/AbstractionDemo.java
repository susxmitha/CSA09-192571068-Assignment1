/** Task 9: abstract class + interfaces (with default and static methods). */
public class AbstractionDemo {

    interface Chargeable {
        double RATE_PER_KWH = 12;
        double chargingFee(int kWh);
        default String chargingNote(int kWh) { return kWh + " kWh at Rs " + RATE_PER_KWH + " = Rs " + chargingFee(kWh); }
        static boolean validUnits(int kWh) { return kWh >= 0; }
    }

    interface Payable { double amountDue(); }

    static abstract class Vehicle {
        private final String regNo;
        protected Vehicle(String regNo) { this.regNo = regNo; }       // constructor + state: only a class can have these
        public String getRegNo() { return regNo; }
        protected abstract double ratePerHour();
        public double calculateFee(int minutes) { return ((minutes + 59) / 60) * ratePerHour(); }
    }

    static class Car extends Vehicle implements Payable {
        private int minutes;
        Car(String r, int minutes) { super(r); this.minutes = minutes; }
        protected double ratePerHour() { return 30; }
        public double amountDue() { return calculateFee(minutes); }
    }

    // one class, multiple behaviours: it IS-A Vehicle and CAN-DO Chargeable and Payable
    static class ElectricCar extends Car implements Chargeable {
        ElectricCar(String r, int minutes) { super(r, minutes); }
        public double chargingFee(int kWh) { return kWh * RATE_PER_KWH; }
        @Override public double amountDue() { return super.amountDue() + chargingFee(10); }
    }

    public static void main(String[] args) {
        ElectricCar ev = new ElectricCar("TN03C3", 130);
        System.out.println(ev.getRegNo() + " amount due = Rs " + ev.amountDue());
        System.out.println(ev.chargingNote(10));
        System.out.println("Chargeable.validUnits(-3) = " + Chargeable.validUnits(-3));
        Payable p = ev; Chargeable c = ev;
        System.out.println("Same object seen as Vehicle, Payable and Chargeable: "
                + (ev instanceof Vehicle) + " " + (p instanceof Payable) + " " + (c instanceof Chargeable));

        System.out.println("\nWhat cannot be expressed if Vehicle became an interface:");
        System.out.println(" - a constructor that validates the registration number");
        System.out.println(" - a private final regNo field (interface fields are public static final constants)");
        System.out.println(" - protected members");
        System.out.println("What cannot be expressed if Chargeable became an abstract class:");
        System.out.println(" - ElectricCar already extends Car, and Java allows only one superclass");
    }
}
