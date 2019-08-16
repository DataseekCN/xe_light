package com.dataseek.xe.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class JdbcSupport {
	protected static Logger logger = LoggerFactory.getLogger(JdbcSupport.class);

	protected JdbcTemplate jdbcTemplate;
	protected NamedParameterJdbcTemplate npJdbcTemplate;

	public JdbcSupport() {
	}

	public JdbcSupport(JdbcTemplate jdbcTemplate) {
		setJdbcTemplate(jdbcTemplate);
	}

	public JdbcSupport(DataSource dataSource) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		setJdbcTemplate(jdbcTemplate);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNpJdbcTemplate() {
		return npJdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.npJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql) {
		try {
			logger.debug("sql:" + sql);
			long a = System.currentTimeMillis();
			int rs = jdbcTemplate.update(sql);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return rs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param param
	 * @return
	 */
	public int update(String sql, MapSqlParameterSource param) {
		try {
			logger.debug("sql param:" + param.getValues());
			String translateSql = TranslateSql.translate(sql, param);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			int rs = npJdbcTemplate.update(sql, param);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return rs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int update(String sql, Object... args) {
		try {
			logger.debug("sql param:" + Arrays.toString(args));
			String translateSql = TranslateSql.translate(sql, args);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			int rs = jdbcTemplate.update(sql, args);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return rs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 批量更新操作
	 * 
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(String... sql) {
		try {
			if (sql.length == 0) {
				logger.warn("batchUpdate sql length 0");
				return new int[0];
			}
			logger.debug("sql:" + sql[0]);
			long a = System.currentTimeMillis();
			int[] rs = jdbcTemplate.batchUpdate(sql);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return rs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 批量更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int[] batchUpdate(String sql, MapSqlParameterSource[] params) {
		try {
			if (params.length == 0) {
				logger.warn("batchUpdate sql params length 0");
				return new int[0];
			}
			logger.debug("sql param:" + params[0].getValues());
			String translateSql = TranslateSql.translate(sql, params[0]);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			int[] rs = npJdbcTemplate.batchUpdate(sql, params);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return rs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 批量更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int[] batchUpdate(String sql, List<? extends MapSqlParameterSource> params) {
		MapSqlParameterSource[] array = new MapSqlParameterSource[params.size()];
		for (int i = 0; i < params.size(); i++) {
			array[i] = params.get(i);
		}
		return batchUpdate(sql, array);
	}

	protected <T> boolean isBaseType(Class<T> clazz) {
		if (clazz.isAssignableFrom(Long.class)) {
			return true;
		} else if (clazz.isAssignableFrom(Integer.class)) {
			return true;
		} else if (clazz.isAssignableFrom(Short.class)) {
			return true;
		} else if (clazz.isAssignableFrom(Double.class)) {
			return true;
		} else if (clazz.isAssignableFrom(Float.class)) {
			return true;
		} else if (clazz.isAssignableFrom(String.class)) {
			return true;
		}
		return false;
	}

	/**
	 * 查询，返回java bean
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz) {
		try {
			logger.debug("sql:" + sql);
			long a = System.currentTimeMillis();
			BeanPropertyRowMapper<T> mapper = new BeanPropertyRowMapper<T>(clazz);
			List<T> objs = null;
			if (isBaseType(clazz)) {
				objs = jdbcTemplate.queryForList(sql, clazz);
			} else {
				objs = jdbcTemplate.query(sql, mapper);
			}
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return objs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param clazz
	 * @param paramSource
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz, MapSqlParameterSource paramSource) {
		try {
			logger.debug("sql param:" + paramSource.getValues());
			String translateSql = TranslateSql.translate(sql, paramSource);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			List<T> objs = null;
			if (isBaseType(clazz)) {
				objs = npJdbcTemplate.queryForList(sql, paramSource, clazz);
			} else {
				BeanPropertyRowMapper<T> mapper = new BeanPropertyRowMapper<T>(clazz);
				objs = npJdbcTemplate.query(sql, paramSource, mapper);
			}
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return objs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param clazz
	 * @param args
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz, Object... args) {
		try {
			logger.debug("sql param:" + Arrays.toString(args));
			String translateSql = TranslateSql.translate(sql, args);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			List<T> objs = null;
			if (isBaseType(clazz)) {
				objs = jdbcTemplate.queryForList(translateSql, clazz, args);
			} else {
				BeanPropertyRowMapper<T> mapper = new BeanPropertyRowMapper<T>(clazz);
				objs = jdbcTemplate.query(sql, mapper, args);
			}
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return objs;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql) {
		try {
			logger.debug("sql:" + sql);
			long a = System.currentTimeMillis();
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return list;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param paramSource
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, MapSqlParameterSource paramSource) {
		try {
			logger.debug("sql param:" + paramSource.getValues());
			String translateSql = TranslateSql.translate(sql, paramSource);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			List<Map<String, Object>> list = npJdbcTemplate.queryForList(sql, paramSource);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return list;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}

	/**
	 * 查询
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args) {
		try {
			logger.debug("sql param:" + Arrays.toString(args));
			String translateSql = TranslateSql.translate(sql, args);
			logger.debug("translateSql:" + translateSql);
			long a = System.currentTimeMillis();
			List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, args);
			long b = System.currentTimeMillis();
			logger.debug("exe sql complete,use time:" + (b - a));
			return list;
		} catch (Exception e) {
			throw new JdbcException(e);
		}
	}


}
