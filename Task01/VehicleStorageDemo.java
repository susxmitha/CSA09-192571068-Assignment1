import java.util.Arrays;

/** Task 1 alternative: Vehicle objects versus parallel arrays. */
public class VehicleStorageDemo {

    static class Vehicle {
        final String regNo; final String type; final long entryTime;
        Vehicle(String r, String t, long e) { regNo = r; type = t; entryTime = e; }
    }

    // object design: find by registration number
    static int findObj(Vehicle[] v, int n, String reg) {
        for (int i = 0; i < n; i++) if (v[i].regNo.equals(reg)) return i;
        return -1;
    }

    // parallel-array design: find by registration number
    static int findPar(String[] regNo, int n, String reg) {
        for (int i = 0; i < n; i++) if (regNo[i].equals(reg)) return i;
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("Adding a new attribute 'slot' in the parallel-array design means: a new array,");
        System.out.println("and every method that adds, removes or shifts a vehicle must update it too.");
        System.out.println("In the object design it is one new field in one class.\n");

        System.out.printf("%-8s %-20s %-20s%n", "n", "Objects (ns/find)", "Parallel (ns/find)");
        for (int n : new int[]{1_000, 10_000, 100_000}) {
            Vehicle[] objs = new Vehicle[n];
            String[] reg = new String[n]; String[] type = new String[n]; long[] time = new long[n];
            for (int i = 0; i < n; i++) {
                String r = "TN" + String.format("%08d", i);
                objs[i] = new Vehicle(r, "CAR", i); reg[i] = r; type[i] = "CAR"; time[i] = i;
            }
            String key = "TN" + String.format("%08d", n - 1);     // last element: worst case
            int sink = 0;
            for (int w = 0; w < 200; w++) sink += findObj(objs, n, key) + findPar(reg, n, key);
            int reps = 500;
            long t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) sink += findObj(objs, n, key);
            double a = (System.nanoTime() - t0) / (double) reps;
            t0 = System.nanoTime();
            for (int r = 0; r < reps; r++) sink += findPar(reg, n, key);
            double b = (System.nanoTime() - t0) / (double) reps;
            System.out.printf("%-8d %-20.1f %-20.1f%n", n, a, b);
            if (sink == 7) System.out.println();
        }
        System.out.println("\nBoth searches are O(n). The parallel version touches one contiguous array of");
        System.out.println("references (better cache behaviour); the object version has one extra reference hop.");
    }
}
