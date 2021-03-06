package com.raiden.util;

/**
 * @创建人:Raiden
 * @Descriotion:
 * @Date:Created in 13:21 2019/7/13
 * @Modified By:
 */
public class JsonUtils {

    /**
     * 工具类最好不要有构造方法
     */
    private JsonUtils(){}
    /**
     * @param resString
     * @return String
     * @throws
     * @Description JSON串格式化
     * @author lgh
     * @date 2018/10/29-13:45
     */
    public static String responseFormat(String resString){

        StringBuffer jsonForMatStr = new StringBuffer();
        int level = 0;
        //将字符串中的字符逐个按行输出
        for (int index = 0,n =  resString.length(); index < n; index++) {
            //获取s中的每个字符
            char c = resString.charAt(index);

            //level大于0并且jsonForMatStr中的最后一个字符为\n,jsonForMatStr加入\t
            if (level > 0  && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            char pre = index == 0 ? c : resString.charAt(index - 1);
            char next = index == n - 1 ? c : resString.charAt(index + 1);
            //遇到"{"和"["要增加空格和换行，遇到"}"和"]"要减少空格，以对应，遇到","要换行
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c);
                    if (pre == '{' || pre == ':' || pre == '['|| pre == ','){
                        jsonForMatStr.append("\n");
                        level++;
                    }
                    break;
                case ',':
                    jsonForMatStr.append(c);
                    if ((pre == '\"' || next == '\"') || (pre == ']' || next == '[') || (pre == '}' || next == '{')){
                         jsonForMatStr.append("\n");
                     }
                    break;
                case '}':
                case ']':
                    if (next == ',' || next == '}'|| next == ']'){
                        jsonForMatStr.append("\n");
                        level--;
                        jsonForMatStr.append(getLevelStr(level));
                    }
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }
        return jsonForMatStr.toString();
    }
    /**
     * @param level
     * @return
     * @throws
     * @author lgh
     * @date 2018/10/29-14:29
     */
    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("   ");
        }
        return levelStr.toString();
    }
}
