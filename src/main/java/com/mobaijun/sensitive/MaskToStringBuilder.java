/*
 * Copyright (C) 2022 www.mobaijun.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mobaijun.sensitive;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * software：IntelliJ IDEA 2022.1
 * class name: MaskToStringBuilder
 * class description：MaskToStringBuilder 用于处理输出对象的字符串表示形式，支持字段脱敏。
 *
 * @author MoBaiJun 2022/5/18 10:49
 */
public class MaskToStringBuilder extends ReflectionToStringBuilder {

    /**
     * 默认构造函数，使用默认样式输出。
     *
     * @param object 对象
     */
    public MaskToStringBuilder(Object object) {
        super(object);
    }

    /**
     * 构造函数，允许设置输出样式。
     *
     * @param object 对象
     * @param style  样式
     */
    public MaskToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    /**
     * 将给定类的对象定义的字段及其值追加到字符串表示形式中。
     *
     * @param clazz 对象的类
     */
    @SuppressWarnings("all")
    protected void appendFieldsIn(Class clazz) {
        if (clazz.isArray()) {
            this.reflectionAppendArray(this.getObject());
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        AccessibleObject.setAccessible(fields, true);

        Arrays.stream(fields)
                .filter(field -> field.getName() != null && accept(field))
                .forEach(field -> {
                    String fieldName = field.getName();
                    try {
                        Object fieldValue = getValue(field);
                        Mask anno = field.getAnnotation(Mask.class);

                        if (anno != null && field.getType() == String.class) {
                            fieldValue = applyMask((String) fieldValue, anno);
                        }

                        append(fieldName, fieldValue);
                    } catch (IllegalAccessException ex) {
                        throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                    }
                });
    }

    /**
     * 应用脱敏规则到字符串字段。
     *
     * @param strFieldVal 字符串字段值
     * @param anno        脱敏注解
     * @return 脱敏后的字符串
     */
    private String applyMask(String strFieldVal, Mask anno) {
        int length = strFieldVal.length();
        int totalNoMaskLen = anno.prefixNoMaskLen() + anno.suffixNoMaskLen();

        if (totalNoMaskLen == 0) {
            return fillMask(anno.maskStr(), length);
        }

        if (totalNoMaskLen < length) {
            return maskPartially(strFieldVal, anno);
        }

        return strFieldVal;
    }

    /**
     * 部分脱敏，按照脱敏规则脱敏指定部分字符。
     *
     * @param strFieldVal 字符串字段值
     * @param anno        脱敏注解
     * @return 脱敏后的字符串
     */
    private String maskPartially(String strFieldVal, Mask anno) {
        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < strFieldVal.length(); j++) {
            if (j < anno.prefixNoMaskLen() || j > (strFieldVal.length() - anno.suffixNoMaskLen() - 1)) {
                sb.append(strFieldVal.charAt(j));
            } else {
                sb.append(anno.maskStr());
            }
        }

        return sb.toString();
    }

    /**
     * 填充脱敏字符串，生成指定长度的脱敏字符。
     *
     * @param maskStr 脱敏字符
     * @param length  字符串长度
     * @return 脱敏后的字符串
     */
    private String fillMask(String maskStr, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(maskStr);
        }
        return sb.toString();
    }
}