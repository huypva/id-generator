/*
 * Copyright (c) 2017 Baidu, Inc. All Rights Reserve.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.huypva.idgenerator.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * DateUtils provides date formatting, parsing
 *
 */
public class DateUtils {
    /**
     * Patterns
     */
    public static final DateTimeFormatter DAY_PATTERN = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String formatByDatePattern(LocalDate date) {
        return date.format(DAY_PATTERN);
    }

    /**
     * Calculate the number of days of two dates
     */
    public static long dayDiff(LocalDate dateBefore, LocalDate dateAfter) {
        return dateBefore.until(dateAfter, ChronoUnit.DAYS);
    }
}
