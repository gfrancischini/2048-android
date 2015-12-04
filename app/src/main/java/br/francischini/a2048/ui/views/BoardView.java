package br.francischini.a2048.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

        Tile[][] cells = grid.getAllCells();
        for (int x = 0; x < grid.getSize(); x++) {
            for (int y = 0; y < grid.getSize(); y++) {
                Tile tile = cells[x][y];
                placeTile(tile, x, y);
            }
        }
    }

    protected Rect rectForCoordinate(int x, int y) {
        int gameboardY = (int) Math.floor(gameboardRect.top);
        int gameboardX = (int) Math.floor(gameboardRect.left);

        int top = (x * tileSize) + gameboardY + (x)*tileMargin;
        int left = (y * tileSize) + gameboardX + (y)*tileMargin;
        return new Rect(left, top, left + tileSize, top + tileSize);
    }

    private int getPieceColor(int value) {
        switch (value) {
            case 2: return getResources().getColor(R.color.piece_2);
            case 4: return getResources().getColor(R.color.piece_4);
            case 8: return getResources().getColor(R.color.piece_8);
            case 16: return getResources().getColor(R.color.piece_16);
            case 32: return getResources().getColor(R.color.piece_32);
            case 64: return getResources().getColor(R.color.piece_64);
            case 128: return getResources().getColor(R.color.piece_128);
            case 256: return getResources().getColor(R.color.piece_256);
            case 512: return getResources().getColor(R.color.piece_512);
            case 1024: return getResources().getColor(R.color.piece_1024);
            case 2048: return getResources().getColor(R.color.piece_2048);
            default: return getResources().getColor(R.color.piece_super);
        }
    }

    protected void placeTile(Tile tile, int x, int y) {
        TileView tileView = new TileView(getContext());

        if(tile != null) {
            tileView.setText("" + tile.getValue());
            tileView.setBackgroundResource(R.drawable.round_rect);
            //tileView.setBackgroundColor(getPieceColor(tile.getValue()));
            tileView.getBackground().setColorFilter(getPieceColor(tile.getValue()), PorterDuff.Mode.ADD);
        }
        else {
            tileView.setText("");
            tileView.setBackgroundResource(R.drawable.round_rect);
            //tileView.setBackgroundColor(getResources().getColor(R.color.piece_empty));
            tileView.getBackground().setColorFilter(getResources().getColor(R.color.piece_empty), PorterDuff.Mode.ADD);
        }
        Rect tileRect = rectForCoordinate(x, y);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSize, tileSize);
        //params.topMargin = tileRect.top;
        //params.leftMargin = tileRect.left;
        //params.setMargins(50, 50, 50, 50);

        tileView.setX(tileRect.left);
        tileView.setY(tileRect.top);
        addView(tileView, params);
        //tile.setImageBitmap(tileServer.serveRandomSlice());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int leftPadding = ((ViewGroup) this).getPaddingLeft();
        int rightPadding = ((ViewGroup) this).getPaddingRight();
        int topPadding = ((ViewGroup) this).getPaddingTop();
        int bottomPadding = ((ViewGroup) this).getPaddingBottom();

        int desiredWidth = (tileSize + tileMargin)*4 + leftPadding + rightPadding;
        int desiredHeight = (tileSize + tileMargin)*4 + topPadding + bottomPadding;

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
        for(int i=0; i<getChildCount(); i++){
            View v = getChildAt(i);
            v.measure(wspec, hspec);
        }
    }
}
