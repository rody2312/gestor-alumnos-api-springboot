package cl.escuelascc.gestor.alumnos.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.escuelascc.gestor.alumnos.models.entity.Alumno;
import cl.escuelascc.gestor.alumnos.models.services.IAlumnoService;

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class AlumnoRestController {

	@Autowired
	private IAlumnoService alumnoService;

	@GetMapping("/alumnos")
	public List<Alumno> index() {
		return alumnoService.findAll();
	}

	@GetMapping("/alumnos/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {

		Map<String, Object> response = new HashMap<>();

		Alumno alumno = null;
		try {
			alumno = alumnoService.findById(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (alumno == null) {
			response.put("mensaje", "El alumno ID".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Alumno>(alumno, HttpStatus.OK);
	}

	@PostMapping("/alumnos")
	public ResponseEntity<?> create(@Valid @RequestBody Alumno alumno, BindingResult result) {
		Alumno alumnoNew = null;
		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				errors.add("El campo '" + error.getField() + "' " + error.getDefaultMessage());
			}

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			alumnoNew = alumnoService.save(alumno);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar el insert en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El alumno ha sido creado con ??xito!");
		response.put("alumno", alumnoNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/alumnos/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id) {
		Alumno alumnoActual = alumnoService.findById(id);
		Alumno alumnoUpdated = null;

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			List<String> errors = new ArrayList<>();

			for (FieldError error : result.getFieldErrors()) {
				errors.add("El campo '" + error.getField() + "' " + error.getDefaultMessage());
			}

			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		if (alumnoActual == null) {
			response.put("mensaje", "Error: no se pudo editar, el alumno ID"
					.concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			alumnoActual.setNombre(alumno.getNombre());
			alumnoActual.setApellido(alumno.getApellido());
			alumnoActual.setEmail(alumno.getEmail());
			alumnoActual.setFono(alumno.getFono());

			alumnoUpdated = alumnoService.save(alumnoActual);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar el alumno en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El alumno ha sido actualizado con ??xito!");
		response.put("alumno", alumnoUpdated);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@DeleteMapping("/alumnos/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();

		try {
			alumnoService.delete(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al eliminar el alumno en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "El alumno ha sido eliminado con ??xito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);

	}
}
