package movieGuess;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Game {
    private final List<String> movies = new ArrayList<>();
    List<String> movieToGuess = new ArrayList<>();
    List<String> lettersProvided = new ArrayList<>();
    String EMPTY_SPACE = "_";
    String WHT_SPAC = "  ";
    int rand = 0;

    public void scanData() throws FileNotFoundException {

        File file = new File("movies");
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            movies.add(scanner.nextLine());
        }
    }

    public void startGame() throws FileNotFoundException {
        scanData();
        int minNo = 1;
        int maxNo = movies.size();

        int num = 0;
        boolean isGameOn = true;

        Random random = new Random();
        rand = random.nextInt(maxNo - minNo) + minNo;
        int attempts = Math.min(movies.get(rand).length(), 20);

        randomMovieToList(rand);

        String[] guess = new String[movies.get(rand).length()];
        fillIn(guess, rand);

        System.out.println("This is the movie you want to guess\nYou have "+attempts+" attempts\nGood luck");
        System.out.println("movie : " + movies.get(rand));

        Scanner scanner = new Scanner(System.in);
        print(guess);

        while (num < attempts && isGameOn) {
            System.out.print("provide letter: ");
            String letter = scanner.next();

            if (!lettersProvided.contains(letter)) {
                lettersProvided.add(letter);
                num++;
                if (movies.get(rand).contains(letter)) {
                    replaceLetter(letter, rand, guess);
                    if (checkifGameOver(guess, num, attempts)) {
                        break;
                    }
                    print(guess);
                    System.out.println(":), you have " + (attempts - num) + " attempts left");
                } else {
                    System.out.println("This letter is not in the title: " + letter + ", you have " + (attempts - num) + " attempts left");
                    if (checkifGameOver(guess, num, attempts)) {
                        break;
                    }
                }

            } else {
                System.out.println("You already provided this letter, you have " + (attempts - num) + " attempts left");

            }

        }
    }


    private boolean checkifGameOver(String[] guess, int leftAttempts, int totalAttempts) {

        Pattern pattern = Pattern.compile(EMPTY_SPACE);
        Predicate<String> predicate = pattern.asMatchPredicate();

        if (Arrays.stream(guess).noneMatch(predicate)) {
            System.out.println("\n" +
                    "**************************\n" +
                    "*** You won, game over ***\n" +
                    "**************************");
            System.out.println(movies.get(rand));
            return true;
        } else if (leftAttempts >= totalAttempts) {
            System.out.println("\n" +
                    "***************************\n" +
                    "*** You lost, game over ***\n" +
                    "***************************");
            System.out.println(movies.get(rand));
            return true;
        }

        return false;
    }

    public void print(String[] guess) {
        System.out.println("\n");
        for (String s : guess) {
            System.out.print(s);
        }
        System.out.println("\n");

    }

    public void fillIn(String[] guess, int no) {
        String movieName = movies.get(no);
        for (int i = 0; i < movieName.length(); i++) {
            if (movieName.charAt(i) == ' ') {
                guess[i] = WHT_SPAC;
            } else {
                guess[i] = (EMPTY_SPACE);
            }
        }
    }

    public void randomMovieToList(int randMove) {
        for (char c : movies.get(randMove).toCharArray()) {
            movieToGuess.add(String.valueOf(c));
        }
    }

    public void replaceLetter(String letter, int randMove, String[] guess) {
        String movie = movies.get(randMove);
        for (int i = 0; i < movie.length(); i++) {
            if (Objects.equals(String.valueOf(movie.charAt(i)), letter)) {
                guess[i] = letter;
            }
        }
    }
}
