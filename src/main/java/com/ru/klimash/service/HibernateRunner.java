package com.ru.klimash.service;

import com.ru.klimash.ListManagerController;
import com.ru.klimash.entity.Task;
import com.ru.klimash.exceptions.ExistingTaskException;
import com.ru.klimash.exceptions.NotExistingTaskException;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class HibernateRunner implements TaskManagerService {
    // Константа статуса выполнения команды
    private static String STATUS = "Waiting";

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
            System.out.println("TRANSACTION HAS BEGUN!");
            for (Object obj : session.createQuery("FROM Task").getResultList()) {
                if (my_task.getTitle().equals(((Task) obj).getTitle()) &&
                        my_task.getText().equals(((Task) obj).getText())) {
                    throw new ExistingTaskException();
                }
            }
            session.persist(my_task);
            System.out.println("TASK PERSISTED!");
            session.getTransaction().commit();
            System.out.println("TRANSACTION COMMITTED!");
            STATUS = "Submitted";
        } catch (ExistingTaskException e) {
            System.out.println("YOU HAVE ADDED AN EXISTING TASK!");
            STATUS = "Rejected";
        } catch (Exception e) {
            System.out.println("UNKNOWN EXCEPTION!");
            STATUS = "Rejected";
        }
    }

    @Override
    public void delete_task(String my_title) throws NotExistingTaskException{
        try(Session session = ListManagerController.getSessionFactory().getCurrentSession()) {
            Task del_task = null;
            session.beginTransaction();
            System.out.println("TRANSACTION HAS BEGUN!");
            // Использование HQL
            for (Object obj : session.createQuery("FROM Task").getResultList())
            {
                if (my_title.equals(((Task)obj).getTitle()))
                {
                    del_task = (Task)obj;
                }
            }
            if (del_task == null) throw new NotExistingTaskException();
            session.remove(del_task);
            System.out.println("TASK DELETION COMMITTED!");
            session.getTransaction().commit();
            STATUS = "Submitted";
            System.out.println("TRANSACTION COMMITTED!");
        } catch (NotExistingTaskException e) {
            System.out.println("YOU WANT TO DELETE AN NON-EXISTING TASK!");
            STATUS = "Rejected";
        } catch (Exception e) {
            System.out.println("UNKNOWN EXCEPTION!");
            STATUS = "Rejected";
        }
    }
}
