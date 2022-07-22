import java.util.*;

/**
 * @author Andrew Cheng
 * class that completes the viterbi tagging process
 */
public class ViterbiDecoding {

    /**
     * completes viterbi tagging process
     * @param lineInput string of line input
     * @param trans hashmap of transitions
     * @param obs hashmap of observations
     * @return a string of viterbi tags
     */
    public static String viterbiTagging(String lineInput, HashMap<String, HashMap<String, Double>> trans, HashMap<String, HashMap<String, Double>> obs) {

        Stack<String> printTag = new Stack<>();
        Set<String> prevState = new HashSet<>();
        Map<String, Double> prevScore = new HashMap<>();
        List<Map<String,String>> backTrack = new ArrayList<>();

        double unseenPenalty = -100.0;
        double maxScore = Double.NEGATIVE_INFINITY;
        String start = "#";
        String lastTag = "";
        String tagString = "";
        String[] wordList = lineInput.split(" ");

        // start
        prevState.add(start);
        prevScore.put(start, 0.0);

        // loop through all the words in the word list
        for (int i = 0; i < wordList.length; i++) {
            double score;
            Map<String,Double> nextScore = new HashMap<>();
            Map<String,String> backPoint = new HashMap<>();
            Set<String> nextState = new HashSet<>();

            // loop through all the states
            for (String state : prevState) {

                if (trans.containsKey(state) && !trans.get(state).isEmpty()) {
                    for (String transition : trans.get(state).keySet()) {
                        nextState.add(transition);
                        // calculates the score based on transition and prevscore
                        if (obs.containsKey(transition) && obs.get(transition).containsKey(wordList[i])) {
                            score = prevScore.get(state) + trans.get(state).get(transition) + obs.get(transition).get(wordList[i]);
                        }
                        else {
                            score = prevScore.get(state) + trans.get(state).get(transition) + unseenPenalty;
                        }
                        // backtracker
                        if (!nextScore.containsKey(transition) || score > nextScore.get(transition)) {
                            nextScore.put(transition, score);
                            backPoint.put(transition, state);
                            if(backTrack.size() > i) {
                                backTrack.remove(i);
                            }
                            backTrack.add(backPoint);
                        }
                    }
                }
            }
            // goes next score and state
            prevScore = nextScore;
            prevState = nextState;
        }
        // finds max score
        for (String scores : prevScore.keySet()) {
            if (prevScore.get(scores) > maxScore) {
                maxScore = prevScore.get(scores);
                lastTag = scores;
            }
        }

        printTag.push(lastTag);
        // go backwards through the wordList
        for (int i = wordList.length - 1; i > 0; i--) {
            printTag.push(backTrack.get(i).get(printTag.peek()));
        }
        // pops all the tags in the string and concatenates to tag string
        while (!printTag.isEmpty()) {
            tagString += printTag.pop() + " ";
        }

        // return the viterbi tag string
        return tagString;
    }


}