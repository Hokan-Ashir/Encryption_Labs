package ist;

public class Pair<First, Second> {
    private First first;
    private Second second;
    public Pair(First first, Second second){
        this.first = first;
        this.second = second;
    }

    public First getFirst() {
        return first;
    }

    public Second getSecond() {
        return second;
    }

    public void setFirst(First first) {
        this.first = first;
    }

    public void setSecond(Second r) {
        this.second = second;
    }
}