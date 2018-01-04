package com.example.chriswu.triple_tac_toe;

import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry point for the application. Gets the TextViews and passes them to the Game_Controller
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        ViewGroup viewgroup = (ViewGroup) findViewById(android.R.id.content);
        ConstraintLayout layout = (ConstraintLayout)viewgroup.getChildAt(0);
        for (int viewIdx = 0; viewIdx < layout.getChildCount()-2; viewIdx++) {
            View view = layout.getChildAt(viewIdx);
            if(view instanceof TextView)
            {
                textViews.add((TextView)view);
            }
        }
        return textViews;
    }

    List<Button> getButtons()
    {
        List<Button> buttons = new ArrayList<>();
        ViewGroup viewgroup = (ViewGroup) findViewById(android.R.id.content);
        ConstraintLayout layout = (ConstraintLayout)viewgroup.getChildAt(0);
        buttons.add((Button)layout.getChildAt(R.id.Reset_Button));
        buttons.add((Button)layout.getChildAt(R.id.Undo_Button));
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