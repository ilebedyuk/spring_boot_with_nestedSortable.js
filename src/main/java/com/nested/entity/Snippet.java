package com.nested.entity;

import lombok.*;

import javax.persistence.*;

/**
 * Created by elizbor on 01.03.2017.
 */
@Data
@NoArgsConstructor
@Entity
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long snippetId;
    private String textDE;
    private String textEN;
    private int indent;
    private boolean isCollapsed;
    private int orderSnippet;
}
