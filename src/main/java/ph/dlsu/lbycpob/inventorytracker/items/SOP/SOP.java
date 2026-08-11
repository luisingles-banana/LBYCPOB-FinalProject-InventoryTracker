package ph.dlsu.lbycpob.inventorytracker.items.SOP;

import java.util.List;

/** A Standard Operating Procedure for a specific crisis scenario. */
public class SOP {
    private final String scenario;
    private final String description;
    private final List<String> protocolSteps;
    private final List<String> requiredSupplies;

    public SOP(String scenario, String description, List<String> protocolSteps, List<String> requiredSupplies) {
        this.scenario = scenario;
        this.description = description;
        this.protocolSteps = protocolSteps;
        this.requiredSupplies = requiredSupplies;
    }

    public String getScenario() {
        return scenario;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getProtocolSteps() {
        return protocolSteps;
    }

    public List<String> getRequiredSupplies() {
        return requiredSupplies;
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ").append(scenario).append(" ===\n");
        sb.append(description).append("\n\n");
        sb.append("Protocol:\n");
        int step = 1;
        for (String s : protocolSteps) {
            sb.append("  ").append(step++).append(". ").append(s).append("\n");
        }
        sb.append("\nRequired Supply Checklist:\n");
        for (String supply : requiredSupplies) {
            sb.append("  [ ] ").append(supply).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return scenario;
    }
}
