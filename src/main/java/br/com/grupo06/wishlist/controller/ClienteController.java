package br.com.grupo06.wishlist.controller;

import br.com.grupo06.wishlist.domain.entity.ClienteEntity;
import br.com.grupo06.wishlist.domain.entity.ProdutoEntity;
import br.com.grupo06.wishlist.service.ClienteService;
import br.com.grupo06.wishlist.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    //Método para listar todos os clientes
    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteEntity>> listar() {
        List<ClienteEntity> clientes = clienteService.listarTodos();

        return new ResponseEntity<List<ClienteEntity>>(clientes, HttpStatus.OK);
    }

    //Método para listar cliente por codigo
    @GetMapping("/clientes/{codigo}")
    public ResponseEntity listarPorCodigo(@PathVariable("codigo") Integer codigo) {

        Optional<ClienteEntity> cliente = this.clienteService.listarPorCodigo(codigo);

        if (cliente.isPresent()) {
            return new ResponseEntity(cliente, HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Record not found.");
        }
    }

    //Método para incluir cliente
    @PostMapping("/clientes")
    public ResponseEntity incluir(@RequestBody ClienteEntity cliente) {
        try {
            this.clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente incluído com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Método para atualizar cliente
    @PutMapping("/clientes")
    public ResponseEntity atualizar( @RequestBody ClienteEntity cliente) {
        try {

            this.clienteService.salvar(cliente);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente atualizado com sucesso!");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Método para excluir um cliente
    @DeleteMapping("/clientes/{codigo}")
    public ResponseEntity excluir(@PathVariable("codigo") Integer codigo) {
        try {
            this.clienteService.excluir(codigo);
            return ResponseEntity.status(HttpStatus.OK).body("Cliente excluído com sucesso!");

            //Exceção quando não encontra o id informado
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }
        //Demais exceções

        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }
}
