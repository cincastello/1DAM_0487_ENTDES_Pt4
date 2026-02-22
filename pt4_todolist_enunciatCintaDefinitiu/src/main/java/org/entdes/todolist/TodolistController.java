package org.entdes.todolist;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TodolistController {
    private final GestorTasques gestor = new GestorTasques();

    public TodolistController() {
        gestor.carregar();
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/tasques")
    public String llistarTasques(@RequestParam(value = "filtreDescripcio", required = false) String filtreDescripcio,
            Model model) {
        List<Tasca> tasques;
        if (filtreDescripcio != null && !filtreDescripcio.isEmpty()) {
            tasques = gestor.llistarTasquesPerDescripcio(filtreDescripcio);
        } else {
            tasques = gestor.llistarTasques();
        }
        model.addAttribute("tasques", tasques);
        model.addAttribute("nombreTasques", gestor.getNombreTasques());
        return "tasques";
    }

    @PostMapping("/tasques")
    public String addTasca(@RequestParam String descripcio, Model model) {

        if (descripcio.trim().isEmpty()) {
            model.addAttribute("errorMessage", "La descripció no pot estar buida.");
            return "index";
        }
        try {
            gestor.afegirTasca(descripcio);
            gestor.guardar();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "index";
        }
        return "redirect:/tasques";
    }

    @PostMapping("/tasques/update/{id}/completar")
    public String completarTasca(@PathVariable int id, Model model) {
        try {
            gestor.marcarCompletada(id);
            gestor.guardar();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/tasques";
    }

    @GetMapping("/tasques/edit/{id}")
    public String editTasca(@PathVariable int id, Model model) {
        try {
            Tasca tasca = gestor.obtenirTasca(id);
            model.addAttribute("tasca", tasca);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/tasques";
        }
        return "edit_tasca";
    }

    @PostMapping("/tasques/update/{id}")
    public String modificarTasca(@PathVariable int id, @RequestParam String descripcio,
            @RequestParam(required = false) Boolean completada, Model model) {
        if (descripcio.trim().isEmpty()) {
            model.addAttribute("errorMessage", "La descripció no pot estar buida.");
            return "edit_tasca";
        }
        try {
            Tasca tasca = gestor.obtenirTasca(id);
            model.addAttribute("tasca", tasca);
            gestor.modificarTasca(id, descripcio, completada);
            gestor.guardar();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "edit_tasca";
        }
        return "redirect:/tasques";
    }

    @PostMapping("/tasques/delete/{id}")
    public String deleteTasca(@PathVariable int id) {
        gestor.eliminarTasca(id);
        gestor.guardar();
        return "redirect:/tasques";
    }
}