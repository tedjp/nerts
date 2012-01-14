package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Paint;
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

    protected void drawStream(Canvas canvas, Stream stream) {
        if (!stream.isFaceDownEmpty()) {
            BitmapDrawable cardBack = DeckGraphics.getCardBack(res);
            Rect dest = cardPosition(canvas, 5, 0);
            canvas.drawBitmap(cardBack.getBitmap(), null, dest, null);
        }

        if (!stream.isFaceUpEmpty()) {
            BitmapDrawable drawable = stream.topCardImage(res);
            Rect dest = cardPosition(canvas, 5, 1);
            canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
        }
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

    protected void drawLake(Canvas canvas) {
        Lake lake = player.getLake();

        int pilenum = 0;
        for (Pile pile : lake.getPiles()) {
            drawPile(canvas, pile, 1 + pilenum, 0);
            ++pilenum;
        }
    }

    protected Rect nertzPileArea, riverArea, streamArea, lakeArea, oppArea;

    private void calculateAreas() {
        int height = getHeight(), width = getWidth();
        int hs = height / 2;
        // XXX: Might want to cache these.
        nertzPileArea = new Rect(0, hs, width / 6, height);
        riverArea = new Rect(nertzPileArea.right, hs, width / 4 * 3, height);
        streamArea = new Rect(riverArea.right, hs, width, height);
        lakeArea = new Rect(0, height / 6, width, hs);
        oppArea = new Rect(0, 0, width, lakeArea.top);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateAreas();

        //canvas.drawARGB(255, 0, 128, 0);
        canvas.drawColor(0xff008000);

        Paint paint = new Paint();

        paint.setColor(0xff800000);
        canvas.drawRect(nertzPileArea, paint);
        paint.setColor(0xff0000ff);
        canvas.drawRect(riverArea, paint);
        paint.setColor(0xff000080);
        canvas.drawRect(streamArea, paint);
        paint.setColor(0xffff0000);
        canvas.drawRect(lakeArea, paint);
        paint.setColor(0xff00ff00);
        canvas.drawRect(oppArea, paint);

        cardWidth = getWidth() / 11;
        cardHeight = getHeight() / 4;

        drawNertzPile(canvas);
        drawRiver(canvas);
        drawStream(canvas, player.getStream());
        drawLake(canvas);
    }
}
