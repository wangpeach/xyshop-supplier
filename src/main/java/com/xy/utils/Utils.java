package com.xy.utils;

import org.omg.CORBA.INTERNAL;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class Utils {
    // md5加密方法
    private final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};

    public static String md5(String pw) {
        String MdValue = "";

        try {
            byte[] strTemp = pw.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = digits[byte0 >>> 4 & 0xf];
                str[k++] = digits[byte0 & 0xf];
            }
            MdValue = new String(str);
            MdValue = MdValue.toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return MdValue;
    }

    /**
     * 字符转义
     *
     * @param str
     * @return
     */
    public static String charEscape(String str) {
        // str = str.replace("<", "&lt;");
        // str = str.replace(">", "&gt;");
        str = str.replace("\\", "\\\\");
        // str = str.replace(",", "&sbquo;");
        // str = str.replace("\n", "&lt;br /&gt;");
        // str = str.replace("\r", "&lt;br /&gt;");
        // str = str.replace("\t", "&lt;br /&gt;");
        str = str.replace("\r\n", "");
        return str;
    }

    public static String uncharEscape(String str) {
        str = str.replace("&lt;", "<").replace("&gt;", ">").replace("&quot;", "\"").replace("&sbquo;", ",")
                .replace("&lt;br /&gt;", "<br/>");
        return str;
    }


    /**
     * @param params
     * @return
     */
    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "?";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    // 将字符串转化为Date类型
    public final static Date strToDate(String date, String type) {
        Date sysdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(type);
        try {
            if (!date.equals("")) {
                sysdate = formatter.parse(date);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sysdate;
    }

    /**
     * emoji表情转换(hex -> utf-16)
     *
     * @param hexEmoji
     * @return
     */
    public static String emoji(int hexEmoji) {
        return String.valueOf(Character.toChars(hexEmoji));
    }

    // 传入String类型的时间 运算方法
    public static String countDate(String type, int num) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        Calendar cd = Calendar.getInstance();
        cd.setTime(dt);
        if (type.equals("y")) {
            cd.add(Calendar.YEAR, num);// 日期中年的加减
        }
        if (type.equals("M")) {
            cd.add(Calendar.MONTH, num);// 日期中月的加减
        }
        if (type.equals("d")) {
            cd.add(Calendar.DAY_OF_YEAR, num);// 日期中天的加减
        }
        dt = cd.getTime();
        return sdf.format(dt);
    }

    // 传入date类型的时间 运算方法
    public final static Date countDate(Date date, String type, int num) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(date);
        if (type.equals("y")) {
            cd.add(Calendar.YEAR, num);// 日期中年的加减
        }
        if (type.equals("M")) {
            cd.add(Calendar.MONTH, num);// 日期中月的加减
        }
        if (type.equals("d")) {
            cd.add(Calendar.DAY_OF_YEAR, num);// 日期中天的加减
        }
        Date dt1 = cd.getTime();
        return dt1;
    }

    // 两个Date类型的数据进行运算
    public final static int dataDiffer(Date startDate, Date endDate) {
        long num = (endDate.getTime() - startDate.getTime()) / (24 * 60 * 60 * 1000);
        return (int) num;
    }

    // 根据日期生成随机编号
    public final static String generateRAND() {
        String[] keys = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L",
                "Z", "X", "C", "V", "B", "N", "M", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f",
                "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
        synchronized (keys) {
            Random random = new Random();
            return (keys[random.nextInt(keys.length - 1)]
                    + new SimpleDateFormat("yyyyMMddHH").format(new Date()) + random.nextInt(10)
                    + random.nextInt(10) + random.nextInt(10)).toString();
        }
    }

    // 获取一个在本系统不会重复的一串数字
    public synchronized static String getMyAppUUID(String qz) {
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return qz + (new Date().getTime() - 1300000000000L);
    }

    /**
     * 获取交易流水号
     *
     * @return
     */
    public synchronized final static String tradeno(String paywhy, String userid) {
        String yz = userid + (int) (Math.random() * (900000) + 100000);
        for (int i = 0; i < 12 - userid.length(); i++) {
            userid = userid + "0";
        }
        return paywhy + Utils.GetNowDateTime("yyMMddHHSS") + yz;
    }

    public final static int rangeRandom(int min, int max) {
        Random random = new Random();
        int res = random.nextInt(max) % (max - min + 1) + min;
        return res;
    }

    public final static String generateNum(int length, int cardinal) {
        int surplus = length - String.valueOf(cardinal).length();
        int range = new Double(Math.pow(10, surplus)).intValue();
        return cardinal + String.valueOf(Math.round(Math.random() * (range - 1)) + 1);
    }

    // 传入1-9数据返回大写的阿拉伯数字
    public final static String numberToC(int num) {
        String number = Integer.toString(num); // 返回字符串,转换失败返回数字
        StringBuffer n = new StringBuffer(); // 做拼接用
        String[] numType = new String[]{"", "十", "百", "千", "万"};
        if (num > 10000) { // 最大为1000
            return number;
        }
        // 声明数组
        int[] intarry = new int[number.length()];
        // 将数字字符串转化为int数组
        for (int i = 0; i < number.length(); i++) {
            intarry[i] = Integer.parseInt(String.valueOf(number.charAt(i)));
        }
        int j = 0;
        for (int i = intarry.length - 1; i >= 0; i--) {
            switch (intarry[i]) {
                case 0:
                    n.insert(0, "零");
                    break;
                case 1:
                    n.insert(0, "一" + numType[j]);
                    break;
                case 2:
                    n.insert(0, "二" + numType[j]);
                    break;
                case 3:
                    n.insert(0, "三" + numType[j]);
                    break;
                case 4:
                    n.insert(0, "四" + numType[j]);
                    break;
                case 5:
                    n.insert(0, "五" + numType[j]);
                    break;
                case 6:
                    n.insert(0, "六" + numType[j]);
                    break;
                case 7:
                    n.insert(0, "七" + numType[j]);
                    break;
                case 8:
                    n.insert(0, "八" + numType[j]);
                    break;
                case 9:
                    n.insert(0, "九" + numType[j]);
                    break;
                default:
                    break;
            }
            j++;
        }
        number = n.toString();
        return number;
    }

    /**
     * 生成Key
     *
     * @param length 生成Key长度
     * @param group  每组key长度
     * @return key
     */
    public final static String generateKey(int length, int group, String split) {
        StringBuilder sbr = new StringBuilder();
        try {
            Random random = new Random();
            String[] keys = {"Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K",
                    "L", "Z", "X", "C", "V", "B", "N", "M", "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s",
                    "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m", "0", "1", "2", "3", "4", "5",
                    "6", "7", "8", "9"};
            for (int i = 0; i < length; i++) {
                if (group == 0) {
                    sbr.append(keys[random.nextInt(keys.length - 1)]);
                } else {
                    if (i != 0 && i % group == 0) {
                        sbr.append(split);
                    }
                    sbr.append(keys[random.nextInt(keys.length - 1)]);
                }

            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        return sbr.toString();
    }

    public synchronized static String getsetordernum(String prefix, String userid) {
        String orderNumber = null;
        try {
            // int userid=userid
            int useryz = userid.length();
            for (int i = 0; i < 6 - useryz; i++) {
                userid = "0" + userid;
            }
            String date = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String dd = ((int) (Math.random() * 900000 + 100000)) + "";
            orderNumber = prefix + userid + date.substring(0, date.length() - 9)
                    + (dd.length() == 6 ? dd : date.substring(date.length() - 6, date.length()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderNumber;
    }


    public static String GetNowDateTime(String type) {
        SimpleDateFormat sf = new SimpleDateFormat(type);
        return sf.format(new Date());
    }

    /**
     * 根据传入的时间获取和现在相差多少秒
     *
     * @param 按照一定格式获取当前时间
     */
    public static long gettimecha(String datestr) {
        long hour = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = new Date();
            Date d2 = df.parse(datestr);
            long diff = d1.getTime() - d2.getTime();
            hour = diff / 1000;
        } catch (Exception e) {
        }
        return hour;
    }

    /**
     * 根据传入的时间获取和现在相差多少秒
     *
     * @param 按照一定格式获取当前时间
     */
    public static long gettimechaHaoMiao(String datestr) {
        long hour = 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d1 = new Date();
            Date d2 = df.parse(datestr);
            hour = d2.getTime() - d1.getTime();
        } catch (Exception e) {
        }
        return hour;
    }

    /**
     * 将传入的时间，按照传入的格式格式化并返回。
     *
     * @param 按照一定格式获取时间
     */
    public static String getDateByType(String date, String type) {
        SimpleDateFormat formatDate = new SimpleDateFormat(type);
        Date time = null;
        String str = formatDate.format(date);
        try {
            time = formatDate.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatDate.format(time);
    }

    /**
     * 获取今天起始时间
     *
     * @author dc
     */
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    /**
     * 获取今天结束时间
     *
     * @author dc
     */
    public static Date getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime();
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress, int erwidth,
                                    int erherght) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > 90) {
                width = 90;
            }
            if (height > 90) {
                height = 90;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (erwidth - width) / 2;
        int y = (erherght - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    public static void insertImage(String sourceurl, String imgPath, boolean needCompress, int erwidth, int erherght)
            throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > 50) {
                width = 50;
            }
            if (height > 50) {
                height = 50;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        BufferedImage source = ImageIO.read(new File(sourceurl));
        Graphics2D graph = source.createGraphics();
        int x = (erwidth - width) / 2;
        int y = (erherght - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
        ImageIO.write(source, "jpg", new File(sourceurl));
    }

    /**
     * 插入LOGO
     *
     * @param source       二维码图片
     * @param imgPath      LOGO图片地址
     * @param needCompress 是否压缩
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress, int erwidth,
                                    int erherght, String username, String outfile) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > 150) {
                width = 150;
            }
            if (height > 150) {
                height = 150;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        graph.drawImage(src, 30, 20, 140, 140, null);
        // graph.drawImage(src, 310, 760, 100, 100, null);
        Shape shape = new RoundRectangle2D.Float(0, 0, 80, 80, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        // graph.draw(shape);
        graph.setFont(new Font("黑体", Font.BOLD, 35));
        graph.setColor(new Color(255, 198, 0));
        graph.drawString(username, 220, 90);
        graph.dispose();
        ImageIO.write(source, "jpg", new File(outfile));
        try {
            file.delete();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    public static void createMycokdeImage(String sourceurl, String imgPath, String outfile, boolean needCompress,
                                          int erwidth, int erherght, String tuxiangurl, String name) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = 400;
        int height = 400;
        if (needCompress) { // 压缩LOGO
            // if (width > 90) {
            // width = 90;
            // }
            // if (height > 90) {
            // height = 90;
            // }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        BufferedImage source = ImageIO.read(new File(sourceurl));
        Graphics2D graph = source.createGraphics();
        int x = (erwidth - width) / 2;
        int y = (erherght - height) / 2 + 255;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();

        // File outFile = new File(outfile);
        // ImageIO.write(source, "jpg", outFile);
        insertImage(source, tuxiangurl, false, 90, 90, name, outfile);
        try {
            file.delete();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

	/*public static void main(String[] args) {
        try {

			Utils.insertImage("C:\\Users\\ChenJiangTao\\Desktop\\33333\\1.jpg",
					"C:\\Users\\ChenJiangTao\\Desktop\\33333\\0.jpg", false, 430, 430);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}*/

    /**
     * 金额为分的格式
     */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

    /**
     * 将分为单位的转换为元并返回金额格式的字符串 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(Long amount, Boolean boo) throws Exception {
        if (!amount.toString().matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }

        int flag = 0;
        String amString = amount.toString();
        if (amString.charAt(0) == '-') {
            flag = 1;
            amString = amString.substring(1);
        }
        StringBuffer result = new StringBuffer();
        if (amString.length() == 1) {
            result.append("0.0").append(amString);
        } else if (amString.length() == 2) {
            result.append("0.").append(amString);
        } else {
            String intString = amString.substring(0, amString.length() - 2);

            for (int i = 1; i <= intString.length(); i++) {
                if (boo) {
                    if ((i - 1) % 3 == 0 && i != 1) {
                        result.append(",");
                    }
                }
                result.append(intString.substring(intString.length() - i, intString.length() - i + 1));
            }
            result.reverse().append(".").append(amString.substring(amString.length() - 2));
        }
        if (flag == 1) {
            return "-" + result.toString();
        } else {
            return result.toString();
        }
    }

    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) throws Exception {
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static String changeY2F(Long amount) {
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).toString();
    }

    //转码
    public static String transcoding(String arg) {
        String flag = null;
        try {
            flag = new String(arg.getBytes("iso-8859-1"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 将元为单位的转换为分 替换小数点，支持以逗号区分的金额
     *
     * @param amount
     * @return
     */
    public static Integer changeY2F(String amount) {
        String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
        // 或者$的金额
        int index = currency.indexOf(".");
        int length = currency.length();
        Long amLong = 0l;
        if (index == -1) {
            amLong = Long.valueOf(currency + "00");
        } else if (length - index >= 3) {
            amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
        } else if (length - index == 2) {
            amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
        } else {
            amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
        }
        return Integer.parseInt(amLong.toString());
    }

    /**
     * @param action
     * @param hiddens
     * @return
     */
    public static String buildRequest(Map<String, String> sParaTemp, String action) {
        // 待请求参数数组
        StringBuffer sbHtml = new StringBuffer();

        sbHtml.append(
                "<form id=\"alipaysubmit\" name=\"alipaysubmit\" action=\"" + action + "\"method='POST'\"" + "\">");

        for (String name : sParaTemp.keySet()) {
            String value = sParaTemp.get(name);

            sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
        }

        // submit按钮控件请不要含有name属性
        sbHtml.append("<input type=\"submit\" style=\"display:none;\"></form>");
        sbHtml.append("<script>document.forms['alipaysubmit'].submit();</script>");

        return sbHtml.toString();
    }


    public static void download(String urlString, String filename, String savePath) throws Exception {
        // 构造URL
        URL url = new URL(urlString);
        // 打开连接
        URLConnection con = url.openConnection();
        // 设置请求超时为5s
        con.setConnectTimeout(5 * 1000);
        // 输入流
        InputStream is = con.getInputStream();

        // 1K的数据缓冲
        byte[] bs = new byte[1024];
        // 读取到的数据长度
        int len;
        // 输出的文件流
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }
        OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
        // 开始读取
        while ((len = is.read(bs)) != -1) {
            os.write(bs, 0, len);
        }
        // 完毕，关闭所有链接
        os.close();
        is.close();
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 根据提供的2个坐标计算相距
     *
     * @param origLng
     * @param origLat
     * @param lng
     * @param lat
     * @return
     */
    public static String distance(double origLng, double origLat, double lng, double lat) {
        String result = null, _strdis = null;
        double _distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(origLat - lat) * 3.14159265359 / 180 / 2, 2) + Math.cos(origLat * 3.14159265359 / 180) * Math.cos(lat * 3.14159265359 / 180) * Math.pow(Math.sin(origLng - lng) * 3.14159265359 / 180 / 2, 2))) * 6378.137;
        _distance = Double.valueOf(String.format("%.3f", _distance));
        _strdis = String.valueOf(_distance * 1000);
        if(_strdis.length() > 5) {
            result = String.format("%.2f", _distance) + "km";
        } else {
            result = _strdis.substring(0, _strdis.lastIndexOf(".")) + "m";
        }
        return result;
    }
}