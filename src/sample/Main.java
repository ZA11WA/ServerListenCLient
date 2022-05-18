package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class Main extends Application {

    private Controller controller;


    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            primaryStage.setTitle("Server");
            primaryStage.setScene(new Scene(root, 550, 420));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

        BlockingQueue<Socket> blockingQueue = new ArrayBlockingQueue<>(2);
        controller.setBlockingQue(blockingQueue);
        controller.consumerStart();


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket ss = new ServerSocket(8000)) {
                    while (true) {
                        Socket s = ss.accept();
                        blockingQueue.put(s);
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    public static void main(String[] args) throws IOException {

        launch(args);
    }
}