package ua.kiev.prog;

import java.util.Scanner;

public abstract class UserHelper {
    public static User createUser(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your login: ");
        String login = scanner.nextLine();

        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        return new User(login, password);
    }

    public static boolean isNewUser() {
        boolean isNew = false;

        System.out.println("Are you a new user? Please type [y/n]");
        Scanner scanner = new Scanner(System.in);
        String userType = scanner.nextLine();
        if ((userType.trim().equalsIgnoreCase("y")) || (userType.trim().equalsIgnoreCase("yes"))) isNew = true;
        return isNew;
    }
}
