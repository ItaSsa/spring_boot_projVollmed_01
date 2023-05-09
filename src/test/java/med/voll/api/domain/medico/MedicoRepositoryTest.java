package med.voll.api.domain.medico;

import med.voll.api.domain.consulta.Consulta;
import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.paciente.DadosCadastroPaciente;
import med.voll.api.domain.paciente.Paciente;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Java6Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Deveria devolver null quando unico medico cadastrado nao esta disponivel na data")
    void escolherMedicoAleatorioNaDataCenario1() {
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);

        var medico= cadastrarMedico("MEdico","medico@vol.com","123456",Especialidade.cardiologia);
        var paciente= cadastrarPaciente("Paciente","paciente@email.com","00000000000");
        cadastrarConsulta(medico,paciente,proximaSegundaAs10);

        var medicoLivre = medicoRepository.escolherMedicoAleatorioNaData(Especialidade.cardiologia,proximaSegundaAs10);
        assertThat(medicoLivre).isNull();
    }

    @Test
    @DisplayName("Deveria devolver medico quando ele esta disponivel na data")
    void escolherMedicoAleatorioNaDataCenario2() {
        //given or arrage
        var proximaSegundaAs10 = LocalDate.now()
                .with(TemporalAdjusters.next(DayOfWeek.MONDAY))
                .atTime(10,0);
        var medico = cadastrarMedico("MEdico","medico@vol.com","123456",Especialidade.cardiologia);

        //when or act
        var medicoLivre = medicoRepository.escolherMedicoAleatorioNaData(Especialidade.cardiologia,proximaSegundaAs10);

        //then or assert
        assertThat(medicoLivre).isEqualTo(medico);
    }
    private void cadastrarConsulta(Medico medico, Paciente paciente, LocalDateTime data){
        em.persist(new Consulta(null,medico,paciente,data,null,null,null));
    }
    private Medico cadastrarMedico(String nome,String email, String crm, Especialidade especialidade){
        var medico = new Medico(dadosMedico(nome,email,crm,especialidade));
        em.persist(medico);
        return medico;
    }

    private Paciente cadastrarPaciente(String nome,String email, String cpf){
        var paciente = new Paciente(dadosPaciente(nome,email,cpf));
        em.persist(paciente);
        return paciente;
    }

    private DadosCadastroMedico dadosMedico(String nome, String email, String crm, Especialidade especialidade){
        return new DadosCadastroMedico(nome,
                email,
                "898889999",
                crm,
                especialidade,
                dadosEndereco());
    }

    private DadosCadastroPaciente dadosPaciente( String nome,String email, String cpf){
        return new DadosCadastroPaciente(nome,
                email,
                "98979889",
                cpf,
                dadosEndereco());
    }

    private DadosEndereco dadosEndereco(){
        return new DadosEndereco("rua xpto",
                "454",
                "",
                "bairro",
                "Brasilia",
                "DF",
                "00000000");
    }
}