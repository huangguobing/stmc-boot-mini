package cn.iocoder.stmc.module.erp.util;

import java.math.BigDecimal;

/**
 * 金额工具类
 *
 * @author stmc
 */
public class MoneyUtils {

    // 数字大写
    private static final String[] NUMBERS = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 整数单位
    private static final String[] UNITS = {"元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟"};

    /**
     * 将数字金额转换为中文大写
     * 例如: 3280 -> 叁仟贰佰捌拾元整
     * 例如: 11742 -> 壹万壹仟柒佰肆拾贰元整
     *
     * @param amount 金额
     * @return 中文大写金额
     */
    public static String toChineseAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return "零元整";
        }

        // 分离整数和小数部分
        long yuan = amount.longValue();
        int jiao = amount.subtract(new BigDecimal(yuan))
                .multiply(new BigDecimal(10)).intValue();
        int fen = amount.subtract(new BigDecimal(yuan))
                .multiply(new BigDecimal(100)).intValue() % 10;

        // 转换整数部分
        String yuanStr = convertIntegerPart(yuan);

        // 处理角分
        StringBuilder result = new StringBuilder(yuanStr);
        if (jiao == 0 && fen == 0) {
            result.append("整");
        } else {
            if (jiao > 0) {
                result.append(NUMBERS[jiao]).append("角");
            } else if (fen > 0) {
                result.append("零");
            }
            if (fen > 0) {
                result.append(NUMBERS[fen]).append("分");
            }
        }

        return result.toString();
    }

    /**
     * 转换整数部分为中文大写
     *
     * @param num 整数
     * @return 中文大写
     */
    private static String convertIntegerPart(long num) {
        if (num == 0) {
            return "零元";
        }

        // 转换为字符串，便于从右往左处理
        String numStr = String.valueOf(num);
        int len = numStr.length();
        StringBuilder result = new StringBuilder();
        boolean needZero = false; // 是否需要补零

        for (int i = 0; i < len; i++) {
            int digit = numStr.charAt(i) - '0';
            int unitIndex = len - i - 1; // 单位索引（从右往左：0=元, 1=拾, 2=佰...）

            if (digit == 0) {
                // 当前位是0
                if (unitIndex == 4 || unitIndex == 8) {
                    // 万位或亿位，必须输出单位
                    result.append(UNITS[unitIndex]);
                    needZero = false;
                } else {
                    // 其他位置的0，标记需要补零
                    needZero = true;
                }
            } else {
                // 当前位不是0
                if (needZero && result.length() > 0) {
                    result.append("零");
                }
                result.append(NUMBERS[digit]).append(UNITS[unitIndex]);
                needZero = false;
            }
        }

        return result.toString();
    }
}
