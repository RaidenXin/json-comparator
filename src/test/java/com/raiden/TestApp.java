package com.raiden;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 21:43 2019/7/24
 * @Modified By:
 */
public class TestApp {

    @Test
    public void test(){
        List<String> strings = new ArrayList<>();
        strings.add("111");
        strings.add("222");
        System.err.println(JSON.toJSONString(strings));
    }
}
