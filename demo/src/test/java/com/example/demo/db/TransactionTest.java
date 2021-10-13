package com.example.demo.db;

import com.example.demo.PayrollApplication;
import com.example.demo.config.TestContainersConfig;
import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.model.Bird;
import com.example.demo.defaultapp.services.AnimalService;
import com.example.demo.defaultapp.services.OutboxService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {TestContainersConfig.class})
@SpringBootTest(classes = {PayrollApplication.class})
public class TransactionTest {
    @Autowired
    AnimalService animalService;
    @MockBean
    OutboxService outboxService;

    @Test
    public void transactionRollBackTest() {
        //#region given
        var owl = new Bird();
        owl.setName("Filin");
        owl.setFlyHeight(30);
        //#endregion

        try {
            //#when
            Mockito.doThrow(new ServiceException("Transaction rollback test")).when(outboxService).persist(owl);
            animalService.persist(owl);
            //#endregion
        } catch (Exception ignored) {
        } finally {
            //#region then
            var animals = animalService.findAll();
            var outboxes = outboxService.findAll();
            Assert.assertEquals(animals.size(), 0);
            Assert.assertEquals(outboxes.size(), 0);
            //#endregion
        }
    }
}
