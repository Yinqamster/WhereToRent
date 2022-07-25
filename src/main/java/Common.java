import java.util.ArrayList;
import java.util.List;

public class Common {

    public static final String SEARCH_PLACE = "https://restapi.amap.com/v3/place/text";
    public static final String CAL_DISTANCE = "https://restapi.amap.com/v3/direction/transit/integrated";
    public static final String SHANG_HAI_CODE = "021";
    public static final String SUBWAY_CODE = "150500";
    public static final String KEY = "";

    public static final List<String> SHANG_HAI_DISTRICT = new ArrayList<>();
    static {
        SHANG_HAI_DISTRICT.add("黄浦区");
        SHANG_HAI_DISTRICT.add("徐汇区");
        SHANG_HAI_DISTRICT.add("长宁区");
        SHANG_HAI_DISTRICT.add("静安区");
        SHANG_HAI_DISTRICT.add("普陀区");
        SHANG_HAI_DISTRICT.add("虹口区");
        SHANG_HAI_DISTRICT.add("杨浦区");
        SHANG_HAI_DISTRICT.add("闵行区");
        SHANG_HAI_DISTRICT.add("宝山区");
        SHANG_HAI_DISTRICT.add("嘉定区");
        SHANG_HAI_DISTRICT.add("浦东新区");
        SHANG_HAI_DISTRICT.add("金山区");
        SHANG_HAI_DISTRICT.add("松江区");
        SHANG_HAI_DISTRICT.add("青浦区");
        SHANG_HAI_DISTRICT.add("奉贤区");
        SHANG_HAI_DISTRICT.add("崇明区");
    }
}
