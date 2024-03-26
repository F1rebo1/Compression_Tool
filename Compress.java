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

    public static void writeHeader(String fileName,HashMap<Character,Integer> hm){
        System.out.println("[writeHeader]");
        // for (Map.Entry<Character, String> entry : hm.entrySet()) {
        //     // Write the character
        //     dos.writeChar(entry.getKey());
        //     // Write the Huffman code as bits
        //     dos.writeByte(entry.getValue().length());
        //     writeHuffmanCodeAsBits(dos, entry.getValue());
        // }
    }

    public static void writeDataToCompressedFile(String fileName,String compressedFileName,HashMap<Character,Integer> hm,HashMap<IHuffmanBaseNode,String> prefixTable){
        System.out.println("[writeDataToCompressedFile]");   
    }

    public static String padStart(String num){
        while(num.length() % 8 != 0){
            num = "0" + num;
        }
        return num;
    }
}