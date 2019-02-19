package com.zaytsevp.testcontainersexample.repository.auto;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import com.google.common.collect.Sets;
import com.zaytsevp.testcontainersexample.config.PostgresTestcontainersExtension;
import com.zaytsevp.testcontainersexample.model.Auto;
import com.zaytsevp.testcontainersexample.model.AutoType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Created on 19.02.2019.
 * <p>
 * Интеграционный тест репозитория (Auto), сконфигурированный
 * через расширение Spring, в котором проинициализирован контейнер Postgres.
 * Для заполнения и сравнения БД используется DBRider.
 *
 * @author Pavel Zaytsev
 */
//@RunWith(SpringRunner.class)
@SpringBootTest //Так работает
//@DataJpaTest // Так валится с ошибкой констрейнта при очистке таблиц
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // при ANY - также schema null
@ExtendWith(PostgresTestcontainersExtension.class)
@DBRider
//@ContextConfiguration
class AutoRepositoryITWithExtension {

    @Autowired
    private AutoRepository repository;

    @Test
    @DataSet(value = "/datasets/auto.json", cleanBefore = true, cleanAfter = true)
    void testFindAll() {

        // Actual
        List<Auto> all = repository.findAll();

        // Assertions
        Assertions.assertEquals(2, all.size());

        Auto auto = all.get(0);
        Assertions.assertEquals("parent1", auto.getName());
        Assertions.assertEquals(1980, auto.getFoundYear());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), auto.getId());
    }

    @Test
    @DataSet(value = "/datasets/auto.json", cleanBefore = true, cleanAfter = true)
    @ExpectedDataSet(value = "/datasets/auto_create__expected.json")
    void testCreate() {
        // Prepare
        Auto auto = Auto.builder()
                        .name("newauto")
                        .types(Sets.newHashSet(AutoType.OFFROAD))
                        .foundYear(2019)
                        .build();

        // Actual
        Auto actualResult = repository.saveAndFlush(auto);

        // Assertion
        Assertions.assertNotNull(actualResult);
    }

    @Test
    @DataSet(value = "/datasets/auto.json", cleanBefore = true, cleanAfter = true)
    void testFindById() {
        // Prepare
        UUID autoId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        // Actual
        Optional<Auto> actualResult = repository.findById(autoId);

        // Assertion
        Assertions.assertTrue(actualResult.isPresent());

        Auto auto = actualResult.get();
        Assertions.assertEquals("parent1", auto.getName());
        Assertions.assertEquals(1980, auto.getFoundYear());
        Assertions.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), auto.getId());
    }
}