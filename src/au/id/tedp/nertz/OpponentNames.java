package au.id.tedp.nertz;

import android.util.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

final class OpponentNames {
    private final static List<Pair<String,String>> names = Arrays.asList(
        new Pair<String,String>("Siegfried", "Roy"),
        new Pair<String,String>("Kermit", "Miss Piggy"),
        new Pair<String,String>("Hall", "Oates"),
        new Pair<String,String>("Lennon", "McCartney"),
        new Pair<String,String>("Hansel", "Gretel"),
        new Pair<String,String>("Jack", "Jill"),
        new Pair<String,String>("Shawn", "Gus"),
        new Pair<String,String>("Bert", "Ernie"),
        new Pair<String,String>("Bonnie", "Clyde"),
        new Pair<String,String>("Jekyll", "Hyde"),
        new Pair<String,String>("Donnie", "Marie"),
        new Pair<String,String>("Sherlock", "Watson"),
        new Pair<String,String>("Starsky", "Hutch"),
        new Pair<String,String>("Tom", "Jerry"),
        new Pair<String,String>("Scooby", "Shaggy"),
        new Pair<String,String>("Romeo", "Juliet"),
        new Pair<String,String>("Simon", "Garfunkel"),
        new Pair<String,String>("Booth", "Bones"),
        new Pair<String,String>("Phineas", "Ferb"),
        new Pair<String,String>("Milo", "Otis"),
        new Pair<String,String>("Cheech", "Chong"),
        new Pair<String,String>("Ren", "Stimpy")
    );

    public static Pair<String,String> getPair() {
        return names.get(new Random().nextInt(names.size()));
    }
}
