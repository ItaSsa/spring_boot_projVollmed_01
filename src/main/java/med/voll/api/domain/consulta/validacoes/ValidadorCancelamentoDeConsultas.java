package med.voll.api.domain.consulta.validacoes;

import med.voll.api.domain.consulta.DadosCancelamentoConsulta;

public interface ValidadorCancelamentoDeConsultas {
    void validar(DadosCancelamentoConsulta dados);
}
