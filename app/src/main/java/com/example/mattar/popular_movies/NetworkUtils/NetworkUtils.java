package com.example.mattar.popular_movies.NetworkUtils;

import android.net.Uri;

import com.example.mattar.popular_movies.Model.MainResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mattar on 4/22/2018.
 */



public class NetworkUtils {




    //Movies Query Values
    public static final String API_KEY        = "8aba2d4512c203a0c97453ceeca503c1";

    // Image Base Url
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    //QueryParameters
    public  static final String SORT_BY_PARAM = "sort_by";
    public  static final String API_KEY_PARAM = "api_key";
    public  static final String PAGE_PARAM    = "page";

    //QueryValues
    public enum sort_order {
        POPULARITY("popularity.desc"),
        VOTE("vote_average.desc");

        private String sort_order;

        sort_order(String sort_order) {
            this.sort_order=sort_order;
        }

        @Override
        public String toString() {
            return sort_order;
        }
    }



    public static Map<String,String> buildQueries (String sort_order, int page){

        Map<String,String> map = new HashMap<>();

        map.put(SORT_BY_PARAM,sort_order);
        map.put(PAGE_PARAM,String.valueOf(page));
        map.put(API_KEY_PARAM,API_KEY);

        return map;
    }



















}


