package com.example.demo.db;

import com.example.demo.PayrollApplication;
import com.example.demo.config.TestContainersConfig;
import com.example.demo.resourceserver.model.Animal;
import com.example.demo.resourceserver.model.Bird;
import com.example.demo.resourceserver.model.Feline;
import com.example.demo.resourceserver.model.Zoo;
import com.example.demo.resourceserver.services.AnimalService;
import com.example.demo.resourceserver.services.OutboxService;
import com.example.demo.resourceserver.services.ZooService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.hibernate.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestContainersConfig.class})
@SpringBootTest(classes = {PayrollApplication.class})
public class DatabaseTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ZooService zooService;
    @Autowired
    AnimalService animalService;
    @Autowired
    OutboxService outboxService;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void purgeDatabase() {

    }

    @Test
    @SneakyThrows
    public void saveToDatabaseWorksCorrectTest() {
        //#region given
        var cat = new Feline();
        cat.setName("PussInBoots");
        cat.setOwner("Me");
        cat.setFurType("Sharp");
        //#endregion

        //#region when
        entityManager.clear();
        animalService.persist(cat);
        entityManager.clear();
        //#endregion

        //#region then
        var savedCat = animalService.findOne(cat.getId());
        Assert.assertNotNull(savedCat);
        Assert.assertEquals(cat, savedCat);
        var catMessage = objectMapper.writeValueAsString(cat);
        var outbox = outboxService.findByMessage(catMessage);
        Assert.assertNotNull(outbox);
        Assert.assertEquals(objectMapper.writeValueAsString(cat), outbox.getMessage());
        //#endregion
    }

    @Test
    @SneakyThrows
    public void nativeQuerySpeedupTest() {
        //#region given
        var zoo1 = createRandomZoo(13, 10_000);
        var zoo2 = createRandomZoo(13, 10_000);
        //#endregion

        //#region when
        entityManager.clear();
        long start_fast = System.nanoTime();
        zooService.persistNative(zoo2);
        long end_fast = System.nanoTime();
        var fastTimeElapsed = end_fast - start_fast;
        System.out.println("Fast elapsing time:" + fastTimeElapsed + " nano");

        entityManager.clear();
        long start_base = System.nanoTime();
        zooService.persist(zoo1);
        long end_base = System.nanoTime();
        var slowTimeElapsed = end_base - start_base;
        System.out.println("Slow elapsing time:" + slowTimeElapsed + " nano");

        entityManager.clear();
        //#endregion

        //#region then
        // time assessment
        Assert.assertTrue(fastTimeElapsed < slowTimeElapsed);

        // objects validation
        var savedZoo1 = zooService.findOne(zoo1.getId());
        Assert.assertNotNull(savedZoo1);
        Hibernate.initialize(savedZoo1);
        Assert.assertEquals(savedZoo1.getAnimals().size(), zoo1.getAnimals().size());

        var savedZoo2 = zooService.findOne(zoo2.getId());
        Assert.assertNotNull(savedZoo2);
        Hibernate.initialize(savedZoo2);
        Assert.assertEquals(savedZoo2.getAnimals().size(), zoo2.getAnimals().size());
        //#endregion
    }

    @Test
    @SneakyThrows
    public void indexingSearchSpeedupTest() {
        //#region given
        var testName = "TestName";
        var testOwner = "TestOwner";
        var myAnimalsNumber = 30;
        var elseAnimalsNumber = 50_000;

        for (int i = 0; i < myAnimalsNumber; i++){
            Feline cat = new Feline();
            cat.setName(testName);
            cat.setOwner(testOwner);
            cat.setFurType("TestFurType_"+i);
            animalService.persist(cat);
        }
        for (int i = 0; i < elseAnimalsNumber; i++){
            Feline cat = new Feline();
            cat.setName("ElseCatName");
            cat.setOwner("Else");
            cat.setFurType("ElseTestFurType_"+i);
            animalService.persist(cat);
        }
        countByOwner(testOwner);
        countByName(testName);
        //#endregion

        //#region when
        entityManager.clear();
        long start_fast = System.nanoTime();
        int fastSize = countByOwner(testOwner);
        long end_fast = System.nanoTime();
        var fastTimeElapsed = end_fast - start_fast;
        System.out.println("Fast elapsing time:" + fastTimeElapsed/1_000_000.0 + " ms");
        System.out.println("Fast elapsing size:" + fastSize);

        entityManager.clear();
        long start_base = System.nanoTime();
        int slowSize = countByName(testName);
        long end_base = System.nanoTime();
        var slowTimeElapsed = end_base - start_base;
        System.out.println("Slow elapsing time:" + slowTimeElapsed/1_000_000.0 + " ms");
        System.out.println("Slow elapsing size:" + slowSize);

        entityManager.clear();
        //#endregion

        Assert.assertTrue(fastTimeElapsed < slowTimeElapsed);
    }

    private Zoo createRandomZoo(int seed, int size) {
        var zoo = new Zoo();
        zoo.setName("Public zoo");
        var random = new Random(seed);

        for (int i = 0; i < size; i++) {
            var animal = createRandomAnimal(i, random);
            zoo.addAnimal(animal);
        }

        return zoo;
    }

    private String createRandomOwner(Random random) {
        double rnd = random.nextDouble();
        if (rnd > 0.5){
            return "me";
        } else {
            return "someone_else";
        }
    }

    private Animal createRandomAnimal(int id, Random random) {
        var rnd = random.nextDouble();
        Animal animal;
        if (rnd > 0.5){
            var cat = new Feline();
            cat.setName("TestCatName_"+id);
            cat.setFurType("TestFurType_"+id);
            animal = cat;
        } else {
            var bird = new Bird();
            bird.setName("TestBirdName_"+id);
            bird.setFlyHeight(1+id);
            animal = bird;
        }
        animal.setOwner(createRandomOwner(random));
        return animal;
    }

    private int countByOwner(String owner) {
        SessionFactory sessionFactory = entityManager
                .getEntityManagerFactory().unwrap(SessionFactory.class);

        StatelessSession statelessSession = sessionFactory.openStatelessSession();

        List<Animal> animalList = statelessSession
                .createQuery("select a from Animal a where a.owner=:owner", Animal.class)
                .setParameter("owner", owner)
                .list();

        statelessSession.close();
        return animalList.size();
    }

    private int countByName(String name) {
        SessionFactory sessionFactory = entityManager
                .getEntityManagerFactory().unwrap(SessionFactory.class);

        StatelessSession statelessSession = sessionFactory.openStatelessSession();

        List<Animal> animalList = statelessSession
                .createQuery("select a from Animal a where a.name=:name", Animal.class)
                .setParameter("name", name)
                .list();

        statelessSession.close();
        return animalList.size();
    }
}
