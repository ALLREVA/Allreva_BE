package com.backend.allreva.chatting.chat.single.command.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class SingleChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
