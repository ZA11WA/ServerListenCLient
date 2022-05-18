package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Stream;

public class Controller {
    @FXML
    private TextArea serverTextArea;
    private int count;
    private BlockingQueue<Socket> blockingQueue;
    private Iterator<Map.Entry<String, String>> iterator;
    private Map.Entry<String, String> current;


    public void setBlockingQue(BlockingQueue<Socket> blockingQueue){
        this.blockingQueue = blockingQueue;
    }

    public void consumerStart(){
        Thread thread = new Thread(new Runnable() {
            private Socket s;
            @Override
            public void run() {
                try {
                    while (count >= 0){
                        s = blockingQueue.take();
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        Answer answer = ((Answer) in.readObject());

                        if(answer.getAnswer().equalsIgnoreCase(current.getKey())){
                            printInfo(answer.getNick() + " (" + s.getInetAddress().getHostAddress() + ")" + " odpowiedział poprawnie :)");
                            if(iterator.hasNext()){
                                current = iterator.next();
                            }
                            count--;
                            printInfo("");
                            if(count >= 0){
                                printInfo("Nr " + (count + 1) + ") " + current.getValue());
                            }
                        }
                        else{
                            printInfo("Nadeszła odpowiedź błędna :(");
                        }
                    }
                }catch (InterruptedException | IOException | ClassNotFoundException e) {
                }
                printInfo("Odpowiedziano już na wszystkie pytania. Koniec zabawy!");
            }
        });
        thread.start();
    }
    private void printInfo(String s){
        serverTextArea.appendText(s + "\n");
    }

    private void readQuestion(){
        Map<String, String> map = new LinkedHashMap<>();
        try(Stream<String> lines = Files.lines(Paths.get("quiz.txt"))){
            lines.filter(line -> line.contains(":"))
                    .forEach(line -> map.putIfAbsent(line.split(":")[1], line.split(":")[0]));
        }catch (IOException e){
            System.err.println("Problem z wczytaniem pliku");
        }
        this.count = map.size();
        iterator = map.entrySet().iterator();
        if(iterator.hasNext()){
            current = iterator.next();
        }
        count--;
        this.printInfo("Nr " + (count + 1) + ") " + current.getValue());
    }
    @FXML
    private void initialize() {
        this.readQuestion();
    }
}