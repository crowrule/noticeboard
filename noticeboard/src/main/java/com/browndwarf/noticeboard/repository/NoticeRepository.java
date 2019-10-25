package com.browndwarf.noticeboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.browndwarf.noticeboard.entity.NoticeEntity;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

}
