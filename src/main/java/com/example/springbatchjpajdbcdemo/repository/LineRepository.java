package com.example.springbatchjpajdbcdemo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springbatchjpajdbcdemo.model.Line;

public interface LineRepository extends JpaRepository<Line, Long> {

	Page<Line> findByName(String name, Pageable pageable);

}
