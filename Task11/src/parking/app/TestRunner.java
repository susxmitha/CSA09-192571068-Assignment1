package parking.app;

import parking.model.*;
import parking.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Plain-Java test table: id, what is tested, expected, actual, PASS/FAIL. */
public class TestRunner {
    static int passed = 0, failed = 0;
    static final LocalDateTime T0 = LocalDateTime.of(2026, 11, 5, 9, 0);

    static void check(String id, String what, Object expected, Object actual) {
        boolean ok = String.valueOf(expected).equals(String.valueOf(actual));
        if (ok) passed++; else failed++;
        System.out.printf("%-4s | %-52s | %-30s | %-30s | %s%n", id, what, expected, actual,
                ok ? "PASS" : "FAIL");
    }

    static String thrown(Runnable r) {
        try { r.run(); return "no exception"; }
        catch (RuntimeException e) { return e.getClass().getSimpleName(); }
    }

    static LocalDateTime at(int h, int m) { return LocalDateTime.of(2026, 11, 5, h, m); }

    public static void main(String[] args) throws Exception {
        System.out.printf("%-4s | %-52s | %-30s | %-30s | %s%n", "ID", "Test", "Expected", "Actual", "Result");
        System.out.println("-".repeat(135));

        // ---------- normal operation ----------
        check("N1", "TwoWheeler, 130 min", 30.0, new TwoWheeler("TN01A1").calculateFee(130));
        check("N2", "Car, 130 min", 90.0, new Car("TN02B2").calculateFee(130));
        ElectricCar ev = new ElectricCar("TN03C3");
        check("N3", "ElectricCar, 130 min + 10 kWh", 210.0, ev.calculateFee(130) + ev.chargingFee(10));

        ParkingFacility f = new ParkingFacility(new int[]{2, 1});
        check("N4", "allocation order (3 vehicles)", "[[0, 0], [0, 1], [1, 0]]",
                Arrays.deepToString(new int[][]{f.allocate("A1"), f.allocate("A2"), f.allocate("A3")}));
        check("N5", "full facility returns null", "null", f.allocate("A4"));
        f.release(0, 1);
        check("N6", "release (0,1) then allocate gives same slot", "[0, 1]", Arrays.toString(f.allocate("A5")));

        ParkingService svc = new ParkingService(new ParkingFacility(new int[]{1, 1}));
        svc.enter("CAR", "TN55AA0001", at(9, 0), null);
        svc.enter("TWOWHEELER", "TN55AA0002", at(9, 0), null);
        check("N7", "occupied after 2 entries", 2, svc.getFacility().occupied());
        svc.exit("TN55AA0001", at(10, 0), 0);
        check("N8", "occupied after 1 exit", 1, svc.getFacility().occupied());
        check("N9", "slot reusable after exit", "Ticket ok",
                svc.enter("CAR", "TN55AA0003", at(11, 0), null) != null ? "Ticket ok" : "null");

        // ---------- boundary cases ----------
        ParkingService last = new ParkingService(new ParkingFacility(new int[]{1, 1}));
        last.enter("CAR", "TN66AA0001", at(9, 0), null);
        ParkingTicket lastTicket = last.enter("CAR", "TN66AA0002", at(9, 0), null);
        check("B1", "last free slot can be allocated", "floor 1 slot 0",
                "floor " + lastTicket.getFloor() + " slot " + lastTicket.getSlot());
        check("B1b", "next vehicle after the last slot is refused", "IllegalStateException",
                thrown(() -> last.enter("CAR", "TN66AA0003", at(9, 0), null)));
        Car c = new Car("TN77AA0001");
        check("B2", "zero minutes", 0.0, c.calculateFee(0));
        check("B3", "1 minute (a started hour is full)", 30.0, c.calculateFee(1));
        check("B4", "exactly 60 minutes", 30.0, c.calculateFee(60));
        check("B5", "61 minutes", 60.0, c.calculateFee(61));
        check("B6", "exactly 480 minutes (8 h = cap)", 240.0, c.calculateFee(480));
        check("B7", "481 minutes (capped)", 240.0, c.calculateFee(481));
        check("B8", "exactly 1440 minutes (one day)", 240.0, c.calculateFee(1440));
        check("B9", "1441 minutes (one day + 1 h)", 270.0, c.calculateFee(1441));
        check("B10", "TwoWheeler 600 min capped at 80", 80.0, new TwoWheeler("TN77AA0002").calculateFee(600));

        ParkingService ps = new ParkingService(new ParkingFacility(new int[]{3}));
        ps.enter("CAR", "TN88AA0001", at(9, 0), new MonthlyPass(LocalDate.of(2026, 11, 1)));
        check("B11", "valid monthly pass parks free", 0.0, ps.exit("TN88AA0001", at(15, 0), 0));
        ps.enter("CAR", "TN88AA0002", at(9, 0), new MonthlyPass(LocalDate.of(2026, 10, 7), LocalDate.of(2026, 11, 5)));
        check("B12", "pass valid on its last day", 0.0, ps.exit("TN88AA0002", at(12, 0), 0));
        ps.enter("CAR", "TN88AA0003", at(9, 0), new MonthlyPass(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 30)));
        check("B13", "expired pass is charged (3 h)", 90.0, ps.exit("TN88AA0003", at(12, 0), 0));
        ps.enter("EV", "TN88AA0004", at(9, 0), new MonthlyPass(LocalDate.of(2026, 11, 1)));
        check("B14", "pass holder still pays charging (5 kWh)", 60.0, ps.exit("TN88AA0004", at(10, 0), 5));

        // ---------- invalid cases ----------
        ParkingService inv = new ParkingService(new ParkingFacility(new int[]{1}));
        check("I1", "unknown vehicle type", "IllegalArgumentException",
                thrown(() -> inv.enter("TRUCK", "TN99AA0001", T0, null)));
        inv.enter("CAR", "TN99AA0002", T0, null);
        check("I2", "facility full", "IllegalStateException",
                thrown(() -> inv.enter("CAR", "TN99AA0003", T0, null)));
        ParkingService dup = new ParkingService(new ParkingFacility(new int[]{3}));
        dup.enter("CAR", "TN99AA0004", T0, null);
        check("I3", "duplicate registration number", "IllegalStateException",
                thrown(() -> dup.enter("TWOWHEELER", "TN99AA0004", T0, null)));
        ParkingFacility pf = new ParkingFacility(new int[]{2});
        pf.allocate("X1");
        check("I4", "release invalid floor (5,0)", false, pf.release(5, 0));
        check("I5", "release negative slot (0,-1)", false, pf.release(0, -1));
        check("I6", "release slot beyond floor size (0,9)", false, pf.release(0, 9));
        check("I7", "release free slot (0,1)", false, pf.release(0, 1));
        check("I8", "release valid slot (0,0), then again", "true false", pf.release(0, 0) + " " + pf.release(0, 0));
        check("I9", "negative duration", "IllegalArgumentException", thrown(() -> c.calculateFee(-5)));
        check("I10", "exit time before entry", "IllegalArgumentException",
                thrown(() -> dup.exit("TN99AA0004", T0.minusHours(1), 0)));
        check("I11", "exit of a vehicle that never entered", "IllegalArgumentException",
                thrown(() -> dup.exit("TN00ZZ0000", T0, 0)));
        check("I12", "invalid registration number (lower case)", "IllegalArgumentException",
                thrown(() -> new Car("tn 01")));
        dup.enter("CAR", "TN99AA0005", T0, null);
        check("I13", "charging a non-electric vehicle", "IllegalArgumentException",
                thrown(() -> dup.exit("TN99AA0005", at(10, 0), 5)));
        check("I14", "negative kWh", "IllegalArgumentException", thrown(() -> ev.chargingFee(-1)));

        // ---------- object contract tests ----------
        ParkingTicket t1 = new ParkingTicket(new Car("TN12AA0001"), 0, 0, T0);
        ParkingTicket t2 = new ParkingTicket(new Car("TN12AA0002"), 0, 1, T0);
        ParkingTicket copy = new ParkingTicket(t1);
        check("O1", "ticket numbers strictly increase", true, t2.getTicketNo() == t1.getTicketNo() + 1);
        check("O2", "copy constructor keeps ticket number", true, copy.getTicketNo() == t1.getTicketNo());
        check("O3", "copy does not consume a new number", true, new ParkingTicket(new Car("TN12AA0003"), 0, 2, T0).getTicketNo() == t2.getTicketNo() + 1);
        check("O4", "Car equals ElectricCar with same plate", true, new Car("TN13AA0001").equals(new ElectricCar("TN13AA0001")));
        Set<Vehicle> set = new HashSet<>();
        set.add(new Car("TN13AA0002"));
        set.add(new TwoWheeler("TN13AA0002"));
        check("O5", "HashSet treats same plate as one vehicle", 1, set.size());

        // ---------- hand-calculated scenario ----------
        double total = Main.run(args.length > 0 ? args[0] : "input/scenario.txt");
        check("S1", "scenario total: 20+180+150+0+240", 590.0, total);

        System.out.println("-".repeat(135));
        System.out.println("Passed: " + passed + "   Failed: " + failed);
    }
}
