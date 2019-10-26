
package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListItem {



    int id1;
    public ArrayList<String> img_src;
    String description;
    String name;
    String id;
    String favorite;
    String imgLink;
    int price;
    int count;

    int num_link;
    JSONArray image_json;
    List<JSONArray> image_series;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }



    public int getNum_link() {
        return num_link;
    }

    public void setNum_link(int num_link) {
        this.num_link = num_link;
    }




    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }


    public ListItem(  String id,String name, String description, String imgLink, String favorite,int num_link,int price, int count) {
        this.id=id;
        this.imgLink=imgLink;
        this.description = description;
        this.name = name;
        this.favorite=favorite;
        this.num_link=num_link;
        this.price=price;
        this.count=count;
    }

    public ListItem() {

    }


    public int getId1() {
        return id1;
    }

    public void setId1(int id1) {
        this.id1 = id1;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public List<JSONArray> getImage_series() {
        return image_series;
    }

    public void setImage_series(List<JSONArray> image_series) {
        this.image_series = image_series;
    }



    public ListItem(String imgLink,String name,String id, String description, JSONArray image_json,ArrayList img_src) {
        this.imgLink = imgLink;
        this.description = description;
        this.image_json = image_json;
        this.name = name;
        this.id = id;
        this.img_src = img_src;
    }

    public List<String> getImg_src() {
        return img_src;
    }
    public void setImg_src(ArrayList <String> img_src) {
        this.img_src = img_src;
    }
    public void setImg_src(String src) {
        this.img_src.add(src);
    }

    public int get_img_src_size(){
         return img_src.size();
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JSONArray getImage_json() {
        return image_json;
    }

    public void setImage_json(JSONArray image_json) {
        this.image_json = image_json;
    }

}