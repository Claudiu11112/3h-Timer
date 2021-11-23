package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.util.Duration;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.time.temporal.ChronoUnit.MINUTES;

public class Controller implements Initializable {
    private static int i = 0;
    private static int i1 = 0;
    @FXML
    private Label cl2;
    @FXML
    private Label cl;
    @FXML
    private Button abtn;
    @FXML
    private Button hbtn;
    @FXML
    private MenuItem reset;
    @FXML
    private TextArea ta;
    private String h2;
    private String s5;
    private String s6;
    private String s7;
    private Timeline t1;
    private Timeline t2;
    private Timeline t3;
    private Timeline t4;

    private void clockLabel() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss ");
        t1 = new Timeline(
                new KeyFrame(Duration.millis(500),
                        event -> {
                            Calendar cal = Calendar.getInstance();
                            //System.out.println(dateFormat.format(cal.getTime()));
                            String s8 = dateFormat.format(cal.getTime());
                            cl.setText(s8 + "\n");
                        }
                )
        );
        t1.setCycleCount(Animation.INDEFINITE);
        t1.play();
    }

    public void closeMi() {
        Alert a = new Alert(AlertType.CONFIRMATION);
        a.setTitle("Exit");
        a.setHeaderText("Do you want to exit?");
//        a.initStyle(StageStyle.TRANSPARENT);
        a.initModality(Modality.APPLICATION_MODAL);
        a.initOwner(Main.getStage());
        Optional<ButtonType> o = a.showAndWait();
        if (o.isPresent() && o.get() == ButtonType.OK) {
            System.gc();
            Platform.exit();
            System.exit(0);
        } else {
            a.close();
        }
    }

    public void resetMi() {
        try {
            abtn.setDisable(false);
            hbtn.setDisable(false);
            if (t2 != null)
                t2.stop();
            if (t3 != null)
                t3.stop();
            if (t4 != null)
                t4.stop();
            cl2.setText("");
            dScreen();
            i = 0;
            i1 = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dScreen() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MM-yyyy");
        String s = sdf.format(d);
        ta.setText(s + "\n");
        reset.setDisable(true);
        if (t1 == null) {
            clockLabel();
        }
    }

    public void aboutMi() {
        Alert a = new Alert(AlertType.INFORMATION);
        a.setTitle("About Timer");
        a.setHeaderText("Timer v.3.0");
        Image i = new Image("/sample/timer.png");
        ImageView iv = new ImageView(i);
        a.setGraphic(iv);
        //a.initStyle(StageStyle.TRANSPARENT);
        a.setContentText("Development: Stark C.\n" + "Java 9 Dev.");
        a.initModality(Modality.APPLICATION_MODAL);
        a.initOwner(Main.getStage());
        a.showAndWait();
    }

    public void hTimer() {
        ta.setStyle("-fx-font-size: 18;-fx-text-fill: indigo;");
        LocalDateTime nowPlus = LocalDateTime.now().plusHours(3);
        Date da1 = new Date(System.currentTimeMillis());
        Date dt = new Date(System.currentTimeMillis() + 10800000L);
        //Date dt = new Date(System.currentTimeMillis() + 10_000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String s = sdf.format(da1);
        String s1 = sdf.format(dt);
        String h1 = "Start task: " + s + "\n" + "End task: " + s1;
        ta.setText(h1);
        abtn.setDisable(true);
        hbtn.setDisable(true);
        reset.setDisable(false);

        t2 = new Timeline(new KeyFrame(Duration.hours(3), ev -> {
//        t2 = new Timeline(new KeyFrame(Duration.seconds(10), ev -> {
            t3.pause();
            t3.stop();
            cl2.setText("");
            String s9 = "Timing completed!\nUse reset for new timing.\n\n" + h1;
            ta.setText("");
            ta.setStyle("-fx-font-size: 18;-fx-text-fill: green;");
            ta.setText(s9);
            playSound();
        }));
        t2.play();

        t3 = new Timeline(
                new KeyFrame(Duration.millis(500),
                        event -> {
//                            Date da = new Date(System.currentTimeMillis());
//                            SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
//                            String s2 = sdf2.format(da);
//                            LocalTime l1 = LocalTime.parse(s2);
//                            System.out.println(l1);
//                            String s3 = sdf2.format(dt);
//                            System.out.println(s3);
//                            LocalTime l2 = LocalTime.parse(s3);
//                            long lm = MINUTES.between(l1, l2);
//                            System.out.println(lm);
//                            System.out.println(l1.until(l2, MINUTES));
//                            System.out.println(MINUTES.between(l1, l2));
                            LocalDateTime now = LocalDateTime.now();
                            long diff = MINUTES.between(now, nowPlus);
//                            System.out.println(now);
//                            System.out.println(nowPlus);
                            cl2.setStyle("-fx-font-size: 17;-fx-text-fill: indigo;");
                            cl2.setText("   Remaining time: \n" + diff + " minute \n");

                            afterHour();
                            closeApp();
                        }
                )
        );
        t3.setCycleCount(Animation.INDEFINITE);
        t3.play();
    }

    private void afterHour() {
        Date da = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        String s9 = sdf1.format(da);
        //System.out.println(s9);
        if (s9.equals("21") || s9.equals("22") || s9.equals("23")) {
            cl2.setText(" After hour 21 : \n" + "    application will be closed. \n");
        }
    }

    private void playSound() {
        URL url = this.getClass().getResource("Alarm01.wav");
        AudioClip ac = null;
        if (url != null) {
            ac = new AudioClip(url.toString());
        }
        if (ac != null) {
            ac.play();
        }
    }

    @FXML
    private void aTimer() {
        ta.setStyle("-fx-font-size: 18;-fx-text-fill: indigo;");
        aTimerTextArea();
        //System.out.println("test");
        t4 = new Timeline(
                new KeyFrame(Duration.hours(3),
//                        new KeyFrame(Duration.seconds(20),
                        event -> {
                            playSound();
                            Controller.i++;
                            if (Controller.i <= 3) {
                                aTimerTextArea();
                                //System.out.println(i);
                            } else if (Controller.i == 4) {
                                resetMi();
                                finalNote();
                            }
                        }
                )
        );
        t4.setCycleCount(4);
        t4.play();
//        Thread thr1 = new Thread(() -> {
//            class AppExit extends TimerTask {
//                public void run() {
////                    System.out.println("Hello coders");
//                    Date da = new Date(System.currentTimeMillis());
//                    SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
//                    String s9 = sdf1.format(da);
//                    if (s9.equals("21") || s9.equals("22") || s9.equals("23")) {
////                    Alert a = new Alert(AlertType.INFORMATION);
////                    a.setTitle("Timing completed!");
////                    a.setHeaderText("Program will be shut down automatically.");
////                    a.setContentText("Development: Stark C.\n" + "Java 9 Dev.");
////                    a.initModality(Modality.APPLICATION_MODAL);
////                    a.initOwner(Main.getStage());
////                    a.showAndWait();
//                        System.gc();
//                        Platform.exit();
//                        System.exit(0);
//                    }
//                }
//            }
//            Timer timer = new Timer();
//            timer.schedule(new AppExit(), 0, 60000);
//        });
//        thr1.start();
        closeApp();
    }

    private void closeApp() {
        Thread thr1 = new Thread(() -> {
            while (true) {
                Date da = new Date(System.currentTimeMillis());
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
                String s9 = sdf1.format(da);
                //System.out.println(s9);

                try {
                    Thread.sleep(60_000L);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
//                System.out.println(s9);
                if (s9.equals("21") || s9.equals("22") || s9.equals("23")) {
//                    Alert a = new Alert(AlertType.INFORMATION);
//                    a.setTitle("Timing completed!");
//                    a.setHeaderText("Program will be shut down automatically.");
//                    a.setContentText("Development: Stark C.\n" + "Java 9 Dev.");
//                    a.initModality(Modality.APPLICATION_MODAL);
//                    a.initOwner(Main.getStage());
//                    a.showAndWait();
                    System.gc();
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
        thr1.start();
    }

    private void aTimerTextArea() {
        Date da1 = new Date(System.currentTimeMillis());
        Date dd1 = new Date(System.currentTimeMillis() + 10_800_000L);
        //Date dt = new Date(System.currentTimeMillis() + 10800000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String s = sdf.format(da1);
        String s1 = sdf.format(dd1);
        String h1 = "Start task: " + s + "\n" + "Next task: " + s1;
        String s0 = "Task 1: completed.";
        String s03 = "Task 1: running.";
        String s01 = "Task 2: completed.";
        String t2r = "Task 2: running.";
        String s02 = "Task 3: completed.";
        String t3r = "Task 3: running.";
        String t4r = "Task 4: running.";
        ++i1;
        String s8;
        if (i1 == 1) {
            s8 = sdf.format(dd1);
            Date d5 = new Date(System.currentTimeMillis() + 21600000L);
            this.s5 = sdf.format(d5);
            Date d6 = new Date(System.currentTimeMillis() + 32400000L);
            this.s6 = sdf.format(d6);
            Date d7 = new Date(System.currentTimeMillis() + 43200000L);
            this.s7 = sdf.format(d7);
            this.h2 = s03 + "\n" + "Task 2: " + s8 + "\n" + "Task 3: " + this.s5 + "\n" + "Task 4: " + this.s6 + "\n" + "Task 5: " + this.s7;
        }
        if (i1 == 2) {
            this.h2 = s0 + "\n" + t2r + "\n" + "Task 3: " + this.s5 + "\n" + "Task 4: " + this.s6 + "\n" + "Task 5: " + this.s7;
        }
        if (i1 == 3) {
            this.h2 = s0 + "\n" + s01 + "\n" + t3r + "\n" + "Task 4: " + this.s6 + "\n" + "Task 5: " + this.s7;
        }
        if (i1 == 4) {
            this.h2 = s0 + "\n" + s01 + "\n" + s02 + "\n" + t4r + "\n" + "Task 5: " + this.s7;
        }
//        if (i1 == 5) {
//            this.h2 = s0 + "\n" + s01 + "\n" + s02 + "\n" + "Task 4: completed." + "\n" + "Task 5: completed";
//        }
        this.abtn.setDisable(true);
        this.hbtn.setDisable(true);
        this.reset.setDisable(false);
        s8 = "Look at schedule.\n" + h1 + "\n" + "\n" + this.h2;
        this.ta.setText(s8);
        Date da = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH");
        String s9 = sdf1.format(da);
        //System.out.println(s9);
        if (s9.equals("21") || s9.equals("22") || s9.equals("23")) {
            this.ta.setText(" \n\n\n     After hour 21 : \n" + "    application will be closed. \n");
        }
    }

    private void finalNote() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("E dd_MM_yyyy '-' a HH:mm");
        String s = sdf.format(d);
        String s1 = "Timing completed!.\n\n" + s;
        ta.setText(s1);
        ta.setStyle("-fx-font-size: 18;-fx-text-fill: green;");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ta.setStyle("-fx-font-size: 18;-fx-text-fill: indigo;");
        dScreen();
    }
}
