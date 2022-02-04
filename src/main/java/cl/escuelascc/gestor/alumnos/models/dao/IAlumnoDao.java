package cl.escuelascc.gestor.alumnos.models.dao;

import org.springframework.data.repository.CrudRepository;

import cl.escuelascc.gestor.alumnos.models.entity.Alumno;

public interface IAlumnoDao extends CrudRepository<Alumno, Long>{

}
