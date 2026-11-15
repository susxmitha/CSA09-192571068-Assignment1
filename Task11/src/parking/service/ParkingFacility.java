package parking.service;

public class ParkingFacility {
    private static int totalOccupied = 0;                // across every facility object

    private final String[][] slots;                      // jagged: floors may differ in size
    private int occupied = 0;

    public ParkingFacility(int[] slotsPerFloor) {
        if (slotsPerFloor == null || slotsPerFloor.length == 0) {
            throw new IllegalArgumentException("At least one floor is required");
        }
        slots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] <= 0) {
                throw new IllegalArgumentException("Floor " + f + " must have at least one slot");
            }
            slots[f] = new String[slotsPerFloor[f]];
        }
    }

    /** Returns {floor, slot} of the first free slot, or null if it is full. */
    public int[] allocate(String vehicleNo) {
        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = vehicleNo;
                    occupied++;
                    totalOccupied++;
                    return new int[]{f, s};
                }
            }
        }
        return null;
    }

    /** Frees a slot. Returns false if the position is invalid or already free. */
    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot] == null) return false;
        slots[floor][slot] = null;
        occupied--;
        totalOccupied--;
        return true;
    }

    public int capacity() {
        int total = 0;
        for (String[] floor : slots) total += floor.length;
        return total;
    }

    public int occupied() { return occupied; }

    public int freeSlots() { return capacity() - occupied; }

    public int floors() { return slots.length; }

    public static int totalOccupiedAllFacilities() { return totalOccupied; }
}
