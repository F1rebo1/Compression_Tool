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
    private static final boolean printDebugs = true;

    public static void writeDataToCompressedFile(String fileData, String compDecompFileName, HashMap<Character,String> charToBitSeq){
        if(printDebugs) System.out.println("[writeDataToCompressedFile]");

        StringBuilder encodedText = new StringBuilder();
        for(char c : fileData.toCharArray()) encodedText.append(charToBitSeq.get(c));

        byte[] byteOutput = encodeToByteArray(encodedText.toString());
        int padding = computePadding(encodedText.toString());

        writeAllData(compDecompFileName,byteOutput,padding,charToBitSeq);
    }

    public static byte[] encodeToByteArray(String text){
        if(printDebugs) System.out.println("[encodeToByteArray]");
        int numBytes = (int)Math.ceil(text.length() / 8.0);
        byte[] output = new byte[numBytes];
        for(int i = 0; i < text.length(); i+=8){
            String byteStr = text.substring(i,Math.min(i + 8,text.length()));
            // System.out.println("byteStr = " + byteStr);
            output[i/8] = (byte)Integer.parseInt(byteStr,2);
        }
        return output;
    }

    public static int computePadding(String txt){
        if(printDebugs) System.out.println("[computePadding]");
        int padding = 8 - (txt.length() % 8);
        if(padding == 8) padding = 0;
        return padding;
    }

    public static void writeAllData(String compDecompFileName, byte[] byteOutput, int padding, HashMap<Character,String> charToBitSeq){
        if(printDebugs) System.out.println("[writeAllData]");
        try{
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(compDecompFileName));
            try{
                dos.writeInt(padding);
                writeHeader(dos,charToBitSeq);
                dos.write(byteOutput);
            }catch(IOException e){
                System.err.println("[ERROR] " + e);
            }
        }catch(FileNotFoundException e){
            System.err.println("[ERROR] " + e);
        }
    }

    public static void writeHeader(DataOutputStream dos, HashMap<Character,String> hm){
        if(printDebugs) System.out.println("[writeHeader]");
        try{
            for(Map.Entry<Character, String> entry : hm.entrySet()){
                System.out.println("headerwrite");
                dos.writeChar(entry.getKey());
                dos.writeByte(entry.getValue().length());
                writeHuffmanCodeAsBits(dos,entry.getValue());
            }
        }catch(IOException e){
            System.err.println("[ERROR] " + e);
        }
    }

    public static void writeHuffmanCodeAsBits(DataOutputStream dos, String bitString){
        if(printDebugs) System.out.println("[writeHuffmanCodeAsBits]");
        try{
            int bitsToWrite = bitString.length();
            int bitPosition = 0;
            while(bitsToWrite > 0){
                byte toWrite = 0;
                for(int i = 0; i < 8 && bitPosition < bitString.length(); i++){
                    toWrite |= (bitString.charAt(bitPosition) == '1' ? 1 : 0) << (7 - i);
                    bitPosition++;
                    bitsToWrite--;
                }
                dos.writeByte(toWrite);
            }
        }catch(IOException e){
            System.err.println("[ERROR] " + e);
        }
    }
}