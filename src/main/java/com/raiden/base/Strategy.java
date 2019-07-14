package com.raiden.base;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 19:21 2019/7/14
 * @Modified By: 策略枚举
 */
public enum Strategy{

    SORT(0),
    COMPARE(1),
    COMPARE_FIELD_NAME(2);


    private int type;

    private Strategy(int type){
        this.type = type;
    }
}
