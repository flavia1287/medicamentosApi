package com.dh.peliculas.api.controller;
import com.example.proyectobackend.persistence.entities.Paciente;
import com.example.proyectobackend.service.DomicilioService;
import com.example.proyectobackend.service.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {
    @Autowired
    PacienteService pacienteService;
    @Autowired
    DomicilioService domicilioService;

    @GetMapping("/all")
    public ResponseEntity<List<Paciente>> listar(){
        return ResponseEntity.ok(pacienteService.listarTodos());
    }

    @PostMapping("/create")
    public ResponseEntity<String> crear(@RequestBody Paciente paciente){
        ResponseEntity<String> respuesta = null;

        LocalDate fecha = paciente.getFechaDeAlta();
        paciente.setFechaDeAlta(fecha);
        domicilioService.guardar(paciente.getDomicilio());
        pacienteService.guardar(paciente);
        return ResponseEntity.ok("creado"+paciente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrar(@PathVariable Integer id){
        ResponseEntity<String> response = null;

        if (pacienteService.buscar(id) != null){
            pacienteService.eliminar(id);
            response = ResponseEntity.status(HttpStatus.NO_CONTENT).body("Eliminado");
        }else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Paciente> buscar(@PathVariable Integer id) {
        Optional<Paciente> pacienteOptional = pacienteService.buscar(id);

        if (pacienteOptional.isPresent()) {
            return ResponseEntity.ok(pacienteOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<Paciente> modificar(@PathVariable("id") Integer id, @RequestBody Paciente pacienteModificado){
        Paciente paciente = pacienteService.actualizar(id, pacienteModificado);
        return ResponseEntity.ok(pacienteModificado);
    }
}