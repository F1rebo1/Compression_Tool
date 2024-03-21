import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Compress{
    public static void main(String args[]){
        // String fileName = "./test.txt";
        // String fileName = "./test2.txt";
        String fileName = args[0];

        HashMap<Character,Integer> hm = countChars(fileName);
        PriorityQueue<HuffmanTree> pq = new PriorityQueue<>((a,b) -> a.weight() - b.weight());

        for(Map.Entry<Character,Integer> entry : hm.entrySet()){
            pq.add(new HuffmanTree(entry.getKey(),entry.getValue()));
        }

        HuffmanTree root = HuffmanTree.buildTree(pq);   //root now contains the head (node) of the HuffmanTree
        
        // System.out.println("Performing Preorder Traversal:");    //This is just for debugging purposes
        // root.preorderTraversal();

        HashMap<IHuffmanBaseNode,String> prefixTable = new HashMap<>(); //prefixTable now stores the encoding data mapping each character to its corresponding bit string
        root.createPrefixTable(root,prefixTable);

        for(Map.Entry<IHuffmanBaseNode,String> entry : prefixTable.entrySet()){
            HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
            System.out.println("Key: " + node.value() + ", Prefix Code = " + entry.getValue() + ", " + Integer.parseInt(entry.getValue(), 2));
        }
        
        System.out.println("Your arguments are: ");
        for(String s : args) System.out.println(s);
        String compressedFileName = args[1];
        System.out.println("The output (compressed) file will be called: " + compressedFileName + ".txt");

        writeToCompressedFile(fileName,compressedFileName,prefixTable);
    }

    public static HashMap<Character,Integer> countChars(String fileName){
        HashMap<Character,Integer> hm = new HashMap<>();
        
        try{
            File file = new File(fileName);
            Scanner keyboard = new Scanner(file);
            while(keyboard.hasNextLine()){
                String data = keyboard.nextLine();
                for(char c : data.toCharArray()) hm.put(c,hm.getOrDefault(c,0) + 1);
            }
            keyboard.close();
        }catch (FileNotFoundException e){
            System.out.println("[ERROR] The file could not be opened.");
            e.printStackTrace();
        }

        return hm;
    }

    public static void writeToCompressedFile(String fileName, String compressedFileName, HashMap<IHuffmanBaseNode,String> header){
        try{
            File file = new File(compressedFileName);
            FileWriter fw = new FileWriter(file + ".txt");
            BufferedWriter out = new BufferedWriter(fw);
            // out.write("Writing to Compressed File");

            //Writing the header (prefixTable) so that we can decode the compressed file
            for(Map.Entry<IHuffmanBaseNode,String> entry : header.entrySet()){
                HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
                out.write(node.value() + "," + entry.getValue() + " ");
            }
            out.write("\n");

            //Encoding and writing the encoded contents of the file to be compressed
            // encodeData(fileName,header);
            

            out.close();
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    // public static void encodeData(String fileName, HashMap<IHuffmanBaseNode,String> header){
        
    // }
}