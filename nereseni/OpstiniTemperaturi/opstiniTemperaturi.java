import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class SLLNode<E> {
    protected E element;
    protected SLLNode<E> succ;

    public SLLNode(E elem, SLLNode<E> succ) {
        this.element = elem;
        this.succ = succ;
    }

    @Override
    public String toString() {
        return element.toString();
    }
}

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    // Each MapEntry object is a pair consisting of a key (a Comparable
    // object) and a value (an arbitrary object).
    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        // Compare this map entry to that map entry.
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "<" + key + "," + value + ">";
    }
}

class CBHT<K extends Comparable<K>, E> {

    // An object of class CBHT is a closed-bucket hash table, containing
    // entries of class MapEntry.
    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        // Construct an empty CBHT with m buckets.
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        // Translate key to an index of the array buckets.
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
        // Find which if any node of this CBHT contains an entry whose key is
        // equal
        // to targetKey. Return a link to that node (or null if there is none).
        int b = hash(targetKey);
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (targetKey.equals(((MapEntry<K, E>) curr.element).key))
                return curr;
        }
        return null;
    }

    public void insert(K key, E val) {		// Insert the entry <key, val> into this CBHT.
        MapEntry<K, E> newEntry = new MapEntry<K, E>(key, val);
        int b = hash(key);
        /*for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                // Make newEntry replace the existing entry ...
                curr.element = newEntry;
                return;
            }
        }*/
        // Insert newEntry at the front of the 1WLL in bucket b ...
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
        // Delete the entry (if any) whose key is equal to key from this CBHT.
        int b = hash(key);
        for (SLLNode<MapEntry<K,E>> pred = null, curr = buckets[b]; curr != null; pred = curr, curr = curr.succ) {
            if (key.equals(((MapEntry<K,E>) curr.element).key)) {
                if (pred == null)
                    buckets[b] = curr.succ;
                else
                    pred.succ = curr.succ;
                return;
            }
        }
    }

    public String toString() {
        String temp = "";
        for (int i = 0; i < buckets.length; i++) {
            temp += i + ":";
            for (SLLNode<MapEntry<K,E>> curr = buckets[i]; curr != null; curr = curr.succ) {
                temp += curr.element.toString() + " ";
            }
            temp += "\n";
        }
        return temp;
    }

    public void replaceValue(K targetKey, E value){
        MapEntry<K, E> newEntry = new MapEntry<K, E>(targetKey, value);
        int b = hash(targetKey);
        for ( SLLNode<MapEntry<K,E>> node = buckets[b]; node != null; node = node.succ ){
            if ( targetKey.equals(node.element.key) ) {
                node.element = newEntry;
                return;
            }
        }
    }
}

public class opstiniTemperaturi {
    public static void main (String []args)throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        CBHT<String, String> hashTable = new CBHT<String, String>((int)(n/0.5));
        for ( int i = 0; i < n; i++ ){
            String []entry = br.readLine().split(" ");
            if ( hashTable.search(entry[0])!=null ){
                //dokolku se vnese vekje postoecki vremenski interval temperaturata da se azurira
                String []pom = hashTable.search(entry[0]).element.value.split(" ");
                if ( pom[0].equals(entry[1]) && pom[1].equals(entry[2]) ){
                    hashTable.replaceValue(entry[0], entry[1] + " " + entry[2] + " " + entry[3]);
                }
                else hashTable.insert(entry[0], entry[1] + " " + entry[2] + " " + entry[3]);
            }
            else hashTable.insert(entry[0], entry[1] + " " + entry[2] + " " + entry[3]);
        }
        String entry = br.readLine();
        if (hashTable.search(entry)!=null){
            SLLNode<MapEntry<String,String>> node = hashTable.search(entry);
            int i = 0;
            Float max = Float.MIN_VALUE;
            for ( ; node != null; node = node.succ){
                 String []pom = node.element.value.split(" ");
                 Float temperature = Float.parseFloat(pom[2]);
                 if ( temperature > max ){
                     i++;
                     max = temperature;
                 }
            }
            node = hashTable.search(entry);
            for ( int j = 1; j < i  ; j++ ){
                node = node.succ;
            }
            System.out.println(hashTable);
            System.out.println(node.element.key + " " + node.element.value);
        }
        else System.out.println("Ne postoi");
    }
}
