package com.example.springbatchjpajdbcdemo.reader;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import com.example.springbatchjpajdbcdemo.model.Line;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JdbcCursorLineReader extends JdbcCursorItemReader<Line> implements ItemReader<Line> {

	public JdbcCursorLineReader(@Autowired DataSource dataSource) {
		setDataSource(dataSource);
		setSql("SELECT id, name, dob FROM line");
		setFetchSize(100);
		setRowMapper(new EmployeeRowMapper());
	}

	public class EmployeeRowMapper implements RowMapper<Line> {
		@Override
		public Line mapRow(ResultSet rs, int rowNum) throws SQLException {
			Line line = new Line();
			line.setId(rs.getLong("id"));
			line.setName(rs.getString("name"));
			line.setDob(rs.getDate("dob").toLocalDate());
			log.info("line inside row mapper {}", line);
			return line;
		}
	}
}
