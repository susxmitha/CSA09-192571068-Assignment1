/** Task 4: constructors, chaining, copy constructor, setters and factories. */
public class ConstructorDemo {

    // (1) overloaded constructors with chaining
    static class Ticket {
        private static int counter = 0;
        private final int no; private final String reg; private final int minutes;
        Ticket(String reg, int minutes) {                 // master constructor
            if (reg == null || reg.isEmpty() || minutes < 0) throw new IllegalArgumentException("bad ticket");
            this.no = ++counter; this.reg = reg; this.minutes = minutes;
        }
        Ticket(String reg) { this(reg, 0); }              // chaining
        Ticket(Ticket other) { this.no = other.no; this.reg = other.reg; this.minutes = other.minutes; } // copy
        public String toString() { return "Ticket#" + no + "[" + reg + "," + minutes + "]"; }
    }

    // (2) default constructor + setters: object can exist half-built
    static class SetterTicket {
        private String reg; private int minutes;
        void setReg(String r) { reg = r; }
        void setMinutes(int m) { if (m < 0) throw new IllegalArgumentException(); minutes = m; }
    }

    // (3) static factory method
    static class FactoryTicket {
        private final String reg; private final int minutes;
        private FactoryTicket(String r, int m) { reg = r; minutes = m; }
        static FactoryTicket of(String r, int m) {
            if (r == null || r.isEmpty() || m < 0) throw new IllegalArgumentException("bad ticket");
            return new FactoryTicket(r, m);
        }
        static FactoryTicket walkIn(String r) { return of(r, 0); }
    }

    public static void main(String[] args) {
        Ticket a = new Ticket("TN01AB1111", 90);
        Ticket b = new Ticket("TN02CD2222");
        Ticket dup = new Ticket(a);
        System.out.println(a + "  " + b + "  duplicate of a: " + dup);
        System.out.println("a == dup: " + (a == dup) + "   (two references, two heap objects)");
        Ticket alias = a;
        System.out.println("a == alias: " + (a == alias) + "   (same heap object, two references)");

        SetterTicket st = new SetterTicket();
        System.out.println("Half-built SetterTicket exists with reg=null: " + (st != null));

        try { new Ticket("", 5); } catch (IllegalArgumentException e) { System.out.println("constructor rejects bad data"); }
        try { FactoryTicket.of("TN", -1); } catch (IllegalArgumentException e) { System.out.println("factory rejects bad data"); }

        System.out.println("\nCost of creating n tickets");
        System.out.printf("%-10s %-22s %-22s %-22s%n", "n", "Constructor (ms)", "Default+setters (ms)", "Static factory (ms)");
        for (int w = 0; w < 3; w++) run(200_000);
        for (int n : new int[]{100_000, 1_000_000, 5_000_000}) {
            long[] t = run(n);
            System.out.printf("%-10d %-22.1f %-22.1f %-22.1f%n", n, t[0] / 1e6, t[1] / 1e6, t[2] / 1e6);
        }
    }

    static long[] run(int n) {
        long sink = 0;
        long t0 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += new Ticket("TN01", i % 100).no;
        long t1 = System.nanoTime();
        for (int i = 0; i < n; i++) { SetterTicket s = new SetterTicket(); s.setReg("TN01"); s.setMinutes(i % 100); sink += s.minutes; }
        long t2 = System.nanoTime();
        for (int i = 0; i < n; i++) sink += FactoryTicket.of("TN01", i % 100).minutes;
        long t3 = System.nanoTime();
        if (sink == 1) System.out.println();
        return new long[]{t1 - t0, t2 - t1, t3 - t2};
    }
}
