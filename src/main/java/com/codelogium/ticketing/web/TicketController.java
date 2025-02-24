package com.codelogium.ticketing.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelogium.ticketing.service.TicketService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(value = "users/{userId}/tickets")
public class TicketController {

    private TicketService TicketService;


}
