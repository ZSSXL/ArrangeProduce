package cn.edu.jxust.arrangeproduce;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ZSS
 * @date 2019/12/3 13:30
 * @description 测试类
 */
public class Test {

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<>();
        map.put("namexiaozhang", "xiaozhang");
        map.put("namexiaoming", "xiaoming");
        map.put("namexiaowang", "xiaowang");
        map.put("namexiaoqiang", "xiaoqiang");
        map.put("age13", "13");
        map.put("age14", "14");
        map.put("age15", "15");
        map.put("age16", "16");
        map.put("age17", "17");
        List<String> name = getLikeByMap(map, "age");
        System.out.println(name.size());
    }

    private static List<String> getLikeByMap(Map<String, String> map, String likeKey) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entity : map.entrySet()) {
            if (entity.getKey().contains(likeKey)) {
                list.add((String) entity.getValue());
            }
        }
        return list;
    }

}
