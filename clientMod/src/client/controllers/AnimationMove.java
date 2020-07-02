package client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class AnimationMove {

    private PathTransition pathTransition;

    public AnimationMove (Group group, double x, double y, double xFr, double yFr) {
        Path path = new Path( );
        path.getElements( ).add(new MoveTo(xFr, yFr));
        path.getElements().add(new LineTo(x, y));

        pathTransition = new PathTransition();
        pathTransition.setDuration(Duration.millis(5000));
        pathTransition.setNode(group);
        pathTransition.setPath(path);
    }

    public void playAnim ( ) {
        System.out.println(1);
        pathTransition.playFromStart( );
    }
}
