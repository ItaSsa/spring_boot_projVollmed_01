package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DadosCancelamentoConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaMininaCancelamento implements ValidadorCancelamentoDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DadosCancelamentoConsulta dados) {
        var consulta = repository.getReferenceById(dados.id());
        var dataConsulta = consulta.getData();
        var agora = LocalDateTime.now();
        var diferencaEmHoras = Duration.between(agora,dataConsulta).toHours();

        if ( diferencaEmHoras < 24){
            throw new ValidacaoException("Uma consulta somente poderá ser cancelada com antecedência mínima de 24 horas");
        }
    }
}
