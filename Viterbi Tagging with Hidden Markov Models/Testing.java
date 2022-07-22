import java.io.*;
import java.util.*;


/**
 * @author Andrew Cheng
 * This class contains some of the tests for the ViterbiDecoding and HMM files and also has console and file tests
 * that run the viterbi tagging process. There is also a method that calculates the accuracy for the viterbi tagging.
 */

public class Testing {

    /**
     * Method to run the Viterbi tagging procedure on a user input from console and prints viterbi tags string
     * @param trans hashmap of transitions
     * @param obs hashmap of observations
     */
    public static void viterbiConsoleTest(HashMap<String, HashMap<String, Double>> trans, HashMap<String, HashMap<String, Double>> obs){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type a sentence to view the Viterbi tags associated:");
        String lineInput = scanner.nextLine();
        String lineOutput = ViterbiDecoding.viterbiTagging(lineInput, trans, obs);
        System.out.println(lineOutput);
    }

    /**
     * Method to run the Viterbi tagging procedure on an input filePath and return a string of the tagged filePath.
     * @param filePath file path location
     * @param trans hashmap of transitions
     * @param obs hashmap of observations
     * @return file name of the file path for the viterbi tags
     */
    public static String viterbiFileTest(String filePath, HashMap<String, HashMap<String, Double>> trans, HashMap<String, HashMap<String, Double>> obs){
        BufferedReader input = null;
        BufferedWriter output = null;
        String taggedFile = null;

        try {

            taggedFile = filePath.substring(0, filePath.length() - 4) + "-viterbiTagged.txt";
            input = new BufferedReader(new FileReader(filePath));
            output = new BufferedWriter(new FileWriter(taggedFile));
            String lineInput;
            String taggedLine;

            // writes the tags to a new file
            while ((lineInput = input.readLine()) != null) {
                taggedLine = ViterbiDecoding.viterbiTagging(lineInput, trans, obs);
                output.write(taggedLine +"\n");
            }

        }
        catch (IOException E) {
            System.err.println("Issue with writing tags!");

        }

        // closes file
        try {
            if (input != null && output != null) {
                input.close();
                output.close();
            }
        }

        catch (IOException E) {
            System.err.println("Issue closing file!");
        }

        return taggedFile;

    }

    /**
     * This method calculates the accuracy in similarity of the two files by comparing each tag and marking
     * how many correct and wrong there are
     * @param A filepath of the first file
     * @param B filepath of the second file
     */
    public static void calculateAccuracy(String A, String B) {
        BufferedReader inputA = null;
        BufferedReader inputB = null;
        int total = 0;
        int correct = 0;
        int wrong;

        try {
            inputA = new BufferedReader(new FileReader(A));
            inputB = new BufferedReader(new FileReader(B));
            String[] tagListA;
            String[] tagListB;
            String stringA = inputA.readLine();
            String stringB = inputB.readLine();

            while (stringA != null) {
                // create lists of the tags
                tagListA = stringA.split(" ");
                tagListB = stringB.split(" ");

                // loops through the tags in the list
                for (int i = 0; i < tagListA.length; i++) {
                    if (tagListA[i].equals(tagListB[i])) {
                        correct++;
                    }
                }
                total += tagListA.length;
                // reads next line
                stringA = inputA.readLine();
                stringB = inputB.readLine();
            }

            // debugging and testing statements
            System.out.println("Currently testing - " + A);
            System.out.println(A + " contains " + total +" tags.");
            wrong = total - correct;
            System.out.println(correct + " of the tags are correct, " + wrong + " of the tags are wrong.");
            System.out.println("Approximately " + (double) correct / total * 100 + "% of the tags are correct in total.");
            System.out.println();
        }

        catch (IOException E) {
            System.err.println("error occurred when calculating accuracy!");
        }

        // closes files
        finally {

            try {
                if (inputA != null && inputB != null) {
                    inputA.close();
                    inputB.close();
                }
            }

            catch (IOException E) {
                System.err.println("Issue closing file!");
            }
        }
    }

    /**
     * method that takes a string and applies the viterbi tagging process on it
     * @param obs hashmap of observations
     * @param trans hashmap of transitions
     * @param input input string that is converted to tags
     */
    public static void viterbiStringTest(HashMap<String, HashMap<String, Double>> obs, HashMap<String, HashMap<String, Double>> trans, String input){
        System.out.println(ViterbiDecoding.viterbiTagging(input, trans, obs));
    }


    public static void main(String[] args) throws IOException{
        // creates the observations and transitions and calls the markov model training
        HashMap<String, HashMap<String, Double>> observations = HMM.obsTracker("PS5/texts/brown-train-sentences.txt", "PS5/texts/brown-train-tags.txt");
        HashMap<String, HashMap<String, Double>> transitions = HMM.transTracker("PS5/texts/brown-train-tags.txt");
        HMM.freqToLogProb(observations);
        HMM.freqToLogProb(transitions);

        // static test cases
        viterbiStringTest(observations, transitions, "your work is beautiful "); // PRO N V ADJ
        viterbiStringTest(observations, transitions, "you work for trains "); // PRO V P N
        viterbiStringTest(observations, transitions, "this cave is my night "); // DET N V PRO N
        viterbiStringTest(observations, transitions, "we hide the watch in a cave "); // PRO V DET N P DET N
        viterbiStringTest(observations, transitions, "trains are fast "); // N V ADJ
        viterbiStringTest(observations, transitions, "we fast in the night "); // PRO V P DET N
        viterbiStringTest(observations, transitions, "my trains bark "); // PRO N V
        viterbiStringTest(observations, transitions, "the dog held the bark "); // DET N V DET N
        viterbiStringTest(observations, transitions, "we should watch the dog work in a cave "); // PRO MOD V DET N V P DET N
        viterbiStringTest(observations, transitions, "he trains the dog "); // PRO V DET N

        System.out.println();

        // tests the file method with brown and simple tests
        String test1 = "PS5/texts/brown-test-sentences.txt", tag1 = "PS5/texts/brown-test-tags.txt";
        String taggedFile = viterbiFileTest(test1, transitions, observations);
        System.out.println("The file is tagged as " + taggedFile + ".");
        calculateAccuracy(tag1, taggedFile);

        String test2 = "PS5/texts/simple-test-sentences.txt", tag2 = "PS5/texts/simple-test-tags.txt";
        String taggedFile2 = viterbiFileTest(test2, transitions, observations);
        System.out.println("The file is tagged as " + taggedFile2 + ".");
        calculateAccuracy(tag2, taggedFile2);

        // tests the console input with scanner
        viterbiConsoleTest(transitions, observations);
    }


}