import com.xy.utils.RandomUtil;

public class MainTest {

    public static void main(String[] args) {
//        System.out.println(StringUtils.splitWithChar("610423199412153415", 4, ' '));

        System.out.println(RandomUtil.getRandom(10, RandomUtil.TYPE.LETTER_CAPITAL_NUMBER));

//        System.out.println(Config.GOOD_PREFIX + RandomUtil.getRandom(12, RandomUtil.TYPE.NUMBER));
    }
}
