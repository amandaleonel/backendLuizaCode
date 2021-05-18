package br.com.grupo06.wishlist.controller;

import br.com.grupo06.wishlist.domain.dto.WishlistDto;
import br.com.grupo06.wishlist.domain.entity.ClienteEntity;
import br.com.grupo06.wishlist.domain.entity.ProdutoEntity;
import br.com.grupo06.wishlist.domain.modelViews.ClienteProdutoSimples;
import br.com.grupo06.wishlist.domain.modelViews.Wishlist;
import br.com.grupo06.wishlist.service.ClienteService;
import br.com.grupo06.wishlist.service.ProdutoService;
import br.com.grupo06.wishlist.service.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
public class WishlistController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/wishlist/{idCliente}")
    public ResponseEntity wishlistDoCliente(@PathVariable("idCliente") Integer id) {

        Optional<ClienteEntity> cliente = this.clienteService.listarPorCodigo(id);

        if (cliente.isPresent()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setId(cliente.get().getCodigo());
            wishlist.setNome(cliente.get().getNome());
            wishlist.setEmail(cliente.get().getEmail());
            wishlist.setProdutos(cliente.get().getProdutos());

            return new ResponseEntity(wishlist, HttpStatus.FOUND);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado!");
        }

    }

    @PostMapping("/wishlist")
    public  ResponseEntity salvarNovoProdutoWishlist(@RequestBody WishlistDto wishlistDto){

        Optional<ClienteEntity> cliente = this.clienteService.listarPorCodigo(wishlistDto.getId_cliente());
        Optional<ProdutoEntity> produto = this.produtoService.listarPorCodigo(wishlistDto.getId_produto());

        if (cliente.isPresent() && produto.isPresent()) {
            try{
                wishlistService.inserirProdutoNaWishlist(cliente.get(),produto.get());
                return ResponseEntity.status(HttpStatus.OK).body("Produto incluído com sucesso!");
            }catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente ou Produto não encontrado!");
        }
    }

    @DeleteMapping("/wishlist")
    public  ResponseEntity deletarProdutoDaWishlist(@RequestBody WishlistDto wishlistDto) {

        Optional<ClienteEntity> cliente = this.clienteService.listarPorCodigo(wishlistDto.getId_cliente());
        Optional<ProdutoEntity> produto = this.produtoService.listarPorCodigo(wishlistDto.getId_produto());

        if(cliente.isPresent() && produto.isPresent()) {
            try {
                this.wishlistService.deletarProdutoNaWishlist(cliente.get(), produto.get());
                return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
            }
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente ou Produto não encontrado!");
        }

    }

    @GetMapping("/wishlist/buscar")
    public  ResponseEntity procurarProdutoNaWishlistdoCliente(@RequestBody WishlistDto wishlistDto) {
        Optional<ClienteEntity> cliente = this.clienteService.listarPorCodigo(wishlistDto.getId_cliente());
        Optional<ProdutoEntity> produto = this.produtoService.listarPorCodigo(wishlistDto.getId_produto());

        if (cliente.isPresent() && produto.isPresent()) {
            ClienteProdutoSimples resposta = this.wishlistService.buscarProdutoNaWishlistDoCliente(cliente.get().getCodigo(), produto.get().getCodigo());
                if(resposta != null){
                    return new ResponseEntity(resposta, HttpStatus.FOUND);
                }else{
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado na wishlist do cliente!");
                }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente ou produto não encontrado!");
        }
    }
}
