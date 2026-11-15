package parking.app;

import parking.model.ParkingTicket;
import parking.model.Pass;
import parking.service.ParkingFacility;
import parking.service.ParkingService;
import parking.service.PassFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

/** Reads a scenario file with Scanner and runs it. Usage: java parking.app.Main input/scenario.txt */
public class Main {

    public static void main(String[] args) throws IOException {
        String file = args.length > 0 ? args[0] : "input/scenario.txt";
        run(file);
    }

    /** Runs a scenario and returns the total collection. */
    public static double run(String file) throws IOException {
        ParkingService service = null;
        try (Scanner sc = new Scanner(new File(file))) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] p = line.split(",");
                try {
                    switch (p[0]) {
                        case "FACILITY" -> {
                            int[] slots = new int[p.length - 1];
                            for (int i = 1; i < p.length; i++) slots[i - 1] = Integer.parseInt(p[i].trim());
                            service = new ParkingService(new ParkingFacility(slots));
                            System.out.println("Facility created: " + service.getFacility().floors()
                                    + " floors, " + service.getFacility().capacity() + " slots");
                        }
                        case "ENTER" -> {
                            Pass pass = null;
                            if (p.length > 4) {
                                String[] pt = p[4].trim().split(":");
                                pass = PassFactory.create(pt[0], LocalDate.parse(pt[1]));
                            }
                            ParkingTicket t = service.enter(p[1].trim(), p[2].trim(),
                                    LocalDateTime.parse(p[3].trim()), pass);
                            System.out.println("ENTER  " + t);
                        }
                        case "EXIT" -> {
                            int kWh = p.length > 3 ? Integer.parseInt(p[3].trim()) : 0;
                            double fee = service.exit(p[1].trim(), LocalDateTime.parse(p[2].trim()), kWh);
                            System.out.println("EXIT   " + p[1].trim() + " fee = Rs " + fee);
                        }
                        default -> System.out.println("IGNORED " + line);
                    }
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("REJECTED " + line + "  ->  " + e.getMessage());
                }
            }
        }
        double total = service == null ? 0 : service.getCollection();
        System.out.println("TOTAL COLLECTION = Rs " + total);
        return total;
    }
}
