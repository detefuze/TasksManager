package com.ru.klimash.service;

import com.ru.klimash.App;
import com.ru.klimash.ListManagerController;
import com.ru.klimash.entity.Task;
import com.ru.klimash.exceptions.ExistingTaskException;
import com.ru.klimash.exceptions.NotExistingTaskException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class HibernateRunner implements TaskManagerService {
    // Константа статуса выполнения команды
    private static String STATUS = "Waiting";

    private final static Logger logger_hr = LoggerFactory.getLogger(HibernateRunner.class);

    public Logger getLogger() {
        return logger_hr;
    }

    // Геттер статуса
    public static String getSTATUS() {
        return STATUS;
    }

    public static void setSTATUS() {
        STATUS = "Waiting";
    }
    // Метод создания задачи
    @Override
    public void create_task(Task my_task) throws ExistingTaskException {
        try (Session session = ListManagerController.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            logger_hr.info("TRANSACTION HAS BEGUN!");
            for (Object obj : session.createQuery("FROM Task").getResultList()) {
                if (my_task.getTitle().equals(((Task) obj).getTitle()) &&
                        my_task.getText().equals(((Task) obj).getText())) {
                    throw new ExistingTaskException();
                }
            }
            session.persist(my_task);
            logger_hr.info("TASK PERSISTED!");
            session.getTransaction().commit();
            logger_hr.info("TRANSACTION COMMITTED!");
            STATUS = "Submitted";
        } catch (ExistingTaskException e) {
            logger_hr.error("YOU HAVE ADDED AN EXISTING TASK!");
            logger_hr.info("TRANSACTION COMMITTED!");
            STATUS = "Rejected";
        } catch (Exception e) {
            logger_hr.error("UNKNOWN EXCEPTION!");
            logger_hr.info("TRANSACTION COMMITTED!");
            STATUS = "Rejected";
        }
    }

    @Override
    public void delete_task(Task my_task) throws NotExistingTaskException{
        try(Session session = ListManagerController.getSessionFactory().getCurrentSession()) {
            Task del_task = null;
            session.beginTransaction();
            logger_hr.info("TRANSACTION HAS BEGUN!");
            // Использование HQL
            for (Object obj : session.createQuery("FROM Task").getResultList())
            {
                if (my_task.getTitle().equals(((Task)obj).getTitle()) &&
                        my_task.getText().equals(((Task) obj).getText()))
                {
                    del_task = (Task)obj;
                }
            }
            if (del_task == null) throw new NotExistingTaskException();
            session.remove(del_task);
            logger_hr.info("TASK DELETION COMMITTED!");
            session.getTransaction().commit();
            STATUS = "Submitted";
            logger_hr.info("TRANSACTION COMMITTED!");
        } catch (NotExistingTaskException e) {
            logger_hr.error("YOU WANT TO DELETE AN NON-EXISTING TASK!");
            logger_hr.info("TRANSACTION COMMITTED!");
            STATUS = "Rejected";
        } catch (Exception e) {
            logger_hr.error("UNKNOWN EXCEPTION!");
            logger_hr.info("TRANSACTION COMMITTED!");
            STATUS = "Rejected";
        }
    }
}
