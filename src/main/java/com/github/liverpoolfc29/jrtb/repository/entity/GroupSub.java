package com.github.liverpoolfc29.jrtb.repository.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Data
@Entity
@Table(name = "group_sub")
@EqualsAndHashCode
public class GroupSub {

    @Id
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "last_article_id")
    private Integer lastArticleId;

    // у нас есть дополнительное поле users, которое будет содержать коллекцию всех пользователей, подписанных на группу.
    // И две аннотации — ManyToMany и JoinTable — как раз для этого нам и нужны.
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_x_user",
            joinColumns = @JoinColumn(name = "group_sub_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<TelegramUser> telegramUsers;

    public void addUser(TelegramUser telegramUser) {
        if (isNull(telegramUsers)) {
            telegramUsers = new ArrayList<>();
        }
        telegramUsers.add(telegramUser);
    }


}
