public class HuffTree implements Comparable{
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
        return this.root.weight;
    }

    public int compareTo(Object t){
        HuffTree other = (HuffTree)t;
        if(this.root.weight() < other.weight()) return -1;
        else if(this.root.weight() == other.weight()) return 0;
        else return 1;
    }

    public static buildTree(){
        HuffmanTree temp1 = null, temp2 = null, temp3 = null;

        PriorityQueue<HuffmanTree> pq = new PriorityQueue<>((a,b) -> a.weight() - b.weight());
        while(pq.size() > 1){
            temp1 = pq.poll();
            temp2 = pq.poll();
            temp3 = new HuffmanTree(temp1.root(), temp2.root(), temp1.weight() + temp2.weight());
            pq.add(temp3);
        }
        return temp3;
    }
}