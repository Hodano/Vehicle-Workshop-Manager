package pl.hodan.carservice.common.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import pl.hodan.carservice.common.entity.Role;
import pl.hodan.carservice.common.enums.RolesEnum;
import pl.hodan.carservice.common.repository.RoleRepository;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;
    @Test
    public void SetUserRoleShouldReturnUserRole(){
        //given
        Role expectedRole = new Role();
        expectedRole.setRole(RolesEnum.USER);
        //when
        Role actual = roleService.setUserRole();
        //then
        assertEquals(expectedRole,actual);
        assertNotNull(actual);

    }

    @Test
    public void SetAdminRoleShouldReturnAdminRole(){
        //given
        Role expectedRole = new Role();
        expectedRole.setRole(RolesEnum.ADMIN);
        //when
        Role actual = roleService.setAdminRole();
        //then
        assertEquals(expectedRole,actual);
        assertNotNull(actual);

    }
    @Test
    public void CreateRolesShouldReturnRoleUserWhenExistAnyRole(){
        //given
        Role role = new Role(1L,RolesEnum.ADMIN);
        when(roleRepository.existsRoleByIdNotNull()).thenReturn(true);
        //when
        Role actual = roleService.createRoles();
        //then
        assertNotNull(actual);
        assertEquals(actual.getRole(),RolesEnum.USER);


    }
    @Test
    public void CreateRolesShouldReturnRoleAdminWhenNotExistAnyRole(){
        //given
        when(roleRepository.existsRoleByIdNotNull()).thenReturn(false);
        //when
        Role actual = roleService.createRoles();
        //then
        assertNotNull(actual);
        assertEquals(actual.getRole(),RolesEnum.ADMIN);


    }

}