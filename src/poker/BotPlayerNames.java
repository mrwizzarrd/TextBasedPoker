package poker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BotPlayerNames {

    private static final List<String> names = new ArrayList<>();


    static {
        names.add("Skynet (BOT)");
        names.add("Mr. Robot (BOT)");
        names.add("Heisenbug (BOT)");
        names.add("GLaDOS (BOT)");
        names.add("Nick Valentine (BOT)");
        names.add("Atlas (BOT)");
        names.add("P-Body (BOT)");
        names.add("Cortana (BOT)");
        names.add("Claptrap (BOT)");
        names.add("Handsome Jack (BOT)");
        names.add("Wheatley (BOT)");
        names.add("R2-D2 (BOT)");
        names.add("C-3PO (BOT)");
        names.add("Big Brother (BOT)");
        names.add("Wall-E (BOT)");
        names.add("Wilhelm (BOT)");
        names.add("W4R-D3N (BOT)");
        names.add("Liberty Prime (BOT)");
        names.add("Codsworth (BOT)");
        names.add("Ada (BOT)");
        names.add("K-2SO (BOT)");
        names.add("NullPointerException (BOT)");
        names.add("SegFault (BOT)");
        names.add("Stack Overflow (BOT) ");
    }

    public static String getName(){
        int size = names.size();
        int index = (int) (Math.random() * (size - 1));

        return names.get(index);
    }
}
