package com.dataseek.xe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.ParsedSql;

public class TranslateSql {
	protected static Logger logger = LoggerFactory.getLogger(TranslateSql.class);

	public static String translate(String sql, MapSqlParameterSource param) {
		try {
			ParsedSql parsedSql = NamedParameterUtils.parseSqlStatement(sql);
			String sqlToUse = NamedParameterUtils.substituteNamedParameters(parsedSql, param);
			Object[] params = NamedParameterUtils.buildValueArray(parsedSql, param, null);
			return translate(sqlToUse, params);
		} catch (Exception e) {
			logger.warn(e.toString(), e);
			return "translate error->" + sql;
		}
	}

	public static String translate(String sql, Object[] args) {
		StringBuilder sb = new StringBuilder();
		try {
			char[] statement = sql.toCharArray();
			int count = 0;
			for (int i = 0; i < statement.length; i++) {
				if (statement[i] == '?') {
					count++;
					if (count > args.length) {
						logger.error("translate sql error");
						break;
					}
					Object p = args[count - 1];
					if (p instanceof String) {
						sb.append("'").append(p).append("'");
					} else if (p instanceof Long) {
						sb.append(p);
					} else if (p instanceof Integer) {
						sb.append(p);
					} else if (p instanceof Short) {
						sb.append(p);
					} else if (p instanceof Double) {
						sb.append(p);
					} else if (p instanceof Float) {
						sb.append(p);
					} else {
						sb.append("'").append(p).append("'");
					}
				} else {
					sb.append(statement[i]);
				}
			}
		} catch (Exception e) {
			logger.warn(e.toString(), e);
			return "translate error->" + sql;
		}
		return sb.toString();
	}
}
