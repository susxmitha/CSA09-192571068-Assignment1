package parking.service;

import parking.model.Chargeable;
import parking.model.ParkingTicket;
import parking.model.Pass;
import parking.model.Vehicle;

import java.time.LocalDateTime;

public class ParkingService {
    private final ParkingFacility facility;
    private final ParkingTicket[] active;                // active tickets, packed from index 0
    private int activeCount = 0;
    private double collection = 0;

    public ParkingService(ParkingFacility facility) {
        this.facility = facility;
        this.active = new ParkingTicket[facility.capacity()];
    }

    /** Vehicle enters: validate, allocate a slot, issue a ticket. */
    public ParkingTicket enter(String type, String regNo, LocalDateTime time, Pass pass) {
        Vehicle vehicle = VehicleFactory.create(type, regNo);   // rejects unknown type / bad plate
        if (indexOf(regNo) >= 0) {
            throw new IllegalStateException("Duplicate registration number: " + regNo);
        }
        int[] pos = facility.allocate(regNo);
        if (pos == null) {
            throw new IllegalStateException("Facility full - no free slot for " + regNo);
        }
        ParkingTicket ticket = new ParkingTicket(vehicle, pos[0], pos[1], time, pass);
        active[activeCount++] = ticket;
        if (pass != null) {
            collection += pass.priceAtGate();
        }
        return ticket;
    }

    /** Vehicle leaves: compute the fee, release the slot, return the amount charged. */
    public double exit(String regNo, LocalDateTime time, int kWh) {
        int idx = indexOf(regNo);
        if (idx < 0) {
            throw new IllegalArgumentException("No active ticket for " + regNo);
        }
        ParkingTicket t = active[idx];
        Vehicle v = t.getVehicle();

        double parkingFee = v.calculateFee(t.getEntryTime(), time);   // dynamic dispatch
        Pass p = t.getPass();
        if (p != null && p.isValidOn(t.getEntryTime().toLocalDate())
                && p.isValidOn(time.toLocalDate())) {
            parkingFee = 0;                                           // covered by a valid pass
        }
        double chargingFee = 0;
        if (kWh > 0) {
            if (!(v instanceof Chargeable)) {
                throw new IllegalArgumentException(regNo + " cannot be charged");
            }
            t.setUnitsCharged(kWh);
            chargingFee = ((Chargeable) v).chargingFee(kWh);
        }

        facility.release(t.getFloor(), t.getSlot());
        active[idx] = active[--activeCount];                          // O(1) removal
        active[activeCount] = null;                                   // let the ticket be collected
        double total = parkingFee + chargingFee;
        collection += total;
        return total;
    }

    private int indexOf(String regNo) {                               // linear search
        for (int i = 0; i < activeCount; i++) {
            if (active[i].getVehicle().getRegNo().equals(regNo)) return i;
        }
        return -1;
    }

    public double getCollection() { return collection; }
    public int activeCount() { return activeCount; }
    public ParkingFacility getFacility() { return facility; }
}
