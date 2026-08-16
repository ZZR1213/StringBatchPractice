package com.example.demo.databaseReader;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StudentInfoRowmapper implements RowMapper<StudentInfo> {

	@Override
	public StudentInfo mapRow(ResultSet rs, int rowNum) throws SQLException {

		StudentInfo si = new StudentInfo();
		si.setId(rs.getInt("student_id"));
		si.setName(rs.getString("student_name"));
		si.setAddress(rs.getString("student_address"));
		si.setEmail(rs.getString("student_email"));
		return si;
	}

}
