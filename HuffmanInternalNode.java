public class HuffLeafNode implements IHuffmanBaseNode{
    HuffLeafNode left;
    HuffLeafNode right;
    int weight;

    public HuffLeafNode(HuffLeafNode l, HuffLeafNode r, int wt){
        this.left = l;
        this.right = r;
        this.weight = wt;
    }

    public IHuffmanBaseNode left(){
        return this.left;
    }

    public IHuffmanBaseNode right(){
        return this.right;
    }

    public boolean isLeaf(){
        return false;
    }

    public int weight(){
        return this.weight;
    }
}