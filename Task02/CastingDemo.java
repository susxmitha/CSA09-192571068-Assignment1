import java.util.Scanner;

/** Task 2: data types, casting risk and the two alternative slot layouts. */
public class CastingDemo {
    static final double RATE = 30.0;

    public static void main(String[] args) {
        int minutes = 130;
        System.out.println("--- Casting risk when converting minutes to hours ---");
        double wrong1 = (minutes / 60) * RATE;                 // integer division: 2 * 30
        double wrong2 = (int) (minutes / 60.0) * RATE;         // cast truncates 2.17 to 2
        double wrong3 = Math.round(minutes / 60.0) * RATE;     // rounds 2.17 to 2 (under-charge)
        double right  = ((minutes + 59) / 60) * RATE;          // integer ceiling: 3 hours
        System.out.println("minutes / 60 * rate            = " + wrong1 + "   (WRONG, 2 h)");
        System.out.println("(int)(minutes / 60.0) * rate   = " + wrong2 + "   (WRONG, 2 h)");
        System.out.println("Math.round(minutes / 60.0)     = " + wrong3 + "   (WRONG, 2 h)");
        System.out.println("(minutes + 59) / 60 * rate     = " + right + "  (CORRECT, 3 h)");

        System.out.println("\n--- long to int narrowing ---");
        long big = 3_000_000_000L;
        System.out.println("(int) 3000000000L = " + (int) big + "   (silently wrong)");
        try { Math.toIntExact(big); } catch (ArithmeticException e) { System.out.println("Math.toIntExact fails loudly: " + e.getMessage()); }

        System.out.println("\n--- Reading input with Scanner ---");
        Scanner sc = new Scanner("TN01AB1111 130 CAR");        // stands in for System.in
        String reg = sc.next(); int mins = sc.nextInt(); String type = sc.next();
        System.out.println("read: " + reg + ", " + mins + " min, " + type);

        System.out.println("\n--- Alternative layouts for floors of different sizes ---");
        int[] perFloor = {3, 2, 4, 1, 2};
        // (a) jagged 2-D array
        String[][] jag = new String[perFloor.length][];
        for (int f = 0; f < perFloor.length; f++) jag[f] = new String[perFloor[f]];
        // (b) flat 1-D array with a prefix-sum offset table (index = offset[f] + s)
        int[] offset = new int[perFloor.length + 1];
        for (int f = 0; f < perFloor.length; f++) offset[f + 1] = offset[f] + perFloor[f];
        String[] flat = new String[offset[perFloor.length]];
        // naive formula floor * slotsPerFloor + slot only works when every floor is equal
        int naive = 2 * 4 + 1;
        int proper = offset[2] + 1;
        System.out.println("floor 2, slot 1: naive f*S+s with S=4 -> index " + naive
                + ", offset table -> index " + proper + " (floor 2 starts at " + offset[2] + ")");
        System.out.println("jagged: jag[2][1] reached in 2 steps, O(1); flat: flat[offset[2]+1], O(1)");
        System.out.println("flat array size = " + flat.length + ", jagged total = "
                + java.util.Arrays.stream(jag).mapToInt(a -> a.length).sum());
        // (c) ArrayList<String[]>
        java.util.ArrayList<String[]> list = new java.util.ArrayList<>();
        for (int s : perFloor) list.add(new String[s]);
        list.add(new String[6]);                                // a 6th floor can be added at run time
        System.out.println("ArrayList<String[]> now holds " + list.size() + " floors (array of arrays is fixed at 5)");
    }
}
