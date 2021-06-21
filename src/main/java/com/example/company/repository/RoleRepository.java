package com.example.company.repository;

import com.example.company.entity.Role;
import com.example.company.entity.enums.Rolename;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "role")
public interface RoleRepository extends JpaRepository<Role,Integer> {
    @Query(value = "select * from role where id=?1", nativeQuery = true)
    Optional<Role> getRole(Integer id);

    Role findByRolename(Rolename rolename);
}
