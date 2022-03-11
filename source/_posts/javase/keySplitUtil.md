title: 以等号分割的键值对字符串转JSON工具类
categories:
- [java,工具]
author: assertor
date: 2022-03-05 23:51:00
sticky: true
---
# 以等号分割的键值对字符串转JSON工具类

在数据处理的过程中需要将类似下面这种结构的数据转换为json格式的字符串，写了下面这个工具类实现转换
```java
 {uid=1111, id=0.1.74, version=2.2.0.200}
 ```
主要是使用逗号加空格的这种方式进行分割定位每一个键值对，然后以第一个等号为分隔符分开key和value。能够满足一些基本的无特殊字符情况的JSON转化。同时支持多层嵌套结构的数据。

```java
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
  * 格式转换工具类
  *
  * @since 2021-11-02
  */
public class FormatUtil {
  private static final Logger analysis = LoggerFactory.getLogger("report");

  /**
    - 将 studio 非标准数据转化为json
    - eg: {uid=1111, version=0.1.74, version=2.2.0.200}
    *
    - @return json格式数据
    */
  public static String formatJson(String text) {
    if (!isJson(text) && isMatchBrace(text)) {
      String strip = text.substring(1, text.length() - 1);
      // 特殊字符处理
      if (text.charAt(1) == '=') {
        strip = strip.substring(3);
      }
      if (text.charAt(text.length() - 2) == '=') {
        strip = strip.substring(0, strip.length() - 3);
      }
      strip = strip + ", ";
      char[] textArray = strip.toCharArray();
      // 当前的{}标识
      int sign = 0;
      List<String> words = Lists.newArrayList();
      // 当前指针位置
      int index = 0;
      for (int i = 0; i < textArray.length; i++) {
        if (textArray[i] == '{') {
          sign = sign + 1;
        }
        if (textArray[i] == '}') {
          sign = sign - 1;
        }
        if (textArray[i] == ',' && sign == 0 && textArray[i + 1] == ' ') {
          words.add(getStringUsingIndex(textArray, index, i - 1));
          index = i + 2;
        }
      }
      return jsonFormat(words, text);
    }
    return text;
  }

  /**
    - 判断是否为json格式
    *
    - @param content 文本
    - @return 布尔值
    */
  public static boolean isJson(String content) {
    if (StringUtils.isEmpty(content)) {
      return false;
    }
    boolean isJsonObject = true;
    boolean isJsonArray = true;
    try {
      JSONObject.parseObject(content);
    } catch (Exception e) {
      isJsonObject = false;
    }
    try {
      JSONObject.parseArray(content);
    } catch (Exception e) {
      isJsonArray = false;
    }
    return isJsonObject || isJsonArray;
  }
  
  /**
    - 判断是否被花括号包裹
    */
  private static boolean isMatchBrace(String text) {
    if (text == null || text.length() < 2) {
      return false;
    } else {
      char[] textChars = text.toCharArray();
      return textChars[0] == '{' && textChars[textChars.length - 1] == '}';
    }
  }

  /**
    - 根据参数从字符数组中获取字符串
    *
    - @param chars 字符数组
    - @param index 起始位置
    - @param end   结束位置
    - @return 字符串
    */
  private static String getStringUsingIndex(char[] chars, int index, int end) {
    StringBuilder splicing = new StringBuilder();
    for (int i = index; i < end + 1; i++) {
      char aChar = chars[i];
      splicing.append(aChar);
    }
    return splicing.toString();
  }

  /**
    - 以等号分割的键值对字符串数组转json
    *
    - @param words 字符串数组
    - @return json数据
    */

  private static String jsonFormat(List<String> words, String text) {
    JSONObject jsonObject = new JSONObject();
    for (String word : words) {
      String[] split = word.split("=");
      if (split.length == 1 && word.endsWith("=")) {
        continue;
      } else if (split.length <= 1) {
        analysis.error("occur illegal text:{}", text);
        continue;
      }
      jsonObject.put(split[0], formatJson(word.substring(word.indexOf("=") + 1)));
    }
    return jsonObject.toJSONString();
  }
}
```
**需要注意的情况**
+  对于value中有逗号加空格的情况需要特殊处理
+  value为数组的情况需要优化一下此工具类，判断数组中括号的开始和结束来判断数据