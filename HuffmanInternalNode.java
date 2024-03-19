public class HuffmanInternalNode implements IHuffmanBaseNode{
    IHuffmanBaseNode left;
    IHuffmanBaseNode right;
    int weight;

    public HuffmanInternalNode(IHuffmanBaseNode l, IHuffmanBaseNode r, int wt){
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