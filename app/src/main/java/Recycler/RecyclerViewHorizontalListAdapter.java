package Recycler;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.qhs.deydigital.R;
import com.example.qhs.deydigital.SecondActivity;

import java.util.List;

import Model.RecyclerHorizentalItem;

/**
 * Created by Sadruddin on 12/24/2017.
 */
public class RecyclerViewHorizontalListAdapter extends RecyclerView.Adapter<RecyclerViewHorizontalListAdapter.ViewHolder> {
    private List <RecyclerHorizentalItem> horizontalList;
    Context context;

    public RecyclerViewHorizontalListAdapter(List <RecyclerHorizentalItem> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_horizental, parent, false);
        ViewHolder gvh = new ViewHolder(groceryProductView);
        return gvh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.imageView.setImageResource(horizontalList.get(position).getImage());
        holder.txtview.setText(horizontalList.get(position).getTitle());
        // holder.imageView.setOnClickListener(new View.OnClickListener() {
        //@Override
        //  public void onClick(View v) {
        //   String productName = horizontalList.get(position).getProd().toString();
        //  Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
        //}
        //});
    }

    @Override
    public int getItemCount() {
        return horizontalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView txtview;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iconH);
            txtview = (TextView) view.findViewById(R.id.textViewH);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    switch (position){
                        case 0:
                            //salon paziraee
                            Intent intent = new Intent(context, RecyclerActivity.class);
                            intent.putExtra("key", "48"); // put image data in Intent
                            //intent.putExtra("count", "275"); // put number of image data in Intent
                            intent.putExtra("count", "100");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 1:
                            //otagh
                            String cList[] = {
                                    "اتاق جوان",
                                    "اتاق خواب"};
                            String idList[]={
                                    "52",
                                    "47"};
                            String mList[]= {
                                    "http://deydigital.ir/wp-content/uploads/2018/07/16381466-64be6aa7573250e940e0ce4f3bc5349c-0-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/06/1-145.jpg"};
                            //String count_list[] = {"141","13","27","26"};
                            String count_list[] = {"100","100"};

                            Intent intent1 = new Intent(context, SecondActivity.class);
                            intent1.putExtra("name", cList); // put image data in Intent
                            intent1.putExtra("image",mList);
                            intent1.putExtra("id",idList);
                            intent1.putExtra("count",count_list);
                            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 2:
                            //posht e TV
                            Intent intent2 = new Intent(context, RecyclerActivity.class);
                            intent2.putExtra("key", "64"); // put image data in Intent
                            //intent2.putExtra("count", "99"); // put number of image data in Intent
                            intent2.putExtra("count", "200");
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent2); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 3:
                            //Gol tazeeni
                            String cList3[] = {
                                    "طرح گل",
                                    "گلدان",
                                    "گل نقاشی",
                                    "گل طلایی",
                                    "گل سه بعدی",
                                    "تابلوی اختصاصی"};
                            String idList3[]={
                                    "154",
                                    "62",
                                    "77",
                                    "61",
                                    "63",
                                    "205"};
                            String mList3[]= {
                                    "http://deydigital.ir/wp-content/uploads/2018/10/2-2.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/07/2-19.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/06/1-138.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/07/39726648Deco3D_17009208_1-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BA-6010-3.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BM-3062.jpg"};
                            // String count_list3[] ={"6","7","25","19","36"};
                            String count_list3[] ={"100","100","100","100","100","200"};

                            Intent intent3= new Intent(context, SecondActivity.class);
                            intent3.putExtra("name", cList3); // put image data in Intent
                            intent3.putExtra("image",mList3);
                            intent3.putExtra("id",idList3);
                            intent3.putExtra("count",count_list3);
                            intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent3); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 4:
                            //fazahaye eestade
                            Intent intent4 = new Intent(context, RecyclerActivity.class);
                            intent4.putExtra("key", "59"); // put image data in Intent
                            //intent4.putExtra("count", "18"); // put number of image data in Intent
                            intent4.putExtra("count", "100");
                            intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent4); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 5:
                            //honari
                            String cList5[] = {
                                    "اسپرت",
                                    "چهره",
                                    "رنگی",
                                    "سیاه و سفید",
                                    "نقاشی",
                                    "سنتی"};
                            String idList5[]={
                                    "159",
                                    "68",
                                    "67",
                                    "66",
                                    "69",
                                    "70"};
                            String mList5[]= {
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BA-1148-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/10/Sh-2041-2.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/10/Sh-1089-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BM-1053-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BM-1051-2.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BM-2041-2.jpg"};
                            //String count_list5[] ={"6","21","9","8","13"};
                            String count_list5[] ={"100","100","100","100","100","100"};
                            Intent intent5= new Intent(context, SecondActivity.class);
                            intent5.putExtra("name", cList5); // put image data in Intent
                            intent5.putExtra("image",mList5);
                            intent5.putExtra("id",idList5);
                            intent5.putExtra("count",count_list5);
                            intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent5); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 6:
                            //tabeiat
                            String cList6[] = {
                                    "طبیعت رویایی",
                                    "حیوانات",
                                    "طبیعت نقاشی",
                                    "منظره"};
                            String idList6[]={
                                    "121",
                                    "58",
                                    "56",
                                    "57"};
                            String mList6[]= {"http://deydigital.ir/wp-content/uploads/2018/12/BA-1150-2.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/NA-1007-3.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/07/rg-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/NA-1041-4.jpg"};
                            //String count_list6[] = {"10","15","26","127"};
                            String count_list6[] = {"100","100","100","200"};
                            Intent intent6= new Intent(context, SecondActivity.class);
                            intent6.putExtra("name", cList6); // put image data in Intent
                            intent6.putExtra("image",mList6);
                            intent6.putExtra("id",idList6);
                            intent6.putExtra("count",count_list6);
                            intent6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent6); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 7:
                       /* //amaken o sakhteman
                        Intent intent7 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent7.putExtra("key", "185"); // put image data in Intent
                        //intent7.putExtra("count", "65"); // put number of image data in Intent
                        intent7.putExtra("count", "100");
                        startActivity(intent7); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/
                            //amaken
                            String cList7[] = {
                                    "اماکن و ساختمان",
                                    "اماکن و طبیعت",
                                    "اماکن ایرانی"};
                            String idList7[]={
                                    "185",
                                    "186",
                                    "187"};
                            String mList7[]= {
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BL-1061-3.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BL-1039-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/BL-1090-2.jpg"};
                            String count_list7[] = {"200","100","100"};
                            Intent intent7= new Intent(context, SecondActivity.class);
                            intent7.putExtra("name", cList7); // put image data in Intent
                            intent7.putExtra("image",mList7);
                            intent7.putExtra("id",idList7);
                            intent7.putExtra("count",count_list7);
                            intent7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent7); // start Intent
                            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 8:
                            //sebodi
                            String cList8[] = {
                                    "تونل",
                                    "گچبری",
                                    "گوی معلق"};
                            String idList8[]={
                                    "74",
                                    "76",
                                    "75"};
                            String mList8[]= {"http://deydigital.ir/wp-content/uploads/2018/11/BA-9053-3.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/11/BA-1155-6.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/10/1-45.jpg"};
                            //String count_list8[] = {"13","31","7"};
                            String count_list8[] = {"100","100","100"};
                            Intent intent8= new Intent(context, SecondActivity.class);
                            intent8.putExtra("name", cList8); // put image data in Intent
                            intent8.putExtra("image",mList8);
                            intent8.putExtra("id",idList8);
                            intent8.putExtra("count",count_list8);
                            intent8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent8); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                        case 9:
                            //poster saghfi
                            Intent intent9 = new Intent(context, RecyclerActivity.class);
                            intent9.putExtra("key", "54"); // put image data in Intent
                            //intent9.putExtra("count", "185"); // put number of image data in Intent
                            intent9.putExtra("count", "300"); // put number of image data in Intent
                            intent9.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent9); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;
                    /*case 10:
                        //tablo ekhtesasi
                        Intent intent10 = new Intent(MainActivity.this, RecyclerActivity.class);
                        intent10.putExtra("key", "162"); // put image data in Intent
                        //intent10.putExtra("count", "103"); // put number of image data in Intent
                        intent10.putExtra("count", "200"); // put number of image data in Intent
                        startActivity(intent10); // start Intent
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;*/
                        case 10:
                            //nemonehaye ejra shode
                            Intent intent10 = new Intent(context, RecyclerActivity.class);
                            intent10.putExtra("key", "162"); // put image data in Intent
                            //intent9.putExtra("count", "185"); // put number of image data in Intent
                            intent10.putExtra("count", "200"); // put number of image data in Intent
                            intent10.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent10); // start Intent
                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;

                        case 11:
                            //otagh e koodak
                            String cList11[] = {
                                    "اتاق دختر",
                                    "اتاق پسر"};
                            String idList11[]={
                                    "51",
                                    "50"};
                            String mList11[]= {
                                    "http://deydigital.ir/wp-content/uploads/2018/07/57397536Deco3D_16598549_1-1.jpg",
                                    "http://deydigital.ir/wp-content/uploads/2018/12/ch-1052-2.jpg"};
                            //String count_list10[] = {"13","31","7"};
                            String count_list11[] = {"200","200"};
                            Intent intent11= new Intent(context, SecondActivity.class);
                            intent11.putExtra("name", cList11); // put image data in Intent
                            intent11.putExtra("image",mList11);
                            intent11.putExtra("id",idList11);
                            intent11.putExtra("count",count_list11);
                            intent11.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent11); // start Intent
                            // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            break;

                        case 12:
                            //vasile naghlie
                            Intent intent12 = new Intent(context, RecyclerActivity.class);
                            intent12.putExtra("key", "191"); // put image data in Intent
                            //intent9.putExtra("count", "185"); // put number of image data in Intent
                            intent12.putExtra("count", "100"); // put number of image data in Intent
                            intent12.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent12); // start Intent

                            //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            break;



                    }
                    // set an Intent to Another Activity
                    // Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                    //intent.putExtra("image", logos[position]); // put image data in Intent
                    //startActivity(intent); // start Intent
                }
            });

        }}}


