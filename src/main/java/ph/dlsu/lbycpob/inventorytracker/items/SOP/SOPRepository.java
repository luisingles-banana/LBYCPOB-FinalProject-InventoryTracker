package ph.dlsu.lbycpob.inventorytracker.items.SOP;

import java.util.List;

/** Holds the categorized library of crisis-specific SOPs. */
public final class SOPRepository {

    private static final List<SOP> SOPS = List.of(
            new SOP(
                    "Earthquake",
                    "Immediate response protocol for a strong seismic event on campus.",
                    List.of(
                            "Drop, Cover, and Hold On until shaking stops.",
                            "Evacuate to designated open ground assembly areas.",
                            "Account for all personnel and report injuries to the response team.",
                            "Open the relief center and begin distribution to affected individuals."
                    ),
                    List.of("First-aid kits", "Flashlights", "Bottled water", "Ready-to-eat food packs", "Emergency blankets")
            ),
            new SOP(
                    "Fire",
                    "Response protocol for a building or campus fire emergency.",
                    List.of(
                            "Trigger fire alarm and call emergency services.",
                            "Evacuate via nearest fire exit, do not use elevators.",
                            "Assemble at the designated fire muster point.",
                            "Deploy rescue gear and first aid to any injured individuals."
                    ),
                    List.of("Fire extinguishers", "First-aid kits", "Rescue ropes", "Flashlights", "Oxygen masks")
            ),
            new SOP(
                    "Flood",
                    "Response protocol for severe flooding around the university.",
                    List.of(
                            "Move to higher ground; avoid walking or driving through floodwater.",
                            "Cut off electrical supply in affected areas if safe to do so.",
                            "Set up a relief center stocked with food and clean water.",
                            "Distribute rescue gear (life vests, ropes) to volunteer response teams."
                    ),
                    List.of("Life vests", "Rescue ropes", "Water purification tablets", "Food packs", "Rubber boots")
            ),
            new SOP(
                    "Typhoon",
                    "Preparedness and response protocol for an incoming typhoon.",
                    List.of(
                            "Monitor PAGASA advisories and suspend classes/operations as needed.",
                            "Pre-position emergency supplies at accessible relief centers.",
                            "Secure loose objects and reinforce vulnerable structures.",
                            "After landfall, assess damage and begin distribution of relief goods."
                    ),
                    List.of("Food packs", "Bottled water", "Flashlights", "Batteries", "Emergency blankets", "First-aid kits")
            ),
            new SOP(
                    "Medical Emergency",
                    "Protocol for handling an on-campus medical emergency during a crisis.",
                    List.of(
                            "Assess the patient and call for on-site medical personnel.",
                            "Administer first aid using available medical supplies.",
                            "Coordinate transport to the nearest hospital if required.",
                            "Log supplies used and request replenishment from donors."
                    ),
                    List.of("First-aid kits", "Medicines", "Personal protective equipment", "Stretchers")
            )
    );

    private SOPRepository() {
    }

    public static List<SOP> getAllSOPs() {
        return SOPS;
    }
}
