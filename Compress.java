import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Compress{
    public static void main(String args[]){
        // String fileName = "./test.txt";
        String fileName = "./test2.txt";

        HashMap<Character,Integer> hm = countChars(fileName);
        PriorityQueue<HuffmanTree> pq = new PriorityQueue<>((a,b) -> a.weight() - b.weight());
        // PriorityQueue<HuffmanLeafNode> pq_nodes = new PriorityQueue<>((a,b) -> a.weight() - b.weight());
        PriorityQueue<HuffmanTree> pq_nodes = new PriorityQueue<>((a,b) -> a.weight() - b.weight());

        for(Map.Entry<Character,Integer> entry : hm.entrySet()){
            pq.add(new HuffmanTree(entry.getKey(),entry.getValue()));
        }
        
        HuffmanTree root = HuffmanTree.buildTree(pq);
        
        System.out.println("Performing Preorder Traversal:");
        root.preorderTraversal();
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