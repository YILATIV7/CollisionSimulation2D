package com.example.collision_sim_2d;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MyApplication extends Application {

    public static final Random RANDOMIZER = new Random(555);
    public static final int WIDTH = 800, HEIGHT = 600;

    private final List<Rect> rectList;

    public MyApplication() {
        rectList = new ArrayList<>();
    }

    @Override
    public void start(Stage stage) {
        for (int i = 0; i < 100; i++) {
            rectList.add(new Rect(
                    RANDOMIZER.nextInt(WIDTH - Rect.WIDTH),
                    RANDOMIZER.nextInt(HEIGHT - Rect.HEIGHT),
                    generateVector()
            ));
        }

        AnchorPane root = new AnchorPane(rectList.toArray(new Node[0]));

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        stage.setTitle("Collision simulation");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Timeline tl = new Timeline(new KeyFrame(Duration.millis(20), e -> update()));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private void update() {
        rectList.forEach(rect -> rect.move(0.02));

        for (Rect r1 : rectList) {
            for (Rect r2 : rectList) {
                if (r1 == r2) continue;

                if (r1.collideWith(r2) && r1.approachTo(r2)) {
                    bounce(r1, r2);
                }
            }
        }
    }

    private void bounce(Rect r1, Rect r2) {
        double x1 = r1.getX(), y1 = r1.getY(), x2 = r2.getX(), y2 = r2.getY();
        double dx1 = r1.getMoveVector()[0], dy1 = r1.getMoveVector()[1];
        double dx2 = r2.getMoveVector()[0], dy2 = r2.getMoveVector()[1];

        double[] v1 = { dx1, dy1 };
        double[] v2 = { dx2, dy2 };

        double[] p1_to_p2Vector = { x2 - x1, y2 - y1 };
        double[] p2_to_p1Vector = { x1 - x2, y1 - y2 };
        double[] pv1 = projection(v1, p1_to_p2Vector);
        double[] pv2 = projection(v2, p2_to_p1Vector);

        r1.setMoveVector(normalize(new double[]{
                v1[0] + pv2[0] - pv1[0],
                v1[1] + pv2[1] - pv1[1],
        }));
        r2.setMoveVector(normalize(new double[]{
                v2[0] + pv1[0] - pv2[0],
                v2[1] + pv1[1] - pv2[1]
        }));

    }

    private double[] projection(double[] v, double[] p) {
        double len = Math.sqrt(p[0] * p[0] + p[1] * p[1]);
        double projectionLen = (v[0] * p[0] + v[1] * p[1]) / len;

        return new double[]{
                projectionLen * p[0] / len,
                projectionLen * p[1] / len
        };
    }

    private double[] normalize(double[] v) {
        double len = Math.sqrt(v[0] * v[0] + v[1] * v[1]);

        return new double[]{
                v[0] / len,
                v[1] / len
        };
    }

    private static double[] generateVector() {
        int degrees = RANDOMIZER.nextInt(360);
        double[] moveVector = {Math.cos(degrees), Math.sin(degrees)};
        if (RANDOMIZER.nextInt(2) == 0)
            moveVector[1] *= -1;
        return moveVector;
    }

    public static void main(String[] args) {
        launch();
    }
}