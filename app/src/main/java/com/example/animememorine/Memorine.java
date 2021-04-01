package com.example.animememorine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Memorine extends View {
    boolean isStart;
    int padding; // Отступ между картами
    boolean isOpenedCardNow = false;
    boolean isOnPauseNow = false;
    List<Card> cardList = new ArrayList<>();

    public Memorine(Context context) {
        super(context);
        this.isStart = true;
        this.padding = 40;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {

        if (isStart) {
            // Количество карт пользователь не может изменить, поэтому хардкодинг.
            List<Integer> ids = new ArrayList<>();
            for (int i=0; i<16; i++) {
                ids.add((i % 8)+1);
            }
            Collections.shuffle(ids);

            float width = getWidth();
            float cardWidth = (width-padding)/4;
            float cardHeight = cardWidth * (float) 1.4;

            for (int i=0; i<4; i++) {
                for (int j=0; j<4; j++) {
                    int[] bounds = {
                            (int) (i*cardWidth) + padding,
                            (int) (j*cardHeight) + padding,
                            (int) ((i+1)*cardWidth),
                            (int) ((j+1)*cardHeight)
                    };
                    int id = getResources().getIdentifier("i"+ids.remove(0), "drawable", getContext().getPackageName());
                    Log.d("MYTAG", id+"");
                    Card card = new Card(id, bounds);
                    cardList.add(card);
                }
            }
        }
        isStart = false;

        for (Card card:cardList) {
            viewImage(card, canvas);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN & !isOnPauseNow) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            for (Card card:cardList) {
                if (card.isTouch(x, y)) {
                    if (!card.isOpened()) {
                        card.open();
                        invalidate();

                        if (!isOpenedCardNow) {
                            isOpenedCardNow = true;
                            return true;
                        }

                        PauseTask task = new PauseTask();
                        task.execute(5000);
                        isOpenedCardNow = false;
                    }
                    break;
                }
            }
        }
        invalidate();
        return true;
    }

    public void viewImage(Card card, Canvas canvas) {
        @SuppressLint("UseCompatLoadingForDrawables")
        Drawable img = getResources().getDrawable(card.getShirt());
        img.setBounds(card.getStartX(), card.getStartY(), card.getEndX(), card.getEndY());
        img.draw(canvas);
    }

    public void finishGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Вы выйграли")
                .setPositiveButton(
                        "Начать заново",
                        (dialog, id) -> {
                            isStart = true;
                            dialog.cancel();
                            invalidate();
                        }
                )
                .create()
                .show();
    }

    public class PauseTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            isOnPauseNow = true;
            try {
                Thread.sleep(integers[0]);
            } catch (InterruptedException ignored) {}
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            List<Card> duplicates = new ArrayList<>();
            for (Card card: cardList) {
                if (card.isOpened()) {
                    duplicates.add(card);
                    card.close();
                }
            }
            if(duplicates.size() == 2)
                if(duplicates.get(0).getImgIndex() == duplicates.get(1).getImgIndex()){
                    cardList.remove(duplicates.get(0));
                    cardList.remove(duplicates.get(1));
                }
            if(cardList.size() == 0){
                finishGame();
            }
            isOnPauseNow = false;
            invalidate();
        }
    }
}
