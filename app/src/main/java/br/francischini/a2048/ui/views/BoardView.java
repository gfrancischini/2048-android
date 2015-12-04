package br.francischini.a2048.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
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

    public BoardView(Context context, AttributeSet attrSet) {
        super(context, attrSet);
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
        tileSize = Math.round(getResources().getDimension(R.dimen.tile_size));
        int gameboardWidth = tileSize * 4;
        int gameboardHeight = tileSize * 4;
        int gameboardTop = viewHeight/2 - gameboardHeight/2;
        int gameboardLeft = viewWidth/2 - gameboardWidth/2;
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
        int top = (x * tileSize) + gameboardY;
        int left = (y * tileSize) + gameboardX;
        return new Rect(left, top, left + tileSize, top + tileSize);
    }

    protected void placeTile(Tile tile, int x, int y) {
        TileView tileView = new TileView(getContext());

        if(tile != null) {
            tileView.setText("" + tile.getValue());
            tileView.setBackgroundColor(Color.WHITE);
        }
        else {
            tileView.setText("");
            tileView.setBackgroundColor(Color.BLACK);
        }
        Rect tileRect = rectForCoordinate(x, y);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(tileSize, tileSize);
        params.topMargin = tileRect.top;
        params.leftMargin = tileRect.left;
        params.setMargins(tileRect.left + 50, tileRect.top + 50, 50, 50);

        addView(tileView, params);
        //tile.setImageBitmap(tileServer.serveRandomSlice());
    }
}
