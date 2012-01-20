package au.id.tedp.nertz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import java.util.ArrayList;

import android.util.Log;

class GameView extends View implements View.OnTouchListener {
    public GameView(android.content.Context context, Player player) {
        super(context);
        cachedCanvasX = -1;
        cachedCanvasY = -1;
        cachedXSep = -1;
        cachedYSep = -1;
        this.player = player;
        this.res = getResources();
        setOnTouchListener(this);
        state = TouchState.NONE;
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

    protected void drawStream(Canvas canvas, Stream stream) {
        int areaWidth = streamArea.right - streamArea.left;
        int areaHeight = streamArea.bottom - streamArea.top;

        Rect dest = new Rect(0, streamArea.centerY() - cardHeight / 2, 0,
                streamArea.centerY() + cardHeight / 2);

        if (!stream.isFaceDownEmpty()) {
            BitmapDrawable cardBack = DeckGraphics.getCardBack(res);
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) * 2 / 3 + cardWidth;
            dest.right = dest.left + cardWidth;
            canvas.drawBitmap(cardBack.getBitmap(), null, dest, null);
        }

        if (!stream.isFaceUpEmpty()) {
            BitmapDrawable drawable = stream.topCardImage(res);
            dest.left = streamArea.left + (areaWidth - cardWidth * 2) / 3;
            dest.right = dest.left + cardWidth;
            canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
        }
    }

    protected void drawNertzPile(Canvas canvas) {
        NertzPile nertzPile = player.getNertzPile();
        if (nertzPile.isEmpty())
            return;

        BitmapDrawable drawable = nertzPile.topCardImage(res);
        int centerX = nertzPileArea.centerX();
        int centerY = nertzPileArea.centerY();
        Rect dest = new Rect(
                centerX - cardWidth / 2,
                centerY - cardHeight / 2,
                centerX + cardWidth / 2,
                centerY + cardHeight / 2);
        canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
    }

    protected void drawRiverPile(Canvas canvas, TableauPile pile, int pilenum) {
        int top = riverArea.top + (riverArea.bottom - riverArea.top) / 15;
        int sep = ((riverArea.right - riverArea.left) - cardWidth * 4) / 5;
        Rect dest = new Rect();
        dest.left = riverArea.left + (1 + pilenum) * sep + pilenum * cardWidth;
        dest.top = top;
        dest.right = dest.left + cardWidth;
        dest.bottom = dest.top + cardHeight;
        BitmapDrawable drawable = pile.topCardImage(res);
        canvas.drawBitmap(drawable.getBitmap(), null, dest, null);
    }

    protected void drawRiver(Canvas canvas) {
        ArrayList<TableauPile> river = player.getRiver();

        int pilenum = 0;
        for (TableauPile pile : river) {
            if (!pile.isEmpty())
                drawRiverPile(canvas, pile, pilenum);
            ++pilenum;
        }
    }

    public final int LAKE_PILES = 8;

    protected void drawLakePile(Canvas canvas, Pile pile, int pilenum) {
        BitmapDrawable bmp = pile.topCardImage(res);
        int top = lakeArea.centerY() - cardHeight / 2;
        int bottom = lakeArea.centerY() + cardHeight / 2;
        int sep = ((lakeArea.right - lakeArea.left) - cardWidth * LAKE_PILES) / 10;
        Rect dest = new Rect(0, top, 0, bottom);
        dest.left = lakeArea.left + cardWidth * pilenum + sep * (pilenum + 1);
        dest.right = dest.left + cardWidth;
        canvas.drawBitmap(bmp.getBitmap(), null, dest, null);
    }

    protected void drawLake(Canvas canvas) {
        Lake lake = player.getLake();

        int pilenum = 0;
        for (Pile pile : lake.getPiles()) {
            drawLakePile(canvas, pile, pilenum);
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

    private void drawLiveCard(Canvas canvas) {
        if (liveCard == null)
            return;

        BitmapDrawable bd = DeckGraphics.getBitmapDrawable(res, liveCard);
        RectF dest = new RectF(liveCardX - cardWidth / 2,
                liveCardY - cardHeight / 8 * 7,
                liveCardX + cardWidth / 2,
                liveCardY + cardHeight / 8);
        canvas.drawBitmap(bd.getBitmap(), null, dest, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calculateAreas();

        canvas.drawColor(0xff669900);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xff99cc00);

        canvas.drawRect(nertzPileArea, paint);
        canvas.drawRect(riverArea, paint);
        canvas.drawRect(streamArea, paint);
        canvas.drawRect(lakeArea, paint);
        canvas.drawRect(oppArea, paint);

        cardWidth = getWidth() / 11;
        cardHeight = getHeight() / 4;

        drawNertzPile(canvas);
        drawRiver(canvas);
        drawStream(canvas, player.getStream());
        drawLake(canvas);
        drawLiveCard(canvas);
    }

    protected enum TouchState {
        NONE,
        DRAG,
        DROP
    }

    protected enum Area {
        VOID,
        LAKE,
        RIVER,
        STREAM,
        NERTZ_PILE
    }

    private TouchState state;

    private Area detectArea(MotionEvent ev) {
        int x, y;

        x = (int) ev.getX();
        y = (int) ev.getY();

        if (streamArea.contains(x, y)) {
            return Area.STREAM;
        } else if (nertzPileArea.contains(x, y)) {
            return Area.NERTZ_PILE;
        } else if (riverArea.contains(x, y)) {
            return Area.RIVER;
        } else if (lakeArea.contains(x, y)) {
            return Area.LAKE;
        }
        return Area.VOID;
    }

    private Card liveCard;
    private Pile fromPile;
    private float liveCardX, liveCardY;
    //! The touch event began on the stream pile
    private boolean streamPileTouched;

    private Pile getRiverPile(float x, float y) {
        ArrayList<TableauPile> river = player.getRiver();
        float cardAreaWidth = riverArea.right - riverArea.left;
        int pileNum = (int) ((x - (float) riverArea.left) / (cardAreaWidth) * (float) river.size());
        Log.d("Nertz", "getRiverPile returning pile " + pileNum);
        return river.get(pileNum);
    }

    private void returnLiveCard() {
        if (liveCard == null || fromPile == null)
            return;

        try {
            fromPile.push(liveCard);
            liveCard = null;
        } catch (CardSequenceException e) {
            Log.e("Nertz", "Cannot put card " + liveCard.toString() + " back on its original pile!");
        }
    }

    private void handleStreamTouch(float x, float y) {
        Stream stream = player.getStream();

        if (x < (float)streamArea.centerX()) {
            // Waste pile
            if (!stream.isFaceUpEmpty())
                fromPile = stream;
        } else {
            // Stream pile (face down)
            streamPileTouched = true;
        }
    }

    private void handleStreamUp(float x, float y) {
        if (streamPileTouched &&
                x > (float)streamArea.centerX())
        {
            // Stream pile (face down)
            Stream stream = player.getStream();
            try {
                if (!stream.isFaceDownEmpty())
                    stream.flipThree();
                else
                    stream.restartPile();
            }
            catch (EmptyPileException e) {
                Log.e("Nertz", "Tried to handle stream touch, but stream was empty");
            }
        }
    }

    private Pile getLakePile(float x, float y) {
        Lake lake = player.getLake();
        int width = lakeArea.right - lakeArea.left;
        x -= (float) lakeArea.left;
        int pileNum = (int) (x / (float) width * (float) LAKE_PILES);
        return lake.getPiles().get(pileNum);
    }

    // XXX: This duplicates code from another implementation of getRiverPile().
    private Pile getRiverPile(float x, float y, Card card) {
        ArrayList<TableauPile> river = player.getRiver();
        int num = (int) ((x - ((float) riverArea.left)) / (float) (riverArea.right - riverArea.left) * river.size());
        TableauPile target = river.get(num);
        if (target.isValidMove(card))
            return target;
        return null;
    }

    // XXX: Provide getRiverPile(Card c) to allow flicking to the river?

    // Not sure if this should be part of some other class
    public boolean onTouch(View v, MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                fromPile = null;
                liveCard = null;
                streamPileTouched = false;

                liveCardX = ev.getX();
                liveCardY = ev.getY();

                switch (detectArea(ev)) {
                    case NERTZ_PILE:
                        fromPile = player.getNertzPile();
                        break;

                    case RIVER:
                        fromPile = getRiverPile(ev.getX(), ev.getY());
                        break;

                    case STREAM:
                        handleStreamTouch(ev.getX(), ev.getY());
                        break;
                }

                // Only pop a card if the pile was selected but nothing
                // has been popped from it yet.
                if (fromPile != null) {
                    junit.framework.Assert.assertNull(liveCard);
                    try {
                        if (!fromPile.isEmpty())
                            liveCard = fromPile.pop();
                    } catch (EmptyPileException e) {
                        Log.d("Nertz", "Tried to pop from empty pile!");
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                Pile toPile = null;
                switch (detectArea(ev)) {
                    case STREAM:
                        handleStreamUp(ev.getX(), ev.getY());
                        break;
                    case LAKE:
                        if (liveCard != null)
                            toPile = player.getLake().getSuitablePile(liveCard);
                        break;
                    case RIVER:
                        if (liveCard != null)
                            toPile = getRiverPile(ev.getX(), ev.getY(), liveCard);
                        break;
                }

                if (toPile != null) {
                    try {
                        toPile.push(liveCard);

                        try {
                            // Nertz Pile needs the top card flipped
                            // after successfully moving the live card
                            // from it.
                            if (fromPile == player.getNertzPile()
                                    && fromPile.isFaceUpEmpty() == true
                                    && fromPile.isFaceDownEmpty() == false)
                                fromPile.flipTopCard();
                        }
                        catch (EmptyPileException e) {
                            Log.e("Nertz", "Tried to flip top nertz pile card but pile was empty");
                        }
                        liveCard = null;
                        fromPile = null;
                    }
                    catch (CardSequenceException e) {
                        Log.e("Nertz", "Failed to push live card onto destination pile");
                        returnLiveCard();
                    }
                } else {
                    returnLiveCard();
                }
                // Ensure that if the touch event began on the stream
                // pile that it is no longer considered the start of the
                // touch.
                // Probably better to just track the down & up locations
                // then figure out what to do.
                streamPileTouched = false;
                break;

            case MotionEvent.ACTION_MOVE:
                liveCardX = ev.getX();
                liveCardY = ev.getY();
                break;

            case MotionEvent.ACTION_CANCEL:
                returnLiveCard();
                streamPileTouched = false;
                break;
        }

        invalidate();

        return true;
    }
}
