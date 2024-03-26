import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Program {
    public static void main(String args[]){
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

        HashMap<Character,String> charToBitSeq = new HashMap<>();
        for(Map.Entry<IHuffmanBaseNode,String> entry : prefixTable.entrySet()){
            HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
            charToBitSeq.put(node.value(),entry.getValue());
        }

        for(Map.Entry<Character,String> entry : charToBitSeq.entrySet()){
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        
        for(String s : args) System.out.println(s);
        String compressedFileName = args[1];
        
        //fileName refers to the file being read and compressed
        //compressedFileName is the name of the compressed file
        // writeToCompressedFile(fileName,compressedFileName,prefixTable);
        Compress.writeHeader(fileName,hm);
        Compress.writeDataToCompressedFile(fileName,compressedFileName,hm,prefixTable);

        String newFile = "uncompressed.txt";
        int byteNum = 3;
        Decompress.decompressFile(compressedFileName,newFile,byteNum);
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
}
