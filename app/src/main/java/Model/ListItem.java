
package Model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ListItem {
    public ArrayList<String> img_src;
    private String description;
    private String name;
    private String id;
    private String favorite;
    private String imgLink;
    private int price;
    private int count;
    private int count_shop;
    private int user_id;
    private int num_link;
    private JSONArray image_json;
    private List<JSONArray> image_series;


    public ListItem(  String id,String name, String description, String imgLink, String favorite,int num_link,int price, int count, int count_shop,int user_id) {
        this.id=id;
        this.imgLink=imgLink;
        this.description = description;
        this.name = name;
        this.favorite=favorite;
        this.num_link=num_link;
        this.price=price;
        this.count=count;
        this.count_shop=count_shop;
        this.user_id=user_id;
    }

    public ListItem() {

    }

    public ListItem(String imgLink,String name,String id, String description, JSONArray image_json,ArrayList img_src) {
        this.imgLink = imgLink;
        this.description = description;
        this.image_json = image_json;
        this.name = name;
        this.id = id;
        this.img_src = img_src;
    }



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


    public int getCount_shop() {
        return count_shop;
    }

    public void setCount_shop(int count_shop) {
        this.count_shop = count_shop;
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


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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