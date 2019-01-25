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
 * Home object for domain model class Toolsdb.
 * @see hibernate.Toolsdb
 * @author Hibernate Tools
 */
public class ToolsdbHome {

	private static final Log log = LogFactory.getLog(ToolsdbHome.class);

	private final SessionFactory sessionFactory = getSessionFactory();

	protected SessionFactory getSessionFactory() {
		try {
			return (SessionFactory) new InitialContext().lookup("SessionFactory");
		} catch (Exception e) {
			log.error("Could not locate SessionFactory in JNDI", e);
			throw new IllegalStateException("Could not locate SessionFactory in JNDI");
		}
	}

	public void persist(Toolsdb transientInstance) {
		log.debug("persisting Toolsdb instance");
		try {
			sessionFactory.getCurrentSession().persist(transientInstance);
			log.debug("persist successful");
		} catch (RuntimeException re) {
			log.error("persist failed", re);
			throw re;
		}
	}

	public void attachDirty(Toolsdb instance) {
		log.debug("attaching dirty Toolsdb instance");
		try {
			sessionFactory.getCurrentSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Toolsdb instance) {
		log.debug("attaching clean Toolsdb instance");
		try {
			sessionFactory.getCurrentSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void delete(Toolsdb persistentInstance) {
		log.debug("deleting Toolsdb instance");
		try {
			sessionFactory.getCurrentSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Toolsdb merge(Toolsdb detachedInstance) {
		log.debug("merging Toolsdb instance");
		try {
			Toolsdb result = (Toolsdb) sessionFactory.getCurrentSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public Toolsdb findById(int id) {
		log.debug("getting Toolsdb instance with id: " + id);
		try {
			Toolsdb instance = (Toolsdb) sessionFactory.getCurrentSession().get("hibernate.Toolsdb", id);
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

	public List findByExample(Toolsdb instance) {
		log.debug("finding Toolsdb instance by example");
		try {
			List results = sessionFactory.getCurrentSession().createCriteria("hibernate.Toolsdb")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: " + results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}
}
