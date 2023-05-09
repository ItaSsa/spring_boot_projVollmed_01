package med.voll.api.domain.consulta;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsultas;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgendaDeConsultas {
    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    /*Detalhe massa se injeção de N validadores!!*/
    @Autowired
    private List<ValidadorAgendamentoDeConsultas> validadores;

    public DadosDetalhamentoConsulta agendar( DadosAgendamentoConsulta dados){
        System.out.println("Teste de retorno"+ pacienteRepository.existsById(dados.idPaciente()));
        if (!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidacaoException("Id do paciente informado nao existe");
        }
        if (dados.idMedico() !=null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidacaoException("Id do médico informado nao existe");
        }

        /* Injeção de todos os validadores*/
        validadores.forEach(v->v.validar(dados));

        var paciente = pacienteRepository.findById(dados.idPaciente()).get();
        var medico =  escolherMedico(dados);

        if(medico == null){
            throw new ValidacaoException("Não existe médico disponível nesta data");
        }
        var consulta = new Consulta(null,medico,paciente,dados.data(),null,null,null);
        consultaRepository.save(consulta);

        return new DadosDetalhamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if (dados.idMedico() != null){
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if(dados.especialidade() == null){
            throw new ValidacaoException("Especialidade é obrigatória quando o médico não for informado");
        }

        return medicoRepository.escolherMedicoAleatorioNaData(dados.especialidade(), dados.data());
    }


}
