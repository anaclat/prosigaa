package com.ifsc.prosigaa.config;

import com.ifsc.prosigaa.enums.TipoUsuario;
import com.ifsc.prosigaa.model.Fase;
import com.ifsc.prosigaa.model.Materia;
import com.ifsc.prosigaa.model.Usuario;
import com.ifsc.prosigaa.repository.FaseRepository;
import com.ifsc.prosigaa.repository.MateriaRepository;
import com.ifsc.prosigaa.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DadosIniciais {

    @Bean
    CommandLineRunner iniciar(FaseRepository fases, MateriaRepository materias, UsuarioRepository usuarios) {
        return args -> {
            if (fases.count() == 0) {
                Fase fase1 = fases.save(new Fase("1ª Fase", 1));
                Fase fase2 = fases.save(new Fase("2ª Fase", 2));

                Materia p1 = new Materia("Programação I", "PROG1");
                Materia c1 = new Materia("Cálculo I", "CALC1");
                Materia ed = new Materia("Estrutura de Dados", "ED");

                p1.getFases().add(fase1);
                c1.getFases().add(fase1);
                ed.getFases().add(fase2);

                fase1.getMaterias().add(p1);
                fase1.getMaterias().add(c1);
                fase2.getMaterias().add(ed);

                Materia sp1 = materias.save(p1);
                Materia sc1 = materias.save(c1);
                Materia sed = materias.save(ed);

                fase1.getMaterias().clear();
                fase1.getMaterias().add(sp1);
                fase1.getMaterias().add(sc1);
                fase2.getMaterias().clear();
                fase2.getMaterias().add(sed);
                fases.save(fase1);
                fases.save(fase2);
            }

            if (usuarios.count() == 0) {
                usuarios.save(new Usuario(
                        "Professor Exemplo",
                        "professor@ifsc.edu.br",
                        "123456",
                        TipoUsuario.PROFESSOR
                ));
                usuarios.save(new Usuario(
                        "Aluno Exemplo",
                        "aluno@ifsc.edu.br",
                        "123456",
                        TipoUsuario.ALUNO
                ));
            }
        };
    }
}
