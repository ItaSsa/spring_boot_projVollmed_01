package med.voll.api.domain.consulta;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.paciente.Paciente;

import java.time.LocalDateTime;

public record DadosDetalhamentoCancelamentoConsulta(
        Long id,
        Long idMedico,
        Long idPaciente,
        LocalDateTime data,
        Boolean ativo,
        MotivoCancelamento motivoCancelamento,
        LocalDateTime dataCancelamento
) {
    public DadosDetalhamentoCancelamentoConsulta(Consulta consulta) {
        this(consulta.getId(), consulta.getMedico().getId(),consulta.getPaciente().getId()
                ,consulta.getData(), consulta.getAtivo(),consulta.getMotivoCancelamento(), consulta.getDataCancelamento());
    }
}
