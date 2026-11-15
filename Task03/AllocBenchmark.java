/** Measures allocate()+release() on a nearly full facility (worst case for the scan). */
public class AllocBenchmark {
    static long timeScan(int floors, int perFloor, int rounds) {
        int[] cfg = new int[floors];
        java.util.Arrays.fill(cfg, perFloor);
        ParkingFacility p = new ParkingFacility(cfg);
        int total = floors * perFloor;
        for (int i = 0; i < total - 1; i++) p.allocate("V" + i);   // leave the LAST slot free
        long t0 = System.nanoTime();
        for (int r = 0; r < rounds; r++) {
            int[] pos = p.allocate("X");
            p.release(pos[0], pos[1]);
        }
        return System.nanoTime() - t0;
    }

    static long timeFast(int floors, int perFloor, int rounds) {
        int[] cfg = new int[floors];
        java.util.Arrays.fill(cfg, perFloor);
        FastParkingFacility p = new FastParkingFacility(cfg);
        int total = floors * perFloor;
        for (int i = 0; i < total - 1; i++) p.allocate("V" + i);
        long t0 = System.nanoTime();
        for (int r = 0; r < rounds; r++) {
            int[] pos = p.allocate("X");
            p.release(pos[0], pos[1]);
        }
        return System.nanoTime() - t0;
    }

    public static void main(String[] args) {
        int[][] cfg = {{1, 20}, {3, 50}, {5, 100}, {20, 500}};
        int rounds = 200_000;
        for (int w = 0; w < 3; w++) { timeScan(5, 100, 20000); timeFast(5, 100, 20000); } // warm-up
        System.out.printf("%-14s %-16s %-16s%n", "Facility", "Scan (ns/op)", "Counts+stack (ns/op)");
        for (int[] c : cfg) {
            double a = timeScan(c[0], c[1], rounds) / (double) rounds;
            double b = timeFast(c[0], c[1], rounds) / (double) rounds;
            System.out.printf("%-14s %-16.1f %-16.1f%n", c[0] + " x " + c[1], a, b);
        }
    }
}
