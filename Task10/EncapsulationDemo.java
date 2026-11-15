/** Task 10: private fields with validating setters versus public fields; wrapper classes. */
public class EncapsulationDemo {

    static class SafeSlot {
        private int floor; private int slot; private int minutes;
        private static final int MAX_FLOOR = 4, MAX_SLOT = 99;
        void setFloor(int f)   { if (f < 0 || f > MAX_FLOOR) throw new IllegalArgumentException("floor " + f); floor = f; }
        void setSlot(int s)    { if (s < 0 || s > MAX_SLOT)  throw new IllegalArgumentException("slot " + s); slot = s; }
        void setMinutes(int m) { if (m < 0) throw new IllegalArgumentException("minutes " + m); minutes = m; }
        int getMinutes() { return minutes; }
    }

    static class OpenSlot { public int floor, slot, minutes; }       // anyone can put anything in

    public static void main(String[] args) {
        SafeSlot s = new SafeSlot();
        try { s.setMinutes(-300); } catch (IllegalArgumentException e) { System.out.println("SafeSlot rejected: " + e.getMessage()); }
        try { s.setSlot(250); } catch (IllegalArgumentException e) { System.out.println("SafeSlot rejected: " + e.getMessage()); }

        OpenSlot o = new OpenSlot();
        o.minutes = -300; o.slot = 250;
        System.out.println("OpenSlot accepted minutes=" + o.minutes + ", slot=" + o.slot + "  <- corrupt state");
        System.out.println("fee for -300 min would be " + (((o.minutes + 59) / 60) * 30) + " (a refund the system never intended)");

        System.out.println("\nWrapper classes and autoboxing");
        Integer a = 127, b = 127, c = 1000, d = 1000;
        System.out.println("Integer 127 == 127   : " + (a == b) + "   (cached object)");
        System.out.println("Integer 1000 == 1000 : " + (c == d) + "  (two different objects)");
        System.out.println("Integer 1000 equals  : " + c.equals(d));
        Integer nothing = null;
        try { int x = nothing; } catch (NullPointerException e) { System.out.println("unboxing null -> NullPointerException"); }
        int v = Integer.parseInt("130");
        System.out.println("Integer.parseInt(\"130\") = " + v);

        // validation cost per update: a few comparisons, O(1)
        int n = 50_000_000; long sink = 0;
        SafeSlot t = new SafeSlot();
        for (int i = 0; i < 5_000_000; i++) { t.setMinutes(i % 500); }
        long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) { t.setMinutes(i % 500); sink += t.getMinutes(); }
        double safe = (System.nanoTime() - t0) / (double) n;
        OpenSlot q = new OpenSlot();
        t0 = System.nanoTime();
        for (int i = 0; i < n; i++) { q.minutes = i % 500; sink += q.minutes; }
        double open = (System.nanoTime() - t0) / (double) n;
        System.out.printf("%nupdate with validation %.2f ns, direct field write %.2f ns%n", safe, open);
        if (sink == 1) System.out.println();
    }
}
