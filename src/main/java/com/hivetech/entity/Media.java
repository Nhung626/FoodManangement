package com.hivetech.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "media")
public class Media {
    @Id
    private String id;
    private String name;
    private String type;

}
