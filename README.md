# json-comparator json串格式化及排序对比工具
一个json的格式化和排序工具
下面的压缩包 解压了能在装了java环境的机器上直接双击运行

该工具的主要功能就是，可以将json串按key排好序，并将其格式化输出。还可以对他进行逐行对比。
分别有三个按钮:
# 1.排序 按照key值进行排序
# 2.对比 将整的一行当做字符串进行对比。

这个工具主要的思路是，将json串格式化并根据key值排好序，理论上相同的字段都排在相同的位置，然后根据这个去逐行进行比较，如果不相同就标为红色。如果相同就不变，试用了下，效果还不错，所以拿出来分享。


#0.0.4加入新功能 
#1. 转换 是将.net格式的model转换为java格式
示例：    public class Assist_Areward
       {
           public string OrderID { get; set; }
           public int Point { get; set; }
           public string MemberID { get; set; }
           public string Mobile { get; set; }
           public string VNO { get; set; }
           public string ToMemberID { get; set; }
           public string ToMobile { get; set; }
           public DateTime ModifyDate { get; set; }
           public DateTime CreateDate { get; set; }
       }
转换后为：
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Setter;
import lombok.Getter;


@Getter
@Setter
public class Assist_Areward{
    @JSONField(name = "OrderID")
    private String orderID;
    @JSONField(name = "Point")
    private int point;
    @JSONField(name = "MemberID")
    private String memberID;
    @JSONField(name = "Mobile")
    private String mobile;
    @JSONField(name = "VNO")
    private String vNO;
    @JSONField(name = "ToMemberID")
    private String toMemberID;
    @JSONField(name = "ToMobile")
    private String toMobile;
    @JSONField(name = "ModifyDate")
    private String modifyDate;
    @JSONField(name = "CreateDate")
    private String createDate;
}
#2.新增赋值功能
加入赋值功能 主要是将.net中 对model属性赋值 转为 java方式的赋值

如：var xxx = new Xxx(){
    a = b,
    d = c
};
转化为
Xxx xxx = new Xxx();
xxx.setA(b);
xxx.setD(c);