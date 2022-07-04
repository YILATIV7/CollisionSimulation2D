package com.example.collision_sim_2d;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Rect extends Rectangle {

    public static final int WIDTH = 40, HEIGHT = 40;

    private double[] moveVector;

    public Rect(double x, double y, double[] moveVector) {
        super(WIDTH, HEIGHT, Color.RED);
        this.moveVector = moveVector;
        setX(x); setY(y);
    }

    public double[] getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(double[] moveVector) {
        this.moveVector = moveVector;
    }

    public boolean collideWith(Rect other) {
        return getX() < other.getX() + WIDTH &&
                getX() + WIDTH > other.getX() &&
                getY() < other.getY() + HEIGHT &&
                HEIGHT + getY() > other.getY();
    }

    public boolean approachTo(Rect other) {
        double x1 = getX(), y1 = getY(), x2 = other.getX(), y2 = other.getY();
        double dx1 = getMoveVector()[0], dy1 = getMoveVector()[1];
        double dx2 = other.getMoveVector()[0], dy2 = other.getMoveVector()[1];

        double dist = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        double distNext = Math.sqrt(Math.pow(x1 + dx1 - x2 - dx2, 2) + Math.pow(y1 + dy1 - y2 - dy2, 2));

        return dist > distNext;
    }

    public void move(double dt) {
        final int speed = 20;
        double dx = speed * dt * moveVector[0];
        double dy = speed * dt * moveVector[1];
        setX(getX() + dx);
        setY(getY() + dy);
        invalidatePosition();
        //System.out.printf("[%.2f %.2f] ", moveVector[0], moveVector[1]);
        //System.out.printf("x: %d, y: %d", (int) getX(), (int) getY());
    }

    private void invalidatePosition() {
        if (getX() < 0) {
            setX(0);
            moveVector[0] *= -1;
        }
        if (getX() > MyApplication.WIDTH - WIDTH) {
            setX(MyApplication.WIDTH - WIDTH);
            moveVector[0] *= -1;
        }
        if (getY() < 0) {
            setY(0);
            moveVector[1] *= -1;
        }
        if (getY() > MyApplication.HEIGHT - HEIGHT) {
            setY(MyApplication.HEIGHT - HEIGHT);
            moveVector[1] *= -1;
        }
    }
}
