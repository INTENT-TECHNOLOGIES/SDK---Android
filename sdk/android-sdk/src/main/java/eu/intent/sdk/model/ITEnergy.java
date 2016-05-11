package eu.intent.sdk.model;

import java.util.Locale;

/**
 * The types of measured energies.
 */
public enum ITEnergy {
    ELEC, GAS, HEAT, TEMP, WATER, UNKNOWN;

    /**
     * Returns an ITEnergy from its name, as defined in the API.
     * @throws IllegalArgumentException if the given key does not exist
     */
    public static ITEnergy fromString(String energy) throws IllegalArgumentException {
        return ITEnergy.valueOf(energy.toUpperCase(Locale.US));
    }

    @Override
    public String toString() {
        return name().toLowerCase(Locale.US);
    }
}
