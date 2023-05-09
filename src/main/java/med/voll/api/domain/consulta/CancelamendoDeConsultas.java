package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsultas;
import med.voll.api.domain.consulta.validacoes.ValidadorCancelamentoDeConsultas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CancelamendoDeConsultas {

    @Autowired
    private ConsultaRepository repository;

    @Autowired
    private List< ValidadorCancelamentoDeConsultas > validadores;

    public DadosDetalhamentoCancelamentoConsulta cancelar(DadosCancelamentoConsulta dados){
        var consulta = repository.getReferenceById(dados.id());
        if(consulta == null){
            throw new ValidacaoException("Consulta informada não existe.");
        }

        /* Injeção de todos os validadores*/
        validadores.forEach(v->v.validar(dados));

        consulta.cancelamentoConsulta(dados.motivoCancelamento());
        return new DadosDetalhamentoCancelamentoConsulta(consulta);

    }


}
