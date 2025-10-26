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

    private static final int TOTAL_STEPS = 50;
    private int stepsRemaining = TOTAL_STEPS;
    private List<Challenge> challengeQueue;
    private Challenge currentChallenge = null; // <--- FIXED: Explicitly initialized to null
    private Scanner scanner;
    
    // --- New Data Structure to hold Port Name and Number ---
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
        initializePortData(); // NEW: Initialize the port list
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
    // --- Challenge Generation ---
    // -------------------------------------------------------------------

    private void initializeChallenges() {
        challengeQueue = new ArrayList<>();
        
        for (int i = 0; i < TOTAL_STEPS; i++) {
            challengeQueue.add(createRandomChallenge());
        }

        Collections.shuffle(challengeQueue);
    }

    private Challenge createRandomChallenge() {
        ChallengeType[] types = ChallengeType.values();
        ChallengeType type = types[ThreadLocalRandom.current().nextInt(types.length)];
        
        String name;
        String requiredValue;
        String instructions;

        switch (type) {
            case PORT_CRACK:
                // --- MODIFIED PORT CRACK LOGIC ---
                // Randomly select a PortInfo object from our list
                PortInfo targetPort = commonPorts.get(
                    ThreadLocalRandom.current().nextInt(commonPorts.size())
                );
                
                // Use a short, recognizable port name for the command
                String shortName = targetPort.name.split(" ")[0].toLowerCase();
                if (shortName.equals("file")) shortName = "ftp";
                
                name = "Open a Backdoor Port: " + targetPort.name + " (Port " + targetPort.number + ")";
                instructions = "COMMAND: crack port " + shortName + " " + targetPort.number;
                requiredValue = "crack port " + shortName + " " + targetPort.number;
                break;
                
            case SEQUENCE_INPUT:
                // Generate a random sequence of 5-7 numbers
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
                // Generate a random malicious code word
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
    // --- Game Loop and Logic (Unchanged) ---
    // -------------------------------------------------------------------

    public void startGame() {
        System.out.println("---------------------------------------------------------");
        System.out.println("  WELCOME, HENCHMAN Qess. Ω, TO THE BUTTON OF ACTIVATION");
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
                return;
            }
            
            if (checkInput(input)) {
                System.out.println("\n[SUCCESS] Step Complete. Access Granted.\n");
                stepsRemaining--;
            } else {
                int randomIndex = ThreadLocalRandom.current().nextInt(challengeQueue.size() + 1);
                challengeQueue.add(randomIndex, currentChallenge);
                
                System.out.println("\n[FAILURE] Invalid Command. System Re-shuffled. Try again, Ω.\n");
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
        System.out.println("\nHenchman Qess. Ω, your mission is complete. The Sun is yours.");
        scanner.close();
    }

    public static void main(String[] args) {
        ButtonOfActivationGame game = new ButtonOfActivationGame();
        game.startGame();
    }
}