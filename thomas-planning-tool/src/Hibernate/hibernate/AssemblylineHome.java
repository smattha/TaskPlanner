package hibernate;
// Generated Feb 6, 2019 3:37:36 PM by Hibernate Tools 3.5.0.Final

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Assemblyline.
 * 
 * @see hibernate.Assemblyline
 * @author Hibernate Tools
 */
public class AssemblylineHome {

	private static final Log log = LogFactory.getLog(AssemblylineHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Assemblyline transientInstance) {
		log.debug("persisting Assemblyline instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Assemblyline instance) {
		log.debug("attaching dirty Assemblyline instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Assemblyline instance) {
		log.debug("attaching clean Assemblyline instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Assemblyline persistentInstance) {
		log.debug("deleting Assemblyline instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Assemblyline merge(Assemblyline detachedInstance) {
		log.debug("merging Assemblyline instance");
		try {
			Assemblyline result = (Assemblyline) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Assemblyline findById(int id) {
		log.debug("getting Assemblyline instance with id: " + id);
		try {
			Assemblyline instance = (Assemblyline) sessionFactory.getCurrentSession().get("hibernate.Assemblyline", id);
			if (instance == null) {
				log.debug("get successful, no instance found");
			} else {
				log.debug("get successful, instance found");
			}
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Assemblyline instance) {
		log.debug("finding Assemblyline instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("hibernate.Assemblyline")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
