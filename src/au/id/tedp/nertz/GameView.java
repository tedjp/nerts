package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

import android.util.Log;

class GameView extends View {
    public GameView(android.content.Context context, Player player) {
        super(context);
        cachedCanvasX = -1;
        cachedCanvasY = -1;
        cachedXSep = -1;
        cachedYSep = -1;
        this.player = player;
        this.res = getResources();
    }

    private int cachedCanvasX, cachedCanvasY;
    private int cachedXSep, cachedYSep;

    private Player player;

    private Resources res;

    /**
     * Return a Rect for the destination card's location on the given Canvas,
     * x &amp; y position, and card dimensions.
     */
    private Rect cardPosition(Canvas canvas, int x, int y, int cardWidth, int cardHeight) {
        Rect bounds = canvas.getClipBounds();
        int xsep = -1, ysep = -1;

        if (cachedCanvasX == bounds.right && cachedCanvasY == bounds.bottom) {
            xsep = cachedXSep;
            ysep = cachedYSep;
        } else {
            ysep = (bounds.bottom - cardHeight * 2) / 3;
            xsep = (bounds.right - cardWidth * 6) / 7;
            cachedXSep = xsep;
            cachedYSep = ysep;
        }

        Rect position = new Rect();
        position.top = ysep + (ysep + cardHeight) * y;
        position.bottom = position.top + cardHeight;
        position.left = xsep + (xsep + cardWidth) * x;
        position.right = position.left + cardWidth;
        return position;
    }

    protected void drawNertzPile(Canvas canvas) {
        NertzPile nertzPile = player.getNertzPile();
        if (nertzPile.isEmpty())
            return;

        BitmapDrawable nertzPileDrawable = nertzPile.topCardImage(res);

        int cardHeight, cardWidth;
        cardHeight = nertzPileDrawable.getIntrinsicHeight();
        cardWidth = nertzPileDrawable.getIntrinsicWidth();
        Rect dest = cardPosition(canvas, 0, 1, cardWidth, cardHeight);
        canvas.drawBitmap(nertzPileDrawable.getBitmap(), null, dest, null);
    }

    protected void drawRiver(Canvas canvas) {
        ArrayList<TableauPile> river = player.getRiver();

        int pilenum = 0;
        for (TableauPile pile : river) {
            if (!pile.isEmpty()) {
                BitmapDrawable topCard = pile.topCardImage(res);
                int cardHeight = topCard.getIntrinsicHeight();
                int cardWidth = topCard.getIntrinsicWidth();
                Rect dest = cardPosition(canvas, 1 + pilenum, 1,
                        cardWidth, cardHeight);
                canvas.drawBitmap(topCard.getBitmap(), null, dest, null);
            }
            ++pilenum;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 128, 0);

        drawNertzPile(canvas);
        drawRiver(canvas);
    }
}
