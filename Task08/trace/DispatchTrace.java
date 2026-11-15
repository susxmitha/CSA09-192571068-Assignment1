package trace;

import java.time.LocalTime;
import java.time.Duration;

/** Compile-time overloading, upcasting and runtime dispatch, with a printed trace. */
public class DispatchTrace {

    static abstract class Vehicle {
        protected abstract double ratePerHour();

        // overload 1: minutes
        public double calculateFee(int minutes) {
            System.out.println("   [executing] Vehicle.calculateFee(int) -> calls ratePerHour() on "
                    + getClass().getSimpleName());
            return ((minutes + 59) / 60) * ratePerHour();
        }

        // overload 2: entry and exit times
        public double calculateFee(LocalTime in, LocalTime out) {
            System.out.println("   [executing] Vehicle.calculateFee(LocalTime, LocalTime)");
            return calculateFee((int) Duration.between(in, out).toMinutes());
        }
    }

    static class Car extends Vehicle {
        @Override protected double ratePerHour() {
            System.out.println("   [executing] Car.ratePerHour()");
            return 30;
        }
    }

    static class TwoWheeler extends Vehicle {
        @Override protected double ratePerHour() {
            System.out.println("   [executing] TwoWheeler.ratePerHour()");
            return 10;
        }
    }

    public static void main(String[] args) {
        System.out.println("1. Compile-time polymorphism (overloading)");
        Car car = new Car();
        System.out.println("   calculateFee(130) selected by argument type int");
        System.out.println("   fee = " + car.calculateFee(130));
        System.out.println("   calculateFee(LocalTime, LocalTime) selected by two LocalTime arguments");
        System.out.println("   fee = " + car.calculateFee(LocalTime.of(9, 0), LocalTime.of(11, 10)));

        System.out.println();
        System.out.println("2. Runtime polymorphism (upcasting + dynamic dispatch)");
        Vehicle[] lot = { new TwoWheeler(), new Car() };   // upcasting into one array
        for (Vehicle v : lot) {
            System.out.println("   declared type = Vehicle, actual type = " + v.getClass().getSimpleName());
            System.out.println("   fee = " + v.calculateFee(130));
        }
    }
}
