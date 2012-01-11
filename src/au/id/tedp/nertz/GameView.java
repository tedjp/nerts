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

class GameView extends View {
    public GameView(android.content.Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawARGB(255, 0, 128, 0);
        //ImageView emptyImage = (ImageView) findViewById(R.id.emptyImage);
        //ImageView streamImage = (ImageView) findViewById(R.id.streamImage);

        Resources res = getResources();
        //streamImage.setImageDrawable(blueBack);

        //Drawable blueBack = res.getDrawable(R.drawable.b1fv);
        BitmapDrawable blueBack = (BitmapDrawable) res.getDrawable(R.drawable.b1fv);
        Bitmap bmp = blueBack.getBitmap();

        //Rect bounds = canvas.getClipBounds();
        RectF dest = new RectF();
        dest.bottom = blueBack.getIntrinsicHeight();
        dest.right = blueBack.getIntrinsicWidth();
        /*
        dest.top = 0.0f;
        dest.bottom = 10.0f;
        dest.left = 0.0f;
        dest.right = 8.0f;
        */
        canvas.drawBitmap(bmp, null, dest, null);
    }
}
