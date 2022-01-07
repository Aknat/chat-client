package ua.kiev.prog;

import java.io.IOException;
import java.net.HttpURLConnection;
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
                printAppLog("the new user registration is performed");
            } else {
                res = user.sendUser(Utils.getURL() + "/auth");
                printAppLog("the new user authentication is performed");
            }
            printAppLog("response HTTP status code: ", res);

            if (res == 200) {
                Thread th = new Thread(new GetThread());
                th.setDaemon(true);
                th.start();

                Thread th2 = new Thread(new GetPrivateChatThread(user.getLogin()));
                th2.setDaemon(true);
                th2.start();

                while (true) {
                    System.out.println("Enter your message:");
                    String text = scanner.nextLine();
                    if (text.trim().isEmpty()) break;

                    System.out.println("Enter the receiver (if empty, then message is sent to chat)");
                    String to = scanner.nextLine();

                    Message m = new Message(user.getLogin(), to, text);
                    res = m.sendMessage(Utils.getURL() + "/add");


                    if (res != 200) {
                        printAppLog("response HTTP status code: ", res);
                        return;
                    }
                }
            } else
                printAppLog("Y failed(((");


        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printAppLog(String str) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms")) + "] " + "APP LOG: " + str);
    }

    public static void printAppLog(String str, int httpCode) {
        System.out.println("[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,ms")) + "] " + "APP LOG: " + str + httpCode);
    }
}
