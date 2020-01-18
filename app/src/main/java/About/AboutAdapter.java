package About;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qhs.wallpapershopping.R;

public class AboutAdapter extends BaseAdapter {
    Context context;
    String nameList[];
    int imgList[];
    LayoutInflater inflter;

    public AboutAdapter(Context context, String[] nameList,int[] imgList) {
        this.context = context;
        this.nameList = nameList;
        this.imgList=imgList;
        //  this.flags = flags;
        inflter = (LayoutInflater.from(context));
    }


    public int getCount() {
        return nameList.length;
    }


    public Object getItem(int i) {
        return null;
    }


    public long getItemId(int i) {
        return 0;
    }


    public View getView(int i, View view, ViewGroup viewGroup) {
        Typeface face=Typeface.createFromAsset(context.getAssets(),"fonts/homa.ttf");
        view = inflter.inflate(R.layout.about_list, null);
        TextView name = (TextView) view.findViewById(R.id.txtA);
        name.setText(nameList[i]);
        name.setTypeface(face);
        ImageView image=(ImageView)view.findViewById(R.id.imgA);
        //Picasso.with(context).load(imgList[i])
        //   .into(image);
        Glide.with(context)
                .load(imgList[i])
                .thumbnail(0.5f)
                //.crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                ///.override(400,200)
                .into(image);
        return view;
    }
}
