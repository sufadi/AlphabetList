package com.wu.su.alphabetlist.utils;

import java.io.UnsupportedEncodingException;

/**
 * Java Chinese characters into Pinyin tools
 * 中文转拼音(或字母)原理，见如下博文
 * https://blog.csdn.net/zhuwentao2150/article/details/70230341?utm_medium=distribute.pc_relevant.none-task-blog-OPENSEARCH-4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-OPENSEARCH-4
 */
public class CharacterParser {
    private static final String[] pystr;
    private static final int[] pinYinValue;

    static {
        pinYinValue = new int[]{0xFFFFB0A1, 0xFFFFB0A3, 0xFFFFB0B0, 0xFFFFB0B9, 0xFFFFB0BC, 0xFFFFB0C5, 0xFFFFB0D7, 0xFFFFB0DF, 0xFFFFB0EE, 0xFFFFB0FA, -20051, -20036, -20032, -20026, -20002, -19990, 0xFFFFB1EE, 0xFFFFB1F2, 0xFFFFB1F8, -19805, 0xFFFFB2B8, 0xFFFFB2C1, 0xFFFFB2C2, 0xFFFFB2CD, 0xFFFFB2D4, 0xFFFFB2D9, 0xFFFFB2DE, 0xFFFFB2E3, 0xFFFFB2E5, 0xFFFFB2F0, 0xFFFFB2F3, 0xFFFFB2FD, -19540, 0xFFFFB3B5, 0xFFFFB3BB, 0xFFFFB3C5, -19500, 0xFFFFB3E4, 0xFFFFB3E9, 0xFFFFB3F5, 0xFFFFB4A7, 0xFFFFB4A8, 0xFFFFB4AF, 0xFFFFB4B5, -19270, 0xFFFFB4C1, 0xFFFFB4C3, 0xFFFFB4CF, 0xFFFFB4D5, 0xFFFFB4D6, 0xFFFFB4DA, 0xFFFFB4DD, 0xFFFFB4E5, 0xFFFFB4E8, 0xFFFFB4EE, 0xFFFFB4F4, -19038, 0xFFFFB5B1, -19018, -19006, -19003, 0xFFFFB5CC, 0xFFFFB5DF, 0xFFFFB5EF, 0xFFFFB5F8, 0xFFFFB6A1, 0xFFFFB6AA, 0xFFFFB6AB, 0xFFFFB6B5, 0xFFFFB6BC, 0xFFFFB6CB, 0xFFFFB6D1, 0xFFFFB6D5, 0xFFFFB6DE, -18710, 0xFFFFB6F7, 0xFFFFB6F8, 0xFFFFB7A2, 0xFFFFB7AA, -18501, -18490, 0xFFFFB7D2, 0xFFFFB7E1, 0xFFFFB7F0, 0xFFFFB7F1, 0xFFFFB7F2, 0xFFFFB8C1, 0xFFFFB8C3, 0xFFFFB8C9, -18220, 0xFFFFB8DD, -18201, 0xFFFFB8F8, 0xFFFFB8F9, 0xFFFFB8FB, -18012, 0xFFFFB9B3, 0xFFFFB9BC, -17970, 0xFFFFB9D4, 0xFFFFB9D7, -17950, 0xFFFFB9E5, 0xFFFFB9F5, 0xFFFFB9F8, 0xFFFFB9FE, 0xFFFFBAA1, 0xFFFFBAA8, 0xFFFFBABB, -17730, 0xFFFFBAC7, -17703, -17701, 0xFFFFBADF, 0xFFFFBAE4, 0xFFFFBAED, 0xFFFFBAF4, 0xFFFFBBA8, 0xFFFFBBB1, 0xFFFFBBB6, 0xFFFFBBC4, 0xFFFFBBD2, 0xFFFFBBE7, 0xFFFFBBED, 0xFFFFBBF7, -17202, 0xFFFFBCDF, 0xFFFFBDA9, -16970, 0xFFFFBDD2, 0xFFFFBDED, 0xFFFFBEA3, -16708, -16706, 0xFFFFBECF, 0xFFFFBEE8, 0xFFFFBEEF, 0xFFFFBEF9, 0xFFFFBFA6, 0xFFFFBFAA, 0xFFFFBFAF, 0xFFFFBFB5, 0xFFFFBFBC, 0xFFFFBFC0, 0xFFFFBFCF, 0xFFFFBFD3, 0xFFFFBFD5, 0xFFFFBFD9, 0xFFFFBFDD, 0xFFFFBFE4, 0xFFFFBFE9, 0xFFFFBFED, 0xFFFFBFEF, 0xFFFFBFF7, 0xFFFFC0A4, 0xFFFFC0A8, 0xFFFFC0AC, 0xFFFFC0B3, 0xFFFFC0B6, 0xFFFFC0C5, 0xFFFFC0CC, 0xFFFFC0D5, 0xFFFFC0D7, 0xFFFFC0E2, 0xFFFFC0E5, 0xFFFFC1A9, 0xFFFFC1AA, 0xFFFFC1B8, 0xFFFFC1C3, 0xFFFFC1D0, 0xFFFFC1D5, 0xFFFFC1E1, 0xFFFFC1EF, 0xFFFFC1FA, -15707, -15701, 0xFFFFC2BF, 0xFFFFC2CD, 0xFFFFC2D3, 0xFFFFC2D5, 0xFFFFC2DC, -15640, 0xFFFFC2F1, 0xFFFFC2F7, 0xFFFFC3A2, 0xFFFFC3A8, 0xFFFFC3B4, 0xFFFFC3B5, 0xFFFFC3C5, 0xFFFFC3C8, 0xFFFFC3D0, 0xFFFFC3DE, 0xFFFFC3E7, 0xFFFFC3EF, 0xFFFFC3F1, 0xFFFFC3F7, 0xFFFFC3FD, 0xFFFFC3FE, 0xFFFFC4B1, -15180, 0xFFFFC4C3, 0xFFFFC4CA, 0xFFFFC4CF, -15150, 0xFFFFC4D3, 0xFFFFC4D8, 0xFFFFC4D9, 0xFFFFC4DB, -15140, 0xFFFFC4DD, 0xFFFFC4E8, 0xFFFFC4EF, 0xFFFFC4F1, 0xFFFFC4F3, 0xFFFFC4FA, 0xFFFFC4FB, 0xFFFFC5A3, 0xFFFFC5A7, 0xFFFFC5AB, -14930, 0xFFFFC5AF, 0xFFFFC5B0, 0xFFFFC5B2, 0xFFFFC5B6, 0xFFFFC5B7, 0xFFFFC5BE, -14908, -14902, 0xFFFFC5D2, 0xFFFFC5D7, 0xFFFFC5DE, 0xFFFFC5E7, 0xFFFFC5E9, 0xFFFFC5F7, 0xFFFFC6AA, 0xFFFFC6AE, -14670, 0xFFFFC6B4, 0xFFFFC6B9, 0xFFFFC6C2, 0xFFFFC6CB, -14630, 0xFFFFC6FE, 0xFFFFC7A3, -14407, 0xFFFFC7C1, 0xFFFFC7D0, 0xFFFFC7D5, 0xFFFFC7E0, 0xFFFFC7ED, 0xFFFFC7EF, 0xFFFFC7F7, -14170, 0xFFFFC8B1, 0xFFFFC8B9, 0xFFFFC8BB, 0xFFFFC8BF, -14140, 0xFFFFC8C7, 0xFFFFC8C9, 0xFFFFC8D3, 0xFFFFC8D5, 0xFFFFC8D6, 0xFFFFC8E0, -14109, -14099, -14097, 0xFFFFC8F2, 0xFFFFC8F4, -14090, 0xFFFFC8F9, 0xFFFFC8FD, 0xFFFFC9A3, 0xFFFFC9A6, -13910, -13907, -13906, -13905, 0xFFFFC9B8, 0xFFFFC9BA, 0xFFFFC9CA, -13870, 0xFFFFC9DD, 0xFFFFC9E9, 0xFFFFC9F9, 0xFFFFCAA6, 0xFFFFCAD5, -13601, -13406, -13404, -13400, 0xFFFFCBAA, 0xFFFFCBAD, 0xFFFFCBB1, 0xFFFFCBB5, 0xFFFFCBB9, 0xFFFFCBC9, 0xFFFFCBD1, 0xFFFFCBD4, 0xFFFFCBE1, -13340, 0xFFFFCBEF, 0xFFFFCBF2, 0xFFFFCBFA, 0xFFFFCCA5, 0xFFFFCCAE, 0xFFFFCCC0, -13107, -13096, -13095, -13091, -13076, 0xFFFFCCF4, 0xFFFFCCF9, -13060, 0xFFFFCDA8, 0xFFFFCDB5, 0xFFFFCDB9, -12860, 0xFFFFCDC6, 0xFFFFCDCC, 0xFFFFCDCF, 0xFFFFCDDA, 0xFFFFCDE1, 0xFFFFCDE3, 0xFFFFCDF4, 0xFFFFCDFE, 0xFFFFCEC1, 0xFFFFCECB, 0xFFFFCECE, 0xFFFFCED7, 0xFFFFCEF4, 0xFFFFCFB9, 0xFFFFCFC6, 0xFFFFCFE0, 0xFFFFCFF4, 0xFFFFD0A8, 0xFFFFD0BD, 0xFFFFD0C7, 0xFFFFD0D6, 0xFFFFD0DD, 0xFFFFD0E6, 0xFFFFD0F9, 0xFFFFD1A5, 0xFFFFD1AB, 0xFFFFD1B9, 0xFFFFD1C9, 0xFFFFD1EA, 0xFFFFD1FB, -11604, 0xFFFFD2BB, 0xFFFFD2F0, 0xFFFFD3A2, -11340, 0xFFFFD3B5, 0xFFFFD3C4, -11303, -11097, -11077, -11067, 0xFFFFD4D1, -11052, -11045, -11041, -11038, 0xFFFFD4F0, -11020, 0xFFFFD4F5, 0xFFFFD4F6, 0xFFFFD4FA, -10838, 0xFFFFD5B0, 0xFFFFD5C1, -10800, -10790, -10780, 0xFFFFD5F4, -10587, 0xFFFFD6D0, -10533, -10519, -10331, -10329, -10328, -10322, -10315, -10309, -10307, -10296, -10281, -10274, -10270, -10262, -10260, 0xFFFFD7F0, 0xFFFFD7F2};
        pystr = new String[]{"a", "ai", "an", "ang", "ao", "ba", "bai", "ban", "bang", "bao", "bei", "ben", "beng", "bi", "bian", "biao", "bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang", "cao", "ce", "ceng", "cha", "chai", "chan", "chang", "chao", "che", "chen", "cheng", "chi", "chong", "chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci", "cong", "cou", "cu", "cuan", "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao", "de", "deng", "di", "dian", "diao", "die", "ding", "diu", "dong", "dou", "du", "duan", "dui", "dun", "duo", "e", "en", "er", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu", "ga", "gai", "gan", "gang", "gao", "ge", "gei", "gen", "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun", "guo", "ha", "hai", "han", "hang", "hao", "he", "hei", "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui", "hun", "huo", "ji", "jia", "jian", "jiang", "jiao", "jie", "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan", "kang", "kao", "ke", "ken", "keng", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la", "lai", "lan", "lang", "lao", "le", "lei", "leng", "li", "lia", "lian", "liang", "liao", "lie", "lin", "ling", "liu", "long", "lou", "lu", "lv", "luan", "lue", "lun", "luo", "ma", "mai", "man", "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu", "mo", "mou", "mu", "na", "nai", "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang", "niao", "nie", "nin", "ning", "niu", "nong", "nu", "nv", "nuan", "nue", "nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pu", "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qiong", "qiu", "qu", "quan", "que", "qun", "ran", "rang", "rao", "re", "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui", "run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen", "seng", "sha", "shai", "shan", "shang", "shao", "she", "shen", "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang", "shui", "shun", "shuo", "si", "song", "sou", "su", "suan", "sui", "sun", "suo", "ta", "tai", "tan", "tang", "tao", "te", "teng", "ti", "tian", "tiao", "tie", "ting", "tong", "tou", "tu", "tuan", "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen", "weng", "wo", "wu", "xi", "xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya", "yan", "yang", "yao", "ye", "yi", "yin", "ying", "yo", "yong", "you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang", "zao", "ze", "zei", "zen", "zeng", "zha", "zhai", "zhan", "zhang", "zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu", "zhua", "zhuai", "zhuan", "zhuang", "zhui", "zhun", "zhuo", "zi", "zong", "zou", "zu", "zuan", "zui", "zun", "zuo"};
    }

    private StringBuilder buffer;

    /**
     * Chinese characters converted to ASCII code
     *
     * @param chs Chinese character
     * @return Ascii code corresponding to Chinese characters
     */
    private int getChsAscii(String chs) {
        int asc = 0;
        try {
            byte[] bytes = chs.getBytes("gb2312");
            if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
                throw new RuntimeException("illegal resource string");
            }
            if (bytes.length == 1) {
                asc = bytes[0];
            }
            if (bytes.length == 2) {
                int hightByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                asc = (256 * hightByte + lowByte) - 256 * 256;
            }
        } catch (Exception e) {
            System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
        }
        return asc;
    }

    /**
     * Word analysis
     *
     * @param str
     * @return
     */
    public String convert(String str) {
        String result = null;
        int ascii = getChsAscii(str);
        if (ascii > 0 && ascii < 160) {
            result = String.valueOf((char) ascii);
        } else {
            for (int i = (pinYinValue.length - 1); i >= 0; i--) {
                if (pinYinValue[i] <= ascii) {
                    result = pystr[i];
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Phrase analysis
     *
     * @param chs
     * @return
     */
    public String getPinYin(String chs) {
        String key, value = null;
        buffer = new StringBuilder();
        for (int i = 0; i < chs.length(); i++) {
            key = chs.substring(i, i + 1);
            // Determine whether it is Chinese characters (Chinese characters are more than two characters)
            try {
                if (key.getBytes("gb2312").length >= 2) {
                    value = (String) convert(key);
                    if (value == null) {
                        value = "unknown";
                    }
                } else {
                    value = key;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            buffer.append(value);
        }
        return buffer.toString();
    }

    /**
     * Determine whether it is Chinese
     */
    public static boolean isChinaString(String text) {
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if ((c >= 0x4e00) && (c <= 0x9fbb)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取字母列表的首字母
     * @param sortKeyString
     * @return
     */
    public String getSortKey(String sortKeyString) {
        String key = sortKeyString.substring(0, 1).toUpperCase();
        if (key.matches("[A-Z]")) {
            return key;
        } else if (isChinaString(key)){
            return getPinYin(key).substring(0, 1).toUpperCase();
        }
        return "#";
    }
}