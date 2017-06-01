package utils;

import java.util.regex.Pattern;

/**
 * Created by ch4r0n on 2017/4/24.
 */

/**
 * @name ValidatorUtils.java
 * @package utils
 * @class name：ValidatorUtils
 * @class
 * @anthor ch4r0n QQ:609461975
 * @time 2017/4/24 下午2:08
 */
public class ValidatorUtils {

    /**
     * 正则表达式:验证用户名(不包含中文和特殊字符)如果用户名使用手机号码或邮箱 则结合手机号验证和邮箱验证
     */
    public static final String REGEX_USERNAME = "^[a-zA-Z]\\w{5,17}$";

    /**
     * 正则表达式:验证密码(不包含特殊字符)
     */
    public static final String REGEX_PASSWORD = "^[a-zA-Z0-9]{6,16}$";

    /**
     * 正则表达式:验证手机号
     */
    public static final String REGEX_MOBILE = "^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";

    /**
     * 正则表达式:验证邮箱
     */
    public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    /**
     * 正则表达式:验证汉字(1-9个汉字)  {1,9} 自定义区间
     */
    public static final String REGEX_CHINESE = "^[\u4e00-\u9fa5]{1,9}$";

    /**
     * 正则表达式：验证昵称(1到6个汉字)或者不包含特殊字符的1-12个英文
     */
    public static final String REGEX_NICKNAME = "^[\\u4e00-\\u9fa5]{1,6}|[a-zA-Z]{1,12}$";



    /**
     * 校验用户名
     * @param username
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUserName(String username) {
        return Pattern.matches(REGEX_USERNAME, username);
    }

    /**
     * 校验密码
     * @param password
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPassword(String password) {
        return Pattern.matches(REGEX_PASSWORD, password);
    }

    /**
     * 校验手机号
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    }

    /**
     * 校验邮箱
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 校验汉字
     * @param chinese
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isChinese(String chinese) {
        return Pattern.matches(REGEX_CHINESE, chinese);
    }

    /**
     * 判断字符串是否相等
     * @param s1
     * @param s2
     * @return
     */
    public static boolean isEqualPasswd(String s1, String s2){
        return s1.equals(s2);
    }


    /**
     * 判断昵称是否合法
     * @param nickname
     * @return
     */
    public static boolean isNickName(String nickname){
        return Pattern.matches(REGEX_NICKNAME,nickname);

    }

}
