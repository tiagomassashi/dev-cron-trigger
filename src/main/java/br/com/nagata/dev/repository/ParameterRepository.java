package br.com.nagata.dev.repository;

import br.com.nagata.dev.model.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParameterRepository extends JpaRepository<Parameter, String> {}
