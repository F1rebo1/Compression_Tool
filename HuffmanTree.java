import java.util.*;

public class HuffmanTree implements Comparable{
    private IHuffmanBaseNode root;

    public HuffmanTree(char el, int wt){
        this.root = new HuffmanLeafNode(el,wt);
    }

    public HuffmanTree(IHuffmanBaseNode l, IHuffmanBaseNode r, int wt){
        this.root = new HuffmanInternalNode(l,r,wt);
    }

    public IHuffmanBaseNode root(){
        return this.root;
    }

    public int weight(){
        return this.root.weight();
    }

    public int compareTo(Object t){
        HuffmanTree other = (HuffmanTree)t;
        if(this.root.weight() < other.weight()) return -1;
        else if(this.root.weight() == other.weight()) return 0;
        else return 1;
    }

    public static HuffmanTree buildTree(PriorityQueue<HuffmanTree> pq){
        HuffmanTree temp1 = null, temp2 = null, temp3 = null;

        while(pq.size() > 1){
            temp1 = pq.poll();
            temp2 = pq.poll();
            temp3 = new HuffmanTree(temp1.root(), temp2.root(), temp1.weight() + temp2.weight());
            pq.add(temp3);
        }
        return temp3;
    }

    public void createPrefixTable(HuffmanTree root, HashMap<IHuffmanBaseNode,String> prefixTable){
        System.out.println("Henlo");
        String toBeAdded = "";
        createTable(this.root,prefixTable,toBeAdded);
    }

    public void createTable(IHuffmanBaseNode root, HashMap<IHuffmanBaseNode,String> prefixTable, String toBeAdded){
        HuffmanInternalNode internalNode = null;
        if(root instanceof HuffmanInternalNode) internalNode = (HuffmanInternalNode) root;

        if(internalNode == null || internalNode.isLeaf()) return;
        
        if(internalNode.left().isLeaf()){
            HuffmanLeafNode node = (HuffmanLeafNode) internalNode.left();
            System.out.println("Adding 0 to " + node.value());
            // System.out.println("Adding 0 to " + internalNode.left().value());
            prefixTable.put(internalNode.left(),prefixTable.getOrDefault(internalNode.left(),"") + toBeAdded + "0");
        }
        createTable(internalNode.left(),prefixTable,toBeAdded + "0");

        if(internalNode.right().isLeaf()){
            HuffmanLeafNode node = (HuffmanLeafNode) internalNode.right();
            System.out.println("Adding 1 to " + node.value());
            // System.out.println("Adding 1 to " + internalNode.right().value());
            prefixTable.put(internalNode.right(),prefixTable.getOrDefault(internalNode.right(),"") + toBeAdded + "1");
        }
        createTable(internalNode.right(),prefixTable,toBeAdded + "1");
        
        return;
    }

    public void preorderTraversal() {
        preorderTraversal(this.root);
    }

    private void preorderTraversal(IHuffmanBaseNode node) {
        if (node == null) return;        

        if (node.isLeaf()) {
            HuffmanLeafNode leaf = (HuffmanLeafNode) node;
            System.out.println("Character: " + leaf.value() + ", Weight: " + leaf.weight());
            return;
        }

        if (node instanceof HuffmanInternalNode) {
            HuffmanInternalNode internalNode = (HuffmanInternalNode) node;
            System.out.println("Internal Node, Weight: " + internalNode.weight());
            preorderTraversal(internalNode.left());
            preorderTraversal(internalNode.right());
        }
    }
}