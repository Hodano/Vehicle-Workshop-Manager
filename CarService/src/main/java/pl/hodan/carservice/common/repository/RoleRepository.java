package pl.hodan.carservice.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.hodan.carservice.common.entity.Role;
import pl.hodan.carservice.common.enums.RolesEnum;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsRoleByIdNotNull();

    Set<Role> findByRoleIn(Set<RolesEnum> rolesEnums);
}
