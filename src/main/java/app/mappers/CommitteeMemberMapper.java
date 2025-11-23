package app.mappers;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.entities.CommitteeMember;

@Repository
public interface CommitteeMemberMapper extends JpaRepository<CommitteeMember, String> {
	Optional<CommitteeMember> findByUsername(String username);
}