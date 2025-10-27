package src;

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

    // --- Configuration: Decreased by 1 to reserve the final step for the backdoor ---
    private static final int TOTAL_STEPS = 50; 
    private static final int RANDOM_CHALLENGES = TOTAL_STEPS - 1; 

    private int stepsRemaining = TOTAL_STEPS;
    private List<Challenge> challengeQueue;
    private Challenge currentChallenge = null; 
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
    // --- Port Data Initialization (No Change) ---
    // -------------------------------------------------------------------
    
    private void initializePortData() {
        commonPorts = new ArrayList<>();
        
        // Add the common and critical Well-Known TCP Ports (0-1023 range)
        commonPorts.add(new PortInfo("TCP Port Service Multiplexer", 1));
        commonPorts.add(new PortInfo("Echo Protocol", 7));
        commonPorts.add(new PortInfo("Discard Protocol", 9));
        commonPorts.add(new PortInfo("Daytime Protocol", 13));
        commonPorts.add(new PortInfo("FTP Data", 20));
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
    // --- Challenge Generation (MODIFIED) ---
    // -------------------------------------------------------------------

    private void initializeChallenges() {
        challengeQueue = new ArrayList<>();
        
        // 1. Generate 49 randomized challenges (RANDOM_CHALLENGES = 49)
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
            ChallengeType.PORT_CRACK, // Still a port crack type
            finalRequiredValue,
            finalInstructions
        );
        
        // 4. Add the final step as the last item in the queue
        challengeQueue.add(finalBackdoor);
    }

    private Challenge createRandomChallenge() {
        // This method only creates random challenges, ensuring it never hits the final Port 0 step
        ChallengeType[] types = ChallengeType.values();
        ChallengeType type = types[ThreadLocalRandom.current().nextInt(types.length)];
        
        String name;
        String requiredValue;
        String instructions;

        switch (type) {
            case PORT_CRACK:
                PortInfo targetPort = commonPorts.get(
                    ThreadLocalRandom.current().nextInt(commonPorts.size())
                );
                
                String shortName = targetPort.name.split(" ")[0].toLowerCase();
                if (shortName.equals("file")) shortName = "ftp";
                
                name = "Open a Backdoor Port: " + targetPort.name + " (Port " + targetPort.number + ")";
                instructions = "COMMAND: crack port " + shortName + " " + targetPort.number;
                requiredValue = "crack port " + shortName + " " + targetPort.number;
                break;
                
            case SEQUENCE_INPUT:
                StringBuilder sequence = new StringBuilder();
                int sequenceLength = ThreadLocalRandom.current().nextInt(5, 8);
                for (int i = 0; i < sequenceLength; i++) {
                    sequence.append(ThreadLocalRandom.current().nextInt(1, 5));
                    if (i < sequenceLength - 1) {
                        sequence.append(",");
                    }
                }
                
                name = "Input Deactivation Sequence (" + sequenceLength + " steps)";
                instructions = "INPUT: The exact number sequence, separated by commas (e.g., 1,3,2,4,2,3)";
                requiredValue = sequence.toString();
                break;
                
            case CODE_INPUT:
                String[] codes = {"OVERRIDE", "DECRYPT", "MALFORM", "INITIATE", "BREACH"};
                String codeWord = codes[ThreadLocalRandom.current().nextInt(codes.length)];
                
                name = "Run Malicious Code: " + codeWord;
                instructions = "COMMAND: " + codeWord;
                requiredValue = codeWord;
                break;
                
            default:
                name = "System Glitch - REBOOT";
                instructions = "COMMAND: REBOOT";
                requiredValue = "reboot";
        }
        
        return new Challenge(name, type, requiredValue, instructions);
    }

    // -------------------------------------------------------------------
    // --- Game Loop and Logic (No Change) ---
    // -------------------------------------------------------------------

    public void startGame() {
        System.out.println("---------------------------------------------------------");
        System.out.println("  WELCOME, HENCHMAN Qess. Omega, TO THE BUTTON OF ACTIVATION");
        System.out.println("---------------------------------------------------------");
        System.out.println("OBJECTIVE: Complete 50 steps to breach the system and activate the Subgiant Phase.");
        System.out.println("Type 'EXIT' to quit at any time.");
        System.out.println("\n... Initializing Alarm Sequence ...\n");
        
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
                pressEnterToContinue(); // <--- ADDED PAUSE ON EXIT
                return;
            }
            
            if (checkInput(input)) {
                System.out.println("\n[SUCCESS] Step Complete. Access Granted.\n");
                stepsRemaining--;
                if (stepsRemaining > 0) {
                    pressEnterToContinue(); // <--- ADDED PAUSE ON SUCCESS (Unless game is won)
                }
            } else {
                int randomIndex = ThreadLocalRandom.current().nextInt(challengeQueue.size() + 1);
                challengeQueue.add(randomIndex, currentChallenge);
                
                System.out.println("\n[FAILURE] Invalid Command. System Re-shuffled. Try again, Omega.\n");
                pressEnterToContinue(); // <--- ADDED PAUSE ON FAILURE
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

        // --- NEW: Wait for the final action ---
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
                // Allow the player to quit gracefully even here
                System.out.println("\nFinal action aborted. System remaining in breached state.");
                buttonPressed = true;
            } else {
                System.out.println("\n[ERROR] Invalid command. Only 'PRESS BUTTON' will trigger the sequence.");
            }
        }
        
        // The pause method is now called right before closing
        pressEnterToContinue(); 
        scanner.close();
    }

    private void pressEnterToContinue() {
        System.out.print("Press ENTER to continue...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        ButtonOfActivationGame game = new ButtonOfActivationGame();
        game.startGame();
    }
}