import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Author: Kuba Spatny
 * Web: kubaspatny.cz
 * E-mail: kuba.spatny@gmail.com
 * Date: 3/29/14
 * Time: 9:49 PM
 * Copyright 2014 Jakub Spatny
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class AffineCipherBreaker {

    public static void main(String[] args) {

        boolean vocabularyAttack = true;
        String text = "CIPHERTEXTCIPHERTEXTCIPHERTEXTCIPHERTEXTCIPHERTEXTCIPHERTEXTCIPHERTEXTCIPHERTEXT";
        decipherAffine(text, vocabularyAttack);

    }

    public static void decipherAffine(String text, boolean vocabularyAttack){
        try {

            File f = new File("affine_outputs_stat.txt");
            if (!f.exists()) {
                f.createNewFile();
            }

            PrintWriter p = new PrintWriter(f);

            for (int j = 1; j <= 25; j++) {
                for (int k = 0; k <= 25; k++) {

                    StringBuilder s = new StringBuilder();

                    for (int i = 0; i < text.length(); i++) {
                        char t = text.charAt(i);
                        int t_ASCII = ((int) t) - 65;
                        int res = j * (t_ASCII - k);

                        res = res % 26;
                        if (res < 0) {
                            res += 26;
                        }
                        res += 65;
                        char res_char = (char) res;
                        s.append(res_char);

                    }
                    String open_text = s.toString();

                    if (vocabularyAttack) {
                        try {
                            Scanner sc = new Scanner(new File("commonwords.txt"));
                            String word;
                            while (sc.hasNext()) {
                                word = sc.next().toUpperCase();
                                if (open_text.contains(word) && word.length() > 4) {
                                    System.out.println(open_text + " [" + word + "] " + "[" + j + "," + k + "]");
                                    break;
                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                        if (isOK(open_text)) {
                            p.print("[" + j + "," + k + "]\t" + open_text + "\r\n");
                            p.flush();
                        }

                    }

                }

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isOK(String text) {

        HashMap<Character, Integer> stat = new HashMap<Character, Integer>();
        for (int i = 'A'; i < 'A' + 26; i++) {
            stat.put((char) i, 0);
        }

        for (int i = 0; i < text.length(); i++) {
            stat.put(text.charAt(i), stat.get(text.charAt(i)) + 1);
        }

        System.out.println('A' + " " + (stat.get('A') / (double) text.length()) * 100);
        System.out.println('E' + " " + (stat.get('E') / (double) text.length()) * 100);
        System.out.println('I' + " " + (stat.get('I') / (double) text.length()) * 100);
        System.out.println('O' + " " + (stat.get('O') / (double) text.length()) * 100);
        System.out.println('U' + " " + (stat.get('U') / (double) text.length()) * 100);

        double a_comp = (stat.get('A') / (double) text.length()) * 100;
        double e_comp = (stat.get('E') / (double) text.length()) * 100;
        double i_comp = (stat.get('I') / (double) text.length()) * 100;
        double o_comp = (stat.get('O') / (double) text.length()) * 100;
        double u_comp = (stat.get('U') / (double) text.length()) * 100;

        double a = 8.2;
        double e = 12.7;
        double i = 6.9;
        double o = 7.5;
        double u = 2.7;

        return (a_comp > a - 4 && a_comp < a + 4) &&
                (e_comp > e - 4 && e_comp < e + 4) &&
                (i_comp > i - 4 && i_comp < i + 4) &&
                (o_comp > o - 4 && o_comp < o + 4) &&
                (u_comp > u - 4 && u_comp < u + 4);
    }

    public static String shift(String text, int shift){

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char t = text.charAt(i);
            int t_ASCII = ((int) t) - 65;

            int res = (t_ASCII + shift);

            res = res % 26;
            if(res < 0){
                res += 26;
            }
            res += 65;
            char res_char = (char)res;
            //System.out.print(res_char);
            s.append(res_char);

        }

        return s.toString();
    }

}
