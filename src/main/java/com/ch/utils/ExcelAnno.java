/* =============================================================
 * Created: [2017年3月2日] by Allen
 * =============================================================
 *
 * Copyright 2014-2015 NetDragon Websoft Inc. All Rights Reserved
 *
 * =============================================================
 */

package com.ch.utils;

import java.lang.annotation.*;

/**
 * @author Allen
 * @since
 */
@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExcelAnno {
    String head() default "";
}
