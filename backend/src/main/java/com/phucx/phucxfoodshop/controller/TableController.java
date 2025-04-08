package com.phucx.phucxfoodshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/tables", consumes = MediaType.APPLICATION_JSON_VALUE)
public class TableController {



}
