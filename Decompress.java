import java.util.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.crypto.Data;

public class Decompress {
    private static final boolean printDebugs = true;

    public static void decompressFile(String compressedFileName, String newFile){
        if(printDebugs) System.out.println("[decompressFile]");
        decodeFile(compressedFileName,newFile);
    }

    public static void decodeFile(String compressedFileName, String newFile){
        if(printDebugs) System.out.println("[decodeFile]");
        try{
            // BufferedReader b = new BufferedReader(new InputStreamReader(new FileInputStream(compressedFileName)));
            DataInputStream dis = new DataInputStream(new FileInputStream(compressedFileName));

            StringBuilder encodedText = new StringBuilder();

            int padding = dis.readInt();
            HashMap<String,Character> bitSeqToChar = fillMap(dis);

            System.out.println("[decodeFile] Number of bytes available: " + dis.available());
            byte[] byteInput = new byte[dis.available()];
            System.out.println("[decodeFile] Number of bytes available: " + dis.available());
            System.out.println("byteInput.length = " + byteInput.length);
            dis.read(byteInput);
            // for(byte binp : byteInput) System.out.println("b = " + binp);
            System.out.println("[decodeFile] Number of bytes available: " + dis.available());
            getStringsFromBytes(encodedText,byteInput);

            // if(padding > 0) encodedText.setLength(encodedText.length() - padding);

            decompress(encodedText,bitSeqToChar,newFile);

        }catch(IOException e){
            System.err.println("[ERROR] " + e);
        }
    }

    public static void getStringsFromBytes(StringBuilder encodedText, byte[] byteInput){
        if(printDebugs) System.out.println("[getStringsFromBytes]");
        for(byte b : byteInput){
            encodedText.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
    }

    public static HashMap<String,Character> fillMap(DataInputStream dis){
        if(printDebugs) System.out.println("[fillMap]");
        HashMap<String,Character> bitSeqToChar = new HashMap<>();
        try{
            System.out.println("Henlo");
            System.out.println("[fillMap] Number of bytes available: " + dis.available());
            int entries = dis.readInt();
            System.out.println("entries = " + entries);

            for(int i = 0; i < entries; i++){
                System.out.println("entry " + i);
                char c = dis.readChar();
                int len = dis.readByte() & 0xFF;
                // System.out.println("c: " + c + ", len: " + len);
                String huffCode = readHuffmanCodeAsBits(dis,len);
                bitSeqToChar.put(huffCode,c);
            }
        }catch(IOException e){
            System.err.println("[ERROR] " + e);
        }
        return bitSeqToChar;
    }

    public static String readHuffmanCodeAsBits(DataInputStream dis, int codeLength){
        if(printDebugs) System.out.println("[readHuffmanCodeAsBits]");
        StringBuilder huffmanCode = new StringBuilder(codeLength);
        try{
            int bitsToRead = codeLength;
            while (bitsToRead > 0) {
                byte readByte = dis.readByte();
                for (int i = 7; i >= 0 && bitsToRead > 0; i--) {
                    boolean bit = ((readByte >> i) & 1) == 1;
                    huffmanCode.append(bit ? '1' : '0');
                    bitsToRead--;
                }
            }
        }catch(IOException e){
            System.err.println("[ERROR] " + e);
        }
        return huffmanCode.toString();
    }

    

    // public static void decompress(StringBuilder encodedText, HashMap<String,Character> bitSeqToChar, String newFile){
    //     if(printDebugs) System.out.println("[decompress]");
    //     try{
    //         DataOutputStream dos = new DataOutputStream(new FileOutputStream(newFile));
            
    //         try{
    //             int l = 0, r = 1;
    //             while(r < encodedText.toString().length()){
    //                 if(bitSeqToChar.containsKey(encodedText.toString().substring(l,r))){
    //                     dos.writeChar(bitSeqToChar.get((encodedText.toString().substring(l,r))));
    //                     l = r;
    //                 }
    //                 r++;
    //             }
    //             dos.close();
    //         }catch(IOException e){
    //             System.err.println("[ERROR] " + e);
    //         }
    //     }catch(FileNotFoundException e){
    //         System.err.println("[ERROR] " + e);
    //     }
    // }

    public static void decompress(StringBuilder encodedText, HashMap<String,Character> bitSeqToChar, String newFile) throws IOException {
        if(printDebugs) System.out.println("[decompress]");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(newFile))) {
            String tempCode = "";
            for (int i = 0; i < encodedText.length(); i++) {
                tempCode += encodedText.charAt(i);
                if (bitSeqToChar.containsKey(tempCode)) {
                    bw.write(bitSeqToChar.get(tempCode));
                    tempCode = "";  // Reset temp code
                }
            }
        }
    }
}
