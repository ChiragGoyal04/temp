package com.chirag.phase5.dto.response;

import com.chirag.phase5.entity.Role;
import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Role role;
    private Boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
