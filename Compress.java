import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class Compress{
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
        
        for(String s : args) System.out.println(s);
        String compressedFileName = args[1];
        
        //fileName refers to the file being read and compressed
        //compressedFileName is the name of the compressed file
        // writeToCompressedFile(fileName,compressedFileName,prefixTable);
        writeToCompressedFile(fileName,compressedFileName,hm,prefixTable);

        String newFile = "uncompressed.txt";
        int byteNum = 3;
        decompressFile(compressedFileName,newFile,byteNum);
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
            // for(Map.Entry<Character,Integer> entry : charFreqs.entrySet()){
            //     head += entry.getKey() + "`" + entry.getValue() + "`";
            //     // output.write(entry.getKey() + "`" + entry.getValue() + "`");
            // }
            try{
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(compressedFileName + ".txt"));
                for(Map.Entry<Character,Integer> entry : charFreqs.entrySet()){
                    head += entry.getKey() + "`" + entry.getValue() + "`";
                    dos.writeBytes(entry.getKey() + "`" + entry.getValue() + "`");
                }

                dos.writeBytes(">");
                dos.writeBytes("\n");
                dos.close();
            }catch(IOException e){
                System.out.println(e);
            }
            int bitsToWrite = head.length();
            System.out.println("bitsToWrite = " + bitsToWrite);
            // Testing/Debugging reading from a FileInputStream to read encoded data
            try{
                BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(compressedFileName + ".txt")));

                System.out.println("DATA: ");
                int i = 0;
                while(i < 3){
                    String str = d.readLine();
                    System.out.println(str);
                    i++;
                }
            }catch(IOException e){
                System.out.println(e);
            }

            System.out.println("Header created successfully:");
            System.out.println(head);

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

            // Pack bit strings into bytes
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
                System.out.println("Processing loop: " + i);
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
                DataOutputStream dos = new DataOutputStream(new FileOutputStream("huffyboi.txt",true));
                for(int i = 0; i < bytes.length; i++) {
                    byte byteNum = bytes[i];
                    dos.writeByte(byteNum);
                    // System.out.println("byteNum: " + byteNum);
                    resultString += byteNum + " ";
                    // binaryString += padStart(String.valueOf(Integer.toBinaryString(byteNum)));
                }
            }catch(IOException e){
                System.out.println("[packBits] " + e);
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
                System.out.println("[packBits -- write]" + e);
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

    public static void decompressFile(String compressedFileName, String newFile, int byteNum){
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(compressedFileName + ".txt"));
            BufferedReader d = new BufferedReader(new InputStreamReader(dis,"UTF-8"));
            StringBuilder decodedData = new StringBuilder();

            System.out.println("[6] HENLO SIR");
            byte[] b = new byte[byteNum];
            int i = 0, bitsToRead = 20;
            while(bitsToRead > 0) {
                byte curByte = dis.readByte();
                // b[i] = dis.readByte();
                for(int n = 7; n >= 0 && bitsToRead > 0; n--){
                    boolean bit = ((curByte >> i) & 1) == 1;
                    decodedData.append(bit ? '1' : '0');
                    bitsToRead--;
                }
                // String str = String.valueOf(b[i]);
                System.out.println(decodedData.toString());
                // String str = dis.readUTF();
                // decodedData.append(str);
                i++;
            }

            dis.close();
            // System.out.println("Decoded Data: " + decodedData.toString());
        } catch (EOFException e) {
            System.out.println("[decompressFile] EOF " + e);
        } catch (IOException e) {
            System.out.println(e);
        }

        // try{
        //     DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile));
        //     DataInputStream dis = new DataInputStream(new FileInputStream(compressedFileName + ".txt"));
        //     BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(compressedFileName + ".txt")));
        //     StringBuffer inputLine = new StringBuffer();
        //     // d.readLine();

        //     byte[] bytes = new byte[byteNum];

        //     System.out.println("[6] HENLO SIR");

        //     System.out.println("DATA: ");
        //     System.out.println(dis.readUTF());
        //     // System.out.println(dis.readUTF());
        //     // System.out.println(dis.readUTF());
        //     // while(byteNum > 0){
        //     //     System.out.println(dis.readUTF());
        //     //     byte readByte = dis.readByte();
        //     //     for (int i = 7; i >= 0 && byteNum > 0; i--) {
        //     //         boolean bit = ((readByte >> i) & 1) == 1;
        //     //         inputLine.append(bit ? '1' : '0');
        //     //         byteNum--;
        //     //     }
        //     // }

        //     // System.out.println("DATA: ");
        //     // System.out.println(d.readLine());
        //     // inputLine.append(d.readLine());
        //     // String data = inputLine.toString();
        //     // System.out.println(data);
        //     // dos.writeChars(data);
        // }catch(IOException e){
        //     System.out.println("[ERROR: decompressFile -- reading] " + e);
        // }
        // try{
        //     DataInputStream dis = new DataInputStream(new FileInputStream(compressedFileName + ".txt"));
        //     StringBuilder outputString = new StringBuilder();
        //     byte[] bytes = new byte[byteCount];
        //     int byteNum = 0;

        //     while(byteNum < byteCount){
        //         byte b = dis.readByte();
        //         bytes[byteNum] = b;
        //         byteNum++;
        //     }
        // }catch(IOException e){
        //     System.out.println(e);
        // }
    }

    // public static void decompressFile(String compressedFileName, String newFile){

    //     HashMap<String,Character> headerPrefixTable = new HashMap<>();
    //     //First we want to read in the header and form the prefix table
    //     try{
    //         File file = new File(compressedFileName + ".txt");
    //         Scanner keyboard = new Scanner(file);
    //         while(keyboard.hasNextLine()){
    //             String data = keyboard.nextLine();
    //             // String[] codes = data.split("`");
    //             String[] codes = data.split("<>");
    //             for(String code : codes){
    //                 System.out.println(code);
    //                 // String[] keyValPair = code.split(",");
    //                 String[] keyValPair = code.split("`");
    //                 // if(keyValPair[0] == "") keyValPair[0] = ",";
    //                 headerPrefixTable.put(keyValPair[1],keyValPair[0].charAt(0));
    //             }
    //             break;
    //         }

    //         //AABBBBCCCCDDDDDEEEEEEFFFFFFFGGGGGGGGHHHHHHHHHIIIIIIIIII

    //         //[DEBUG] Printing out contents of headerPrefixTable
    //         System.out.println("[decompressFile] Printing headerPrefixTable");
    //         for(Map.Entry<String,Character> entry : headerPrefixTable.entrySet()){
    //             System.out.println(entry.getValue() + "," + entry.getKey());
    //         }
    //         System.out.println("===========================================================");

    //         try{
    //             File f = new File(newFile);
    //             FileWriter fw = new FileWriter(f);
    //             BufferedWriter output = new BufferedWriter(fw);
                
    //             while(keyboard.hasNextLine()){
    //                 String data = keyboard.nextLine();
    //                 int start = 0, end = 1;
    //                 System.out.println("data.length(): " + data.length());
    //                 while(end < data.length() + 1){
    //                     if(headerPrefixTable.containsKey(data.substring(start,end))){
    //                         System.out.println("Found key: " + data.substring(start,end) + ", value: " + headerPrefixTable.get(data.substring(start,end)));
    //                         output.write(headerPrefixTable.get(data.substring(start,end)));
    //                         start = end;
    //                     }
    //                     end += 1;
    //                 }
    //                 break;
    //             }

    //             output.close();
    //         }catch(IOException e){
    //             System.err.println("Error: " + e.getMessage());
    //         }

    //         keyboard.close();
    //     }catch (FileNotFoundException e){
    //         System.out.println("[ERROR] The file could not be opened.");
    //         e.printStackTrace();
    //     }
    // }
}