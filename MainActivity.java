package com.example.chriswu.triple_tac_toe;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the application. Gets the TextViews and passes them to the Game_Controller
 */
public class MainActivity extends AppCompatActivity {
    private int screen_width;
    private int screen_height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int x = -1%3;
        System.out.println("Test: "+x);
//        setContentView(R.layout.main_menu);
        //get screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screen_width = size.x;
        screen_height = size.y;
        setContentView(R.layout.game2);
        Game_Controller.getInstance().instantiate(getTexts(), getButtons());
    }

    /**
     * Gets all the TextViews from the layout through .getChildAt(int)
     * @return List<TextView>
     *
     */
    List<TextView> getTexts()
    {
        List<TextView> textViews = new ArrayList<>();
        List<TextView> largeText = new ArrayList<>();
//        ViewGroup viewgroup = (ViewGroup) findViewById(android.R.id.content);
//        ConstraintLayout layout = (ConstraintLayout)viewgroup.getChildAt(0);
//        for (int viewIdx = 0; viewIdx < layout.getChildCount()-2; viewIdx++) {
//            View view = layout.getChildAt(viewIdx);
//            if(view instanceof TextView)
//            {
//                textViews.add((TextView)view);
//
//            }
//        }

        float topBuffer = (screen_height)/5f, colBuffer = screen_width/20f;
        float adjustedWidth = (9*screen_width)/10f;
        float tileBuffer = (adjustedWidth)/24f;
        int tileSize = (int)(2*adjustedWidth)/27;

        ViewGroup viewgroup = (ViewGroup) findViewById(android.R.id.content);
        ConstraintLayout constraintLayout = (ConstraintLayout)viewgroup.getChildAt(0);

        float smallRow = topBuffer, smallCol = colBuffer;

        for (int largeRow = 0; largeRow < 3; largeRow++) {
            for (int largeCol = 0; largeCol < 3; largeCol++) {
                for (int tileRow = 0; tileRow < 3; tileRow++) {
                    for (int tileCol = 0; tileCol < 3; tileCol++) {
                        TextView textView = new TextView(getApplicationContext());
                        constraintLayout.addView(textView);
                        textView.setWidth(tileSize);
                        textView.setHeight(tileSize);
                        textView.setVisibility(View.VISIBLE);
                        float col = tileSize+tileBuffer;
                        textView.setX(smallCol + tileCol*col);
                        textView.setY(smallRow + tileRow*col);
                        textView.setGravity(Gravity.CENTER);
                        textViews.add(textView);
                    }
                }

                TextView large = new TextView(getApplicationContext());
                constraintLayout.addView(large);
                float col = 3*tileSize+2*tileBuffer;
                large.setWidth((int)(col));
                large.setHeight((int)(col));
                large.setTextSize(50f);
                large.setVisibility(View.VISIBLE);
                large.setX(colBuffer + largeCol*(col+tileBuffer));
                large.setY(topBuffer + largeRow*(col+tileBuffer));
                large.setGravity(Gravity.CENTER);
                largeText.add(large);

                smallCol += 3*(tileSize+tileBuffer);
                if(largeCol != 2) {
                    TextView textView = new TextView(this);
                    constraintLayout.addView(textView);
                    textView.setBackgroundColor(Color.BLACK);
                    textView.setWidth((int) tileBuffer/2);
                    textView.setHeight((int) (tileBuffer + tileSize) * 9);

                    textView.setX(smallCol - (3*tileBuffer) / 4);
                    textView.setY(topBuffer - tileBuffer / 2);
                }
            }

            smallCol = colBuffer;
            smallRow +=  3*(tileSize+tileBuffer);
            if(largeRow != 2) {
                TextView textView = new TextView(this);
                constraintLayout.addView(textView);
                textView.setBackgroundColor(Color.BLACK);
                textView.setWidth((int) (tileBuffer + tileSize) * 9);
                textView.setHeight((int) tileBuffer/2);

                textView.setX(colBuffer - tileBuffer / 2);
                textView.setY(smallRow - (3*tileBuffer) / 4);
            }
        }

        textViews.addAll(largeText);
        textViews.add((TextView) findViewById(R.id.Notifier));

        return textViews;
    }

    List<Button> getButtons()
    {
        List<Button> buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.Reset_Button));
        buttons.add((Button) findViewById(R.id.Undo_Button));
        return buttons;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}