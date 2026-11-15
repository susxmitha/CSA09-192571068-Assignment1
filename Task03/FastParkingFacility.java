import java.util.ArrayDeque;

/**
 * Alternative concept for Task 3: a free-slot count per floor and a stack of free
 * slot numbers per floor. Same main() as ParkingFacility, so the outputs can be compared.
 */
public class FastParkingFacility {
    private final String[][] slots;
    private final int[] freeCount;                 // free slots on each floor
    private final ArrayDeque<Integer>[] freeStack; // free slot numbers on each floor

    @SuppressWarnings("unchecked")
    public FastParkingFacility(int[] slotsPerFloor) {
        slots = new String[slotsPerFloor.length][];
        freeCount = new int[slotsPerFloor.length];
        freeStack = new ArrayDeque[slotsPerFloor.length];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            slots[f] = new String[slotsPerFloor[f]];
            freeCount[f] = slotsPerFloor[f];
            freeStack[f] = new ArrayDeque<>();
            for (int s = slotsPerFloor[f] - 1; s >= 0; s--) freeStack[f].push(s); // 0 on top
        }
    }

    public int[] allocate(String vehicleNo) {
        for (int f = 0; f < freeCount.length; f++) {      // O(F) - no slot scanning
            if (freeCount[f] > 0) {
                int s = freeStack[f].pop();                // O(1)
                slots[f][s] = vehicleNo;
                freeCount[f]--;
                return new int[]{f, s};
            }
        }
        return null;
    }

    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        freeStack[floor].push(slot);                       // O(1)
        freeCount[floor]++;
        return true;
    }

    public static void main(String[] args) {
        FastParkingFacility p = new FastParkingFacility(new int[]{2, 1});
        System.out.println(java.util.Arrays.toString(p.allocate("TN01AB1111")));
        System.out.println(java.util.Arrays.toString(p.allocate("TN02CD2222")));
        System.out.println(java.util.Arrays.toString(p.allocate("TN03EF3333")));
        System.out.println(java.util.Arrays.toString(p.allocate("TN04GH4444")));
        System.out.println(p.release(0, 1) + " " + p.release(0, 1) + " "
                + p.release(5, 0));
        System.out.println(java.util.Arrays.toString(p.allocate("TN04GH4444")));
    }
}
