/*
 * Copyright (c) 2016 Mnubo. Released under MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.mnubo.sdk_android_test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.SneakyThrows;
import lombok.Value;

public class ResitutionTestHelper {

    static class NoResult extends Throwable { }

    @Value
    static class SearchResultColumn {
        final String label;
        final String type;

        @JsonCreator
        public SearchResultColumn(@JsonProperty("label") String label, @JsonProperty("type") String type) {
            this.label = label;
            this.type = type;
        }

        static SearchResultColumn text(String label) {
            return new SearchResultColumn(label, "text");
        }

        static SearchResultColumn datetime(String label) {
            return new SearchResultColumn(label, "datetime");
        }
    }

    @Value
    static class SearchResult {
        final List<SearchResultColumn> columns;
        final List<List<Object>> rows;

        @JsonCreator
        public SearchResult(@JsonProperty("columns") List<SearchResultColumn> columns, @JsonProperty("rows") List<List<Object>> rows) {
            this.columns = columns;
            this.rows = rows;
        }

        @SneakyThrows
        public List<Map<SearchResultColumn, Object>> resultsAsMap() {
            if (rows.isEmpty()) throw new NoResult();

            List<Map<SearchResultColumn, Object>> results = new ArrayList<>(rows.size());
            for (int row = 0; row < rows.size(); row++) {
                Map<SearchResultColumn, Object> result = new HashMap<>(columns.size());
                for (int column = 0; column < columns.size(); column++) {
                    result.put(columns.get(column), rows.get(row).get(column));
                }
                results.add(result);
            }
            return results;
        }

        public Map<SearchResultColumn, Object> firstResult() {
            return resultsAsMap().get(0);
        }
    }

}
