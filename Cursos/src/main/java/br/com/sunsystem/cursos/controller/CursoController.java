package br.com.sunsystem.cursos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sunsystem.cursos.interfaces.CursoRepository;
import br.com.sunsystem.cursos.model.Curso;

@Controller
public class CursoController {

	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping("/cursos")
	public String getAll(Model model, @Param("keyword") String keyword) {
		try {
			List<Curso> cursos = new ArrayList<Curso>();

			if (keyword == null) {
				cursoRepository.findAll().forEach(cursos::add);
			} else {
				cursoRepository.findByTitleContainingIgnoreCase(keyword).forEach(cursos::add);
				model.addAttribute("keyword", keyword);
			}

			model.addAttribute("cursos", cursos);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
		}

		return "cursos";
	}

	@GetMapping("/cursos/new")
	public String addCurso(Model model) {
		Curso curso = new Curso();
		curso.setPublished(true);

		model.addAttribute("curso", curso);
		model.addAttribute("pageTitle", "Cadastrar Novo Curso");
		return "curso_form";
	}

	@PostMapping("/cursos/save")
	public String saveCurso(Curso curso, RedirectAttributes redirectAttributes) {
		try {
			cursoRepository.save(curso);

			redirectAttributes.addFlashAttribute("message", "O Curso foi salvo com Sucesso!");
			return "redirect:/cursos";
		} catch (Exception e) {
			redirectAttributes.addAttribute("message", e.getMessage());
		}
		return "redirect:/cursos";
	}

	@GetMapping("/cursos/{id}")
	public String editCurso(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Curso curso = cursoRepository.findById(id).get();

			model.addAttribute("curso", curso);
			model.addAttribute("pageTitle", "Editar Curso (ID: " + id + ")");

			return "curso_form";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());

			return "redirect:/cursos";
		}
	}

	@GetMapping("/cursos/delete/{id}")
	public String deleteCurso(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			cursoRepository.deleteById(id);

			redirectAttributes.addFlashAttribute("message", "O Curso com id=" + id + " foi deletado com sucesso!");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/cursos";
	}

	@GetMapping("/cursos/{id}/published/{status}")
	public String updateCursoPublishedStatus(@PathVariable("id") Integer id, @PathVariable("status") boolean published,
			Model model, RedirectAttributes redirectAttributes) {

		try {
			cursoRepository.updatePublishedStatus(id, published);

			String status = published ? "published" : "disabled";
			String message = "O Curso id=" + id + " foi atualizado " + status;

			redirectAttributes.addFlashAttribute("message", message);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}

		return "redirect:/cursos";
	}
}
