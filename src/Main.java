import Modele.Jeu;
import Modele.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Classe main
 * C'est dans cette classe que le programme démarre et instancie un objet Jeu
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        Jeu j = new Jeu(primaryStage);
        Timeline t = new Timeline("timeline", j);
        primaryStage.setOnCloseRequest(event->{
            t.setEnd(true);
        });
    }

    public static void main(String[] args) { launch(args); }
}
