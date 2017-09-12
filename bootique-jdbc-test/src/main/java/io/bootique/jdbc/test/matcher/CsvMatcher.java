package io.bootique.jdbc.test.matcher;

import io.bootique.jdbc.test.Column;
import io.bootique.jdbc.test.Table;
import io.bootique.jdbc.test.csv.CsvDataSet;
import io.bootique.jdbc.test.csv.CsvRecordSet;
import io.bootique.jdbc.test.csv.ValueConverter;
import io.bootique.resource.ResourceFactory;

import java.util.Objects;

/**
 * @since 0.24
 */
public class CsvMatcher {

    private ResourceFactory referenceCsvResource;
    private Table table;
    private String[] keyColumns;

    public CsvMatcher(Table table) {
        this.table = Objects.requireNonNull(table);
    }

    public CsvMatcher referenceCsvResource(ResourceFactory csvResource) {
        this.referenceCsvResource = csvResource;
        return this;
    }

    public CsvMatcher rowKeyColumns(String... keyColumns) {
        this.keyColumns = keyColumns;
        return this;
    }

    public void assertMatches() {

        Objects.requireNonNull(referenceCsvResource);

        CsvRecordSet refData = new CsvDataSet(table, new ValueConverter(), referenceCsvResource)
                .read();

        RowKeyFactory keyFactory = createRowKeyFactory(refData, keyColumns);
        new CsvMatcherRunner(table, refData, keyFactory).assertMatches();
    }

    private RowKeyFactory createRowKeyFactory(CsvRecordSet refData, String... keyColumns) {
        if (keyColumns == null || keyColumns.length == 0) {
            keyColumns = refData.getHeader().stream().map(Column::getName).toArray(i -> new String[i]);
        }

        return RowKeyFactory.create(refData.getHeader(), keyColumns);
    }
}
