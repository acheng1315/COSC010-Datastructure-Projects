import java.io.*;
import java.util.*;

/**
 * @author Andrew Cheng and Sangha (Noel) Jang
 * Date: 5/4/2022
 * Purpose: PSET 3
 * HuffmanEncoding Class for compressing and decompressing a text file
 */
public class HuffmanEncoding {
    public HashMap<Character, Integer> freqTable = new HashMap<>();
    public PriorityQueue<BinaryTree<TreeCreationData>> pQ;
    public static BinaryTree<TreeCreationData> huffmanTree;
    public BufferedReader input;
    private BufferedBitWriter output;
    public HashMap<Character, ArrayList<Boolean>> code;
    public String filePath;
    /**
     * creates new BufferedReader and BufferedBitWriter for compression
     * @param filePath
     * @throws FileNotFoundException
     */
    public HuffmanEncoding(String filePath) throws FileNotFoundException {
        input = new BufferedReader(new FileReader(filePath));
        output = new BufferedBitWriter(filePath+"Compressed.txt");
        this.filePath = filePath;
    }
    /**
     * calculates frequency of characters and adds it to freqTable
     * @param filePath
     */
    public void calcFreq(String filePath) {
        try {
            BufferedReader input1 = new BufferedReader(new FileReader(filePath));
            int c1 = input1.read();
            while (c1 != -1) {
                char c = (char) c1;
                if (freqTable.containsKey(c)) {
                    // increment value
                    freqTable.put(c, freqTable.get(c) + 1);
                }
                else {
                    // add new value
                    freqTable.put(c, 1);
                }
                c1 = input1.read();
            }
            input1.close();
        }
        catch (IOException e) {
            System.err.println("error");
        }

    }
    /**
     * inserts trees into a priority queue
     */
    public void insertTrees() {

        pQ = new PriorityQueue<>(freqTable.size(), new TreeComparator());
        for (Character c : freqTable.keySet()) {
            BinaryTree<TreeCreationData> tree = new BinaryTree<>(new TreeCreationData(c, freqTable.get(c)));
            pQ.add(tree);
        }

    }
    /**
     * creates the huffmanTree by reducing from the priority queue
     */
    public void treeCreation() {

        while (pQ.size() > 1) {
            BinaryTree<TreeCreationData> T1 = pQ.remove();
            BinaryTree<TreeCreationData> T2 = pQ.remove();
            BinaryTree<TreeCreationData> T = new BinaryTree<>
                    (new TreeCreationData(T1.getData().getFrequency() + T2.getData().getFrequency()), T1, T2);
            pQ.add(T);
        }
       huffmanTree = pQ.remove();
//        System.out.println(huffmanTree);
        codeRetrieval();
    }
    /**
     * codeRetrieval recurses through codeRetrievalHelper
     */
    public void codeRetrieval() {
        code = codeRetrievalHelper(huffmanTree, new ArrayList<>());
    }
    /**
     * returns a HashMap pathMap
     * @param tree  the huffmanTree
     * @param path  a new boolean ArrayList for the path
     * @return  pathMap, a HashMap with the character and the ArrayList with the Boolean path
     */
    public HashMap<Character, ArrayList<Boolean>> codeRetrievalHelper(BinaryTree<TreeCreationData> tree, ArrayList<Boolean> path){
        HashMap<Character, ArrayList<Boolean>> pathMap = new HashMap<>();
        // if left make it 0
        if(tree.hasLeft()){
            ArrayList<Boolean> leftPath = new ArrayList<>(path);
            leftPath.add(false);
            pathMap.putAll(codeRetrievalHelper(tree.getLeft(),leftPath));
        }
        // if right make it 1
        if(tree.hasRight()) {
            ArrayList<Boolean> rightPath = new ArrayList<>(path);
            rightPath.add(true);
            pathMap.putAll(codeRetrievalHelper(tree.getRight(), rightPath));
        }
        // if leaf finish
        if(tree.isLeaf()){
            pathMap.put(tree.getData().getCharacter(),path);
        }
        return pathMap;
    }
    /**
     * Compresses the file; reads file using BufferedReader and writes compressed file using BufferedBitWriter
     * @throws IOException exception if input file is empty
     */
    public void compress() throws IOException {
        System.out.println("compressing "+filePath+"...");
        try {
            calcFreq(filePath);
            insertTrees();
            treeCreation();
        }
        catch (RuntimeException E){
            System.out.println("blank file");
        }
        if (huffmanTree == null) {
            return;
        }
        if (input != null) {
            BufferedReader copy = new BufferedReader(new FileReader(filePath));
            int c = copy.read();
            while (c != -1) {
                ArrayList<Boolean> cipher = code.get((char) c);
                for (Boolean b : cipher) {
                    output.writeBit(b);
                }
                c = copy.read();
            }
        }
        output.close();
        System.out.println("compression finished");

    }

    /**
     * Decompresses the file. Uses BufferedBitReader to read compressed file and BufferedWriter to write uncompressed file.
     * @param path  the path of the file
     * @throws IOException
     */
    public static void decompress(String path) throws IOException {
        if (huffmanTree == null) {
            return;
        }
        BufferedBitReader read = new BufferedBitReader(path+"Compressed.txt");
        BufferedWriter decompressedOutput = new BufferedWriter(new FileWriter(path+"Uncompressed.txt"));

        BinaryTree<TreeCreationData> decompressTree = huffmanTree;
        try {
            while (read.hasNext()) {
                boolean c = read.readBit();
                if (c)
                    decompressTree = decompressTree.getRight();
                else
                    decompressTree = decompressTree.getLeft();

                if (decompressTree.isLeaf()) {
                    decompressedOutput.write(decompressTree.getData().getCharacter());
                    decompressTree = huffmanTree;
                }
            }

        } finally {
            decompressedOutput.close();
            System.out.println("decompression finished");
        }
    }

    /**
     * driver to test a specific file.
     * @param path  path of the file
     * @throws Exception
     */
    public static void driver(String path) throws Exception {
        HuffmanEncoding compresser = new HuffmanEncoding(path);
        compresser.compress();
        HuffmanEncoding.decompress(path);

    }

    /**
     * main method that runs tests
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        String path1 = "ps3/inputs/blank";
        driver(path1);
        String path2 = "ps3/inputs/USConstitution.txt";
        driver(path2);
        // war and peace file: compressed version size is 1.8 mb, original is 3.2 mb
//        String path3 = "ps3/inputs/WarAndPeace.txt";
//        driver(path3);
        String path4 = "ps3/inputs/test1";
        driver(path4);
        String path5 = "ps3/inputs/test2";
        driver(path5);
        String path6 = "ps3/inputs/testing";
        driver(path6);
        String path7 = "ps3/inputs/testSingleCharacter";
        driver(path7);
        String path8 = "ps3/inputs/testSingleRepeating";
        driver(path8);


    }

}
