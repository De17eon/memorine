package com.example.animememorine;

public class Card {
    private final int imgIndex;
    private final int startX;
    private final int startY;
    private final int endX;
    private final int endY;
    private int shirt;
    private boolean isOpen;

    public Card(int imageIndex, int[] bounds) {
        this.imgIndex = imageIndex;
        this.shirt = R.drawable.i0;
        this.startX = bounds[0];
        this.startY = bounds[1];
        this.endX = bounds[2];
        this.endY = bounds[3];
        this.isOpen = false;
    }

    public boolean isTouch(int x, int y){
        return  x > startX & x < endX & y > startY & y < endY;
    }
    public boolean isOpened() {
        return isOpen;
    }
    public void close() {
        isOpen = false;
        shirt = R.drawable.i0;
    }
    public void open() {
        isOpen = true;
        shirt = imgIndex;
    }
    public int getImgIndex() {
        return imgIndex;
    }
    public int getStartX() {
        return startX;
    }
    public int getStartY() {
        return startY;
    }
    public int getEndX() {
        return endX;
    }
    public int getEndY() {
        return endY;
    }
    public int getShirt() {
        return shirt;
    }
}
