import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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

        // for(Map.Entry<IHuffmanBaseNode,String> entry : prefixTable.entrySet()){
        //     HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
        //     System.out.println("Key: " + node.value() + ", Prefix Code = " + entry.getValue() + ", " + Integer.parseInt(entry.getValue(), 2));
        // }
        
        // System.out.println("Your arguments are: ");
        for(String s : args) System.out.println(s);
        String compressedFileName = args[1];
        // System.out.println("The output (compressed) file will be called: " + compressedFileName + ".txt");
        
        //fileName refers to the file being read and compressed
        //compressedFileName is the name of the compressed file
        writeToCompressedFile(fileName,compressedFileName,prefixTable);
        // writeToCompressedFile(fileName,compressedFileName,root);

        String newFile = "uncompressed.txt";
        decompressFile(compressedFileName,newFile);
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
        HashMap<Character,String> convertedMap = new HashMap<>();
        for(Map.Entry<IHuffmanBaseNode,String> entry : header.entrySet()){
            HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
            convertedMap.put(node.value(),entry.getValue());
        }

        try{
            File file = new File(compressedFileName + ".txt");
            FileWriter fw = new FileWriter(file);
            // FileOutputStream fos = new FileOutputStream(compressedFileName + ".txt");
            // FileWriter fw = new FileWriter(file + ".bin");
            BufferedWriter output = new BufferedWriter(fw);
            // out.write("Writing to Compressed File");

            //Writing the header (prefixTable) so that we can decode the compressed file
            System.out.println("Writing header");
            String head = "";
            for(Map.Entry<IHuffmanBaseNode,String> entry : header.entrySet()){
                HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
                // head += node.value() + "," + entry.getValue() + "`";
                // output.write(node.value() + "," + entry.getValue() + "`");
                head += node.value() + "`" + entry.getValue() + "<>";
                output.write(node.value() + "`" + entry.getValue() + "<>");
            }
            output.write("\n");
            System.out.println("Header created successfully:");
            System.out.println(head);

            //Encoding and writing the encoded contents of the file to be compressed
            try{
                File f = new File(fileName);
                Scanner keyboard = new Scanner(f);

                StringBuilder encodedData = new StringBuilder();
                StringBuilder binaryStr = new StringBuilder();
                // String binaryStr = "";

                System.out.println("Encoding data");
                while(keyboard.hasNextLine()){
                    String data = keyboard.nextLine();
                    encodedData.append(data);
                    //Need to match the get to the existing IHuffmanBaseNode characters
                    for(char c : data.toCharArray()){
                        binaryStr.append(convertedMap.getOrDefault(c,""));
                        // binaryStr += convertedMap.getOrDefault(c,"");
                        output.write(convertedMap.getOrDefault(c,""));
                    }
                }
                System.out.println("Data encoded successfully:");
                System.out.println(encodedData);
                System.out.println("Binary Values of the encodedData:");
                System.out.println(binaryStr);

                // Pack bit strings into bytes
                // StringBuilder buffer = new StringBuilder();
                // List<Byte> packedBytes = new ArrayList<>();
                
                StringBuilder paddedBitString = binaryStr;
                while(paddedBitString.length() % 8 != 0){
                    paddedBitString.append("0");
                }

                int padding = paddedBitString.length() - binaryStr.length();
                int byteCount = paddedBitString.length() / 8;
                int[] bytes = new int[byteCount];

                for(int i = 0; i < byteCount; i++) {
                    int start = i * 8;
                    int end = start + 8;
                    String byteStr = paddedBitString.toString().substring(start, end);
                    bytes[i] = Integer.parseInt(byteStr, 2);
                }
                
                String binaryString = "";

                for(int i = 0; i < bytes.length; i++) {
                    int byteNum = bytes[i];
                    System.out.println("byteNum: " + byteNum);
                    binaryString += String.valueOf(byteNum);
                }
                System.out.println("[Padded] binaryString = " + binaryString);
                // Remove padding
                binaryString.substring(0, binaryString.length() - padding);
                System.out.println("[Unpadded] binaryString = " + binaryString);

                // output.write(binaryString);

                // for(char bit : binaryStr.toString().toCharArray()){
                //     buffer.append(bit);

                //     if(buffer.length() == 8){
                //         System.out.println("cur byte: " + (byte) Integer.parseInt(buffer.toString(), 2));
                //         packedBytes.add((byte) Integer.parseInt(buffer.toString(), 2));
                //         buffer.setLength(0);  // Clear buffer
                //     }
                // }

                // if(buffer.length() > 0){
                //     while(buffer.length() < 8){
                //         buffer.append('0');
                //     }
                //     System.out.println("cur byte: " + (byte) Integer.parseInt(buffer.toString(), 2));
                //     packedBytes.add((byte) Integer.parseInt(buffer.toString(), 2));
                // }
                
                keyboard.close();
            }catch (FileNotFoundException e){
                System.out.println("[ERROR] The file could not be opened.");
                e.printStackTrace();
            }

            output.close();
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    // public static void writeToCompressedFile(String fileName, String compressedFileName, HuffmanTree root){
    //     try{
    //         File file = new File(compressedFileName + ".txt");
    //         FileWriter fw = new FileWriter(file);
    //         // FileOutputStream fos = new FileOutputStream(compressedFileName + ".txt");
    //         // FileWriter fw = new FileWriter(file + ".bin");
    //         BufferedWriter output = new BufferedWriter(fw);
    //         // output.write("Writing to Compressed File");

    //         //Writing the header (prefixTable) so that we can decode the compressed file
    //         output.write(root);
    //         System.out.println("Writing header");
    //         String head = "";
    //         for(Map.Entry<IHuffmanBaseNode,String> entry : header.entrySet()){
    //             HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
    //             // head += node.value() + "," + entry.getValue() + "`";
    //             // output.write(node.value() + "," + entry.getValue() + "`");
    //             head += node.value() + "`" + entry.getValue() + "<>";
    //             output.write(node.value() + "`" + entry.getValue() + "<>");
    //         }
    //         output.write("\n");
    //         System.out.println("Header created successfully:");
    //         System.out.println(head);

    //         }catch (FileNotFoundException e){
    //             System.out.println("[ERROR] The file could not be opened.");
    //             e.printStackTrace();
    //         }
    // }

    public static void decompressFile(String compressedFileName, String newFile){

        HashMap<String,Character> headerPrefixTable = new HashMap<>();
        //First we want to read in the header and form the prefix table
        try{
            File file = new File(compressedFileName + ".txt");
            Scanner keyboard = new Scanner(file);
            while(keyboard.hasNextLine()){
                String data = keyboard.nextLine();
                // String[] codes = data.split("`");
                String[] codes = data.split("<>");
                for(String code : codes){
                    System.out.println(code);
                    // String[] keyValPair = code.split(",");
                    String[] keyValPair = code.split("`");
                    // if(keyValPair[0] == "") keyValPair[0] = ",";
                    headerPrefixTable.put(keyValPair[1],keyValPair[0].charAt(0));
                }
                break;
            }

            //AABBBBCCCCDDDDDEEEEEEFFFFFFFGGGGGGGGHHHHHHHHHIIIIIIIIII

            //[DEBUG] Printing out contents of headerPrefixTable
            System.out.println("[decompressFile] Printing headerPrefixTable");
            for(Map.Entry<String,Character> entry : headerPrefixTable.entrySet()){
                System.out.println(entry.getValue() + "," + entry.getKey());
            }
            System.out.println("===========================================================");

            try{
                File f = new File(newFile);
                FileWriter fw = new FileWriter(f);
                BufferedWriter output = new BufferedWriter(fw);
                
                while(keyboard.hasNextLine()){
                    String data = keyboard.nextLine();
                    int start = 0, end = 1;
                    System.out.println("data.length(): " + data.length());
                    while(end < data.length() + 1){
                        if(headerPrefixTable.containsKey(data.substring(start,end))){
                            System.out.println("Found key: " + data.substring(start,end) + ", value: " + headerPrefixTable.get(data.substring(start,end)));
                            output.write(headerPrefixTable.get(data.substring(start,end)));
                            start = end;
                        }
                        end += 1;
                    }
                    break;
                }

                output.close();
            }catch(IOException e){
                System.err.println("Error: " + e.getMessage());
            }

            keyboard.close();
        }catch (FileNotFoundException e){
            System.out.println("[ERROR] The file could not be opened.");
            e.printStackTrace();
        }
    }
}