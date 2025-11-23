package app.mappers;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.Company;


@Repository
public interface CompanyMapper extends JpaRepository<Company, String> {
	Optional<Company> findByUsername(String username);
}
