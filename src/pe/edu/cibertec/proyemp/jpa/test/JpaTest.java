package pe.edu.cibertec.proyemp.jpa.test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import pe.edu.cibertec.proyemp.jpa.domain.Departamento;
import pe.edu.cibertec.proyemp.jpa.domain.Empleado;
import pe.edu.cibertec.proyemp.util.UtilFormat;

public class JpaTest {
private EntityManager manager;

private JpaTest(EntityManager manager){
	this.manager = manager; //Inyeccion de dependencia: Se setea el manager
}

public static void main(String[] args) {
	//Utilizamos patron factory
	EntityManagerFactory factory = Persistence.createEntityManagerFactory("MyPersistenceUnit1");//Factory esta amarrado al persistence("MyPersistenceUnit1")
	
	//Obtenemos el entity manager
	EntityManager em = factory.createEntityManager();
	
	JpaTest test = new JpaTest(em);
	
	//Definimos la transaccion
	EntityTransaction tx = em.getTransaction();
	
	tx.begin();
	
	//Insert, update, delete
	test.crearEmpleados();
	
	tx.commit();
	test.listarEmpleados();
	test.obtenerEmpleadoPorID(new Long(1));
	}

private void obtenerEmpleadoPorID(Long id) {
	
	//Forma inyeccion SQL

	//1° forma
//	Empleado emp = manager.createQuery(
//			"select e from Empleado e where e.id= ? ",Empleado.class)
//			.setParameter(1, id).getSingleResult();
//	
//	System.out.println(emp);
	
	//2° Forma
//	Empleado emp = manager.createQuery(
//			"select e from Empleado e where e.id= :myID ",Empleado.class)//Alias: "myID"
//			.setParameter("myID", id).getSingleResult(); // Es mas seguro 
//	
//	System.out.println(emp);
	
	//3! Forma (funciona por id o primary key)
	Empleado emp = manager.find(Empleado.class, id);
			
	
	//1. where e.id=" + id //=>Ya no se usa xq no es seguro
	//2. where e.id= ? " //=> Es por un tema fe seguridad
}

private void listarEmpleados() {
	String jql = "select e from Empleado e";
	String jql2 = "from Empleado";
	
	List<Empleado> empleados = manager.createQuery(jql, Empleado.class).getResultList(); // 2 forma de hacer
	
	//List<Empleado> empleados = manager.createQuery("select e from Empleado e", Empleado.class).getResultList(); // 1 forma de hacer
	
	//List<Empleado> empleados = manager.createNativeQuery("select * from Empleado", Empleado.class).getResultList(); // 3 forma de hacer
	
	for(Empleado empleado : empleados){
		System.out.println(empleado);
		//System.out.println(empleado.getNombre());
	}
}

private void crearEmpleados() {
	Departamento lima = new Departamento("Lima");
	//lima.setNombre("Lima");
	manager.persist(lima);
	
	Departamento aqp = new Departamento("Arequipa");
	manager.persist(aqp);
	
//	LocalDate fecha = LocalDate.of(2012, Month.JULY, 18); 
//	Date f = UtilFormat.getFecha(fecha); //MENCIONAMOS EN UTILFORMAT
	
	Empleado emp1 = new Empleado("Luis", new BigDecimal(1200.00), 
			UtilFormat.getFecha(2012, Month.JULY, 14),lima);
	Empleado emp2 = new Empleado("Marco", new BigDecimal(800.00), 
			UtilFormat.getFecha(2000, Month.APRIL, 18),lima);
	Empleado emp3 = new Empleado("Maria", new BigDecimal(950.00), 
			UtilFormat.getFecha(2001, Month.DECEMBER, 4),aqp);
	Empleado emp4 = new Empleado("Carlos", new BigDecimal(1000.00), 
			UtilFormat.getFecha(2015, Month.OCTOBER, 10),aqp);
	
	manager.persist(emp1);
	manager.persist(emp2);
	manager.persist(emp3);
	manager.persist(emp4);
	
}
}
