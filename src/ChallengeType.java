package src;

/**
 * Defines the different types of interactive challenges for the alarm deactivation.
 */
public enum ChallengeType {
    // A command requiring a specific sequence of numbers (e.g., 1,3,2,4,2,3)
    SEQUENCE_INPUT,

    // A command requiring a specific word or code to be typed (e.g., 'override')
    CODE_INPUT,

    // A command requiring specific syntax (e.g., 'crack port ftp 21')
    PORT_CRACK
}