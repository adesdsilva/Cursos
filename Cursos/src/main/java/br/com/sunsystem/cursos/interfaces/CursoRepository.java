package br.com.sunsystem.cursos.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.sunsystem.cursos.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

	List<Curso> findByTitleContainingIgnoreCase(String keyword);

	@Query("UPDATE Curso t SET t.published = :published WHERE t.id = :id")
	@Modifying
	public void updatePublishedStatus(Integer id, boolean published);
}
