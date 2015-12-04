package br.francischini.a2048.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import br.francischini.a2048.R;
import br.francischini.a2048.game.Manager;
import br.francischini.a2048.ui.views.BoardView;

public class GameActivity extends AppCompatActivity {
    Manager manager;
    BoardView boardView;
    RelativeLayout rootRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        manager = new Manager(4);

        boardView = (BoardView)this.findViewById(R.id.gameboard);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boardView.update(manager.getGrid());
            }
        }, 2000);


        rootRelativeLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            // TODO: this is screw .. need to make it again
            // 0: up, 1: right, 2: down, 3: left
            public void onSwipeTop() {
                manager.move(3);
                boardView.update(manager.getGrid());
            }
            public void onSwipeRight() {
                manager.move(2);
                boardView.update(manager.getGrid());
            }
            public void onSwipeLeft() {
                manager.move(0);
                boardView.update(manager.getGrid());
            }
            public void onSwipeBottom() {
                manager.move(1);
                boardView.update(manager.getGrid());
            }

            public boolean onTouch(View v, MotionEvent event) {
                return this.gestureDetector.onTouchEvent(event);
            }
        });


    }
}
