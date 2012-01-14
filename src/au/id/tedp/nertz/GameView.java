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

    private int cardHeight, cardWidth;

    /**
     * Return a Rect for the destination card's location on the given Canvas,
     * x &amp; y position, and card dimensions.
     */
    private Rect cardPosition(Canvas canvas, int x, int y) {
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

    protected void drawPile(Canvas canvas, Pile pile, int x, int y) {
        BitmapDrawable drawable = pile.topCardImage(res);

        Rect dest = cardPosition(canvas, x, y);
        canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
    }

    protected void drawNertzPile(Canvas canvas) {
        NertzPile nertzPile = player.getNertzPile();
        if (nertzPile.isEmpty())
            return;

        drawPile(canvas, nertzPile, 0, 1);
    }

    protected void drawRiver(Canvas canvas) {
        ArrayList<TableauPile> river = player.getRiver();

        int pilenum = 0;
        for (TableauPile pile : river) {
            if (!pile.isEmpty())
                drawPile(canvas, pile, 1 + pilenum, 1);
            ++pilenum;
        }
    }

    protected void drawStream(Canvas canvas) {
        Stream stream = player.getStream();

        if (stream.isEmpty())
            return;

        drawPile(canvas, stream, 5, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 128, 0);

        cardWidth = getWidth() / 10;
        cardHeight = (int)((double)getHeight() / (double)3.5);

        drawNertzPile(canvas);
        drawRiver(canvas);
        drawStream(canvas);
    }
}
