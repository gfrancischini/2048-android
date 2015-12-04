package br.francischini.a2048.ui.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import java.util.List;

import br.francischini.a2048.R;
import br.francischini.a2048.game.Grid;
import br.francischini.a2048.game.Tile;

/**
 * Created by gabriel on 12/2/15.
 */
public class BoardView extends RelativeLayout {
    boolean boardCreated = false;
    int tileSize;
    RectF gameboardRect;
    int tileMargin;
    int numOfColumns = 4;
    int numOfLines = 4;

    public BoardView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
        tileSize = Math.round(getResources().getDimension(R.dimen.tile_size));
        tileMargin = 10;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!boardCreated) {
            determineGameboardSizes();
            boardCreated = true;
        }
    }

    protected void determineGameboardSizes() {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int gameboardWidth = (tileSize + tileMargin) * 4;
        int gameboardHeight = (tileSize + tileMargin) * 4;
        int gameboardTop = 0;//viewHeight/2 - gameboardHeight/2;
        int gameboardLeft = 0;//viewWidth/2 - gameboardWidth/2;
        gameboardRect = new RectF(gameboardLeft, gameboardTop, gameboardLeft + gameboardWidth, gameboardTop + gameboardHeight);
    }

    public void update(Grid grid) {
        //List<Tile> tiles = grid.occupiedCells();
        //this.removeAllViews();

        Tile[][] cells = grid.getAllCells();
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                Tile tile = cells[x][y];
                if (tile != null) {
                    placeTile(tile, x, y);
                }
            }
        }
    }

    protected Rect rectForCoordinate(int x, int y) {
        int gameboardY = (int) Math.floor(gameboardRect.top);
        int gameboardX = (int) Math.floor(gameboardRect.left);

        int top = (x * tileSize) + gameboardY + (x) * tileMargin;
        int left = (y * tileSize) + gameboardX + (y) * tileMargin;
        return new Rect(left, top, left + tileSize, top + tileSize);
    }

    private int getPieceColor(int value) {
        switch (value) {
            case 2:
                return getResources().getColor(R.color.piece_2);
            case 4:
                return getResources().getColor(R.color.piece_4);
            case 8:
                return getResources().getColor(R.color.piece_8);
            case 16:
                return getResources().getColor(R.color.piece_16);
            case 32:
                return getResources().getColor(R.color.piece_32);
            case 64:
                return getResources().getColor(R.color.piece_64);
            case 128:
                return getResources().getColor(R.color.piece_128);
            case 256:
                return getResources().getColor(R.color.piece_256);
            case 512:
                return getResources().getColor(R.color.piece_512);
            case 1024:
                return getResources().getColor(R.color.piece_1024);
            case 2048:
                return getResources().getColor(R.color.piece_2048);
            default:
                return getResources().getColor(R.color.piece_super);
        }
    }

    private void animateTile(Tile tile, Animator.AnimatorListener animatorListener) {
        TileView tileView = (TileView) this.findViewWithTag(tile.getId());


        Rect tileRect = rectForCoordinate(tile.getX(), tile.getY());
        ObjectAnimator animX = ObjectAnimator.ofFloat(tileView, "translationX", tileRect.left);
        ObjectAnimator animY = ObjectAnimator.ofFloat(tileView, "translationY", tileRect.top);

        animX.start();
        animY.start();

        AnimatorSet animatorSet = new AnimatorSet();

        if (animatorListener != null) {
            animatorSet.addListener(animatorListener);
        }
        animatorSet.setDuration(100);
        animatorSet.playTogether(animX, animY);
        animatorSet.start();
    }

    private void playNewMergedTileAnimation(TileView tileView) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(tileView, "scaleX", 1f);
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(tileView, "scaleY", 1f);

        AnimatorSet animatorSetScaleDown = new AnimatorSet();
        animatorSetScaleDown.setDuration(50);
        animatorSetScaleDown.playTogether(scaleDownX, scaleDownY);

        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(tileView, "scaleX", 1.1f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(tileView, "scaleY", 1.1f);

        AnimatorSet animatorSetScaleUp = new AnimatorSet();
        animatorSetScaleUp.setDuration(50);
        animatorSetScaleUp.playTogether(scaleUpX, scaleUpY);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorSetScaleUp).before(animatorSetScaleDown);
        animatorSet.start();

    }

    private void playNewTileAnimation(TileView tileView) {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(tileView, "scaleX", 0f, 1.f);
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(tileView, "scaleY", 0f, 1.f);
        ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(tileView, View.ALPHA, 0,1);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(100);
        animatorSet.setStartDelay(100);
        animatorSet.playTogether(alphaAnimation, scaleUpX, scaleUpY);


        animatorSet.start();
    }

    private void playMergeAnimation(final Tile newTile, final Tile merge1, final Tile merge2) {
        Animator.AnimatorListener animatorListener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                TileView tileView1 = (TileView) findViewWithTag(merge1.getId());
                TileView tileView2 = (TileView) findViewWithTag(merge2.getId());

                // apply only the first time as merged1 and merged2 can change position at the same time
                if (tileView1 != null && tileView2 != null) {
                    removeView(tileView2);
                    removeView(tileView1);
                    TileView tileView = createNewTileView(newTile, newTile.getX(), newTile.getY());
                    addView(tileView);
                    playNewMergedTileAnimation(tileView);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        };


        if (merge1.hasChangedPosition()) {
            animateTile(merge1, animatorListener);
        }

        if (merge2.hasChangedPosition()) {
            animateTile(merge2, animatorListener);
        }

    }

    protected void placeTile(Tile tile, int x, int y) {
        if (tile.getMergedFrom() != null) {
            // we are merging an tile
            List<Tile> tiles = tile.getMergedFrom();

            playMergeAnimation(tile, tiles.get(0), tiles.get(1));
            return;
        }

        if (tile.getPreviousPosition() != null) {
            // we need to move this tile
            animateTile(tile, null);
            return;
        }


        // check if its a new tile or we just dont need to move it
        TileView tileView = (TileView) this.findViewWithTag(tile.getId());
        if (tileView == null) {
            tileView = createNewTileView(tile, x, y);
            tileView.setAlpha(0);
            addView(tileView);
            playNewTileAnimation(tileView);
        }
    }

    private TileView createNewTileView(Tile tile, int x, int y) {
        // we need to create a new tile view
        TileView tileView = new TileView(getContext());


        if (tile != null) {
            tileView.setText("" + tile.getValue());
            tileView.setColor(getPieceColor(tile.getValue()));
        } else {
            tileView.setText("");
            tileView.setColor(getResources().getColor(R.color.piece_empty));
        }
        Rect tileRect = rectForCoordinate(x, y);
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSize, tileSize);
        tileView.setX(tileRect.left);
        tileView.setY(tileRect.top);

        tileView.setTag(tile.getId());


        tileView.setLayoutParams(params);
        return tileView;
        /*if (delay == 0) {
            addView(tileView, params);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addView(tileView, params);
                }
            }, delay);
        }*/

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int leftPadding = ((ViewGroup) this).getPaddingLeft();
        int rightPadding = ((ViewGroup) this).getPaddingRight();
        int topPadding = ((ViewGroup) this).getPaddingTop();
        int bottomPadding = ((ViewGroup) this).getPaddingBottom();

        int desiredWidth = (tileSize + tileMargin) * 4 + leftPadding + rightPadding;
        int desiredHeight = (tileSize + tileMargin) * 4 + topPadding + bottomPadding;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        int widthspec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        int heightspec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        //setMeasuredDimension(width, height);


        super.onMeasure(widthspec, heightspec);
        int wspec = MeasureSpec.makeMeasureSpec(tileSize, MeasureSpec.EXACTLY);
        int hspec = MeasureSpec.makeMeasureSpec(tileSize, MeasureSpec.EXACTLY);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(wspec, hspec);
        }
    }
}
