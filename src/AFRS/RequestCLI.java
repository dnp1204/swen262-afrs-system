package AFRS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestCLI {

    private static final String PROMPT = "> ";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        ArrayList<String> response;
        RequestController parser = new RequestController();
        System.out.println("WELCOME TO THE AIRLINE FLIGHT RESERVATION SYSTEM");
        System.out.println("PLEASE INPUT A REQUEST FOLLOWED BY A SEMICOLON");
        do {
            String input = "";
            do {
                System.out.print(PROMPT);
                    input += scan.nextLine();
                if (input.length() == 0 || !input.substring(input.length() - 1).equals(";")) {
                    System.out.println("partial-request");
                }
            } while (input.length() == 0 || !input.substring(input.length() - 1).equals(";"));

            response = parser.parse(input);

            if (response.get(0).equals("quit")) {
                System.exit(0);
            }
            printResponse(response);
        } while (!response.get(0).equals("quit"));
    }

    private static void printResponse(ArrayList<String> response) {
        for (String str : response) {
            System.out.println(str);
        }
    }

}
