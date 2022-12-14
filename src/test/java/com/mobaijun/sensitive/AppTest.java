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

import java.time.LocalDateTime;

/**
 * software：IntelliJ IDEA 2022.1
 * class name: AppTest
 * class description：
 *
 * @author MoBaiJun 2022/5/18 10:55
 */
public class AppTest {
    @Mask(prefixNoMaskLen = 3, suffixNoMaskLen = 4)
    private String arced;

    @Mask(prefixNoMaskLen = 1)
    private String name;

    @Mask(prefixNoMaskLen = 4)
    private String birthday;

    @Mask(prefixNoMaskLen = 1, suffixNoMaskLen = 8, maskStr = "-")
    private String email;

    public String getArced() {
        return arced;
    }

    public void setArced(String arced) {
        this.arced = arced;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        // 重写toString（）方法
        return new MaskToStringBuilder(this).toString();
    }

    /**
     * [arced=454*********5765,name=墨**,birthday=2022*************************,email=m--------@163.com]
     *
     * @param args 参数说明
     */
    public static void main(String[] args) {
        AppTest testDemo = new AppTest();
        testDemo.setArced("4547665157465765");
        testDemo.setName("墨白君");
        testDemo.setEmail("mobaijun8@163.com");
        testDemo.setBirthday(LocalDateTime.now().toString());
        System.out.println("testDemo = " + testDemo);
    }
}