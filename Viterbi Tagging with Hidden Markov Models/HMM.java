
import java.util.*;
import java.io.*;

/**
 * @author Andrew Cheng
 * This class contains the emethods that are used to keep track of observations
 * states and transitions of the hidden markov model and calculates the probabilities
 * using natural log in the markov model.
 */
public class HMM {

    /**
     * creates a map that counts the amount of observations
     * @param wordFilePath file path of the actual words
     * @param posFilePath  file path of the parts of speech
     * @return a map where key is a word and value is another map where the part of speech is the key and the value is
     * the frequency of the part of speech for the word
     */
    public static HashMap<String, HashMap<String, Double>> obsTracker(String wordFilePath, String posFilePath) throws IOException {
        BufferedReader posInput = null;
        BufferedReader wordInput = null;
        HashMap<String, HashMap<String, Double>> obs = new HashMap<>();

        try {

            // open files with buffered readers
            posInput = new BufferedReader(new FileReader(posFilePath));
            wordInput = new BufferedReader(new FileReader(wordFilePath));
            String posLine = posInput.readLine();
            String wordLine = wordInput.readLine();

            // makes the words lowercase
            while (posLine != null && wordLine != null) {
                wordLine = wordLine.toLowerCase();
                // array of strings for all the words
                String[] posList = posLine.split(" ");
                String[] wordList = wordLine.split(" ");

                // looks for # tag to see and if its there in the map then add 1 to freq otherwise add to map
                if (obs.containsKey("#")) {
                    HashMap<String, Double> wordFreqCount;
                    wordFreqCount = obs.get("#");

                    if (wordFreqCount.containsKey(wordList[0])) {
                        Double count = wordFreqCount.get(wordList[0]) + 1;
                        wordFreqCount.put(wordList[0], count);
                        obs.put("#", wordFreqCount);
                    }

                    else {
                        wordFreqCount.put(wordList[0], 1.0);
                        obs.put("#", wordFreqCount);
                    }
                }

                else {
                    HashMap<String, Double> newMap = new HashMap<>();
                    newMap.put(wordList[0], 1.0);
                    obs.put("#", newMap);
                }

                // for every word in word list
                for (int i = 0; i < wordList.length - 1; i++) {
                    HashMap<String, Double> wordCount = new HashMap<>();

                    // if the map contains pos already
                    if (obs.containsKey(posList[i])) {

                        wordCount = obs.get(posList[i]);

                        // if the inner map contains the same word add 1 to freq value
                        if (wordCount.containsKey(wordList[i])) {
                            Double num = wordCount.get(wordList[i]) + 1;
                            wordCount.put(wordList[i], num);
                        }
                        // else add word into map with freq value of 1
                        else {
                            wordCount.put(wordList[i], 1.0);
                        }
                    }
                    // if map is empty add with freq value of 1
                    else {
                        wordCount.put(wordList[i], 1.0);
                    }

                    obs.put(posList[i], wordCount);
                }

                posLine = posInput.readLine();
                wordLine = wordInput.readLine();
            }
        }

        catch (IOException E) {
            System.err.print("IO exception occurred!");
        }

        finally {
            if (wordInput != null && posInput != null) {
                wordInput.close();
                posInput.close();
            }
        }

        return obs;
    }

    /**
     * method that counts frequency of transitions for pos
     *
     * @param filePath    file path of the pos
     * @return a map where the keys are the pos and the values are maps of all the pos and the values are the frequency
     * of transitions between the pos.
     */
    public static HashMap<String, HashMap<String, Double>> transTracker(String filePath) throws IOException {
        BufferedReader input = null;
        HashMap<String, HashMap<String, Double>> trans = new HashMap<>();

        try {

            input = new BufferedReader(new FileReader(filePath));
            String line = input.readLine();

            while (line != null) {
                // add to state list
                String[] stateList = line.split(" ");
                // loops through the state list
                for (int i = 0; i < stateList.length - 1; i++){

                    if (i == 0) {
                        // checks for # tag if it is there then add 1 to freq of inner map otherwise add new state key
                        if (trans.containsKey("#")) {
                            HashMap<String, Double> map = trans.get("#");

                            if (map.containsKey(stateList[0])) {
                                double count = map.get(stateList[0]) + 1;
                                map.put(stateList[0], count);
                                trans.put("#", map);
                            }

                            else {
                                map.put(stateList[0], 1.0);
                                trans.put("#", map);
                            }
                        }

                        else {
                            HashMap<String, Double> newMap = new HashMap<>();
                            newMap.put(stateList[0], 1.0);
                            trans.put("#", newMap);
                        }
                    }

                    String nextState = stateList[i + 1];
                    // if trans map contains the state
                    if (trans.containsKey(stateList[i])) {
                        // set the current map to the inner map with the key of the state
                        HashMap<String, Double> currMap = trans.get(stateList[i]);

                        // if the inner map has pos then add 1 to freq value
                        if (currMap.containsKey(nextState)) {
                            currMap.put(nextState, currMap.get(nextState) + 1);
                            trans.put(stateList[i], currMap);
                        }

                        // else add next state to current map and the map to trans map
                        else {
                            currMap.put(nextState, 1.0);
                            trans.put(stateList[i], currMap);
                        }
                    }

                    // else create new map with key of next state and freq value of 1
                    else{
                        HashMap<String, Double> newMap = new HashMap<>();
                        newMap.put(nextState, 1.0);
                        trans.put(stateList[i], newMap);
                    }
                }
                line = input.readLine();
            }
        }

        catch (IOException E) {
            System.err.print("IO exception occurred!");
        }

        finally {
            if (input != null) {
                input.close();
            }
        }

        return trans;
    }

    /**
     * calculates the log probability from the frequency counts of the map
     * @param map    the map which will have freq values converted into log probabilities
     * @return     the new map with log probabilities instead of freq values
     */
    public static HashMap<String, HashMap<String, Double>> freqToLogProb(HashMap<String, HashMap<String, Double>> map) {

        for (String key : map.keySet()) {

            HashMap<String,Double> innerMap = map.get(key);
            Double prob = 0.0;

            // loop through the inner map to add freq values
            for (String innerKey : innerMap.keySet()) {
                prob += innerMap.get(innerKey);
            }

            // loop through the inner map to calculate the log probability
            for (String innerKey: innerMap.keySet()) {
                innerMap.put(innerKey, Math.log(innerMap.get(innerKey) / prob));
            }
        }

        return map;
    }


}