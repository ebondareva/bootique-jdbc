package io.bootique.jdbc.test.matcher;

import io.bootique.jdbc.test.Table;
import io.bootique.resource.ResourceFactory;

/**
 * Assists in making assertions about the data in a DB table.
 *
 * @since 0.24
 */
public class TableMatcher {

    private Table table;

    public TableMatcher(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public RowCountMatcher eq(String column, Object value) {
        return new RowCountMatcher(table).eq(column, value);
    }

    public void assertHasRows(int expectedRows) {
        new RowCountMatcher(table).assertHasRows(expectedRows);
    }

    public void assertIsPresent() {
        new RowCountMatcher(table).assertIsPresent();
    }

    public void assertIsAbsent() {
        new RowCountMatcher(table).assertIsAbsent();
    }

    public void assertMatchesCsv(String csvResource, String... keyColumns) {
        assertMatchesCsv(new ResourceFactory(csvResource), keyColumns);
    }

    public void assertMatchesCsv(ResourceFactory csvResource, String... keyColumns) {
        new CsvMatcher(table).referenceCsvResource(csvResource).rowKeyColumns(keyColumns).assertMatches();
    }
}
