import java.lang.ref.WeakReference;

/** Task 5: static versus instance ticket counter, static block, object life cycle. */
public class StaticDemo {

    static class Ticket { final String ticketNo; Ticket(String n) { ticketNo = n; } }

    // Case A: counter is an INSTANCE variable of the facility
    static class FacilityA {
        private int counter = 1000;
        private final String gate;
        FacilityA(String gate) { this.gate = gate; }
        Ticket issue() { return new Ticket(String.valueOf(++counter)); }
    }

    // Case B: counter is STATIC - one sequence for the whole class
    static class FacilityB {
        private static int counter = 1000;
        private static final java.util.List<String> LOG = new java.util.ArrayList<>();
        static { LOG.add("FacilityB class loaded, counter starts at " + counter); }  // static block
        Ticket issue() { return new Ticket(String.valueOf(++counter)); }
    }

    // Case C: per-facility counter made unique by a gate prefix
    static class FacilityC {
        private int counter = 1000;
        private final String gate;
        FacilityC(String gate) { this.gate = gate; }
        Ticket issue() { return new Ticket(gate + "-" + (++counter)); }
    }

    public static void main(String[] args) {
        System.out.println("Case A - instance counter:");
        FacilityA n = new FacilityA("N"), s = new FacilityA("S");
        System.out.println("  north issues " + n.issue().ticketNo + ", south issues " + s.issue().ticketNo + "  <- same number twice");

        System.out.println("Case B - static counter:");
        FacilityB bn = new FacilityB(), bs = new FacilityB();
        System.out.println("  north issues " + bn.issue().ticketNo + ", south issues " + bs.issue().ticketNo + "  <- unique");
        System.out.println("  " + FacilityB.LOG.get(0));

        System.out.println("Case C - gate prefix:");
        FacilityC cn = new FacilityC("N"), cs = new FacilityC("S");
        System.out.println("  north issues " + cn.issue().ticketNo + ", south issues " + cs.issue().ticketNo + "  <- unique");

        System.out.println("\nObject life cycle:");
        Ticket t = new Ticket("1001");
        WeakReference<Ticket> watcher = new WeakReference<>(t);
        System.out.println("  ticket reachable, watcher.get() != null : " + (watcher.get() != null));
        t = null;                                        // the vehicle has left: last reference dropped
        for (int i = 0; i < 5 && watcher.get() != null; i++) { System.gc(); try { Thread.sleep(50); } catch (InterruptedException e) { } }
        System.out.println("  after t = null and System.gc(), collected : " + (watcher.get() == null) + "  (gc() is only a hint)");

        System.out.println("\nPassing objects: Java passes the reference by value");
        Ticket x = new Ticket("A");
        reassign(x);
        System.out.println("  after reassign(x), x still points to ticket " + x.ticketNo + " (caller's reference unchanged)");
    }

    static void reassign(Ticket t) { t = new Ticket("B"); }
}
