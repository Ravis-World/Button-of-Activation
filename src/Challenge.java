package src;

/**
 * Represents a single step in the 50-step alarm deactivation.
 */
public class Challenge {
    private String name;
    private ChallengeType type;
    private String requiredValue; // The expected input (e.g., "ftp 21", or "1,3,2,4,2,3")
    private String instructions;  // What the player needs to type

    public Challenge(String name, ChallengeType type, String requiredValue, String instructions) {
        this.name = name;
        this.type = type;
        this.requiredValue = requiredValue.toLowerCase(); // Standardize for easy comparison
        this.instructions = instructions;
    }

    // --- Getters ---
    public String getName() { return name; }
    public ChallengeType getType() { return type; }
    public String getRequiredValue() { return requiredValue; }
    public String getInstructions() { return instructions; }
}