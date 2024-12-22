import java.util.*;

public class School implements Comparable<School> {
    private String name;
    private Double ratings;

    public School(String name, Double ratings) {
        this.name = name;
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("schoolName = ").append(name).append(", ratings = ").append(ratings);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name, this.ratings);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School that = (School) o;
        return (this.name.equals(that.name) && this.ratings.equals(that.ratings));
    }

    @Override
    public int compareTo(School that) {
        if (Double.compare(this.ratings, that.ratings) == 0) {
            return this.name.compareTo(that.name);
        } else {
            return Double.compare(this.ratings, that.ratings);
        }
    }

    public static void main(String[] args) {
        PriorityQueue<School> pq = new PriorityQueue<>();
        pq.add(new School("one", 2.5d));
        pq.add(new School("two", 5.7d));
        pq.add(new School("two", 7.8d));
        pq.add(new School("two", 7.8d));


        TreeMap<School, Integer> mp = new TreeMap<>();
        mp.put(new School("one", 2.5d), 1);
        mp.put(new School("two", 5d), 1);
        mp.put(new School("two", 2.5d), 1);

//        for(Map.Entry<School, Integer> entry : mp.entrySet()) {
//            System.out.println(entry.getKey());
//        }

        SortedMap<School, Integer> sortedMap = mp.tailMap(new School("a", -1d), true);

        School floorKey = mp.ceilingKey(new School("one", 2.6d));

        System.out.println(floorKey);
        Iterator<School> it = pq.iterator();


//        while(it.hasNext()) {
//            System.out.println(it.next());
//        }
    }
}
