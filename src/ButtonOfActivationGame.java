import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Button of Activation: Text Adventure Console Game
 * Your objective: Complete 50 randomized breach steps.
 */
public class ButtonOfActivationGame {

    // --- Configuration ---
    private static final int TOTAL_STEPS = 50; 
    private static final int RANDOM_CHALLENGES = TOTAL_STEPS - 1; 

    // --- Code Mapping for Sequence Input ---
    // Rule: Length 5 = ALPHA, Length 6 = BETA, Length 7 = GAMMA
    private static final String[] SEQUENCE_CODES = {"ALPHA", "BETA", "GAMMA", "DELTA", "EPSILON"}; 
    
    // --- Game State ---
    private int stepsRemaining = TOTAL_STEPS;
    private List<Challenge> challengeQueue;
    private Challenge currentChallenge = null; // Variable initialization fix
    private Scanner scanner;
    
    // --- Data Structures ---
    private List<PortInfo> commonPorts;

    /**
     * Inner class to pair a port's name with its number.
     */
    private static class PortInfo {
        String name;
        int number;

        PortInfo(String name, int number) {
            this.name = name;
            this.number = number;
        }
    }

    public ButtonOfActivationGame() {
        scanner = new Scanner(System.in);
        initializePortData();
        initializeChallenges();
    }
    
    // -------------------------------------------------------------------
    // --- Port Data Initialization ---
    // -------------------------------------------------------------------
    
    /**
     * Populates the list of common ports with official IANA data.
     */
    private void initializePortData() {
        commonPorts = new ArrayList<>();
        
        // Add the common and critical Well-Known TCP Ports (0-1023 range)
        commonPorts.add(new PortInfo("TCP Port Service Multiplexer", 1));
        commonPorts.add(new PortInfo("Echo Protocol", 7));
        commonPorts.add(new PortInfo("Discard Protocol", 9));
        commonPorts.add(new PortInfo("Daytime Protocol", 13));
        commonPorts.add(new PortInfo("FTP Control (Command)", 21));
        commonPorts.add(new PortInfo("Secure Shell (SSH)", 22));
        commonPorts.add(new PortInfo("Telnet", 23));
        commonPorts.add(new PortInfo("Simple Mail Transfer Protocol (SMTP)", 25));
        commonPorts.add(new PortInfo("Time Protocol", 37));
        commonPorts.add(new PortInfo("Domain Name System (DNS)", 53));
        commonPorts.add(new PortInfo("Hypertext Transfer Protocol (HTTP)", 80));
        commonPorts.add(new PortInfo("Post Office Protocol v3 (POP3)", 110));
        commonPorts.add(new PortInfo("Internet Message Access Protocol (IMAP)", 143));
        commonPorts.add(new PortInfo("HTTP Secure (HTTPS)", 443));
    }


    // -------------------------------------------------------------------
    // --- Challenge Generation ---
    // -------------------------------------------------------------------

    private void initializeChallenges() {
        challengeQueue = new ArrayList<>();
        
        // 1. Generate 49 randomized challenges
        for (int i = 0; i < RANDOM_CHALLENGES; i++) {
            challengeQueue.add(createRandomChallenge());
        }

        // 2. Shuffle the 49 challenges
        Collections.shuffle(challengeQueue);
        
        // 3. Create the final, Port 0 Backdoor challenge
        String finalName = "System Backdoor Final Access - Port 0 Injection";
        String finalInstructions = "COMMAND: crack port backdoor 0";
        String finalRequiredValue = "crack port backdoor 0";
        
        Challenge finalBackdoor = new Challenge(
            finalName, 
            ChallengeType.PORT_CRACK, 
            finalRequiredValue,
            finalInstructions
        );
        
        // 4. Add the final step as the last item in the queue
        challengeQueue.add(finalBackdoor);
    }

    private Challenge createRandomChallenge() {
        // Ensure all challenge types are used, including the new ones
        ChallengeType[] allTypes = ChallengeType.values();
        ChallengeType type = allTypes[ThreadLocalRandom.current().nextInt(allTypes.length)];
        
        String name;
        String requiredValue;
        String instructions;

        switch (type) {
            case PORT_CRACK:
                PortInfo targetPort = commonPorts.get(
                    ThreadLocalRandom.current().nextInt(commonPorts.size())
                );
                
                String shortName = "";
                
                if (targetPort.name.contains("(")) {
                    // Rule 1: Extract name from parentheses (e.g., 'SSH', 'SMTP')
                    int start = targetPort.name.indexOf("(") + 1;
                    int end = targetPort.name.indexOf(")");
                    shortName = targetPort.name.substring(start, end).toLowerCase();
                } else {
                    // Rule 2: Use specific names for clarity if no parentheses exist
                    switch(targetPort.number) {
                        case 23: shortName = "telnet"; break;
                        case 21: shortName = "ftp"; break;
                        case 7: shortName = "echo"; break;
                        case 37: shortName = "time"; break;
                        default:
                            shortName = targetPort.name.split(" ")[0].toLowerCase();
                            break;
                    }
                }

                name = "Remote Access Protocol Port " + targetPort.number;
                instructions = "TARGET SERVICE: " + targetPort.name + " (" + targetPort.number + "). Use the standard crack syntax: **crack port [short_name] [number]**.";
                requiredValue = "crack port " + shortName + " " + targetPort.number;
                break;
                
            case SEQUENCE_INPUT:
                // Generate the sequence string (for display/name)
                StringBuilder sequence = new StringBuilder();
                int sequenceLength = ThreadLocalRandom.current().nextInt(5, 10);
                for (int i = 0; i < sequenceLength; i++) {
                    sequence.append(ThreadLocalRandom.current().nextInt(1, 5));
                    if (i < sequenceLength - 1) {
                        sequence.append(",");
                    }
                }
                
                // Define the secret code word based on the LENGTH (the hint rule)
                int codeIndex = sequenceLength - 5;
                if (codeIndex >= SEQUENCE_CODES.length) { codeIndex = 0; }
                String secretCode = SEQUENCE_CODES[codeIndex];
                
                name = "Deactivation Sequence Pattern: " + sequence.toString();
                instructions = "INSTRUCTION: The sequence is active. Input the corresponding Code Word to deactivate the pattern.";
                requiredValue = secretCode.toLowerCase();
                break;
                
            case CODE_INPUT:
                String[] codes = {"OVERRIDE", "DECRYPT", "MALFORM", "INITIATE", "BREACH", "SABOTAGE", "REVERSE"};
                String codeWord = codes[ThreadLocalRandom.current().nextInt(codes.length)];
                
                name = "Run Malicious Code: " + codeWord;
                instructions = "COMMAND: Execute the required code word.";
                requiredValue = codeWord.toLowerCase();
                break;

            // --- NEW CHALLENGES START HERE ---
            
            case FILE_DELETE: // NEW
                String[] dirs = {"/tmp/", "/system/cache/", "/log/access/", "/usr/data/"};
                String[] extensions = {".log", ".tmp", ".dat", ".cfg"};
                
                // Generate a random path and file name
                String path = dirs[ThreadLocalRandom.current().nextInt(dirs.length)];
                String extension = extensions[ThreadLocalRandom.current().nextInt(extensions.length)];
                String fileName = "file" + ThreadLocalRandom.current().nextInt(100, 999) + extension;
                
                name = "Emergency File Cleanup";
                instructions = "COMMAND: Execute 'delete' on the critical file located at: **" + path + fileName + "**";
                requiredValue = "delete " + path + fileName;
                break;

            case MATH_CRACK: // NEW
                // Generate two numbers and an operator
                int num1 = ThreadLocalRandom.current().nextInt(20, 100); // 20-99
                int num2 = ThreadLocalRandom.current().nextInt(10, num1 - 5); // Ensure positive result for subtraction
                String operator = (ThreadLocalRandom.current().nextBoolean() ? "+" : "-");
                
                int answer = (operator.equals("+")) ? (num1 + num2) : (num1 - num2);
                
                name = "CPU Core Verification Check";
                instructions = "TASK: Calculate the result and input the exact integer: **" + num1 + " " + operator + " " + num2 + "**";
                requiredValue = String.valueOf(answer); // The required value is the simple integer answer
                break;
            // --- NEW CHALLENGES END HERE ---
                
            default:
                name = "System Glitch - REBOOT";
                instructions = "COMMAND: REBOOT";
                requiredValue = "reboot";
        }
        
        return new Challenge(name, type, requiredValue, instructions);
    }

    // -------------------------------------------------------------------
    // --- Game Loop and Logic (Rest of the class remains the same) ---
    // -------------------------------------------------------------------

    public void startGame() {
        System.out.println("---------------------------------------------------------");
        System.out.println("  WELCOME, HENCHMAN Qess. Omega, TO THE BUTTON OF ACTIVATION");
        System.out.println("---------------------------------------------------------");
        System.out.println("OBJECTIVE: Complete 50 steps to breach the system and activate the Sun's Subgiant Phase.");
        System.out.println("Type 'EXIT' to quit at any time.");
        // Display the crucial hint for sequence challenges
        System.out.println("HINT: Deactivation Codes are mapped by pattern length: 5=ALPHA, 6=BETA, 7=GAMMA, 8=DELTA, 9=EPSILON.");
        System.out.println("\n... Initialising Alarm Sequence ...\n");
        
        while (stepsRemaining > 0) {
            currentChallenge = challengeQueue.remove(0); 
            
            System.out.println("--- STEP " + (TOTAL_STEPS - stepsRemaining + 1) + " of " + TOTAL_STEPS + " ---");
            System.out.println("STATUS: Alarm Deactivation Steps Remaining: " + stepsRemaining);
            System.out.println("CURRENT TASK: " + currentChallenge.getName());
            System.out.println("INSTRUCTIONS: " + currentChallenge.getInstructions());
            System.out.print("> ");
            
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("EXIT")) {
                System.out.println("\nMission aborted. The Sun will wait for your return.");
                pressEnterToContinue(); 
                return;
            }
            
            if (checkInput(input)) {
                System.out.println("\n[SUCCESS] Step Complete. Access Granted.\n");
                stepsRemaining--;
                if (stepsRemaining > 0) {
                    pressEnterToContinue(); 
                }
            } else {
                int randomIndex = ThreadLocalRandom.current().nextInt(challengeQueue.size() + 1);
                challengeQueue.add(randomIndex, currentChallenge);
                
                // Hint for failed PORT_CRACK attempts
                if (currentChallenge.getType() == ChallengeType.PORT_CRACK) {
                     System.out.println("\n[FAILURE] Invalid Command Syntax. Did you remember 'crack port [short_name] [number]'?");
                } else {
                     System.out.println("\n[FAILURE] Invalid Command. System Re-shuffled. Try again, Omega.\n");
                }
                pressEnterToContinue(); 
            }
        }
        
        gameWon();
    }
    
    private boolean checkInput(String input) {
        String standardizedInput = input.trim().toLowerCase();
        String required = currentChallenge.getRequiredValue(); 
        return standardizedInput.equals(required);
    }

    private void gameWon() {
        System.out.println("=========================================================");
        System.out.println("||              SYSTEM BREACH COMPLETE!                ||");
        System.out.println("||  INITIATING SUBGIANT PHASE ACTIVATION SEQUENCE...   ||");
        System.out.println("=========================================================");
        System.out.println("\nHenchman Qess. Omega, your mission is complete. The Button is ready.");

        // --- Wait for the final action (manual button press) ---
        boolean buttonPressed = false;
        while (!buttonPressed) {
            System.out.println("\n[FINAL COMMAND] The activation matrix is primed. Execute the final order.");
            System.out.println("INSTRUCTIONS: Type 'PRESS BUTTON' to begin Subgiant Phase.");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("PRESS BUTTON")) {
                System.out.println("\n<<< BUTTON PRESSED >>>");
                System.out.println("---------------------------------------------------------");
                System.out.println("The Subgiant Phase has begun. Mission accomplished.");
                buttonPressed = true; // Exit the loop
            } else if (input.equalsIgnoreCase("EXIT")) {
                System.out.println("\nFinal action aborted. System remaining in breached state.");
                buttonPressed = true;
            } else {
                System.out.println("\n[ERROR] Invalid command. Only 'PRESS BUTTON' will trigger the sequence.");
            }
        }
        
        pressEnterToContinue(); 
        scanner.close();
    }
    
    /**
     * Pauses the console and waits for the user to press the ENTER key.
     */
    private void pressEnterToContinue() {
        System.out.print("Press ENTER to continue...");
        scanner.nextLine(); 
    }

    public static void main(String[] args) {
        // *** CODE ADDED TO SET CONSOLE TITLE ON WINDOWS ***
        try {
            // Execute the Windows 'title' command using cmd.exe
            // The /c flag tells cmd to carry out the command and then terminate
            new ProcessBuilder("cmd", "/c", "title Button of Activation").start();
        } catch (IOException e) {
            // This block handles potential errors (e.g., if running on a non-Windows OS
            // where 'cmd' is not found, or if permissions are insufficient).
            // We just print the error and let the game continue.
            System.err.println("Could not set console title: " + e.getMessage());
        }
        
        // Start the game instance and run the game loop
        ButtonOfActivationGame game = new ButtonOfActivationGame();
        game.startGame();
    }
}