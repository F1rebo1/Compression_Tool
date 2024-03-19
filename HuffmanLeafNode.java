public class HuffmanLeafNode implements IHuffmanBaseNode{
    private char element;
    private int weight;

    public HuffmanLeafNode(char el, int wt){
        this.element = el;
        this.weight = wt;
    }

    public char value(){
        return this.element;
    }

    public int weight(){
        return this.weight;
    }

    public boolean isLeaf(){
        return true;
    }
}