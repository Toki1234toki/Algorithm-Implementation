import java.io.*;
import java.util.*;


class HuffmanNode {
    char character;
    double frequency;
    HuffmanNode left, right;

    HuffmanNode(char character, double frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = this.right = null;
    }
}


class HuffmanComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode a, HuffmanNode b) {
        if (Double.compare(a.frequency, b.frequency) == 0) {
            return Character.compare(a.character, b.character);
        }
        return Double.compare(a.frequency, b.frequency);
    }
}

public class HuffmanCompression {
    private static final Map<Character, String> huffmanCodes = new HashMap<>();
    private static HuffmanNode root;

    
    private static final Map<Character, Double> letterFrequencies = new HashMap<>();
    static {
        letterFrequencies.put('A', 8.2); letterFrequencies.put('B', 1.5); letterFrequencies.put('C', 2.8);
        letterFrequencies.put('D', 4.3); letterFrequencies.put('E', 12.7); letterFrequencies.put('F', 2.2);
        letterFrequencies.put('G', 2.0); letterFrequencies.put('H', 6.1); letterFrequencies.put('I', 7.0);
        letterFrequencies.put('J', 0.15); letterFrequencies.put('K', 0.77); letterFrequencies.put('L', 4.0);
        letterFrequencies.put('M', 2.4); letterFrequencies.put('N', 6.7); letterFrequencies.put('O', 7.5);
        letterFrequencies.put('P', 1.9); letterFrequencies.put('Q', 0.095); letterFrequencies.put('R', 6.0);
        letterFrequencies.put('S', 6.3); letterFrequencies.put('T', 9.1); letterFrequencies.put('U', 2.8);
        letterFrequencies.put('V', 0.98); letterFrequencies.put('W', 2.4); letterFrequencies.put('X', 0.15);
        letterFrequencies.put('Y', 2.0); letterFrequencies.put('Z', 0.074);
    }

   
    private static void buildHuffmanTree() {
        PriorityQueue<HuffmanNode> pq = new PriorityQueue<>(new HuffmanComparator());

        for (Map.Entry<Character, Double> entry : letterFrequencies.entrySet()) {
            pq.add(new HuffmanNode(entry.getKey(), entry.getValue()));
        }

        while (pq.size() > 1) {
            HuffmanNode left = pq.poll();
            HuffmanNode right = pq.poll();

            HuffmanNode newNode = new HuffmanNode('\0', left.frequency + right.frequency);
            newNode.left = left;
            newNode.right = right;
            pq.add(newNode);
        }

        root = pq.poll();
    }

 
    private static void generateHuffmanCodes(HuffmanNode node, String code) {
        if (node == null) return;
        if (node.character != '\0') {
            huffmanCodes.put(node.character, code);
        }
        generateHuffmanCodes(node.left, code + "0");
        generateHuffmanCodes(node.right, code + "1");
    }

  
    private static void encodeFile(String inputFile, String outputFile) throws IOException {
        buildHuffmanTree();
        generateHuffmanCodes(root, "");

        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        StringBuilder encodedText = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            char character = (char) ch;
            String code = huffmanCodes.get(character);
            if (code != null) {
                encodedText.append(code);
            }
        }
        reader.close();

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(encodedText.toString());
        writer.close();

        System.out.println("Encoding completed. Encoded text saved to " + outputFile);
    }

    
    private static void decodeFile(String encodedFile, String decodedFile) throws IOException {
        if (root == null) {
            buildHuffmanTree();
            generateHuffmanCodes(root, "");

        }

        BufferedReader reader = new BufferedReader(new FileReader(encodedFile));
        StringBuilder encodedText = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            encodedText.append((char) ch);
        }
        reader.close();

        StringBuilder decodedText = new StringBuilder();
        HuffmanNode currentNode = root;

        for (char bit : encodedText.toString().toCharArray()) {
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;
            if (currentNode.left == null && currentNode.right == null) {
                decodedText.append(currentNode.character);
                currentNode = root;
            }
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(decodedFile));
        writer.write(decodedText.toString());
        writer.close();

        System.out.println("Decoding completed. Decoded text saved to " + decodedFile);
    }

   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("1. Encode input.txt to output.txt");
        System.out.println("2. Decode output.txt to decoded.txt");
        int choice = scanner.nextInt();
        scanner.close();

        try {
            if (choice == 1) {
                encodeFile("input.txt", "output.txt");
            } else if (choice == 2) {
                decodeFile("output.txt", "decoded.txt");
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
