package com.example.gruppeb.madbestillingsapp.Domain;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gruppeb.madbestillingsapp.R;

public class FragmentPage extends Fragment implements View.OnClickListener {

    Chip mLight, mDark;
    private BreadType listener;
    private String title;
    private String description;
    @DrawableRes int image;

    TextView mTitle, mDescription;
    ImageView mImageView;

   public FragmentPage(){
       //Required empty constructor
   }

   @Override
    public void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);

   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_page1, container, false);
        mLight = view.findViewById(R.id.page1_lightbread);
        mDark = view.findViewById(R.id.page1_darkbread);
        mTitle = view.findViewById(R.id.page1_title);
        mDescription = view.findViewById(R.id.page1_subtitle);
        mImageView = view.findViewById(R.id.page1_image);

        mLight.setOnClickListener(this);
        mDark.setOnClickListener(this);

        //default breadtype set checked
        mDark.setChecked(true);

        modifyView();
        return view;

    }

    /**
     * TODO: be able to change image in fragments
      */
    private void modifyView() {
        title = getArguments().getString("title");
        description = getArguments().getString("description");
        image = getArguments().getInt("image");
        mTitle.setText(title);
        mDescription.setText(description);
        //mImage set image
    }

    @Override
    public void onClick(View v) {
        if (v == mLight) {
            listener.setBreadType(true);
            mLight.setChecked(true);
            mDark.setChecked(false);
        }
        if (v == mDark) {
            listener.setBreadType(false);
            mDark.setChecked(true);
            mLight.setChecked(false);
        }
    }

    /**
     * Gør det muligt at implementere et interface, som kan anvendes i Mainscreen.java, således der er lav coupling.
     * <p>
     * https://stackoverflow.com/questions/12659747/call-an-activity-method-from-a-fragment
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (BreadType) context;
        } catch (ClassCastException castException) {
            //
        }
    }

}
