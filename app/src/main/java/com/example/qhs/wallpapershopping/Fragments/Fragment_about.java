package com.example.qhs.wallpapershopping.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.qhs.wallpapershopping.R;

import About.AboutAdapter;


public class Fragment_about extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        //Toolbar
        Toolbar toolbar = (Toolbar) ((AppCompatActivity)getActivity()).findViewById(R.id.toolbar);
        TextView title = (TextView) ((AppCompatActivity)getActivity()).findViewById(R.id.txtTitle);
        title.setText("درباره ما");


        ImageView map = (ImageView) view.findViewById(R.id.imgView_map);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=34.61608232923757,50.860158371844136(Maninagar)");
                Intent intent4 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent4);
            }
        });

        TextView txtView_contact = (TextView) view.findViewById(R.id.txtView_contactUs);

        final ListView simpleList1 = (ListView) view.findViewById(R.id.listA);

        final String[] myStrings = {
                "025-0000000",
                "0910000000",
                "Wallpaper_Shopping",
                "Wallpaper_Shopping@",
                "Wallpaper_Shoping@gmail.com",
                " قم/30 متری قائم/..."};

        final int[] imgStrings = {
                R.mipmap.phone,
                R.mipmap.smartphone,
                R.mipmap.instagram ,
                R.mipmap.telegram,
                R.mipmap.mail,
                R.mipmap.address};

        final int[] imageButton_about = {
                R.drawable.button_aboutlist,
                R.drawable.button_aboutlist_whatsapp,
                R.drawable.button_aboutlist_pressed_instagram ,
                R.drawable.button_aboutlist_telegram,
                R.drawable.button_aboutlist_mail,
                R.drawable.button_aboutlist_location};

                /*R.drawable.phone,
                R.drawable.smartphone,
                R.drawable.instagram ,
                R.drawable.telegram,
                R.drawable.address};*/

        final AboutAdapter aboutAdapter = new AboutAdapter(getContext(), myStrings, imageButton_about);
        simpleList1.setAdapter(aboutAdapter);


        simpleList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {

                //TextView textView = (TextView) view.findViewById(R.id.txtA);
                //          textView.setTypeface(face);

                switch (position) {
                    case 0:
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+982532931515", null));
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "+989127592318", null));
                        startActivity(intent1);

                        break;
                    case 2:
                        //Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.telegram.me/ِDey_digital"));
                       /* Intent intent2 = new Intent(Intent.ACTION_VIEW);
                        intent2.setData(Uri.parse("http://instagram.com/_u/" + myStrings[2]));
                        intent2.setPackage("com.instagram.android");
                        startActivity(intent2);*/
                        String scheme = "http://instagram.com/_u/dey_digital";
                        String path = "https://instagram.com/dey_digital";
                        String nomPackageInfo ="com.instagram.android";
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(scheme));

                        try {
                            getContext().getPackageManager().getPackageInfo(nomPackageInfo, 0);
                        } catch (Exception e) {
                            intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
                        }
                        startActivity(intent2);


                        break;
                    case 3:

                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/_u/" + myStrings[2] ));
                        startActivity(intent3);
                        break;

                    case 4:
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"Deydigital@gmail.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }




                        break;

                    case 5:
                        Uri uri = Uri.parse("geo:0,0?q=34.61608232923757,50.860158371844136(چاپ دیجیتال دی)");
                        Intent intent5 = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent5);

                }

            }


        });


        return view;
    }


}
