import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
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
        // writeToCompressedFile(fileName,compressedFileName,prefixTable);
        writeToCompressedFile(fileName,compressedFileName,hm,prefixTable);

        // String newFile = "uncompressed.txt";
        // decompressFile(compressedFileName,newFile);
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

    // public static void writeToCompressedFile(String fileName, String compressedFileName, HashMap<IHuffmanBaseNode,String> header){
    //     HashMap<Character,String> convertedMap = new HashMap<>();
    //     for(Map.Entry<IHuffmanBaseNode,String> entry : header.entrySet()){
    //         HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
    //         convertedMap.put(node.value(),entry.getValue());
    //     }

    //     try{
    //         File file = new File(compressedFileName + ".txt");
    //         FileWriter fw = new FileWriter(file);
    //         // FileOutputStream fos = new FileOutputStream(compressedFileName + ".txt");
    //         // FileWriter fw = new FileWriter(file + ".bin");
    //         BufferedWriter output = new BufferedWriter(fw);
    //         output.write("Writing to Compressed File");

    //         //Writing the header (prefixTable) so that we can decode the compressed file
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

    //         //Encoding and writing the encoded contents of the file to be compressed
    //         try{
    //             File f = new File(fileName);
    //             Scanner keyboard = new Scanner(f);

    //             StringBuilder encodedData = new StringBuilder();
    //             StringBuilder binaryStr = new StringBuilder();
    //             // String binaryStr = "";

    //             System.out.println("Encoding data");
    //             while(keyboard.hasNextLine()){
    //                 String data = keyboard.nextLine();
    //                 encodedData.append(data);
    //                 //Need to match the get to the existing IHuffmanBaseNode characters
    //                 for(char c : data.toCharArray()){
    //                     binaryStr.append(convertedMap.getOrDefault(c,""));
    //                     // binaryStr += convertedMap.getOrDefault(c,"");
    //                     output.write(convertedMap.getOrDefault(c,""));
    //                 }
    //             }
    //             System.out.println("Data encoded successfully:");
    //             System.out.println(encodedData);
    //             System.out.println("Binary Values of the encodedData:");
    //             System.out.println(binaryStr);

    //             // Pack bit strings into bytes
    //             // StringBuilder buffer = new StringBuilder();
    //             // List<Byte> packedBytes = new ArrayList<>();
                
    //             StringBuilder paddedBitString = binaryStr;
    //             while(paddedBitString.length() % 8 != 0){
    //                 paddedBitString.append("0");
    //             }

    //             int padding = paddedBitString.length() - binaryStr.length();
    //             int byteCount = paddedBitString.length() / 8;
    //             int[] bytes = new int[byteCount];

    //             for(int i = 0; i < byteCount; i++) {
    //                 int start = i * 8;
    //                 int end = start + 8;
    //                 String byteStr = paddedBitString.toString().substring(start, end);
    //                 bytes[i] = Integer.parseInt(byteStr, 2);
    //             }
                
    //             String binaryString = "";

    //             for(int i = 0; i < bytes.length; i++) {
    //                 int byteNum = bytes[i];
    //                 System.out.println("byteNum: " + byteNum);
    //                 binaryString += String.valueOf(byteNum);
    //             }
    //             System.out.println("[Padded] binaryString = " + binaryString);
    //             // Remove padding
    //             binaryString = binaryString.substring(0, binaryString.length() - padding);
    //             System.out.println("[Unpadded] binaryString = " + binaryString);

    //             // output.write(binaryString);

    //             // for(char bit : binaryStr.toString().toCharArray()){
    //             //     buffer.append(bit);

    //             //     if(buffer.length() == 8){
    //             //         System.out.println("cur byte: " + (byte) Integer.parseInt(buffer.toString(), 2));
    //             //         packedBytes.add((byte) Integer.parseInt(buffer.toString(), 2));
    //             //         buffer.setLength(0);  // Clear buffer
    //             //     }
    //             // }

    //             // if(buffer.length() > 0){
    //             //     while(buffer.length() < 8){
    //             //         buffer.append('0');
    //             //     }
    //             //     System.out.println("cur byte: " + (byte) Integer.parseInt(buffer.toString(), 2));
    //             //     packedBytes.add((byte) Integer.parseInt(buffer.toString(), 2));
    //             // }
                
    //             keyboard.close();
    //         }catch (FileNotFoundException e){
    //             System.out.println("[ERROR] The file could not be opened.");
    //             e.printStackTrace();
    //         }

    //         output.close();
    //     }catch(IOException e){
    //         System.err.println("Error: " + e.getMessage());
    //     }
    // }

    public static void writeToCompressedFile(String fileName, String compressedFileName, HashMap<Character,Integer> charFreqs, HashMap<IHuffmanBaseNode,String> prefixTable){
        HashMap<Character,String> convertedMap = new HashMap<>();
        for(Map.Entry<IHuffmanBaseNode,String> entry : prefixTable.entrySet()){
            HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
            convertedMap.put(node.value(),entry.getValue());
        }        
        try{
            // File f = new File(fileName);
            // Scanner keyboard = new Scanner(f);

            // StringBuilder encodedData = new StringBuilder();
            // StringBuilder binaryStr = new StringBuilder();

            // System.out.println("Encoding data");
            // while(keyboard.hasNextLine()){
            //     String data = keyboard.nextLine();
            //     encodedData.append(data);
            //     //Need to match the get to the existing IHuffmanBaseNode characters
            //     for(char c : data.toCharArray()){
            //         binaryStr.append(convertedMap.getOrDefault(c,""));
            //     }
            // }
            // keyboard.close();

            File file = new File(compressedFileName + ".txt");
            FileWriter fw = new FileWriter(file);
            // FileOutputStream fos = new FileOutputStream(compressedFileName + ".txt");
            BufferedWriter output = new BufferedWriter(fw);
            // output.write("Writing to Compressed File");

            //Writing the header (prefixTable) so that we can decode the compressed file
            System.out.println("Writing header");
            String head = "";
            for(Map.Entry<Character,Integer> entry : charFreqs.entrySet()){
                head += entry.getKey() + "`" + entry.getValue() + "`";
                // output.write(entry.getKey() + "`" + entry.getValue() + "`");
                // head += node.value() + "," + entry.getValue() + "`";
                // output.write(node.value() + "," + entry.getValue() + "`");
                // head += node.value() + "`" + entry.getValue() + "<>";
                // output.write(node.value() + "`" + entry.getValue() + "<>");
            }
            // output.write("\n");
            System.out.println("Header created successfully:");
            System.out.println(head);
            output.write(head + ">");

            packBits(output,fileName,prefixTable);

            // output.close();
        }catch(IOException e){
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void packBits(BufferedWriter output, String fileName, HashMap<IHuffmanBaseNode,String> prefixTable){
        HashMap<Character,String> convertedMap = new HashMap<>();
        for(Map.Entry<IHuffmanBaseNode,String> entry : prefixTable.entrySet()){
            HuffmanLeafNode node = (HuffmanLeafNode) entry.getKey();
            convertedMap.put(node.value(),entry.getValue());
        }
        // Encoding and writing the encoded contents of the file to be compressed
        try{
            File f = new File(fileName);
            Scanner keyboard = new Scanner(f);

            StringBuilder encodedData = new StringBuilder();
            StringBuilder binaryStr = new StringBuilder();

            System.out.println("Encoding data");
            while(keyboard.hasNextLine()){
                String data = keyboard.nextLine();
                encodedData.append(data);
                //Need to match the get to the existing IHuffmanBaseNode characters
                for(char c : data.toCharArray()){
                    binaryStr.append(convertedMap.getOrDefault(c,""));
                }
            }

            // try{
            //     System.out.println("Encoding data");
            //     while(keyboard.hasNextLine()){
            //         String data = keyboard.nextLine();
            //         encodedData.append(data);
            //         //Need to match the get to the existing IHuffmanBaseNode characters
            //         for(char c : data.toCharArray()){
            //             binaryStr.append(convertedMap.getOrDefault(c,""));
            //             // binaryStr += convertedMap.getOrDefault(c,"");
            //             output.write(convertedMap.getOrDefault(c,""));
            //         }
            //     }
            // }catch(IOException e){
            //     System.err.println("Error: " + e.getMessage());
            // }
            System.out.println("Data encoded successfully:");
            // System.out.println(encodedData);
            System.out.println("Binary Values of the encodedData:");
            // System.out.println(binaryStr);
            System.out.println("[1] HENLO SIR");

            // try{
            //     int bitsToWrite = binaryStr.toString().length();
            //     int bitPosition = 0;
            //     DataOutputStream dos = new DataOutputStream(new FileOutputStream("test2_output_boi.txt"));
            //     while (bitsToWrite > 0) {
            //         byte toWrite = 0;
            //         for (int i = 0; i < 8 && bitPosition < binaryStr.toString().length(); i++) {
            //         toWrite |= (binaryStr.toString().charAt(bitPosition) == '1' ? 1 : 0) << (7 - i);
            //         bitPosition++;
            //         bitsToWrite--;
            //         }
            //         System.out.println("THIS IS A BYTE: " + toWrite);
            //         dos.writeByte(toWrite);
            //     }
            // }catch(IOException e){
            //     System.out.println(e);
            // }

            // Pack bit strings into bytes
            // StringBuilder buffer = new StringBuilder();
            // List<Byte> packedBytes = new ArrayList<>();
            String oldBinaryStr = binaryStr.toString();
            StringBuilder paddedBitString = binaryStr;
            while(paddedBitString.toString().length() % 8 != 0){
                paddedBitString.append("0");
            }
            System.out.println("[2] HENLO SIR");

            int padding = paddedBitString.toString().length() - oldBinaryStr.length();    //By subtracting the binary value length from the padded binary value length,
            int byteCount = paddedBitString.length() / 8;                   //we find the number of padded bits
            // int[] bytes = new int[byteCount];
            byte[] bytes = new byte[byteCount];

            // System.out.println("padding = " + padding);
            // System.out.println("paddedBitString = " + paddedBitString.toString());

            // System.out.println("Padded binary Values of the encodedData (paddedBitString):");
            // System.out.println(paddedBitString);
            // System.out.println("[DECIMAL] Padded binary Values of the encodedData (paddedBitString):");
            // int decimalVal = Integer.parseInt(paddedBitString.toString(),2);
            // System.out.println(decimalVal);
            // System.out.println("[BINARY] Converted DECIMAL above to Binary (paddedBitString):");
            // int dec = Integer.parseInt(paddedBitString.toString(),2);
            // String decStr = Integer.toBinaryString(dec);
            // System.out.println(decStr);
            // System.out.println("[BINARY] Added missing padding (0's) to start of converted Binary string (paddedBitString):");
            // int padding = paddedBitString.toString().length() - decStr.length();
            // String res = padStart(decStr,padding);
            // System.out.println("[PADDED] res = " + res);
            // System.out.println(Integer.toBinaryString(dec));
            System.out.println("[3] HENLO SIR");
            System.out.println("byteCount = " + byteCount);
            for(int i = 0; i < byteCount; i++) {
                // System.out.println("Processing loop: " + i);
                int start = i * 8;
                int end = start + 8;
                String byteStr = paddedBitString.toString().substring(start, end);
                int byteInt = Integer.parseInt(byteStr, 2);
                byte byteVal = (byte) byteInt;
                bytes[i] = byteVal;
            }
            System.out.println("[4] HENLO SIR");
            String binaryString = "";
            String resultString = "";
            
            try{
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("test3_output_boi.txt"));
                for(int i = 0; i < bytes.length; i++) {
                    byte byteNum = bytes[i];
                    dos.writeByte(byteNum);
                    // System.out.println("byteNum: " + byteNum);
                    resultString += byteNum + " ";
                    // binaryString += padStart(String.valueOf(Integer.toBinaryString(byteNum)));
                }
            }catch(IOException e){
                System.out.println(e);
            }

            // System.out.println("[Padded] binaryString = " + binaryString);
            // Remove padding
            // binaryString = binaryString.substring(0, binaryString.length() - padding);
            // System.out.println("[Unpadded] binaryString = " + binaryString);
            System.out.println("[5] HENLO SIR");
            try{
                output.write(String.valueOf(resultString));
                output.close();
            }catch(IOException e){
                System.out.println("IOException e");
            }
            
            keyboard.close();
        }catch (FileNotFoundException e){
            System.out.println("[ERROR] The file could not be opened.");
            e.printStackTrace();
        }
    }

    // public static String padStart(String dec, int paddingLen){
    //     String res = dec;
    //     System.out.println("res = " + res);
    //     while(paddingLen > 0){
    //         res = "0" + res;
    //         paddingLen--;
    //     }
    //     return res;
    // }

    public static String padStart(String dec){
        String res = dec;
        // System.out.println("res = " + res);
        while(res.length() < 8){
            res = "0" + res;
        }
        return res;
    }

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