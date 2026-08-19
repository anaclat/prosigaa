package com.ifsc.prosigaa.service;

import com.ifsc.prosigaa.repository.PostagemRepository;
import org.springframework.stereotype.Service;

@Service
public class PostagemService {
    private final PostagemRepository postagemRepository;

    public PostagemService(PostagemRepository postagemRepository) {
        this.postagemRepository = postagemRepository;
    }
}
