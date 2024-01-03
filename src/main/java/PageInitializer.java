import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Collections;

import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;


class PageInitializer {
    static private HashMap<Integer, Question> questions;
    static private ArrayList<Integer> randOrder;
    static private int currInd = 0;
    static private Question currQ;
    static private int[] stats = new int[3]; // 0 - total, 1 - wrong, 2 - right

    static void init() throws IOException {
        final String START_MARKER = "Effective 7/01/2022 – 6/30/2026";
        final String BREAK_MARKER = " ";
        final String END_MARKER = "~~~~End of question pool text~~~~";
        questions = new HashMap<>();
        randOrder = new ArrayList<>(410);

        HashSet<String> headings = new HashSet<>(45);

        File q = new File("q.txt");
        BufferedReader br = new BufferedReader(new FileReader(q));

        String line = "";
        while (!START_MARKER.equals(line)) {
            line = br.readLine();
        }
        line = br.readLine();

        while (!BREAK_MARKER.equals(line)) {
            if (!line.isEmpty()) {
                headings.add(line);
            }

            line = br.readLine();
        }

        while (!START_MARKER.equals(line)) {
            line = br.readLine();
        }
        line = br.readLine();

        int ctr = 0;
        while (!END_MARKER.equals(line)) {
            if (!line.isEmpty() && line.charAt(0) == 'T' && !headings.contains(line)) {
                questions.put(ctr, new Question(line, br.readLine(), br.readLine(), br.readLine(),
                        br.readLine(), br.readLine()));
                randOrder.add(ctr);
                ctr++;
            }

            line = br.readLine().trim();
        }

        Collections.shuffle(randOrder);

        // Initialize currQ for 1st question.
        nextQ();
    }

    static void reset() {
        Collections.shuffle(randOrder);
        stats[0] = 0;
        stats[1] = 0;
        stats[2] = 0;
    }

    static protected void nextQ() {
        if (currInd < questions.size()) {
            currInd++;
            currQ = questions.get(randOrder.get(currInd - 1));
        } else {
            //System.out.println("reset");
            reset();
            nextQ();
        }
    }

    static void wrongAns() {
        stats[0]++;
        stats[1]++;
    }

    static void rightAns() {
        stats[0]++;
        stats[2]++;
    }

    static char correctAns() {
        return currQ.correct;
    }

    static String qToString() {
        return currQ.toString();
    }

    static int getTotalAttempts() {
        return stats[0];
    }

    static int getNumCorrect() {
        return stats[2];
    }

    static int getNumWrong() {
        return stats[1];
    }

    private static class Question {
        private final String[] choices;
        private final String question;
        private final char correct;
        private final String tag;

        Question(String h, String q, String a, String b, String c, String d) {
            choices = new String[] {a, b, c, d};
            question = q;
            correct = h.charAt(7); // - 'A';
            tag = h.substring(0, 5);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();

            sb.append("     Q:  ");
            sb.append(tag);
            sb.append("\n ");
            sb.append(question);
            sb.append("\n");
            sb.append("\n     ").append(choices[0]);
            sb.append("\n     ").append(choices[1]);
            sb.append("\n     ").append(choices[2]);
            sb.append("\n     ").append(choices[3]);
            sb.append("\n\n");

            return sb.toString();
        }
    }
}


