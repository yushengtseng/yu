package com.example.dao;

import java.io.*;
import java.text.*;
import java.util.*;
import com.example.model.Employee;

public class EmployeeDAOFileImpl implements EmployeeDAO {
	
	private SortedMap<Integer, Employee> employees = new TreeMap<>();
	private SimpleDateFormat df = new SimpleDateFormat("MMM d, yyy",Locale.US);
	private String fileName;
	
	public EmployeeDAOFileImpl(String fileName) {
		this.fileName = fileName;
	}
	
	private void syncData() throws DAOException{
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
			employees.clear();
			String line;
			while((line = br.readLine()) != null && line.trim().length() != 0) {
				String[] data = line.split("\\|");
				try {
					int id = Integer.parseInt(data[0]);
					String fName = data[1];
					String lName = data[2];
					Date bDate = df.parse(data[3]);
					float salary = Float.parseFloat(data[4]);
					Employee emp = new Employee(id, fName, lName, bDate,salary);
					employees.put(id, emp);
					}catch(RuntimeException | ParseException ex) {
						System.err.println("資料轉換失敗:"+line);
					}
				}
			}catch(IOException ex) {
				throw new DAOException("資料讀取失敗",ex);
		}
	}
	
	private void commit() throws DAOException{
		try(PrintWriter pw = new PrintWriter(new FileWriter(fileName))){
			Set<Integer> index = employees.keySet();
			for(Integer i : index) {
				Employee emp = employees.get(i);
				String line = String.format("%d|%s|%s|%s|%.2f",
					emp.getId(),
					emp.getFirstName(),
					emp.getLastName(),
					df.format(emp.getBirthDate()),
					emp.getSalary());
				pw.println(line);
			}
			pw.flush();
		}catch(IOException ex) {
			throw new DAOException("資料寫出失敗。");
		}
	}
	
	@Override
	public void add(Employee emp) throws DAOException {
		int id = emp.getId();
		if(employees.containsKey(id))
			throw new DAOException(id+"號員工已存在，新增失敗。");
		employees.put(id, emp);
		commit();
	}

	@Override
	public void update(Employee emp) throws DAOException {
		int id = emp.getId();
		if(!employees.containsKey(id))
			throw new DAOException(id+"號員工不存在，修改失敗。");
		employees.put(id, emp);
		commit();
	}

	@Override
	public void delete(int id) throws DAOException {
		if(!employees.containsKey(id))
			throw new DAOException(id+"號員工不存在，刪除失敗。");
		employees.remove(id);
		commit();
	}

	@Override
	public Employee findById(int id) throws DAOException {
		syncData();
		Employee emp = employees.get(id);
		if(emp == null)
			throw new DAOException(id+"號員工不存在。");
		return emp;
	}

	@Override
	public Employee[] getAllEmployees() throws DAOException {
		syncData();
		Collection<Employee> emps = employees.values();
		return emps.toArray(new Employee[0]);
	}
	
	@Override
	public void close() throws Exception {
		System.out.println("資源關閉。");
	}
}
