package hibernate;
// Generated Jan 25, 2019 4:02:58 PM by Hibernate Tools 3.5.0.Final

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Assemblystation.
 * @see hibernate.Assemblystation
 * @author Hibernate Tools
 */
public class AssemblystationHome {

	private static final Log log = LogFactory.getLog(AssemblystationHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Assemblystation transientInstance) {
		log.debug("persisting Assemblystation instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Assemblystation instance) {
		log.debug("attaching dirty Assemblystation instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Assemblystation instance) {
		log.debug("attaching clean Assemblystation instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Assemblystation persistentInstance) {
		log.debug("deleting Assemblystation instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Assemblystation merge(Assemblystation detachedInstance) {
		log.debug("merging Assemblystation instance");
		try {
			Assemblystation result = (Assemblystation) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Assemblystation findById(int id) {
		log.debug("getting Assemblystation instance with id: " + id);
		try {
			Assemblystation instance = (Assemblystation) sessionFactory.getCurrentSession()
					.get("hibernate.Assemblystation", id);
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

	public List findByExample(Assemblystation instance) {
		log.debug("finding Assemblystation instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("hibernate.Assemblystation")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
