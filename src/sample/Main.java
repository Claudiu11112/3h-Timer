package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Objects;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class Main extends Application {
    private static final int PORT = 12546;
    private TrayIcon ti;
    private boolean b;
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    @Override
    public void start(Stage ps) throws Exception {
        stage = ps;
        this.b = true;
        Platform.setImplicitExit(false);
        ps.setTitle("3h Timer");
        Parent root = FXMLLoader.load(Objects.requireNonNull(this.getClass().getResource("sample.fxml")));
        ps.setScene(new Scene(root, 539.0, 311.0));
        ps.setResizable(false);
        ps.getIcons().add(new Image("/sample/timer.png"));
        this.createI(ps);
        ps.show();
//        System.out.println(System.getProperties());
    }

    public static void main(String[] args) {
        try {
            // new ServerSocket(PORT, 10, InetAddress.getLocalHost());
            ServerSocket ss = new ServerSocket(PORT, 10, InetAddress.getLocalHost());
            System.out.println(ss);
            launch(args);
        } catch (Exception e) {
            int i = JOptionPane.showConfirmDialog(null, "The program is already running.",
                    "Information", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (i == 0) {
//                e.printStackTrace();
                log.print("Ops!");
                System.out.println("The program is already running");
                System.exit(0);
            }
        }
    }

    private void createI(Stage ps) {
        if (SystemTray.isSupported()) {
            SystemTray st = SystemTray.getSystemTray();
            BufferedImage bi = null;

            try {
                // URL url = System.class.getResource("/sample/16x16-t-icon.png");
                bi = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/sample/16x16-t-icon.png")));
            } catch (IOException ioe) {
                System.err.println("Icon it is not found");
            }
            ps.setOnCloseRequest((e) -> this.hide(ps));
            PopupMenu pm = new PopupMenu();
            MenuItem mi = new MenuItem("Open Main Program");
            pm.add(mi);
            ActionListener al = (e) -> Platform.runLater(ps::show);
            mi.addActionListener(al);
            MenuItem mi1 = new MenuItem("Exit");
            pm.add(mi1);
            ActionListener closeL = (e) -> {
                System.gc();
                Platform.exit();
                System.exit(0);
            };
            mi1.addActionListener(closeL);

            if (bi != null) {
                this.ti = new TrayIcon(bi, "Timer", pm);
            }
            this.ti.addActionListener(al);
            try {
                st.add(this.ti);
            } catch (AWTException e) {
//                e.printStackTrace();
                log.print("Ops!");
            }
        }
    }

    private void hide(Stage ps) {
        Platform.runLater(() -> {
            if (SystemTray.isSupported()) {
                ps.hide();
                this.minimizedMsg();
            } else {
                System.exit(0);
            }

        });
    }

    private void minimizedMsg() {
        if (this.b) {
            this.ti.displayMessage(null, "Timer app.", MessageType.INFO);
            this.b = false;
        }
    }
}
