package com.raiden;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 21:43 2019/7/24
 * @Modified By:
 */
public class TestApp {

    private static final Pattern pattern = Pattern.compile("var (\\w+) = new (\\w+)\\(\\)");
    @Test
    public void test(){
        String str = "var award = new Assist_Areward()";
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()){
            System.out.println(matcher.group(1));
        }
    }
}
