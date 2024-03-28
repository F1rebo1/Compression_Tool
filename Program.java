import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Program {
    private static final boolean printDebugs = false;
    public static final String ANSI_RESET = "\u001B[0m"; 
    public static final String ANSI_YELLOW = "\u001B[33m"; 
    public static void main(String args[]){
        //compDecompFlag is the flag -c or -d indicating compression or decompression
        //fileName is the file to be compressed or decompressed
        //compDecompFileName is the output file
        String compDecompFlag = args[0], fileName = args[1], compDecompFileName = args[2];

        if(args.length != 3){
            System.err.println(ANSI_YELLOW + "[ERROR] Missing arguments: needed 3, but only found " + args.length + ANSI_RESET);
            return;
        }

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
            if(printDebugs) System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        
        if(compDecompFlag.equals("-c") || compDecompFlag.equals("--compress")){
            try{
                String data = new String(Files.readAllBytes(Paths.get(fileName)));
                Compress.writeDataToCompressedFile(data,compDecompFileName,charToBitSeq);
            }catch(IOException e){
                System.err.println("[ERROR] " + e);
            }
        }else if(compDecompFlag.equals("-d") || compDecompFlag.equals("--decompress")){
            Decompress.decompressFile(fileName,compDecompFileName);
        }else{
            System.err.println(ANSI_YELLOW + "[ERROR] Incorrect flag: please use either -c/--compress, or -d/--decompress" + ANSI_RESET);
            return;
        }
    }

    public static HashMap<Character,Integer> countChars(String fileName){
        HashMap<Character,Integer> hm = new HashMap<>();

        try{
            FileReader fr = new FileReader(fileName);
            try{
                int c;
                while((c = fr.read()) != -1){
                    hm.put((char)c,hm.getOrDefault((char)c,0) + 1);
                }
                fr.close();
            }catch(IOException e){
                System.err.println("[ERROR] " + e);
            }
        }catch(FileNotFoundException e){
            System.err.println("[ERROR] " + e);
        }
        
        // try{
        //     File file = new File(fileName);
        //     Scanner keyboard = new Scanner(file);
        //     while(keyboard.hasNextLine()){
        //         String data = keyboard.nextLine();
        //         data += '\n';
        //         for(char c : data.toCharArray()){
        //             hm.put(c,hm.getOrDefault(c,0) + 1);
        //             // if(c == '\n') System.out.println("[countChars] FOUND A NEW LINE");
        //         }
        //     }
        //     keyboard.close();
        // }catch (FileNotFoundException e){
        //     System.err.println("[ERROR] The file could not be opened.");
        //     e.printStackTrace();
        // }

        return hm;
    }
}
