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
 * class description： 打码
 *
 * @author MoBaiJun 2022/5/18 10:49
 */
public class MaskToStringBuilder extends ReflectionToStringBuilder {

    /**
     * his constructor outputs using the default style set with
     *
     * @param object object
     */
    public MaskToStringBuilder(Object object) {
        super(object);
    }

    /**
     * If the style is <code>null</code>, the default style is used.
     *
     * @param object object
     * @param style  style
     */
    public MaskToStringBuilder(Object object, ToStringStyle style) {
        super(object, style);
    }

    /**
     * Appends the fields and values defined by the given object of the given Class.
     * If a cycle is detected as an object is &quot;toString()'ed&quot;, such an object is rendered as if
     * <code>Object.toString()</code> had been called and not implemented by the object.
     *
     * @param clazz The class of object parameter
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
                .filter(field -> field.getName() != null)
                .forEach(field -> {
                    String fieldName = field.getName();
                    if (this.accept(field)) {
                        try {
                            Object fieldValue = this.getValue(field);
                            // 如果需要打码
                            Mask anno = field.getAnnotation(Mask.class);
                            if (anno != null) {
                                if (field.getType() == String.class) {
                                    String strFieldVal = (String) fieldValue;
                                    int length = strFieldVal.length();
                                    int totalNoMaskLen = anno.prefixNoMaskLen() + anno.suffixNoMaskLen();
                                    if (totalNoMaskLen == 0) {
                                        fieldValue = fillMask(anno.maskStr(), length);
                                    }
                                    if (totalNoMaskLen < length) {
                                        StringBuilder sb = new StringBuilder();
                                        for (int j = 0; j < strFieldVal.length(); j++) {
                                            if (j < anno.prefixNoMaskLen()) {
                                                sb.append(strFieldVal.charAt(j));
                                                continue;
                                            }
                                            if (j > (strFieldVal.length() - anno.suffixNoMaskLen() - 1)) {
                                                sb.append(strFieldVal.charAt(j));
                                                continue;
                                            }
                                            sb.append(anno.maskStr());
                                        }
                                        fieldValue = sb.toString();
                                    }
                                }
                            }
                            this.append(fieldName, fieldValue);
                        } catch (IllegalAccessException ex) {
                            throw new InternalError("Unexpected IllegalAccessException: " + ex.getMessage());
                        }
                    }
                });
    }

    private String fillMask(String maskStr, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(maskStr);
        }
        return sb.toString();
    }
}