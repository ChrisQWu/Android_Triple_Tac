package com.example.chriswu.triple_tac_toe;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
        Game_Controller.getInstance().instantiate(getTexts());
    }

    /**
     * Gets all the TextViews from the layout through .getChildAt(int)
     * @return List<TextView>
     */
    List<TextView> getTexts()
    {
        List<TextView> textViews = new ArrayList<>();
        ViewGroup viewgroup = (ViewGroup) findViewById(android.R.id.content);
        ConstraintLayout layout = (ConstraintLayout)viewgroup.getChildAt(0);
        for (int viewIdx = 0; viewIdx < layout.getChildCount(); viewIdx++) {
            View view = layout.getChildAt(viewIdx);
            if(view instanceof TextView)
            {
                textViews.add((TextView)view);
            }
        }
        return textViews;
    }
}