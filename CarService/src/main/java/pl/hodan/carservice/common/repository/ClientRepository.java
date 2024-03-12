package pl.hodan.carservice.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.hodan.carservice.common.entity.Client;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findClientByUserId(Long userId);

    boolean existsClientById(Long clientId);

    Optional<Client> findClientByUserIdAndId(Long userId, Long clientId);


    @Query("SELECT c FROM Client c WHERE (lower(c.name) LIKE lower(concat('%', :searchTerm, '%')) OR lower(c.surname) LIKE lower(concat('%', :searchTerm, '%'))) AND c.user.id = :userId")
    List<Client> findBySearchTermAndUserId(String searchTerm, Long userId);


}
