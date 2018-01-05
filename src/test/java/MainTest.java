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

        String pub = "+EK4mEdxh5dM7AxtY51Fct1f4jCme5hu7L+wLfi4PCM0r78IOoEs4kABWasB2SkieBvyyV7JwR5gpyL+SvUzEGmf3WCNLmTUU4at3aavkxbE4qWBJbtcb1JJ3p6ZMcsg0QARr3XftEnLrA1JyoG+T1LKEz3BlGM2XwF86+V9cH5FKALd8vbIEceEof53zlfqWhPv5EQaZA47bwJnHicjav3ttPAoRYTErWmVsuCKuaH4chNoYvlzwfFRSg1UMuWGGsCZaO3DxskFnV8/SzlK7A+o1z0umqnodjF4oltsu6fRLY0Fou2CwIDAQAB";

        String pub1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuAH4PnWQ13392fTL79nVAKAAPfvJxWd0P+EK4mEdxh5dM7AxtY51Fct1f4jCme5hu7L+wLfi4PCM0r78IOoEs4kABWasB2SkieBvyyV7JwR5gpyL+SvUzEGmf3WCNLmTUU4at3aavkxbE4qWBJbtcb1JJ3p6ZMcsg0QARr3XftEnLrA1JyoG+T1LKEz3BlGM2XwF86+V9cH5FKALd8vbIEceEof53zlfqWhPv5EQaZA47bwJnHicjav3ttPAoRYTErWmVsuCKuaH4chNoYvlzwfFRSg1UMuWGGsCZaO3DxskFnV8/SzlK7A+o1z0umqnodjF4oltsu6fRLY0Fou2CwIDAQAB";

        System.out.println(pub.equals(pub1));
    }
}
