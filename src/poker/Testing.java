package poker;

public class Testing {
    public static void main(String[] args) {
        Deck dck = new Deck();

        System.out.println(dck);

        dck.shuffleDeck();

        System.out.println(dck);
    }
}
