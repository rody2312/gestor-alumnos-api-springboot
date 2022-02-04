package cl.escuelascc.gestor.alumnos.models.services;

import java.util.List;

import cl.escuelascc.gestor.alumnos.models.entity.Alumno;

public interface IAlumnoService {
	
	public List<Alumno> findAll();
	
}
