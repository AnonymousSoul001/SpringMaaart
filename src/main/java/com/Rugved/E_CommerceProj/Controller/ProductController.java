package com.Rugved.E_CommerceProj.Controller;

import com.Rugved.E_CommerceProj.Model.Product;
import com.Rugved.E_CommerceProj.Model.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")

public class ProductController
{
    @Autowired
    private ProductService serivce;


    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        return new ResponseEntity<>(serivce.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity <Product> getProduct(@PathVariable int id)
    {
        Product product = serivce.getProductById(id);

        if(product != null)
        {
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
     //return new ResponseEntity<>(serivce.getProductById(id), HttpStatus.OK) ;
    }

    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile)
    {
        try{
            Product product1 = serivce.addProduct(product, imageFile);
            return new ResponseEntity<>(product1, HttpStatus.CREATED);
        }

        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getImageByProductId(@PathVariable int productId) {
        Product product = serivce.getProductById(productId);
        byte[] imageFile = product.getImageData();

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(product.getImageType()))
                .body(imageFile);
    }

    @PutMapping("/product/{id}")
    public ResponseEntity<String>updateProduct(@PathVariable int id, @RequestPart Product product,
                                               @RequestPart MultipartFile imageFile) {
        Product product1 = null;
        try {
            product1 = serivce.updateProduct(id, product, imageFile);
        }catch(IOException e){
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if(product1 != null)
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        else
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/product/{id}")
    public ResponseEntity<String>deleteProduct(@PathVariable int id)
    {
        Product product = serivce.getProductById(id);
        if(product != null)
        {
            serivce.deleteProduct(id);
            return new ResponseEntity<>("Failed to delete", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Product not available", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword)
    {
        List<Product> products = serivce.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
