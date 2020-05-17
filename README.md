## 字母排序列表效果
**字母列表的实际运用：提供根据字母排序列表，方便用户快速找到自己需要的内容。**

如果GitHub无法加载出来，可以查看根目录的“运行效果视频”查看是否满足你的需求

![字母列表效果图](https://img-blog.csdnimg.cn/20200517214532705.gif#pic_center)

## 下载地址
[https://github.com/sufadi/AlphabetList](https://github.com/sufadi/AlphabetList)

## 功能介绍
1. 实现列表按字母进行排序
2. 滑动列表同步更新侧边字母栏的选中事件
3. 侧边字母栏点击同步更新List的内容显示
4. 快速滑动时，视图中间显示字母小框
5. 提供搜索输入框进行快速查询

## 效果开发思路拆解
1. 实现普通ListView加载模拟数据
2. 实现字母排序功能（核心）
3. 实现自定义View的侧边字母栏(A~Z)
4. 实现侧边字母栏的点击事件
5. 实现ListView滑动时更新侧边字母栏的选中效果
6. 实现ListView惯性滑动时，显示字母小对话框的效果
7. 搜索框的模糊查询

### 1. 实现普通ListView加载模拟数据
#### 1.1 模拟数据或假数据准备（arrays.xml）
```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <!-- 模拟数据数据源 -->
    <string-array name="fake_data">
        <item>Angry Birds 2</item>
        <item>浏览器</item>
        <item>哔哩哔哩</item>
        <item>百度地图</item>
        <item>天际通</item>
        <item>AlarmDemo</item>
        <item>百词斩</item>
        <item>周杰伦</item>
    </string-array>
</resources>
```
#### 1.2 ListView加载模拟数据
```
    private void initValues() {
        sourceDataList = loadFakeData(getResources().getStringArray(R.array.fake_data));
        mAlphabetAadpter = new AlphabetSortAdapter(this, sourceDataList);
        sortListView.setAdapter(mAlphabetAadpter);
    }
```

### 2. 实现字母排序功能（核心）
其实国内做的字母列表排序，目前大部分只能支持英文和中文，若其他语言本身也是用a-z表示也是默认支持，但是非英文的一般怎么处理呢？例如中文是根据以下原理进行实现的
 [https://blog.csdn.net/zhuwentao2150/article/details/70230341?utm_medium=distribute.pc_relevant.none-task-blog-OPENSEARCH-4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-OPENSEARCH-4](https://blog.csdn.net/zhuwentao2150/article/details/70230341?utm_medium=distribute.pc_relevant.none-task-blog-OPENSEARCH-4&depth_1-utm_source=distribute.pc_relevant.none-task-blog-OPENSEARCH-4)

故一些类似俄罗斯语言，阿拉伯语等默认是不支持，在代码中默认规划到“#’分类中。例如如下代码片段

```

    /**
     * 获取字母列表的首字母
     * 1. 若为英文字母，则直接取首字母
     * 2. 若为中文，则先转换为拼音，再取首字母
     * 3. 非英文或中文，则默认非"#"分类
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
```
上述中关于中文转换拼音字母，目前大家都是使用CharacterParser进行实现，当然一些多音字还是无法准确识别。例如长城（zhangcheng非changcheng）。由于这个CharacterParser是本Demo的核心，故完整代码如下

```
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
     * 1. 若为英文字母，则直接取首字母
     * 2. 若为中文，则先转换为拼音，再取首字母
     * 3. 非英文或中文，则默认非"#"分类
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
```


### 3. 实现自定义View的侧边字母栏(A~Z)
侧边栏本质是继承View的自定义控件，将a~z按垂直分布进行排列，并计算出点击事件。原理上比较简单，对于实际项目中，我们一般主要定制化高度，字体颜色，选择时颜色即可。如果代码逻辑中重点查看onDraw()绘制逻辑和dispatchTouchEvent()选中坐标的计算即可。看代码应该很容易理解

```
package com.wu.su.alphabetlist.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.wu.su.alphabetlist.R;

/**
 * List of first letters on the right
 */
public class SideBarView extends View {

    public SideBarView(Context context) {
        super(context);
    }

    public SideBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SideBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static String[] characters = {"#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    private static final int SELECT_FONT_COLOR = 0xFF3399ff;
    private static final int NORMAL_FONT_COLOR = 0xFF999999;
    private static final double SCALING_RATIO = 0.85;

    private int ifSelected = -1;
    private Paint paint = new Paint();
    private String currCharacter = "#";
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    // 字母列表点击事件监听
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    // 绘制字母列表
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = (int) (getHeight() * SCALING_RATIO);
        int width = getWidth();
        int singleHeight = height / characters.length; // Get the height of each letter

        for (int i = 0; i < characters.length; i++) {
            paint.setColor(NORMAL_FONT_COLOR);
            paint.setAntiAlias(true);
            paint.setTextSize(getResources().getDimension(R.dimen.side_bar_font_size));
            if (characters[i].equals(currCharacter)) {
                paint.setColor(SELECT_FONT_COLOR);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(characters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(characters[i], xPos, yPos, paint);
            paint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldSelected = ifSelected;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int selected = (int) (y / (getHeight()* SCALING_RATIO) * characters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                ifSelected = -1;
                invalidate();
                break;
            default:
                if (oldSelected != selected) {
                    if (selected >= 0 && selected < characters.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(characters[selected]);
                        }
                        ifSelected = selected;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    public void setCurrCharacter(String character) {
        if (!currCharacter.equals(character)) {
            currCharacter = character;
            invalidate();
        }
    }
}
```

### 4. 实现侧边字母栏的点击事件
#### 4.1 自定义侧边字母栏SideBar的dispatchTouchEvent事件
主要在onTouch实际中，根据点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.计算哪个字母被点击了，并通知ListView显示对应首字母内容

```
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldSelected = ifSelected;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int selected = (int) (y / (getHeight()* SCALING_RATIO) * characters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                ifSelected = -1;
                invalidate();
                break;
            default:
                if (oldSelected != selected) {
                    if (selected >= 0 && selected < characters.length) {
                        if (listener != null) {
                            // 将事件通知ListView显示对应首字母内容
                            listener.onTouchingLetterChanged(characters[selected]);
                        }
                        ifSelected = selected;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }
```
#### 4.2 ListView的setSelection显示指定positon内容的方法
根据侧边字母栏选择的字母，通知ListView.setSelection调整到指定的position进行页面显示
```
        // 设置右侧触摸监听
        SideBarView.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                Log.d(TAG, "onTouchingLetterChanged:" + s);
                int position = mAlphabetAadpter.getPositionForSection(s.charAt(0));
                tv_dialog.setText(s);
                showDialog();
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });
```

### 5. 实现ListView滑动时更新侧边字母栏的选中效果
#### 5.1 ListView的OnScrollChangeListener事件
ListView有个很实用的方法getFirstVisiblePosition，可以获取当前列表第一个显示的子项，我们根据这个子项的首字母，同步更新SideBar的选中状态
```
        sortListView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int positon = sortListView.getFirstVisiblePosition();
                String alpha = mAlphabetAadpter.getAlpha(positon);
                SideBarView.setCurrCharacter(alpha);
                Log.d(TAG, "onScrollChange positon:" + positon + ", alpha:" + alpha);
            }
        });
```

#### 5.2 侧边字母栏onDraw中对选中的字母进行字体颜色改变
这里的绘制方法很简单粗暴，例如
```
    // 绘制字母列表
    protected void onDraw(Canvas canvas) {
		...
       if (characters[i].equals(currCharacter)) {
            paint.setColor(SELECT_FONT_COLOR);
            paint.setFakeBoldText(true);
       }
       ....
·	}
```

### 6. 实现ListView惯性滑动时，显示字母小对话框的效果
#### 6.1 惯性滑动事件-SCROLL_STATE_FLING
这个效果我是看到华为手机有这个效果，故新增了一下，主要是在

```
        sortListView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        //手指用力滑动
                        //手指离开listview后由于惯性继续滑动
                        showDialog();
                        break;
                    default:
                        break;
                }
            }
```
#### 6.2 小对话框
小对话框是根据Framelayou或RelativeLayout，将布局默认居中显示，但是默认是不显示，如果需要显示的情况下，短暂显示1秒
例如布局效果，重点见android:layout_centerInParent="true"

```
    <TextView
        android:id="@+id/tv_dialog"
        android:layout_width="60dp"
        android:layout_height="60dp"
        android:background="@drawable/alphabet_dialog_background"
        android:textSize="20sp"
        android:gravity="center"
        android:layout_centerInParent="true"
        android:visibility="gone" />
```
短暂显示的粗劣函数如下

```
    // 只显示 1 秒的字母对话框
    private void showDialog() {
        tv_dialog.setVisibility(View.VISIBLE);
        mHandle.removeMessages(MSG_HIDE_DIALOG);
        mHandle.sendEmptyMessageDelayed(MSG_HIDE_DIALOG, 1000);
    }

    private void hideDialog() {
        tv_dialog.setVisibility(View.GONE);
    }
```

### 7. 搜索框的模糊查询
#### 7.1 EditText实现即可
搜索框一般需要做得太高大上，只需要满足需求即可，本质来说，越简单越稳定。代码也好维护。例如我们只需要改变下颜色和背景即可

```
        <!-- 上面的搜索框 -->
        <EditText
            android:id="@+id/et_searchview"
            android:layout_marginTop="5dip"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:background="@drawable/alphabet_searchview_background"
            android:drawableLeft="@android:drawable/ic_menu_search"
            android:layout_gravity="top"
            android:hint="请输入关键字"
            android:singleLine="true"
            android:paddingLeft="10dp"
            android:layout_margin="10dp"
            android:textSize="15sp" />
```
#### 7.2 监听文本内容变化addTextChangedListener，并实时进行搜索

```
        // 根据输入框输入值的改变来过滤搜索
        et_searchview.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                startSearch(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void afterTextChanged(Editable s) { }
        });
```
#### 7.3 搜索功能
原理很简单，就是使用 contains 进行字母的匹配，如果是的话，都单独保存起来，并通知ListView显示这些内容。如果找不到就不显示。如果用户删掉了文本，则显示原生数据。

```
    /**
     * 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
     */
    private void startSearch(String filterStr) {
        List<SortModel> startSearchList = new ArrayList<SortModel>();
        if (TextUtils.isEmpty(filterStr)) {
            startSearchList = sourceDataList;
        } else {
            startSearchList.clear();
            for (SortModel sortModel : sourceDataList) {
                String name = sortModel.info;
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1
                        || mCharacterParser.getPinYin(name).toUpperCase().contains(filterStr.toString().toUpperCase())) {
                    startSearchList.add(sortModel);
                }
            }
        }

        Collections.sort(startSearchList, mPinyinComparator);
        mAlphabetAadpter.updateListView(startSearchList);
    }
```

上述中，对功能进行一个个拆解，这个效果就做出来的，感兴趣也可以试下。