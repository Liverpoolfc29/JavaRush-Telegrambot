package com.github.liverpoolfc29.jrtb.repository;

import com.github.liverpoolfc29.jrtb.repository.entity.GroupSub;
import com.github.liverpoolfc29.jrtb.repository.entity.TelegramUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

/**
 * Integration-level testing for {@link GroupSubRepository}.
 * Идея теста заключается в том, что мы добавим в базу данных 5 групп подписок на одного пользователя через sql скрипт,
 * получим этого пользователя по его ID и проверим,
 * что нам пришли именно те группы и именно с такими значениями
 */

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GroupSubRepositoryIT {

    @Autowired
    private GroupSubRepository groupSubRepository;

    @Test
    @Sql(scripts = {"/sql/clearDbs.sql", "/sql/fiveUsersForGroupSub.sql"})
    public void shouldProperlyGetAllUsersForGroupSub() {
        //when
        Optional<GroupSub> groupSubFromDB = groupSubRepository.findById(1);

        //then
        Assertions.assertTrue(groupSubFromDB.isPresent());
        Assertions.assertEquals(1, groupSubFromDB.get().getId());
        List<TelegramUser> users = groupSubFromDB.get().getTelegramUsers();
        for (int i = 0; i < users.size(); i++) {
            Assertions.assertEquals(Long.valueOf(i + 1), users.get(i).getChatId());
            Assertions.assertTrue(users.get(i).isActive());
        }
    }

}
