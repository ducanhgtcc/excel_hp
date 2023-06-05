package com.example.onekids_project.repository;

import com.example.onekids_project.entity.agent.Supplier;
import com.example.onekids_project.repository.repositorycustom.SupplierRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, SupplierRepositoryCustom {

    Optional<Supplier> findByIdAndDelActive(Long id, boolean b);

}
