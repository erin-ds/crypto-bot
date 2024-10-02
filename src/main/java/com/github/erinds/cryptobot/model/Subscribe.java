package com.github.erinds.cryptobot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "subscribes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Subscribe {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "telegram_id")
    @NotNull
    private Long telegramId;

    @Column
    private BigDecimal price;

    @Column(name = "last_send")
    private Instant lastSend;

}
