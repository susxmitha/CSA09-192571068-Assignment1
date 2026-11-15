package parking.app;

import parking.model.*;
import parking.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Task12Tests {
    static int passed = 0, failed = 0;

    static void check(String id, String what, Object expected, Object actual) {
        boolean ok = String.valueOf(expected).equals(String.valueOf(actual));
        if (ok) passed++; else failed++;
        System.out.printf("%-4s | %-56s | %-26s | %-26s | %s%n", id, what, expected, actual, ok ? "PASS" : "FAIL");
    }

    static String thrown(Runnable r) {
        try { r.run(); return "no exception"; } catch (RuntimeException e) { return e.getClass().getSimpleName(); }
    }

    static LocalDateTime at(int d, int h, int m) { return LocalDateTime.of(2026, 11, d, h, m); }

    public static void main(String[] args) throws Exception {
        Task12Main.register();
        System.out.printf("%-4s | %-56s | %-26s | %-26s | %s%n", "ID", "Test", "Expected", "Actual", "Result");
        System.out.println("-".repeat(130));

        EBike eb = new EBike("TN20EB0001");
        check("E1", "e-bike 30 minutes", 20.0, eb.calculateFee(30));
        check("E2", "e-bike 10 hours (still flat)", 20.0, eb.calculateFee(600));
        check("E3", "e-bike zero minutes", 0.0, eb.calculateFee(0));
        check("E4", "e-bike negative duration", "IllegalArgumentException", thrown(() -> eb.calculateFee(-1)));

        DeliveryVan v = new DeliveryVan("TN20DV0001");
        check("V1", "van 08:30-10:00 (1 off-peak h + 1 peak h)", 90.0, v.calculateFee(at(5, 8, 30), at(5, 10, 0)));
        check("V2", "van 16:30-18:00 (peak h then off-peak h)", 90.0, v.calculateFee(at(5, 16, 30), at(5, 18, 0)));
        check("V3", "van 09:00-17:00 (8 peak hours = 480, = cap)", 480.0, v.calculateFee(at(5, 9, 0), at(5, 17, 0)));
        check("V4", "van 09:00-17:01 (capped at 480)", 480.0, v.calculateFee(at(5, 9, 0), at(5, 17, 1)));
        check("V5", "van 18:00-19:00 (off-peak, car rate)", 30.0, v.calculateFee(at(5, 18, 0), at(5, 19, 0)));
        check("V6", "van 08:59-09:01 (one started hour, priced by its start)", 30.0, v.calculateFee(at(5, 8, 59), at(5, 9, 1)));
        check("V6b", "van 08:00-09:01 (off-peak hour, then peak hour)", 90.0, v.calculateFee(at(5, 8, 0), at(5, 9, 1)));
        check("V7", "van zero minutes", 0.0, v.calculateFee(at(5, 9, 0), at(5, 9, 0)));
        check("V8", "van exit before entry", "IllegalArgumentException", thrown(() -> v.calculateFee(at(5, 10, 0), at(5, 9, 0))));
        check("V9", "car 09:00-10:30 unchanged by the new rules", 60.0, new Car("TN20CR0001").calculateFee(at(5, 9, 0), at(5, 10, 30)));

        check("P1", "one-day pass valid on its own day", true, new OneDayPass(LocalDate.of(2026, 11, 5)).isValidOn(LocalDate.of(2026, 11, 5)));
        check("P2", "one-day pass invalid next day", false, new OneDayPass(LocalDate.of(2026, 11, 5)).isValidOn(LocalDate.of(2026, 11, 6)));

        ParkingFacility fac = new ParkingFacility(new int[]{3, 2, 4, 1, 2});
        check("F1", "5 floors with different slot counts: capacity", 12, fac.capacity());
        check("F2", "floors", 5, fac.floors());

        ParkingService svc = new ParkingService(new ParkingFacility(new int[]{3, 2, 4, 1, 2}));
        svc.enter("CAR", "TN20CR0002", at(5, 10, 0), new OneDayPass(LocalDate.of(2026, 11, 5)));
        check("D1", "visitor with day pass, same-day stay: parking free", 0.0, svc.exit("TN20CR0002", at(5, 16, 0), 0));
        check("D2", "day pass price collected at the gate", 100.0, svc.getCollection());
        svc.enter("CAR", "TN20CR0003", at(5, 22, 0), new OneDayPass(LocalDate.of(2026, 11, 5)));
        check("D3", "day pass, stay crosses midnight: charged 4 h", 120.0, svc.exit("TN20CR0003", at(6, 2, 0), 0));
        check("D4", "unknown type still rejected", "IllegalArgumentException", thrown(() -> svc.enter("BUS", "TN20BS0001", at(5, 9, 0), null)));
        svc.enter("EBIKE", "TN20EB0002", at(5, 9, 0), null);
        svc.enter("DELIVERYVAN", "TN20DV0002", at(5, 9, 0), null);
        check("D5", "e-bike through the service", 20.0, svc.exit("TN20EB0002", at(5, 13, 0), 0));
        check("D6", "van through the service (4 peak hours)", 240.0, svc.exit("TN20DV0002", at(5, 13, 0), 0));

        double total = Main.run(args.length > 0 ? args[0] : "input/scenario12.txt");
        check("S1", "scenario total (see hand calculation in report)", 580.0, total);

        System.out.println("-".repeat(130));
        System.out.println("Passed: " + passed + "   Failed: " + failed);
    }
}
