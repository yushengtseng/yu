package com.example.dao;

import com.example.model.Employee;
import java.sql.*;
import java.util.ArrayList;

public class EmployeeDAOJDBCImpl implements EmployeeDAO {
	
	private Connection conn;	

	EmployeeDAOJDBCImpl() {
        String url = "jdbc:mysql://localhost:3306/EmployeeDB";
        String username = "root";
        String password = "abc123";
        try {
        	conn = DriverManager.getConnection(url, username, password);
        } catch(SQLException ex) {
        	System.out.println("資料庫連線建立失敗:"+ex);
        	System.exit(0);
        }
	}

	@Override
	public void close() throws Exception {
		try {
			if (conn!=null) {
				conn.close();
			}
		} catch(SQLException ex) {
        	System.out.println("資料庫連線關閉失敗:"+ex);
        }
	}

	@Override
	public void add(Employee emp) throws DAOException {
		//INSERT INTO EMPLOYEE VALUES (id, firstName, lastName, birthDate, salary)
		String sql = "INSERT INTO EMPLOYEE VALUES (?,?,?,?,?)";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, emp.getId());
			pstmt.setString(2, emp.getFirstName());
			pstmt.setString(3, emp.getLastName());
			pstmt.setDate(4, new java.sql.Date(emp.getBirthDate().getTime()));
			pstmt.setFloat(5, emp.getSalary());
			if(pstmt.executeUpdate()!=1)
				throw new DAOException("新增員工失敗!");
		} catch(SQLException ex) {
			throw new DAOException("資料庫新增發生錯誤:", ex);
        }
	}

	@Override
	public void update(Employee emp) throws DAOException {
		//UPDATE EMPLOYEE SET FIRSTNAME=firstName, LASTNAME=lastName, BIRTHDATE=birthDate, SALARY=salary WHERE ID=id;
		String sql = "UPDATE EMPLOYEE SET FIRSTNAME=?, LASTNAME=?, BIRTHDATE=?, SALARY=? WHERE ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(5, emp.getId());
			pstmt.setString(1, emp.getFirstName());
			pstmt.setString(2, emp.getLastName());
			pstmt.setDate(3, new java.sql.Date(emp.getBirthDate().getTime()));
			pstmt.setFloat(4, emp.getSalary());
			if(pstmt.executeUpdate()!=1)
				throw new DAOException("更新員工失敗!");
		} catch(SQLException ex) {
			throw new DAOException("資料庫更新發生錯誤:", ex);
        }
	}

	@Override
	public void delete(int id) throws DAOException {
		//DELETE FROM EMPLOYEE WHERE ID=id
		String sql = "DELETE FROM EMPLOYEE WHERE ID=?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, id);
			if(pstmt.executeUpdate()!=1)
				throw new DAOException("刪除員工失敗!");
		} catch(SQLException ex) {
			throw new DAOException("資料庫刪除發生錯誤:", ex);
        }
	}

	@Override
	public Employee findById(int id) throws DAOException {
		//SELECT * FROM EMPLOYEE WHERE ID=id
		String query = "SELECT * FROM EMPLOYEE WHERE ID=?";
		Employee emp = null;
		try(PreparedStatement pstmt = conn.prepareStatement(query)){
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next())
				emp =  new Employee(rs.getInt("ID"), 
						rs.getString("FIRSTNAME"), rs.getString("LASTNAME"),
						rs.getDate("BIRTHDATE"), rs.getFloat("SALARY"));
			return emp;
		} catch(SQLException ex) {
			throw new DAOException("資料庫查詢發生錯誤:", ex);
        }
	}

	@Override
	public Employee[] getAllEmployees() throws DAOException {
		//SELECT * FROM EMPLOYEE
		String query = "SELECT * FROM EMPLOYEE";
		ArrayList<Employee> emps = new ArrayList<>();;
		try(Statement stmt = conn.createStatement()){
			ResultSet rs = stmt.executeQuery(query);
			while(rs.next())
				emps.add(new Employee(rs.getInt("ID"), 
							rs.getString("FIRSTNAME"), rs.getString("LASTNAME"),
							rs.getDate("BIRTHDATE"), rs.getFloat("SALARY")));
			return emps.toArray(new Employee[0]);
		} catch(SQLException ex) {
			throw new DAOException("資料庫查詢發生錯誤:", ex);
        }
	}

}
