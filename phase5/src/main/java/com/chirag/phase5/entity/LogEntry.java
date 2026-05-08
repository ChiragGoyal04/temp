package com.chirag.phase5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Timestamp;

@Entity
@Table(name="logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String status;

    private Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id") // FK column in DB
    private User user;
}
