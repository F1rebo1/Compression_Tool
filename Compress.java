import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Compress{
    public static void main(String args[]){
        String fileName = "./test.txt";

        HashMap<Character,Integer> hm = countChars(fileName);
        PriorityQueue<HuffmanLeafNode> pq = new PriorityQueue<>((a,b) -> a.weight() - b.weight());
        for(Map.Entry<Character,Integer> entry : hm.entrySet()){
            // System.out.println(entry);
            pq.add(new HuffmanLeafNode(entry.getKey(),entry.getValue()));
        }

        while(!pq.isEmpty()){
            HuffmanLeafNode cur = pq.poll();
            System.out.print("Element: " + cur.value());
            System.out.println(", Weight: " + cur.weight());
        }
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