package parking.model;

import java.time.LocalDateTime;

public final class ParkingTicket {
    private static int lastTicketNo = 1000;              // shared by every ticket ever issued

    private final int ticketNo;
    private final Vehicle vehicle;
    private final int floor;
    private final int slot;
    private final LocalDateTime entryTime;
    private final Pass pass;                             // null for a visitor without a pass
    private int unitsCharged;                            // kWh charged, set at exit

    /** Master constructor: every other constructor ends up here. */
    public ParkingTicket(Vehicle vehicle, int floor, int slot,
                         LocalDateTime entryTime, Pass pass) {
        if (vehicle == null || entryTime == null) {
            throw new IllegalArgumentException("Vehicle and entry time are required");
        }
        if (floor < 0 || slot < 0) {
            throw new IllegalArgumentException("Invalid slot position");
        }
        this.ticketNo = ++lastTicketNo;                  // unique, never reused
        this.vehicle = vehicle;
        this.floor = floor;
        this.slot = slot;
        this.entryTime = entryTime;
        this.pass = pass;
    }

    public ParkingTicket(Vehicle vehicle, int floor, int slot, LocalDateTime entryTime) {
        this(vehicle, floor, slot, entryTime, null);
    }

    public ParkingTicket(Vehicle vehicle, int floor, int slot) {
        this(vehicle, floor, slot, LocalDateTime.now());
    }

    /** Copy constructor: a duplicate ticket keeps the SAME ticket number. */
    public ParkingTicket(ParkingTicket other) {
        this.ticketNo = other.ticketNo;
        this.vehicle = other.vehicle;
        this.floor = other.floor;
        this.slot = other.slot;
        this.entryTime = other.entryTime;
        this.pass = other.pass;
        this.unitsCharged = other.unitsCharged;
    }

    public static int ticketsIssued() { return lastTicketNo - 1000; }

    public int getTicketNo() { return ticketNo; }
    public Vehicle getVehicle() { return vehicle; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public Pass getPass() { return pass; }
    public int getUnitsCharged() { return unitsCharged; }

    public void setUnitsCharged(int kWh) {
        if (kWh < 0) {
            throw new IllegalArgumentException("Units cannot be negative: " + kWh);
        }
        this.unitsCharged = kWh;
    }

    @Override public String toString() {
        return "Ticket#" + ticketNo + " " + vehicle + " floor " + floor + " slot " + slot
                + " in " + entryTime;
    }
}
