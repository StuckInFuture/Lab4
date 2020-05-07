package sample;

import javafx.application.Application;
import java.util.UUID;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import sample.philosopher.Philosopher;

public class Main extends Application {
   // long inf = (long)Double.POSITIVE_INFINITY;
    public ArrayList<Philosopher> table = new ArrayList<Philosopher>();
    public String uniqueId;
    public ReentrantLock globalMtx = new ReentrantLock();
    //volatile Thread a;
    @Override
    public void start(Stage stage) throws InterruptedException {
        stage.setTitle("Ужин");
        stage.setHeight(200);
        stage.setWidth(400);
        FlowPane Layer1 = new FlowPane(Orientation.VERTICAL);
        Layer1.setColumnHalignment(HPos.CENTER);
        Button addPhilosopher = new Button("Add philosopher");
        Layer1.getChildren().addAll(addPhilosopher);
        FlowPane.setMargin(Layer1, new Insets(300, 5, 5, 5));
        FlowPane mainLayer = new FlowPane(5, 5, Layer1);
        mainLayer.setAlignment(Pos.CENTER);
        Scene scene = new Scene(mainLayer);
        stage.setScene(scene);
        stage.show();
        addPhilosopher.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    globalMtx.tryLock(10000, TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    table.add(new Philosopher( uniqueId=UUID.randomUUID().toString(),table, globalMtx));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                table.get(table.size()-1).start();
                synchronized (globalMtx){
                    globalMtx.unlock();}
            }
       });

        //Thread.currentThread().join(inf);
    }

}
