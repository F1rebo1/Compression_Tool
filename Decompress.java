import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Decompress {
    public static void decompressFile(String compressedFileName, String newFile, int byteCount, int sz){
        try{
            BufferedReader d = new BufferedReader(new InputStreamReader(new FileInputStream(compressedFileName + ".txt")));
            d.readLine();

            System.out.println("DATA: ");
            System.out.println(d.readLine());
        }catch(IOException e){
            System.out.println(e);
        }

        try{
            DataInputStream dis = new DataInputStream(new FileInputStream(compressedFileName + ".txt"));
            StringBuilder outputString = new StringBuilder();
            byte[] bytes = new byte[byteCount];
            int byteNum = 0;

            

            while(byteNum < byteCount){
                byte b = dis.readByte();
                bytes[byteNum] = b;
                byteNum++;
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
