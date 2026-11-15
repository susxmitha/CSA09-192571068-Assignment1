package parking.service;

import parking.model.Car;
import parking.model.ElectricCar;
import parking.model.TwoWheeler;
import parking.model.Vehicle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/** Creates vehicles from a type name. New types are registered, not coded into a switch. */
public final class VehicleFactory {
    private static final Map<String, Function<String, Vehicle>> REGISTRY = new HashMap<>();

    static {                                             // static block: runs once at class load
        register("TWOWHEELER", TwoWheeler::new);
        register("CAR", Car::new);
        register("EV", ElectricCar::new);
    }

    private VehicleFactory() { }

    public static void register(String type, Function<String, Vehicle> maker) {
        REGISTRY.put(type.trim().toUpperCase(), maker);
    }

    public static Vehicle create(String type, String regNo) {
        Function<String, Vehicle> maker =
                REGISTRY.get(type == null ? "" : type.trim().toUpperCase());
        if (maker == null) {
            throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
        return maker.apply(regNo);
    }
}
