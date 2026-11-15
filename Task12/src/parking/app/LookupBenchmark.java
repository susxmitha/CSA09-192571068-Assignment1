package parking.app;

import parking.model.Car;
import parking.model.ParkingTicket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** Time to find an active ticket on exit: array vs ArrayList vs HashMap. */
public class LookupBenchmark {
    static final LocalDateTime T = LocalDateTime.of(2026, 11, 5, 9, 0);

    static long usedMemory() {
        System.gc(); System.gc();
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    public static void main(String[] args) {
        int[] sizes = {1_000, 10_000, 100_000};
        int lookups = 2_000;
        System.out.printf("%-8s %-18s %-18s %-18s%n", "n", "Array (ns/find)", "ArrayList (ns/find)", "HashMap (ns/find)");
        for (int n : sizes) {
            ParkingTicket[] arr = new ParkingTicket[n];
            List<ParkingTicket> list = new ArrayList<>();
            Map<String, ParkingTicket> map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                ParkingTicket t = new ParkingTicket(new Car("TN" + String.format("%08d", i)), 0, i % 100, T);
                arr[i] = t; list.add(t); map.put(t.getVehicle().getRegNo(), t);
            }
            String[] keys = new String[lookups];            // search the NEWEST vehicles: worst case for a scan
            for (int i = 0; i < lookups; i++) keys[i] = "TN" + String.format("%08d", n - 1 - (i % 50));
            long sink = 0;
            // warm-up
            for (int w = 0; w < 3; w++) for (String k : keys) { sink += findArray(arr, k) + list.indexOf(null) + (map.get(k) != null ? 1 : 0); }

            long t0 = System.nanoTime();
            for (String k : keys) sink += findArray(arr, k);
            double a = (System.nanoTime() - t0) / (double) lookups;

            t0 = System.nanoTime();
            for (String k : keys) sink += findList(list, k);
            double b = (System.nanoTime() - t0) / (double) lookups;

            t0 = System.nanoTime();
            for (String k : keys) sink += map.get(k) != null ? 1 : 0;
            double c = (System.nanoTime() - t0) / (double) lookups;

            System.out.printf("%-8d %-18.1f %-18.1f %-18.1f%n", n, a, b, c);
            if (sink == 42) System.out.println();
        }
        // space: n = 100000 tickets
        int n = 100_000;
        long base = usedMemory();
        ParkingTicket[] arr = new ParkingTicket[n];
        for (int i = 0; i < n; i++) arr[i] = new ParkingTicket(new Car("TN" + String.format("%08d", i)), 0, 0, T);
        long withArray = usedMemory();
        List<ParkingTicket> list = new ArrayList<>(n);
        for (ParkingTicket t : arr) list.add(t);
        long withList = usedMemory();
        Map<String, ParkingTicket> map = new HashMap<>();
        for (ParkingTicket t : arr) map.put(t.getVehicle().getRegNo(), t);
        long withMap = usedMemory();
        System.out.println();
        System.out.println("Extra memory for the structure itself, n = " + n + " (tickets shared, so only the container is counted):");
        System.out.printf("  array     : %,d bytes (tickets + vehicles + array)%n", withArray - base);
        System.out.printf("  ArrayList : +%,d bytes%n", withList - withArray);
        System.out.printf("  HashMap   : +%,d bytes%n", withMap - withList);
        if (arr.length + list.size() + map.size() == 0) System.out.println();
    }

    static int findArray(ParkingTicket[] a, String reg) {
        for (int i = 0; i < a.length; i++) if (a[i].getVehicle().getRegNo().equals(reg)) return i;
        return -1;
    }

    static int findList(List<ParkingTicket> l, String reg) {
        for (int i = 0; i < l.size(); i++) if (l.get(i).getVehicle().getRegNo().equals(reg)) return i;
        return -1;
    }
}
