package hibernate;
// Generated Jan 25, 2019 5:59:12 PM by Hibernate Tools 3.5.0.Final

import java.util.List;
import javax.naming.InitialContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;

/**
 * Home object for domain model class Toolparameters.
 * @see hibernate.Toolparameters
 * @author Hibernate Tools
 */
public class ToolparametersHome {

	private static final Log log = LogFactory.getLog(ToolparametersHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Toolparameters transientInstance) {
		log.debug("persisting Toolparameters instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Toolparameters instance) {
		log.debug("attaching dirty Toolparameters instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Toolparameters instance) {
		log.debug("attaching clean Toolparameters instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Toolparameters persistentInstance) {
		log.debug("deleting Toolparameters instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Toolparameters merge(Toolparameters detachedInstance) {
		log.debug("merging Toolparameters instance");
		try {
			Toolparameters result = (Toolparameters) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Toolparameters findById(int id) {
		log.debug("getting Toolparameters instance with id: " + id);
		try {
			Toolparameters instance = (Toolparameters) sessionFactory.getCurrentSession()
					.get("hibernate.Toolparameters", id);
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

	public List findByExample(Toolparameters instance) {
		log.debug("finding Toolparameters instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("hibernate.Toolparameters")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
