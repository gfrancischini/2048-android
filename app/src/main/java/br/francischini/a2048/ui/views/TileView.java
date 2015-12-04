package br.francischini.a2048.ui.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import br.francischini.a2048.R;

/**
 * Created by gabriel on 12/2/15.
 */
public class TileView extends RelativeLayout {
    private TextView textView;

    public TileView(Context context) {
        super(context);
        init();
    }

    public void init() {
        inflate(getContext(), R.layout.module_tile, this);

        textView = (TextView)this.findViewById(R.id.textView);
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setColor(int color) {
        textView.getBackground().setColorFilter(color, PorterDuff.Mode.ADD);
    }

}
