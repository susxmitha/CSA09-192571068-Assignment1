package parking.app;

import parking.model.DeliveryVan;
import parking.model.EBike;
import parking.model.OneDayPass;
import parking.service.PassFactory;
import parking.service.VehicleFactory;

import java.io.IOException;

/** Registers the new categories, then reuses the unchanged Main to run the scenario. */
public class Task12Main {
    public static void register() {
        VehicleFactory.register("EBIKE", EBike::new);
        VehicleFactory.register("DELIVERYVAN", DeliveryVan::new);
        PassFactory.register("DAYPASS", OneDayPass::new);
    }

    public static void main(String[] args) throws IOException {
        register();
        Main.run(args.length > 0 ? args[0] : "input/scenario12.txt");
    }
}
