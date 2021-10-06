package com.example.demo.resourceserver.dao;

import com.example.demo.resourceserver.model.Animal;
import com.example.demo.resourceserver.model.Zoo;
import com.example.demo.resourceserver.utils.NamedParamStatement;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ZooDao extends AbstractHibernateDao<Zoo> {
     private static final int MAX_BATCH_SIZE_AT_A_TIME = 50;
     private final SessionFactory sessionFactory;

     public ZooDao(SessionFactory sessionFactory) {super(Zoo.class);
          this.sessionFactory = sessionFactory;
     }

     public void persist(Zoo zoo) {
          getCurrentSession().persist(zoo);
          for (Animal animal: zoo.getAnimals())
               getCurrentSession().persist(animal);
     }

     public void persistNative(Zoo zoo) {
          getCurrentSession().persist(zoo);

          var animalTypes = splitByClassName(zoo.getAnimals());
          for (final List<Animal> animals : animalTypes.values()){
               if (animals.isEmpty())
                    continue;

               List<String> insertParameters = animals.get(0).getQueryInsertParameters();
               String insertString = insertParameters.stream().reduce((x, y) -> x + "," + y).get();
               String placeholders = insertParameters.stream().map(v->" :"+v+" ").reduce((x,y) -> x + "," + y).get();

               final String sqlInsertString = "INSERT INTO animal " +
                       "(" + insertString + ")" +
                       " VALUES " +
                       "(" + placeholders + ")";

               getCurrentSession().doWork(connection -> {
                    NamedParamStatement statement = new NamedParamStatement(connection, sqlInsertString);
                    for (Animal animal: animals)
                         statement = animal.addBatch(statement);

                    statement.executeBatch();
               });
          }
     }

     private Map<String, List<Animal>> splitByClassName(List<Animal> animals) {
          HashMap<String, List<Animal>> map = new HashMap<>();
          for (Animal animal: animals) {
               String className = animal.getClass().getName();

               List<Animal> list = map.computeIfAbsent(className, k -> new ArrayList<Animal>());
               list.add(animal);
          }

          return map;
     }

     /**
      * This method is an optimized version of usual save method. It does not store objects in Spring Session, but
      * saves info to the database.
      */
     public void persistByBlocks(Zoo zoo) {
          if (zoo == null)
               return;

          Session session = sessionFactory.openSession();
          Transaction transaction = session.beginTransaction();
          session.save(zoo);
          List<Animal> animals = zoo.getAnimals();
          for (int i = 0; i < animals.size(); i++) {
               Animal animal = animals.get(i);
               session.save(animal);
               if (i % MAX_BATCH_SIZE_AT_A_TIME == 0){
                    session.flush();
                    session.clear();
               }
          }
          transaction.commit();
          session.close();
     }
}
