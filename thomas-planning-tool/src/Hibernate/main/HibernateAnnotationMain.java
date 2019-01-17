package main;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.Employee1;
import model.HibernateUtil;

public class HibernateAnnotationMain {
	



		public static void main(String[] args) {
			Employee1 emp = new Employee1();
			
			emp.setId(101);
			emp.setEl_id(1);
			emp.setMaterial("eeee");
			emp.setMaxDimension(12);
			emp.setOperations_id(1);
			emp.setPartscol("1");
			emp.setStifness(1);
			emp.setWeight(7777);
			
			

		
		//Get Session
		SessionFactory sessionFactory = HibernateUtil.getSessionAnnotationFactory();
		Session session = sessionFactory.getCurrentSession();
		//start transaction
		session.beginTransaction();
		//Save the Model object
		session.save(emp);
		//Commit transaction
		session.getTransaction().commit();
		System.out.println("Employee ID="+emp.getId());
		
		//terminate session factory, otherwise program won't end
		sessionFactory.close();
	}

	
	
	
}
