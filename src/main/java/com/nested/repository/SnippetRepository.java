package com.nested.repository;

import com.nested.entity.Snippet;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by elizbor on 01.03.2017.
 */
public interface SnippetRepository extends JpaRepository<Snippet, Long> {}
