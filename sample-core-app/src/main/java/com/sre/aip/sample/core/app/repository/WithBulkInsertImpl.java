package com.sre.aip.sample.core.app.repository;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WithBulkInsertImpl<T> implements WithBulkInsert<T> {

	private final NamedParameterJdbcTemplate template;

	public WithBulkInsertImpl(NamedParameterJdbcTemplate template) {
	    this.template = template;
	}

	public List<T> bulkInsert(List<T> models, Class<T> clazz) {
		if (models.isEmpty()) {
			return Collections.emptyList();
		}
		String sql = createSql(clazz);
		if (sql == null) {
			return Collections.emptyList();
		}
		int [] affectedRows = template.batchUpdate(sql,
				SqlParameterSourceUtils.createBatch(models));
		List<T> ret = new ArrayList<>();
		for (int i = 0 ; i < affectedRows.length ; i++) {
			if (affectedRows[i] == Statement.EXECUTE_FAILED) {
				ret.add(models.get(i));
			}
		}
		return ret;
	}

	@Nullable
	private String createSql(Class<T> clazz) {
		Table table = clazz.getAnnotation(Table.class);
		String tableName = table.value();
		List<String> fieldNames = getFieldNames(clazz);
	    if (fieldNames.isEmpty()) {
	    	return null;
		}
	    // Entityクラスのフィールド宣言の順がSQLテーブルのフィールドの宣言の順と異なると
		// うまく動作しない気がする
		return String.format("INSERT INTO %s VALUES (%s)",
				tableName,
				fieldNames.stream()
						.map(n -> ":" + n)
						.collect(Collectors.joining(",")));
	}

	private List<String> getFieldNames(Class<?> clazz) {
		// base case
		if (clazz.equals(Object.class)) {
			return Collections.emptyList();
		}
		List<String> ret = new ArrayList<>();
		for (Field f : clazz.getDeclaredFields()) {
			// use @Column value if available
			Column c = f.getAnnotation(Column.class);
			ret.add(c != null ? c.value() : f.getName());
		}
		// add the super class fields recursively
		ret.addAll(getFieldNames(clazz.getSuperclass()));
		return ret;
	}
}
