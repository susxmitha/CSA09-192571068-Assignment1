package parking.service;

import parking.model.MonthlyPass;
import parking.model.Pass;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/** Creates passes from a kind name and a start date, e.g. MONTHLY:2026-11-01. */
public final class PassFactory {
    private static final Map<String, Function<LocalDate, Pass>> REGISTRY = new HashMap<>();

    static {
        register("MONTHLY", MonthlyPass::new);
    }

    private PassFactory() { }

    public static void register(String kind, Function<LocalDate, Pass> maker) {
        REGISTRY.put(kind.trim().toUpperCase(), maker);
    }

    public static Pass create(String kind, LocalDate start) {
        Function<LocalDate, Pass> maker = REGISTRY.get(kind.trim().toUpperCase());
        if (maker == null) {
            throw new IllegalArgumentException("Unknown pass type: " + kind);
        }
        return maker.apply(start);
    }
}
