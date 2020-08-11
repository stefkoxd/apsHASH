import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class MapEntry<K extends Comparable<K>,E> implements Comparable<K> {

    K key;
    E value;

    public MapEntry (K key, E val) {
        this.key = key;
        this.value = val;
    }

    public int compareTo (K that) {
        @SuppressWarnings("unchecked")
        MapEntry<K,E> other = (MapEntry<K,E>) that;
        return this.key.compareTo(other.key);
    }

    public String toString () {
        return "(" + key + "," + value + ")";
    }
}

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

class CBHT<K extends Comparable<K>, E> {

    private SLLNode<MapEntry<K,E>>[] buckets;

    @SuppressWarnings("unchecked")
    public CBHT(int m) {
        buckets = (SLLNode<MapEntry<K,E>>[]) new SLLNode[m];
    }

    private int hash(K key) {
        return Math.abs(key.hashCode()) % buckets.length;
    }

    public SLLNode<MapEntry<K,E>> search(K targetKey) {
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
        for (SLLNode<MapEntry<K,E>> curr = buckets[b]; curr != null; curr = curr.succ) {
            if (key.equals(((MapEntry<K, E>) curr.element).key)) {
                curr.element = newEntry;
                return;
            }
        }
        buckets[b] = new SLLNode<MapEntry<K,E>>(newEntry, buckets[b]);
    }

    public void delete(K key) {
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

}

public class hash1 {
    public static String evaluate(String s){
        s = s.replace("ch","c");
        s = s.replace("zh", "z");
        s = s.replace("sh", "s");
        s = s.replace("Ch","C");
        s = s.replace("Zh", "Z");
        s = s.replace("Sh", "S");
        return s;
    }
    public static void main(String []args) throws IOException {
        // vo imeDobriDeca se zachuvuvaat iminjata na dobrite deca
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        String[] imeDobriDeca = new String[N];
        // tuka se zachuvuvaat soodvetnite adresi na decata
        String[] poklonDobriDeca = new String[N];
        String pom;
        CBHT<String,String> hashTable = new CBHT<String, String>((int)(N/0.7));
        for (int i = 0; i < N; i++) {
            pom = br.readLine();
            String[] del = pom.split(" ");
            imeDobriDeca[i] = del[0];
            poklonDobriDeca[i] = del[1];
            hashTable.insert(del[0],del[1]);
        }
        //tuka se zapishuva imeto na deteto shto treba da se proveri
        String deteZaProverka = br.readLine();
        deteZaProverka = evaluate(deteZaProverka);
        if ( hashTable.search(deteZaProverka) != null ){
            System.out.println(hashTable.search(deteZaProverka).element.value);
        }
        else System.out.println("Nema poklon");
    }

}
