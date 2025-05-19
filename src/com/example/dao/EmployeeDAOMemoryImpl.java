package com.example.dao;

import java.util.ArrayList;
import java.util.List;
import com.example.model.Employee;

public class EmployeeDAOMemoryImpl implements EmployeeDAO{
	private Employee[] employeeArray = new Employee[10];
	
	@Override
	public void add(Employee emp) throws DAOException{
		int id = emp.getId();
		try {
		if(employeeArray[id] != null)
			throw new DAOException("員工已存在，新增失敗。");
		employeeArray[id] = emp;
		} catch(ArrayIndexOutOfBoundsException ex){
			throw new DAOException("員工編號需小於10，新增失敗。");
		}
	}
	
	@Override
	public void update(Employee emp) throws DAOException{
		int id = emp.getId();
		try {
			if(employeeArray[id] == null )
				throw new DAOException("員工不存在，修改失敗。");
			employeeArray[id] = emp;
		} catch(ArrayIndexOutOfBoundsException ex){
			throw new DAOException("員工編號需小於10，修改失敗。", ex);
		}
	}
	
	@Override
	public void delete(int id) throws DAOException{
		try {
			employeeArray[id] = null;	
		}catch(ArrayIndexOutOfBoundsException ex) {
			throw new DAOException("員工編號需小於10，刪除失敗。", ex);
		}
	}
	
	@Override
	public Employee findById(int id) throws DAOException{
		try {
			return employeeArray[id];
		} catch(ArrayIndexOutOfBoundsException ex){
			throw new DAOException("員工編號需小於10，查詢失敗。", ex);
		}
	}
	
	@Override
	public Employee[] getAllEmployees() {
		List<Employee> emps = new ArrayList<>();
		for(Employee e : employeeArray) {
			if(e != null) {
				emps.add(e);
			}
		}
		return emps.toArray(new Employee[0]);
	}

	@Override
	public void close()  {
		System.out.println("資源關閉......");
	    }
	}
