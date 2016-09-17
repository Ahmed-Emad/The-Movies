package io.zarda.moviesapp;

import java.util.List;

/**
 * Created by Ahmed Emad on 4 May, 2015.
 */
public class Utils {

    public static String MOVIES_APP_KEY = "2fb851b2ffe827556aa102a807e3e6e2";

    public static String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    public static String BACKDROP_BASE_URL = "http://image.tmdb.org/t/p/w780/";

    public static String stringJoin(String sSep, List<String> aArr) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.size(); i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr.get(i));
        }
        return sbStr.toString();
    }

}
