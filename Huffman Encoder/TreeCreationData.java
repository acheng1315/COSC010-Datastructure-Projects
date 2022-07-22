/**
 * @author Andrew Cheng and Noel(Sangha) Jang
 * Tree class data type that stores the characters and their respective frequencies.
 */

public class TreeCreationData {
    private char character;
    private int frequency;

    /**
     * constructor for the data type
     * @param character
     * @param frequency
     */
    TreeCreationData(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    /**
     * sets freq
     * @param frequency
     */
    TreeCreationData(int frequency) {
        this.frequency = frequency;
    }

    /**
     * getter
     * @return character
     */
    public char getCharacter() {
        return character;
    }

    /**
     * getter
     * @return frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * setter
     * @param character
     */
    public void setCharacter(char character) {
        this.character = character;
    }

    /**
     * setter
     * @param frequency
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}


