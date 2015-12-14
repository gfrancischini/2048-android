package br.francischini.a2048.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.francischini.a2048.R;
import br.francischini.a2048.game.Manager;
import br.francischini.a2048.ui.views.BoardView;

public class GameActivity extends AppCompatActivity {
    Manager manager;
    BoardView boardView;
    RelativeLayout rootRelativeLayout;
    TextView scoreValueTextView;
    TextView bestValueTextView;
    Button newGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        manager = new Manager(4);

        boardView = (BoardView) this.findViewById(R.id.gameboard);
        rootRelativeLayout = (RelativeLayout) this.findViewById(R.id.rootRelativeLayout);
        scoreValueTextView = (TextView) this.findViewById(R.id.scoreValueTextView);
        bestValueTextView = (TextView) this.findViewById(R.id.bestValueTextView);
        newGameButton = (Button) this.findViewById(R.id.newGameButton);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boardView.createGameTiles(manager.getGrid());
                updateScores();
            }
        }, 2000);


        rootRelativeLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            // TODO: this is screw .. need to make it again
            // 0: up, 1: right, 2: down, 3: left
            public void onSwipeTop() {
                doPlay(3);
            }

            public void onSwipeRight() {
                doPlay(2);
            }

            public void onSwipeLeft() {
                doPlay(0);
            }

            public void onSwipeBottom() {
                doPlay(1);
            }

            public boolean onTouch(View v, MotionEvent event) {
                return this.gestureDetector.onTouchEvent(event);
            }
        });


        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.restart();
                boardView.createGameTiles(manager.getGrid());
            }
        });

    }

    private void doPlay(int direction) {
        manager.move(direction);
        boardView.update(manager.getGrid());
        updateScores();
    }

    private void updateScores() {
        scoreValueTextView.setText(String.valueOf(manager.getScore()));
        bestValueTextView.setText(String.valueOf(manager.getBestScore()));
    }
}
