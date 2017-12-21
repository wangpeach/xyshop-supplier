import com.alibaba.fastjson.JSONObject;
import com.xy.config.Config;
import com.xy.models.User;
import com.xy.utils.DateUtils;
import com.xy.utils.Md5Util;
import com.xy.utils.RandomUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MainTest {

    public static void main(String[] args) {
//        System.out.println(StringUtils.splitWithChar("610423199412153415", 4, ' '));
        System.out.println(RandomUtil.getRandom(32, RandomUtil.TYPE.LETTER_CAPITAL_NUMBER));
//        System.out.println(Config.GOOD_PREFIX + RandomUtil.getRandom(12, RandomUtil.TYPE.NUMBER));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(sdf.format(DateUtils.getCurrentOffsetDay(-7)) );

//        String[] strarr = {"a", "b", "c"};
//        StringUtils.join(strarr, "");
//        String result = "";
//        for (int i = 0; i < strarr.length; i++) {
//            result += strarr[i];
//            if(i < strarr.length - 1) {
//                result += ";";
//            }
//        }


//        System.out.println(result);

//        System.out.println(new BigDecimal("128.56"));
//
        System.out.println(Md5Util.md5UpperCase("0"));
//
//        User user = new User();
//        System.out.println(JSONObject.toJSON(user));
//
//        List<User> items = new ArrayList<>();
//
//        items.add(new User(5, "u5"));
//        items.add(new User(11, "u11"));
//        items.add(new User(0, "u0"));
//        items.add(new User(2, "u2"));
//        items.add(new User(4, "u4"));
//
//
//        items.sort(Comparator.comparing(User::getCoin));
//
//        System.out.println(items.stream().map(User::getName).collect(Collectors.toList()));
    }
}
