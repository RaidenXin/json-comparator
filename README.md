# json-comparator json串格式化及排序对比工具
一个json的格式化和排序工具
下面的压缩包 解压了能在装了java环境的机器上直接双击运行

该工具的主要功能就是，可以将json串按key排好序，并将其格式化输出。还可以对他进行逐行对比。
分别有三个按钮:
# 1.排序 按照key值进行排序
# 2.对比 将整的一行当做字符串进行对比。

这个工具主要的思路是，将json串格式化并根据key值排好序，理论上相同的字段都排在相同的位置，然后根据这个去逐行进行比较，如果不相同就标为红色。如果相同就不变，试用了下，效果还不错，所以拿出来分享。


#0.0.4加入新功能 
##1. 转换 是将.net格式的model转换为java格式
示例：   \<br>  
public class Assist_Areward\<br>  
       {\<br>  
           public string OrderID { get; set; }\<br>  
           public int Point { get; set; }\<br>  
           public string MemberID { get; set; }\<br>  
           public string Mobile { get; set; }\<br>  
           public string VNO { get; set; }\<br>  
           public string ToMemberID { get; set; }\<br>  
           public string ToMobile { get; set; }\<br>  
           public DateTime ModifyDate { get; set; }\<br>  
           public DateTime CreateDate { get; set; }\<br>  
       }\<br>  
转换后为：
import com.alibaba.fastjson.annotation.JSONField;\<br>  
import lombok.Setter;\<br>  
import lombok.Getter;\<br>  
\<br>  
\<br>  
@Getter\<br>  
@Setter\<br>  
public class Assist_Areward{\<br>  
    @JSONField(name = "OrderID")\<br>  
    private String orderID;\<br>  
    @JSONField(name = "Point")\<br>  
    private int point;\<br>  
    @JSONField(name = "MemberID")\<br>  
    private String memberID;\<br>  
    @JSONField(name = "Mobile")\<br>  
    private String mobile;\<br>  
    @JSONField(name = "VNO")\<br>  
    private String vNO;\<br>  
    @JSONField(name = "ToMemberID")\<br>  
    private String toMemberID;\<br>  
    @JSONField(name = "ToMobile")\<br>  
    private String toMobile;\<br>  
    @JSONField(name = "ModifyDate")\<br>  
    private String modifyDate;\<br>  
    @JSONField(name = "CreateDate")\<br>  
    private String createDate;\<br>  
}
##2.新增赋值功能
加入赋值功能 主要是将.net中 对model属性赋值 转为 java方式的赋值\<br>  

如：\<br>  
var xxx = new Xxx(){\<br>  
    a = b,\<br>  
    d = c\<br>  
};\<br>  
转化为\<br>  
Xxx xxx = new Xxx();\<br>  
xxx.setA(b);\<br>  
xxx.setD(c);\<br>  
