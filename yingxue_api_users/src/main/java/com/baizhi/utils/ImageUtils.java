package com.baizhi.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 初始化随机头像工具类
 */
public class ImageUtils {

    private static List<String> photos;

    static{
        photos = new ArrayList<>();
        photos.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3528124961,3599494671&fm=11&gp=0.jpg");
        photos.add("https://p.qqan.com/up/2021-2/16137866796094190.jpg");
        photos.add("https://p.qqan.com/up/2021-2/16137866805953840.jpg");
        photos.add("https://p.qqan.com/up/2020-10/20201015101537441.jpg");
        photos.add("https://p.qqan.com/up/2020-9/20209913086005.jpg");
        photos.add("https://p.qqan.com/up/2020-7/2020071308424438852.jpg");
        photos.add("https://p.qqan.com/up/2021-2/20212121453462226.jpg");
        photos.add("https://p.qqan.com/up/2021-1/2021127112032518.jpg");
        photos.add("https://p.qqan.com/up/2021-2/202128104346789.jpg");
        photos.add("https://p.qqan.com/up/2021-1/202111411186632.jpg");
        photos.add("https://p.qqan.com/up/2021-1/202116132678380.jpg");

    }

    /**
     * 随机选择list中一个头像路径
     * @return
     */
    public static String getPhoto(){
        int i = new Random().nextInt(photos.size());
        return photos.get(i);
    }

}
