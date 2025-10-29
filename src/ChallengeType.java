/**
 * ChallengeType.java
 * Enumerates the different types of security challenges the player must complete.
 * This determines the logic for generating the puzzle and checking the input.
 */
public enum ChallengeType {
    
    /** Requires combining a command verb, short name, and number (e.g., crack port ssh 22). */
    PORT_CRACK,
    
    /** Requires inputting a secret code word associated with a displayed pattern (e.g., ALPHA). */
    SEQUENCE_INPUT,
    
    /** Requires typing a specific, visible code word (e.g., OVERRIDE). */
    CODE_INPUT,
    
    /** Requires typing a file path and name to delete (e.g., delete /temp/log.tmp). */
    FILE_DELETE, // NEW CHALLENGE TYPE
    
    /** Requires typing the correct numerical answer to a simple math problem. */
    MATH_CRACK // NEW CHALLENGE TYPE
}
