import java.util.List;
import java.util.Set;

import org.hibernate.Session;

//import hibernate.Argumentsdb;
import hibernate.HibernateUtil;
import hibernate.Operationsdb;
import testingDemo.xmlDemo;

public class testingHiper {

	public void  run() {


		
		
      
      
     /************************************************************************/
      
     Session sessionThree = HibernateUtil.getSessionFactory().openSession();
     sessionThree.beginTransaction();
      
     //second load() method example
     //Argumentsdb emp2 = (Argumentsdb) sessionThree.load("hibernate.Argumentsdb",1);

     //Operations op=(Operations) emp2.getOperationses().iterator()
     
     //List<Operations> opsL=(List<Operations>) emp2.getOperationses();

     //Set op=emp2.getOperationsdbs();   
    
     //Operationsdb op2= (Operationsdb) op.iterator().next();

	
	}

public static void main(String argv[]) {
	testingHiper d1=new testingHiper();
	d1.run();	
	}



}
