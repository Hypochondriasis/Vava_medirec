package org.medirec.medirec.backend.service;

import com.backend.config.DatabaseConfig;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.medirec.medirec.backend.model.IgnoreColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {

	private static final Logger logger = LoggerFactory.getLogger(
		DatabaseService.class
	);

	private DatabaseService() {
		// prevent instantiation
	}

	private static String getTableName(Class<?> cls) throws Exception {
		String table = (String) cls.getField("TABLE").get(null);
		logger.debug(
			"Resolved table name '{}' for class {}",
			table,
			cls.getSimpleName()
		);
		return table;
	}

	private static List<Field> getColumns(Class<?> cls) {
		List<Field> cols = new ArrayList<>();
		for (Field f : cls.getDeclaredFields()) {
			if (!Modifier.isStatic(f.getModifiers())) {
				cols.add(f);
			}
		}
		logger.debug(
			"Found {} column(s) for class {}",
			cols.size(),
			cls.getSimpleName()
		);
		return cols;
	}

	private static <T> T mapRow(ResultSet rs, Class<T> cls) throws Exception {
		T obj = cls.getDeclaredConstructor().newInstance();
		for (Field f : getColumns(cls)) {
			if (f.isAnnotationPresent(IgnoreColumn.class)) {
				continue; // Skip this field
			}
			f.setAccessible(true);
			Object val = rs.getObject(f.getName());
			f.set(obj, val);
		}
		logger.trace("Mapped row to {}", obj);
		return obj;
	}

	public static <T> T getOne(Class<T> cls, int id) throws Exception {
		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		List<Field> cols = getColumns(cls);
		String colList = cols.stream()
				.filter(field -> !field.isAnnotationPresent(IgnoreColumn.class))
				.map(Field::getName)
				.collect(Collectors.joining(", "));

		String sql = String.format(
			"SELECT %s FROM %s WHERE id = ?",
			colList,
			table
		);

		logger.debug("getOne: SQL = {}, id = {}", sql, id);
		try (
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)
		) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (!rs.next()) {
					logger.info(
						"getOne: no {} found with id={}",
						cls.getSimpleName(),
						id
					);
					return null;
				}
				T result = mapRow(rs, cls);
				logger.info(
					"getOne: retrieved {} with id={}",
					cls.getSimpleName(),
					id
				);
				return result;
			}
		} catch (Exception e) {
			logger.error(
				"Error in getOne for {} id={}",
				cls.getSimpleName(),
				id,
				e
			);
			throw e;
		}
	}

	public static <T> List<T> getAll(Class<T> cls) throws Exception {
		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		List<Field> cols = getColumns(cls);
		String colList = cols.stream()
				.filter(field -> !field.isAnnotationPresent(IgnoreColumn.class))
				.map(Field::getName)
				.collect(Collectors.joining(", "));
		String sql = String.format("SELECT %s FROM %s", colList, table);

		logger.debug("getAll: SQL = {}", sql);
		List<T> list = new ArrayList<>();
		try (
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery()
		) {
			while (rs.next()) {
				list.add(mapRow(rs, cls));
			}
			logger.info(
				"getAll: retrieved {} record(s) from {}",
				list.size(),
				table
			);
			return list;
		} catch (Exception e) {
			logger.error("Error in getAll for {}", cls.getSimpleName(), e);
			throw e;
		}
	}

	public static <T> T createOrUpdate(T obj) throws Exception {
		Class<?> cls = obj.getClass();
		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		List<Field> cols = getColumns(cls);
		Field idField = cls.getDeclaredField("id");
		idField.setAccessible(true);
		Object idVal = idField.get(obj);

		List<Field> nonId = new ArrayList<>();
		for (Field f : cols) {
			if (!f.getName().equals("id")) nonId.add(f);
		}

		try (Connection conn = DatabaseConfig.getConnection()) {
			if ((Integer) idVal == 0) {
				// INSERT
				String colNames = String.join(
					", ",
					nonId.stream().map(Field::getName).toArray(String[]::new)
				);
				String placeholders = String.join(
					", ",
					Collections.nCopies(nonId.size(), "?")
				);
				String sql = String.format(
					"INSERT INTO %s (%s) VALUES (%s) RETURNING id",
					table,
					colNames,
					placeholders
				);
				logger.debug("create: SQL = {}, values = {}", sql, nonId);

				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					for (int i = 0; i < nonId.size(); i++) {
						Field f = nonId.get(i);
						f.setAccessible(true);
						ps.setObject(i + 1, f.get(obj));
					}
					try (ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							idField.set(obj, rs.getObject(1));
							logger.info(
								"Inserted new {} with generated id={}",
								cls.getSimpleName(),
								rs.getObject(1)
							);
						}
					}
				}
			} else {
				// UPDATE
				String setClause = String.join(
					", ",
					nonId
						.stream()
						.map(f -> f.getName() + " = ?")
						.toArray(String[]::new)
				);
				String sql = String.format(
					"UPDATE %s SET %s WHERE id = ?",
					table,
					setClause
				);
				logger.debug(
					"update: SQL = {}, id = {}, values = {}",
					sql,
					idVal,
					nonId
				);

				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					int idx = 1;
					for (Field f : nonId) {
						f.setAccessible(true);
						ps.setObject(idx++, f.get(obj));
					}
					ps.setObject(idx, idVal);
					int updated = ps.executeUpdate();
					logger.info(
						"Updated {} record(s) in {} for id={}",
						updated,
						table,
						idVal
					);
				}
			}
		} catch (Exception e) {
			logger.error(
				"Error in createOrUpdate for {} [{}]",
				cls.getSimpleName(),
				idVal,
				e
			);
			throw e;
		}

		return obj;
	}

	public static <T> void delete(Class<T> cls, int id) throws Exception {
		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		String sql = String.format("DELETE FROM %s WHERE id = ?", table);
		logger.debug("delete: SQL = {}, id = {}", sql, id);

		try (
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)
		) {
			ps.setInt(1, id);
			int affected = ps.executeUpdate();
			logger.info(
				"Deleted {} record(s) from {} for id={}",
				affected,
				table,
				id
			);
		} catch (Exception e) {
			logger.error(
				"Error in delete for {} id={}",
				cls.getSimpleName(),
				id,
				e
			);
			throw e;
		}
	}

	public static <T> ArrayList<T> getAllInGroup(
		Class<T> cls,
		String foreignKeyColumn,
		Integer foreignKeyId
	) throws Exception {
		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		List<Field> cols = getColumns(cls);
		String colList = cols.stream()
				.filter(field -> !field.isAnnotationPresent(IgnoreColumn.class))
				.map(Field::getName)
				.collect(Collectors.joining(", "));
		String sql = String.format(
			"SELECT %s FROM %s WHERE %s = ?",
			colList,
			table,
			foreignKeyColumn
		);

		logger.debug(
			"getAllInGroup: SQL = {}, {} = {}",
			sql,
			foreignKeyColumn,
			foreignKeyId
		);
		ArrayList<T> list = new ArrayList<>();
		try (
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)
		) {
			ps.setInt(1, foreignKeyId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					list.add(mapRow(rs, cls));
				}
			}
			logger.info(
				"getAllInGroup: retrieved {} record(s) from {} where {}={}",
				list.size(),
				table,
				foreignKeyColumn,
				foreignKeyId
			);
			return list;
		} catch (Exception e) {
			logger.error(
				"Error in getAllInGroup for {} {}={}",
				cls.getSimpleName(),
				foreignKeyColumn,
				foreignKeyId,
				e
			);
			throw e;
		}
	}

	public static <T> List<T> getMultiple(Class<T> cls, List<Integer> ids)
		throws Exception {
		if (ids == null || ids.isEmpty()) {
			logger.info("getMultiple: no IDs provided, returning empty list");
			return Collections.emptyList();
		}

		String table = getTableName(cls);
		table = table.replace("\"", "");
		if (table.equals("user")) table += "s";
		List<Field> cols = getColumns(cls);
		String colList = cols.stream()
				.filter(field -> !field.isAnnotationPresent(IgnoreColumn.class))
				.map(Field::getName)
				.collect(Collectors.joining(", "));

		String placeholders = String.join(
			", ",
			Collections.nCopies(ids.size(), "?")
		);

		String sql = String.format(
			"SELECT %s FROM %s WHERE id IN (%s)",
			colList,
			table,
			placeholders
		);
		logger.debug("getMultiple: SQL = {}, ids = {}", sql, ids);

		List<T> result = new ArrayList<>();
		try (
			Connection conn = DatabaseConfig.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql)
		) {
			for (int i = 0; i < ids.size(); i++) {
				ps.setInt(i + 1, ids.get(i));
			}
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result.add(mapRow(rs, cls));
				}
			}
			logger.info(
				"getMultiple: retrieved {} record(s) from {}",
				result.size(),
				table
			);
			return result;
		} catch (Exception e) {
			logger.error(
				"Error in getMultiple for {} ids={}",
				cls.getSimpleName(),
				ids,
				e
			);
			throw e;
		}
	}
}
