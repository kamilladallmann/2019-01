package br.edu.utfpr.servico;

import br.edu.utfpr.dao.Cliente;
import br.edu.utfpr.dto.ClienteDTO;
import br.edu.utfpr.dto.PaisDTO;
import br.edu.utfpr.excecao.NomeClienteMenor5CaracteresException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class ServicoCliente {

    private List<ClienteDTO> clientes;
    private List<PaisDTO> paises;

    public ServicoCliente() {
        clientes = Stream.of(
                ClienteDTO.builder().id(1).nome("Kamilla").idade(21).limiteCredito(1200.50).telefone("12345678").pais(paises.get(1)).build(),
                ClienteDTO.builder().id(2).nome("Gustavo").idade(22).limiteCredito(570.00).telefone("09876543").pais(paises.get(0)).build(),
                ClienteDTO.builder().id(3).nome("Yago").idade(27).limiteCredito(3900.00).telefone("38313755").pais(paises.get(2)).build()
        ).collect(Collectors.toList());

        paises = Stream.of(
                PaisDTO.builder().id(1).nome("Brasil").sigla("BR").codigoTelefone(55).build(),
                PaisDTO.builder().id(2).nome("Estados Unidos da América").sigla("EUA").codigoTelefone(33).build(),
                PaisDTO.builder().id(3).nome("Reino Unido").sigla("RU").codigoTelefone(44).build()
        ).collect(Collectors.toList());
    }

    @GetMapping("/servico/cliente")
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(clientes);
    }

    @GetMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> listarPorId(@PathVariable int id) {
        Optional<ClienteDTO> clienteEncontrado = clientes.stream().filter(p -> p.getId() == id).findAny();

        return ResponseEntity.of(clienteEncontrado);
    }

    @PostMapping("/servico/cliente")
    public ResponseEntity<ClienteDTO> criar (@RequestBody ClienteDTO cliente) {

        cliente.setId(clientes.size() + 1);
        clientes.add(cliente);

        return ResponseEntity.status(201).body(cliente);
    }

    @DeleteMapping ("/servico/cliente/{id}")
    public ResponseEntity excluir (@PathVariable int id) {

        if (clientes.removeIf(cliente -> cliente.getId() == id))
            return ResponseEntity.noContent().build();

        else
            return ResponseEntity.notFound().build();
    }

    @PutMapping ("/servico/cliente/{id}")
    public ResponseEntity<ClienteDTO> alterar (@PathVariable int id, @RequestBody ClienteDTO cliente){
        Optional<ClienteDTO> clienteExistente = clientes.stream().filter(c -> c.getId() == id).findAny();

        clienteExistente.ifPresent(c -> {
            try{
                c.setNome(cliente.getNome());

            }catch (NomeClienteMenor5CaracteresException exception){
                exception.printStackTrace();
            }

            c.setIdade(cliente.getIdade());
            c.setPais(paises.stream().filter(p -> p.getId() == cliente.getPais().getId()).findAny().get());
            c.setTelefone(cliente.getTelefone());
            c.setLimiteCredito(cliente.getLimiteCredito());
        });

        return ResponseEntity.of(clienteExistente);
    }

}
