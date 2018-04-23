package meijia.com.meijianet.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 跟货币计算相关的工具类
 */
public class NumberUtils {
    /**
     * 两个double值相加
     *
     * @param i 结果保留的小数位，不够用0补全
     * @return v1-v2
     */
    public static String addAndFormat(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val3 = new BigDecimal(v2);
        BigDecimal result = val1.add(val3);
        double doubleValue = result.setScale(i, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return formatDouble(doubleValue, i);
    }

    /**
     * 两个double值相加
     *
     * @param i 结果最多保留的小数位
     * @return v1+v2
     */
    public static double add(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val3 = new BigDecimal(v2);
        BigDecimal result = val1.add(val3);
        return result.setScale(i, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }


    /**
     * 两个double值相减
     *
     * @param i 结果最多保留的小数位
     * @return v1-v2
     */
    public static double subtract(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val3 = new BigDecimal(v2);
        BigDecimal result = val1.subtract(val3);
        double doubleValue = result.setScale(i, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return doubleValue;
    }

    /**
     * 两个double值相减
     *
     * @param i 结果保留指定小数位，不够用0补全
     * @return v1-v2
     */
    public static String subtractAndFormat(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val3 = new BigDecimal(v2);
        BigDecimal result = val1.subtract(val3);
        double doubleValue = result.setScale(i, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
        return formatDouble(doubleValue, i);
    }


    /**
     * 两个double相乘
     *
     * @param i 结果最多保留的小数位
     */
    public static double multiply(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val3 = new BigDecimal(v2);
        BigDecimal result = val1.multiply(val3);
        return result.setScale(i, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    /**
     * 两个double值相乘
     *
     * @param i 结果保留指定的小数位.不够用0补全
     * @return
     */
    public static String multiplyAndFormat(double v1, double v2, int i) {
        if (i < 0)
            i = 2;
        double d = multiply(v1, v2, i);
        return formatDouble(d, i);
    }

    /**
     * 按四舍五入的方式，将指定的value值进行格式化
     *
     * @param i double值要保留的小数位，不够用0补全
     */
    public static String formatDouble(double value, int i) {
        if (i < 0)
            i = 2;
        StringBuilder sb = new StringBuilder();
        sb.append("####################################0.");
        for (int x = 0; x < i; x++)
            sb.append("0");
        DecimalFormat df = new DecimalFormat(sb.toString());
        return df.format(value);
    }
}
