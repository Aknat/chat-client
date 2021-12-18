package ua.kiev.prog;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static ua.kiev.prog.UserHelper.*;

public class Main {
    public static void main(String[] args) {

        User user = createUser();
        try (Scanner scanner = new Scanner(System.in)) {

            int res;
            if (isNewUser()) {
                res = user.sendUser(Utils.getURL() + "/reg");
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "the new user registration is performed");
            } else {
                res = user.sendUser(Utils.getURL() + "/auth");
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "the new user authentication is performed");
            }
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "response HTTP status code: " + res);

            Thread th = new Thread(new GetThread());
            th.setDaemon(true);
            th.start();

            if (res == 200) {
                System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "Enter your message: ");
                while (true) {
                    String text = scanner.nextLine();
                    if (text.isEmpty()) break;

                    Message m = new Message(user.getLogin(), text);
                    res = m.sendMessage(Utils.getURL() + "/add");

                    if (res != 200) { // 200 OK
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "response HTTP status: " + res);
                        return;
                    }
                }
            } else System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms ")) + "Y failed(((");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
