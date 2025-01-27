package com.backend.allreva.chatting.chat.single.command.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "chatroom")
@Entity
public class SingleChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
